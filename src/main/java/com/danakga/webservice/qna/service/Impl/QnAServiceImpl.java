package com.danakga.webservice.qna.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.qna.dto.request.ReqQnADto;
import com.danakga.webservice.qna.dto.response.ResQnADto;
import com.danakga.webservice.qna.model.QnA;
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
    public ResQnADto qnaList(Pageable pageable, int page) {

        final String deleted = "N";

        pageable = PageRequest.of(page, 10, Sort.by("siteQCreated").descending());
        Page<QnA> QnA = qnaRepository.findByQDeleted(pageable, deleted);

        List<Map<String, Object>> qnaList = new ArrayList<>();

        QnA.forEach(entity -> {

            Map<String,Object> qnaMap = new LinkedHashMap<>();

            qnaMap.put("siteQ_id", entity.getQId());
            qnaMap.put("siteQ_type", entity.getQType());
            qnaMap.put("siteQ_userid", entity.getUserInfo().getUserid());
            qnaMap.put("siteQ_title", entity.getQTitle());
            qnaMap.put("siteQ_created", entity.getQCreated());
            qnaMap.put("siteQ_state", entity.getQState());
            qnaMap.put("totalPage", QnA.getTotalPages());
            qnaMap.put("totalElement", QnA.getTotalPages());
            qnaList.add(qnaMap);
        });

        return new ResQnADto(qnaList);
    }

    /* 문의사항 조회 */
    @Override
    public ResQnADto qnaPost(Long q_id) {

        QnA checkQnA = qnaRepository.findById(q_id)
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
    @Transactional
    @Override
    public ResResultDto qnaWrite(UserInfo userInfo, ReqQnADto reqQnADto) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        QnA qna = qnaRepository.save(
                QnA.builder()
                        .qType(reqQnADto.getQType())
                        .qTitle(reqQnADto.getQContent())
                        .qWriter(userInfo.getUserid())
                        .qContent(reqQnADto.getQContent())
                        .userInfo(recentUserInfo)
                        .build()
        );

        return new ResResultDto(qna.getQId(), "문의 사항을 작성 했습니다.");
    }
    
    /* 문의사항 수정 */
    @Transactional
    @Override
    public ResResultDto qnaEdit(UserInfo userInfo, ReqQnADto reqQnADto, Long q_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        QnA checkQnA = qnaRepository.findById(q_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        checkQnA = qnaRepository.save(
                QnA.builder()
                        .qId(checkQnA.getQId())
                        .qType(reqQnADto.getQType())
                        .qTitle(reqQnADto.getQTitle())
                        .qContent(reqQnADto.getQContent())
                        .qCreated(checkQnA.getQCreated())
                        .qDeleted(checkQnA.getQDeleted())
                        .userInfo(recentUserInfo)
                        .build()
        );

        return new ResResultDto(checkQnA.getQId(), "문의 사항을 수정했습니다.");
    }

    /* 문의사항 삭제 상태 변경 */
    @Transactional
    @Override
    public ResResultDto qnaDelete(UserInfo userInfo, Long q_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        QnA checkQnA = qnaRepository.findById(q_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        /* 문의사항 삭제 상태 변경 (삭제) */
        qnaRepository.updateQnADeleted(checkQnA.getQId());

        return new ResResultDto(checkQnA.getQId(), "문의 사항을 삭제 했습니다.");
    }
}
