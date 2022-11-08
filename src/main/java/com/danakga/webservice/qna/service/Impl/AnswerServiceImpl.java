package com.danakga.webservice.qna.service.Impl;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.qna.dto.response.ResAnswerDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final QnaRepository qnaRepository;
    private final AnswerRepository answerRepository;
    
    /* 문의사항 답변 조회 */

    @Override
    public ResAnswerDto answerPost(Long qn_id, Pageable pageable, int page) {

        final String deletedN = "N";

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        pageable = PageRequest.of(page, 10, Sort.by("anCreated").descending());
        Page<Answer> checkAnswer = answerRepository.findByQnaAndAnDeleted(checkQna, deletedN, pageable);

        List<Map<String, Object>> answerList = new ArrayList<>();

        checkAnswer.forEach(entity -> {

            Map<String, Object> answerMap = new LinkedHashMap<>();

            answerMap.put("an_id", entity.getAnId());
            answerMap.put("an_writer", entity.getAnWriter());
            answerMap.put("an_created", entity.getAnCreated());
            answerMap.put("an_deleted", entity.getAnDeleted());
            answerMap.put("an_content", entity.getAnContent());

            answerList.add(answerMap);
        });

        return new ResAnswerDto(answerList);
    }
    /* ======================== 가게 문의사항 답변 (manager) ======================== */

    /* 가게 문의사항 답변 작성 */
    @Override
    public ResResultDto productAnswerWrite(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long p_id, Long qn_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Product checkProduct = productRepository.findById(p_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("상품 정보를 찾을 수 없습니다."));

        CompanyInfo checkCompanyInfo = companyRepository.findByUserInfo(checkUserInfo)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("가게 정보를 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        /* 가게 아이디 값이 일치하고, 매니저인 경우에만 작성 가능 */
        if(checkUserInfo.getRole().equals(UserRole.ROLE_MANAGER)
                && checkQna.getCompanyInfo().getCompanyId().equals(checkCompanyInfo.getCompanyId())) {

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
        return new ResResultDto(-1L, "가게 매니저만 작성할 수 있습니다.");
    }

    /* 가게 문의사항 답변 수정 */
    @Override
    public ResResultDto productAnswerEdit(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long p_id, Long qn_id, Long an_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        CompanyInfo checkCompanyInfo = companyRepository.findByUserInfo(checkUserInfo)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("가게 정보를 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        Answer checkAnswer = answerRepository.findByAnIdAndQna(an_id, checkQna)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("답변을 찾을 수 없습니다."));

        /* 가게 아이디 값이 일치하고, 매니저인 경우에만 작성 가능 */
        if(checkUserInfo.getRole().equals(UserRole.ROLE_MANAGER)
                && checkQna.getCompanyInfo().getCompanyId().equals(checkCompanyInfo.getCompanyId())) {

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
        return new ResResultDto(-1L, "가게 매니저만 수정할 수 있습니다.");
    }

    /* 가게 문의사항 답변 삭제 */
    @Override
    public ResResultDto productAnswerDelete(UserInfo userInfo, Long p_id, Long qn_id, Long an_id) {

        final String deleted = "N";

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        CompanyInfo checkCompanyInfo = companyRepository.findByUserInfo(checkUserInfo)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("가게 정보를 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        Answer checkAnswer = answerRepository.findByAnIdAndQna(an_id, checkQna)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("답변을 찾을 수 없습니다."));

        if(checkUserInfo.getRole().equals(UserRole.ROLE_MANAGER)
                && checkQna.getCompanyInfo().getCompanyId().equals(checkCompanyInfo.getCompanyId())) {

            /* 답변 삭제 상태로 변경 */
            answerRepository.updateAnswerDeleted(checkAnswer.getAnId());

            /* 삭제 상태로 변경 후 답변이 아예 없는 경우 문의사항을 다시 답변 대기 상태로 변경 */
            List<Answer> stateAnswer = answerRepository.findByAnDeletedAndQna(deleted, checkQna);

            if(stateAnswer.isEmpty()) {
                qnaRepository.updateQnaStandByState(checkQna.getQnId());
            }

            return new ResResultDto(checkAnswer.getAnId(), "답변을 삭제 했습니다.");
        }
        return new ResResultDto(-1L, "가게 매니저만 삭제할 수 있습니다.");
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
        if(checkUserInfo.getRole().equals(UserRole.ROLE_ADMIN)) {

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
        return new ResResultDto(-1L, "사이트 관리자만 작성할 수 있습니다.");
    }

    /* 사이트 문의사항 답변 수정 */
    @Override
    public ResResultDto siteAnswerEdit(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long qn_id, Long an_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        Answer checkAnswer = answerRepository.findByAnIdAndQna(an_id, checkQna)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("답변을 찾을 수 없습니다."));

        /* 사이트 관리자가 아닌 경우 수정 불가 */
        if(checkUserInfo.getRole().equals(UserRole.ROLE_ADMIN)) {

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
        return new ResResultDto(-1L, "사이트 관리자만 수정할 수 있습니다.");
    }

    /* 사이트 문의사항 답변 삭제 */
    @Override
    public ResResultDto siteAnswerDelete(UserInfo userInfo, Long qn_id, Long an_id) {

        final String deleted = "N";

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        Answer checkAnswer = answerRepository.findByAnIdAndQna(an_id, checkQna)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("답변을 찾을 수 없습니다."));

        /* 사이트 관리자가 아닌 경우 삭제 불가 */
        if (checkUserInfo.getRole().equals(UserRole.ROLE_ADMIN)) {

            /* 답변 삭제 상태로 변경 */
            answerRepository.updateAnswerDeleted(checkAnswer.getAnId());

            /* 삭제 상태로 변경 후 답변이 아예 없는 경우 문의사항을 다시 답변 대기 상태로 변경 */
            List<Answer> stateAnswer = answerRepository.findByAnDeletedAndQna(deleted, checkQna);

            if (stateAnswer.isEmpty()) {
                qnaRepository.updateQnaStandByState(checkQna.getQnId());
            }

            return new ResResultDto(checkAnswer.getAnId(), "답변을 삭제 했습니다.");
        }
        return new ResResultDto(-1L, "사이트 관리자만 삭제할 수 있습니다.");
    }
}
