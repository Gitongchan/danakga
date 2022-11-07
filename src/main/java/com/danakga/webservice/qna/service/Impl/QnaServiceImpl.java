package com.danakga.webservice.qna.service.Impl;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.qna.dto.request.ReqQnaDto;
import com.danakga.webservice.qna.dto.response.ResQnaDto;
import com.danakga.webservice.qna.model.Qna;
import com.danakga.webservice.qna.service.QnaService;
import com.danakga.webservice.qna.repository.QnaRepository;
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
public class QnaServiceImpl implements QnaService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final QnaRepository qnaRepository;

    /* 문의사항 목록 */
    @Override
    public ResQnaDto qnaList(Pageable pageable, Integer q_sort, int page, Long p_id) {

        final String deleted = "N";

        List<Map<String, Object>> qnaList = new ArrayList<>();

        /* 사이트 문의사항 목록 */
        if(p_id == null) {
            /* 들어오는 sort값 따라서 사이트, 가게 문의사항 구분 */
            pageable = PageRequest.of(page, 10, Sort.by("qnCreated").descending());
            Page<Qna> checkQna = qnaRepository.findByQnDeletedAndQnSort(pageable, deleted, q_sort);

            checkQna.forEach(entity -> {

                Map<String,Object> siteQnaMap = new LinkedHashMap<>();

                siteQnaMap.put("qn_id", entity.getQnId());
                siteQnaMap.put("qn_type", entity.getQnType());
                siteQnaMap.put("qn_userid", entity.getUserInfo().getUserid());
                siteQnaMap.put("qn_title", entity.getQnTitle());
                siteQnaMap.put("qn_created", entity.getQnCreated());
                siteQnaMap.put("qn_state", entity.getQnState());
                siteQnaMap.put("totalPage", checkQna.getTotalPages());
                siteQnaMap.put("totalElement", checkQna.getTotalPages());

                qnaList.add(siteQnaMap);
            });

            return new ResQnaDto(qnaList);
        }

        /* 상품별 문의사항 목록 */
        Product checkProduct = productRepository.findById(p_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("상품을 찾을 수 없습니다."));

        CompanyInfo checkCompanyInfo = companyRepository.findById(checkProduct.getProductCompanyId().getCompanyId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("가게 정보를 찾을 수 없습니다."));

        pageable = PageRequest.of(page, 10, Sort.by("qnCreated").descending());
        Page<Qna> checkQna = qnaRepository.findByQnDeletedAndProduct(deleted, checkProduct, pageable);

        checkQna.forEach(entity -> {

            Map<String,Object> productQnaMap = new LinkedHashMap<>();

            productQnaMap.put("qn_id", entity.getQnId());
            productQnaMap.put("qn_type", entity.getQnType());
            productQnaMap.put("qn_userid", entity.getUserInfo().getUserid());
            productQnaMap.put("qn_title", entity.getQnTitle());
            productQnaMap.put("qn_created", entity.getQnCreated());
            productQnaMap.put("qn_state", entity.getQnState());
            productQnaMap.put("product_name", entity.getProduct().getProductName());
            productQnaMap.put("company_Info", entity.getCompanyInfo().getCompanyName());
            productQnaMap.put("totalPage", checkQna.getTotalPages());
            productQnaMap.put("totalElement", checkQna.getTotalPages());

            qnaList.add(productQnaMap);

        });

        return new ResQnaDto(qnaList);
    }

    /* 가게 문의사항 전체 목록 */
    @Override
    public ResQnaDto companyQnaList(Pageable pageable, int page, Long c_id) {

        CompanyInfo checkCompanyInfo = companyRepository.findById(c_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("가게 정보를 찾을 수 없습니다."));

        pageable = PageRequest.of(page, 10, Sort.by("qnCreated").descending());
        Page<Qna> checkQna = qnaRepository.findByCompanyInfo(checkCompanyInfo, pageable);

        List<Map<String, Object>> qnaList = new ArrayList<>();

        checkQna.forEach(entity -> {

            Map<String, Object> companyQnaMap = new LinkedHashMap<>();

            companyQnaMap.put("qn_id", entity.getQnId());
            companyQnaMap.put("qn_type", entity.getQnType());
            companyQnaMap.put("qn_userid", entity.getUserInfo().getUserid());
            companyQnaMap.put("qn_title", entity.getQnTitle());
            companyQnaMap.put("qn_created", entity.getQnCreated());
            companyQnaMap.put("qn_state", entity.getQnState());
            companyQnaMap.put("product_name", entity.getProduct().getProductName());
            companyQnaMap.put("totalPage", checkQna.getTotalPages());
            companyQnaMap.put("totalElement", checkQna.getTotalPages());

            qnaList.add(companyQnaMap);

        });

        return new ResQnaDto(qnaList);
    }

    /* 문의사항 조회 */
    @Override
    public ResQnaDto qnaPost(Long qn_id) {

        Qna checkQna = qnaRepository.findById(qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        List<Map<String,Object>> qnaList = new ArrayList<>();

        Map<String, Object> qnaMap = new LinkedHashMap<>();

            qnaMap.put("q_id", checkQna.getQnId());
            qnaMap.put("q_type", checkQna.getQnType());
            qnaMap.put("q_userid", checkQna.getUserInfo().getUserid());
            qnaMap.put("q_title", checkQna.getQnTitle());
            qnaMap.put("q_content", checkQna.getQnContent());
            qnaMap.put("q_created", checkQna.getQnCreated());
            qnaMap.put("q_state", checkQna.getQnState());
            qnaMap.put("q_sort", checkQna.getQnSort());

            qnaList.add(qnaMap);

            return new ResQnaDto(qnaList);
    }

    /* 문의사항 작성 */
    /* dto qSort 0 = 사이트, 1 = 가게 */
    @Transactional
    @Override
    public ResResultDto qnaWrite(UserInfo userInfo, ReqQnaDto reqQnaDto, Long p_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        /* 사이트 문의사항 작성 */
        if (reqQnaDto.getQnaSort() == 0 && p_id == null) {

            Qna qna = qnaRepository.save(
                    Qna.builder()
                            .qnSort(reqQnaDto.getQnaSort())
                            .qnType(reqQnaDto.getQnaType())
                            .qnTitle(reqQnaDto.getQnaContent())
                            .qnWriter(userInfo.getUserid())
                            .qnContent(reqQnaDto.getQnaContent())
                            .userInfo(checkUserInfo)
                            .build()
            );

            return new ResResultDto(qna.getQnId(), "사이트 문의사항을 작성 했습니다.");
        }

        /* 상품 문의사항 작성 */
        Product checkProduct = productRepository.findById(p_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("상품을 찾을 수 없습니다."));

        CompanyInfo checkCompanyInfo = companyRepository.findById(checkProduct.getProductCompanyId().getCompanyId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("가게 정보를 찾을 수 없습니다."));

        Qna qna = qnaRepository.save(
                Qna.builder()
                        .qnSort(reqQnaDto.getQnaSort())
                        .qnType(reqQnaDto.getQnaType())
                        .qnTitle(reqQnaDto.getQnaContent())
                        .qnWriter(userInfo.getUserid())
                        .qnContent(reqQnaDto.getQnaContent())
                        .userInfo(checkUserInfo)
                        .companyInfo(checkCompanyInfo)
                        .product(checkProduct)
                        .build()
        );

        return new ResResultDto(qna.getQnId(), "상품 문의사항을 작성 했습니다.");
    }
    
    /* 문의사항 수정 */
    @Transactional
    @Override
    public ResResultDto qnaEdit(UserInfo userInfo, ReqQnaDto reqQnaDto, Long p_id, Long qn_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        /* 사이트 문의사항 수정 */
        if(p_id == null) {

            Qna checkQna = qnaRepository.findById(qn_id)
                    .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

            checkQna = qnaRepository.save(
                    Qna.builder()
                            .qnId(checkQna.getQnId())
                            .qnSort(checkQna.getQnSort())
                            .qnType(reqQnaDto.getQnaType())
                            .qnTitle(reqQnaDto.getQnaTitle())
                            .qnContent(reqQnaDto.getQnaContent())
                            .qnWriter(checkUserInfo.getUserid())
                            .qnCreated(checkQna.getQnCreated())
                            .qnDeleted(checkQna.getQnDeleted())
                            .userInfo(checkUserInfo)
                            .build()
            );

            return new ResResultDto(checkQna.getQnId(), "사이트 문의사항을 수정 했습니다.");
        }

        /* 상품 문의사항 수정 */
        Product checkProduct = productRepository.findById(p_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("상품을 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findByProductAndQnId(checkProduct, qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        CompanyInfo checkCompanyInfo = companyRepository.findById(checkProduct.getProductCompanyId().getCompanyId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("가게 정보를 찾을 수 없습니다."));

        checkQna = qnaRepository.save(
                Qna.builder()
                        .qnId(checkQna.getQnId())
                        .qnSort(checkQna.getQnSort())
                        .qnType(reqQnaDto.getQnaType())
                        .qnTitle(reqQnaDto.getQnaTitle())
                        .qnContent(reqQnaDto.getQnaContent())
                        .qnWriter(checkUserInfo.getUserid())
                        .qnCreated(checkQna.getQnCreated())
                        .qnDeleted(checkQna.getQnDeleted())
                        .userInfo(checkUserInfo)
                        .companyInfo(checkQna.getCompanyInfo())
                        .product(checkProduct)
                        .build()
        );

        return new ResResultDto(checkQna.getQnId(),"상품 문의사항을 수정 했습니다.");

    }

    /* 문의사항 삭제 상태 변경 */
    @Transactional
    @Override
    public ResResultDto qnaDelete(UserInfo userInfo, Long p_id, Long qn_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        /* 사이트 문의사항 삭제 */
        if(p_id == null) {

            Qna checkQna = qnaRepository.findById(qn_id)
                    .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

            /* 문의사항 삭제 상태 변경 (삭제) */
            qnaRepository.updateQnADeleted(checkQna.getQnId());

            return new ResResultDto(checkQna.getQnId(),"문의사항을 삭제 했습니다.");
        }

        /* 상품 문의사항 삭제 */
        Product checkProduct = productRepository.findById(p_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("상품을 찾을 수 없습니다."));

        Qna checkQna = qnaRepository.findByProductAndQnId(checkProduct, qn_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        /* 문의사항 삭제 상태 변경 (삭제) */
        qnaRepository.updateQnADeleted(checkQna.getQnId());

        return new ResResultDto(checkQna.getQnId(), "문의사항을 삭제 했습니다.");
    }
}
