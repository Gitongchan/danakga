package com.danakga.webservice.qna.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.qna.dto.request.ReqQnADto;
import com.danakga.webservice.qna.dto.response.ResQnADto;
import com.danakga.webservice.qna.model.Qna;
import com.danakga.webservice.qna.service.QnAService;
import com.danakga.webservice.qna.repository.QnARepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService {

    private final UserRepository userRepository;
    private final QnARepository qnaRepository;

    /* 문의사항 목록 */
    @Override
    public ResQnADto qnaList(Pageable pageable, int q_sort, int page) {

        final String deleted = "N";

        /* 들어오는 sort값 따라서 사이트, 가게 문의사항 구분 */
        pageable = PageRequest.of(page, 10, Sort.by("qCreated").descending());
        Page<Qna> QnA = qnaRepository.findByQDeletedAndQSort(pageable, deleted, q_sort);

        List<Map<String, Object>> qnaList = new ArrayList<>();

        QnA.forEach(entity -> {

            Map<String,Object> qnaMap = new LinkedHashMap<>();

            qnaMap.put("q_id", entity.getQId());
            qnaMap.put("q_type", entity.getQType());
            qnaMap.put("q_userid", entity.getUserInfo().getUserid());
            qnaMap.put("q_title", entity.getQTitle());
            qnaMap.put("q_created", entity.getQCreated());
            qnaMap.put("q_state", entity.getQState());
            qnaMap.put("totalPage", QnA.getTotalPages());
            qnaMap.put("totalElement", QnA.getTotalPages());
            qnaList.add(qnaMap);
        });

        return new ResQnADto(qnaList);
    }

    /* 문의사항 조회 */
    @Override
    public ResQnADto qnaPost(Long q_id) {

        Qna checkQnA = qnaRepository.findById(q_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        List<Map<String,Object>> qnaList = new ArrayList<>();

        Map<String, Object> qnaMap = new LinkedHashMap<>();

        qnaMap.put("q_id", checkQnA.getQId());
        qnaMap.put("q_type", checkQnA.getQType());
        qnaMap.put("q_userid", checkQnA.getUserInfo().getUserid());
        qnaMap.put("q_title", checkQnA.getQTitle());
        qnaMap.put("q_content", checkQnA.getQContent());
        qnaMap.put("q_created", checkQnA.getQCreated());

        qnaList.add(qnaMap);

        return new ResQnADto(qnaList);
    }

    /* 문의사항 작성 */
    /* dto qSort 0 = 사이트, 1 = 가게 */
    @Transactional
    @Override
    public ResResultDto qnaWrite(UserInfo userInfo, ReqQnADto reqQnADto) {

        /* 사이트 문의사항 작성 */
        if(reqQnADto.getQnaSort() == 0) {

            UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                    .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

            Qna qna = qnaRepository.save(
                    Qna.builder()
                            .qSort(reqQnADto.getQnaSort())
                            .qType(reqQnADto.getQnaType())
                            .qTitle(reqQnADto.getQnaContent())
                            .qWriter(userInfo.getUserid())
                            .qContent(reqQnADto.getQnaContent())
                            .userInfo(checkUserInfo)
                            .build()
            );

            return new ResResultDto(qna.getQId(), "문의사항을 작성 했습니다.");
        }

        /* 가게 문의사항 작성 */
        /* 가게 아이디 값 어디서 받아올지 생각 */
//        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
//                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));
//
//        Qna qna = qnaRepository.save(
//                Qna.builder()
//                        .qSort(reqQnADto.getQnaSort())
//                        .qType(reqQnADto.getQnaType())
//                        .qTitle(reqQnADto.getQnaContent())
//                        .qWriter(userInfo.getUserid())
//                        .qContent(reqQnADto.getQnaContent())
//                        .userInfo(checkUserInfo)
//                        .build()
//        );
//
        return new ResResultDto(0L, "문의사항을 작성 했습니다.");
    }
    
    /* 문의사항 수정 */
    @Transactional
    @Override
    public ResResultDto qnaEdit(UserInfo userInfo, ReqQnADto reqQnADto, Long q_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Qna checkQnA = qnaRepository.findById(q_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        checkQnA = qnaRepository.save(
                Qna.builder()
                        .qId(checkQnA.getQId())
                        .qSort(reqQnADto.getQnaSort())
                        .qType(reqQnADto.getQnaType())
                        .qTitle(reqQnADto.getQnaTitle())
                        .qContent(reqQnADto.getQnaContent())
                        .qCreated(checkQnA.getQCreated())
                        .qDeleted(checkQnA.getQDeleted())
                        .userInfo(checkUserInfo)
                        .build()
        );

        return new ResResultDto(checkQnA.getQId(), "문의 사항을 수정 했습니다.");
    }

    /* 문의사항 삭제 상태 변경 */
    @Transactional
    @Override
    public ResResultDto qnaDelete(UserInfo userInfo, Long q_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Qna checkQnA = qnaRepository.findById(q_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        /* 문의사항 삭제 상태 변경 (삭제) */
        qnaRepository.updateQnADeleted(checkQnA.getQId());

        return new ResResultDto(checkQnA.getQId(), "문의 사항을 삭제 했습니다.");
    }
}
