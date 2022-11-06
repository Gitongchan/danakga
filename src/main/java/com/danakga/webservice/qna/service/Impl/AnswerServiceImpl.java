package com.danakga.webservice.qna.service.Impl;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.qna.service.AnswerService;
import com.danakga.webservice.qna.model.Answer;
import com.danakga.webservice.qna.model.Qna;
import com.danakga.webservice.qna.repository.AnswerRepository;
import com.danakga.webservice.qna.repository.QnaRepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final QnaRepository qnaRepository;
    private final AnswerRepository answerRepository;


    /* ======================== 가게 문의사항 답변 (manager) ======================== */

    /* 가게 문의사항 답변 작성 */
    @Override
    public ResResultDto shopAnswerWrite(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long c_id, Long qn_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        CompanyInfo checkCompanyInfo = companyRepository.findById(c_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("가게 정보를 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        /* 회원, 관리자, 해당 가게의 매니저가 아닌 경우 작성 불가 */
        if(!checkUserInfo.getRole().equals(UserRole.ROLE_USER) && !checkUserInfo.getRole().equals(UserRole.ROLE_ADMIN)
                && !checkQna.getCompanyInfo().getCompanyId().equals(checkCompanyInfo.getCompanyId())) {
            
            return new ResResultDto(-1L, "가게 매니저만 작성할 수 있습니다.");

        }
            Answer answer = answerRepository.save(
                    Answer.builder()
                            .anWriter(checkCompanyInfo.getCompanyName())
                            .anContent(reqAnswerDto.getAnswerContent())
                            .qna(checkQna)
                            .build()
            );

            /* 문의사항 답변 상태 변경 (작성 완료) */
            qnaRepository.updateQnaCompleteState(checkQna.getQnId());

            return new ResResultDto(answer.getAnId(),"답변을 작성 했습니다.");
    }

    /* 가게 문의사항 답변 수정 */
    @Override
    public ResResultDto shopAnswerEdit(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long c_id, Long qn_id, Long an_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        CompanyInfo checkCompanyInfo = companyRepository.findById(c_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("가게 정보를 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        Answer checkAnswer = answerRepository.findByAnIdAndQna(an_id, checkQna)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("답변을 찾을 수 없습니다."));

        /* 회원, 관리자, 해당 가게의 매니저가 아닌 경우 수정 불가 */
        if(!checkUserInfo.getRole().equals(UserRole.ROLE_USER) && !checkUserInfo.getRole().equals(UserRole.ROLE_ADMIN)
                && !checkCompanyInfo.getCompanyId().equals(checkQna.getCompanyInfo().getCompanyId())) {

            return new ResResultDto(-1L, "가게 매니저만 수정할 수 있습니다.");
        }

        checkAnswer = answerRepository.save(
                Answer.builder()
                        .anId(checkAnswer.getAnId())
                        .anWriter(checkUserInfo.getUserid())
                        .anContent(reqAnswerDto.getAnswerContent())
                        .anCreated(checkAnswer.getAnCreated())
                        .anDeleted(checkAnswer.getAnDeleted())
                        .qna(checkQna)
                        .build()
        );

        return new ResResultDto(checkAnswer.getAnId(),"답변을 수정 했습니다.");
    }

    /* 가게 문의사항 답변 삭제 */
    @Override
    public ResResultDto shopAnswerDelete(UserInfo userInfo, Long c_id, Long qn_id, Long an_id) {

        final String deleted = "N";

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        CompanyInfo checkCompanyInfo = companyRepository.findById(c_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("가게 정보를 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        Answer checkAnswer = answerRepository.findByAnIdAndQna(an_id, checkQna)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("답변을 찾을 수 없습니다."));

        /* 회원, 관리자, 해당 가게의 매니저가 아닌 경우 삭제 불가 */
        if(!checkUserInfo.getRole().equals(UserRole.ROLE_USER) && !checkUserInfo.getRole().equals(UserRole.ROLE_ADMIN)
                && !checkCompanyInfo.getCompanyId().equals(checkQna.getCompanyInfo().getCompanyId())) {

            return new ResResultDto(-1L, "가게 매니저만 수정할 수 있습니다.");
        }

        /* 답변 삭제 상태로 변경 */
        answerRepository.updateAnswerDeleted(checkAnswer.getAnId());
        
        /* 삭제 상태로 변경 후 답변이 아예 없는 경우 문의사항을 다시 답변 대기 상태로 변경 */
        List<Answer> stateAnswer = answerRepository.findByAnDeletedAndQna(deleted, checkQna);

        if(stateAnswer.isEmpty()) {
            qnaRepository.updateQnaStandByState(checkQna.getQnId());
        }

        return new ResResultDto(checkAnswer.getAnId(), "답변을 삭제 했습니다.");
    }



    /* ======================== 사이트 문의사항 답변 (admin) ======================== */
    
    /* 사이트 문의사항 답변 작성 */
    @Override
    public ResResultDto siteAnswerWrite(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long qn_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        /* 사이트 관리자가 아닌 경우 작성 불가 */
        if(!checkUserInfo.getRole().equals(UserRole.ROLE_USER) && !checkUserInfo.getRole().equals(UserRole.ROLE_MANAGER)) {
            return new ResResultDto(-1L, "사이트 관리자만 작성할 수 있습니다.");
        }

        Answer answer = answerRepository.save(
                Answer.builder()
                        .anWriter(userInfo.getUserid())
                        .anContent(reqAnswerDto.getAnswerContent())
                        .qna(checkQna)
                        .build()
        );

        /* 문의사항 답변 상태 변경 (작성 완료) */
        qnaRepository.updateQnaCompleteState(checkQna.getQnId());

        return new ResResultDto(answer.getAnId(), "답변을 작성 했습니다.");
    }

    @Override
    public ResResultDto siteAnswerEdit(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long qn_id, Long an_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        Answer checkAnswer = answerRepository.findByAnIdAndQna(qn_id, checkQna)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("답변을 찾을 수 없습니다."));

        /* 사이트 관리자가 아닌 경우 작성 불가 */
        if(!checkUserInfo.getRole().equals(UserRole.ROLE_USER) && !checkUserInfo.getRole().equals(UserRole.ROLE_MANAGER)) {
            return new ResResultDto(-1L, "사이트 관리자만 수정할 수 있습니다.");
        }

        checkAnswer = answerRepository.save(
                Answer.builder()
                        .anId(checkAnswer.getAnId())
                        .anWriter(checkUserInfo.getUserid())
                        .anContent(reqAnswerDto.getAnswerContent())
                        .anCreated(checkAnswer.getAnCreated())
                        .anDeleted(checkAnswer.getAnDeleted())
                        .qna(checkQna)
                        .build()
        );

        return new ResResultDto(checkAnswer.getAnId(), "답변을 수정 했습니다.");
    }

    @Override
    public ResResultDto siteAnswerDelete(UserInfo userInfo, Long qn_id, Long an_id) {

        final String deleted = "N";

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        Answer checkAnswer = answerRepository.findByAnIdAndQna(an_id, checkQna)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("답변을 찾을 수 없습니다."));

        /* 사이트 관리자가 아닌 경우 작성 불가 */
        if(!checkUserInfo.getRole().equals(UserRole.ROLE_USER) && !checkUserInfo.getRole().equals(UserRole.ROLE_MANAGER)) {
            return new ResResultDto(-1L, "사이트 관리자만 삭제할 수 있습니다.");
        }

        /* 답변 삭제 상태로 변경 */
        answerRepository.updateAnswerDeleted(checkAnswer.getAnId());

        /* 삭제 상태로 변경 후 답변이 아예 없는 경우 문의사항을 다시 답변 대기 상태로 변경 */
        List<Answer> stateAnswer = answerRepository.findByAnDeletedAndQna(deleted, checkQna);

        if(stateAnswer.isEmpty()) {
            qnaRepository.updateQnaStandByState(checkQna.getQnId());
        }

        return new ResResultDto(0L, "답변을 삭제 했습니다.");
    }
}
