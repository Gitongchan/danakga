package com.danakga.webservice.admin.service.Impl;

import com.danakga.webservice.admin.dto.response.ResAdminProductListDto;
import com.danakga.webservice.admin.service.AdminActivityService;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Comment;
import com.danakga.webservice.board.model.Board_Files;
import com.danakga.webservice.board.repository.BoardFileRepository;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.board.repository.CommentRepository;
import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.product.dto.response.ResProductListDto;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.review.dto.response.ResReviewListDto;
import com.danakga.webservice.review.model.Review;
import com.danakga.webservice.review.repository.ReviewRepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminActivityServiceImpl implements AdminActivityService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;


    /* ======================================= 게시판 ======================================= */

    /* 관리자 게시판 목록 */
    /* sort == deleted N, Y */
    @Override
    public ResBoardListDto adminBoardListDto(UserInfo userInfo, String sort, String type, Pageable pageable, int page) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        pageable = PageRequest.of(page, 10, Sort.by("bdCreated").descending());
        Page<Board> checkBoard = boardRepository.findAllByBdDeletedAndBdType(sort, type, pageable);

        List<Map<String,Object>> adminBoardList = new ArrayList<>();

        checkBoard.forEach(entity -> {

            Map<String,Object> adminBoardMap = new LinkedHashMap<>();

            adminBoardMap.put("bd_id", entity.getBdId());
            adminBoardMap.put("bd_type", entity.getBdType());
            adminBoardMap.put("bd_title", entity.getBdTitle());
            adminBoardMap.put("bd_writer", entity.getBdWriter());
            adminBoardMap.put("bd_created", entity.getBdCreated());
            adminBoardMap.put("bd_views", entity.getBdViews());
            adminBoardMap.put("bd_deleted", entity.getBdDeleted());
            adminBoardMap.put("totalElement", checkBoard.getTotalElements());
            adminBoardMap.put("totalPage", checkBoard.getTotalPages());

            adminBoardList.add(adminBoardMap);
        });

        return new ResBoardListDto(adminBoardList);
    }
    
    /* 관리자 게시판 검색 */
    @Override
    public ResBoardListDto adminBoardSearch(UserInfo userInfo, Pageable pageable, int page, String category, String sort, String type, String content) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        pageable = PageRequest.of(page, 10, Sort.by("bdCreated").descending());
        Page<Board> checkBoard;

        List<Map<String, Object>> adminSearchList = new ArrayList<>();
        
        switch (category) {
            case "제목" : // 게시글 제목
                checkBoard = boardRepository.SearchBoardTitle(sort, content, type, pageable);
                break;
            case "내용" :
                checkBoard = boardRepository.SearchBoardContent(sort, content, type, pageable);
                break;
            case "작성자" :
                checkBoard = boardRepository.SearchBoardWriter(sort, content, type, pageable);
                break;
            case "전체" :
                if(content.equals("")) {
                    checkBoard = boardRepository.findAllByBdDeletedAndBdType(sort, type, pageable);
                } else {
                    checkBoard = boardRepository.searchBoard(content, sort, type, pageable);
                }
                break;
            default:
                checkBoard = null;
        }

        if(checkBoard != null) {

            checkBoard.forEach(entity -> {

                Map<String, Object> adminSearchMap = new LinkedHashMap<>();

                adminSearchMap.put("bd_id", entity.getBdId());
                adminSearchMap.put("bd_type", entity.getBdType());
                adminSearchMap.put("bd_title", entity.getBdTitle());
                adminSearchMap.put("bd_writer", entity.getBdWriter());
                adminSearchMap.put("bd_created", entity.getBdCreated());
                adminSearchMap.put("bd_views", entity.getBdViews());
                adminSearchMap.put("bd_deleted", entity.getBdDeleted());
                adminSearchMap.put("totalElement", checkBoard.getTotalElements());
                adminSearchMap.put("totalPage", checkBoard.getTotalPages());
                adminSearchList.add(adminSearchMap);
            });
        }
        return new ResBoardListDto(adminSearchList);
    }

    /* 게시글 삭제 */
    @Override
    public ResResultDto adminPostDelete(UserInfo userInfo, Long bd_id) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        Board checkBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        /* 게시글 삭제 */
        boardRepository.deleteById(checkBoard.getBdId());

        return new ResResultDto(checkBoard.getBdId(), "게시글을 삭제 했습니다.");
    }


    /* ======================================= 댓글,대댓글 ======================================= */

    /* 관리자 댓글, 대댓글 목록 */
    @Override
    public ResCommentListDto adminCommentList(UserInfo userInfo, String type, String sort,Pageable pageable, int page) {

        //댓글, 대댓글 구분
        final int commentStep = 0;
        final int answerStep = 1;

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        List<Map<String, Object>> commentList = new ArrayList<>();

        if(type.equals("댓글")) {

            pageable = PageRequest.of(page, 10, Sort.by("cmCreated").descending());
            Page<Board_Comment> checkComments = commentRepository.findByCmStepAndCmDeleted(commentStep, sort, pageable);

            checkComments.forEach(entity -> {

                Map<String, Object> commentsMap = new LinkedHashMap<>();

                commentsMap.put("cm_id", entity.getCmId());
                commentsMap.put("cm_content", entity.getCmContent());
                commentsMap.put("cm_writer", entity.getCmWriter());
                commentsMap.put("cm_step", entity.getCmStep());
                commentsMap.put("cm_deleted", entity.getCmDeleted());
                commentsMap.put("cm_created", entity.getCmCreated());
                commentsMap.put("cm_modify", entity.getCmModified());
                commentsMap.put("cm_answerNum", entity.getCmAnswerNum());
                commentsMap.put("board_id", entity.getBoard().getBdId());
                commentsMap.put("totalElement", checkComments.getTotalElements());
                commentsMap.put("totalPage", checkComments.getTotalPages());
                commentList.add(commentsMap);

            });
        }
        
        if(type.equals("대댓글")) {

            pageable = PageRequest.of(page, 10, Sort.by("cmCreated").descending());
            Page<Board_Comment> checkComments = commentRepository.findByCmStepAndCmDeleted(answerStep, sort, pageable);

            checkComments.forEach(entity -> {

                Map<String, Object> answerMap = new LinkedHashMap<>();

                answerMap.put("cm_id", entity.getCmId());
                answerMap.put("cm_content", entity.getCmContent());
                answerMap.put("cm_writer", entity.getCmWriter());
                answerMap.put("cm_step", entity.getCmStep());
                answerMap.put("cm_deleted", entity.getCmDeleted());
                answerMap.put("cm_created", entity.getCmCreated());
                answerMap.put("cm_modify", entity.getCmModified());
                answerMap.put("cm_parentNum", entity.getCmParentNum());
                answerMap.put("board_id", entity.getBoard().getBdId());
                answerMap.put("totalElement", checkComments.getTotalElements());
                answerMap.put("totalPage", checkComments.getTotalPages());
                commentList.add(answerMap);
            });
        }

        return new ResCommentListDto(commentList);
    }
    
    /* 관리자 댓글, 대댓글 검색 */
    @Override
    public ResCommentListDto adminCommentSearch(UserInfo userInfo, Pageable pageable, int page,
                                                String category, String sort, String type, String content) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        pageable = PageRequest.of(page, 10, Sort.by("cmCreated").descending());
        Page<Board_Comment> checkComments;

        List<Map<String, Object>> searchCommentList = new ArrayList<>();

        //댓글, 대댓글 구분
        final int commentStep = 0;
        final int answerStep = 1;

        /* 댓글 검색 */
        if(type.equals("댓글")) {

            switch (category) {
                case "내용" :
                    checkComments = commentRepository.SearchBoardContent(sort, content, commentStep, pageable);
                    break;
                case "작성자" :
                    checkComments = commentRepository.SearchBoardWriter(sort, content, commentStep, pageable);
                    break;
                case "전체" :
                    if(content.equals("")) {
                        checkComments = commentRepository.findByCmDeletedAndCmStep(sort, commentStep, pageable);
                    } else {
                        checkComments = commentRepository.searchComment(content, sort, commentStep, pageable);
                    }
                    break;
                    default :
                        checkComments = null;
                        break;
            }

            if(checkComments != null) {

                long totalPages = checkComments.getTotalPages();
                long totalElements = checkComments.getTotalElements();

                checkComments.forEach(entity -> {

                    Map<String, Object> searchCommentMap = new LinkedHashMap<>();

                    searchCommentMap.put("cm_id", entity.getCmId());
                    searchCommentMap.put("cm_content", entity.getCmContent());
                    searchCommentMap.put("cm_writer", entity.getCmWriter());
                    searchCommentMap.put("cm_step", entity.getCmStep());
                    searchCommentMap.put("cm_deleted", entity.getCmDeleted());
                    searchCommentMap.put("cm_created", entity.getCmCreated());
                    searchCommentMap.put("cm_modify", entity.getCmModified());
                    searchCommentMap.put("cm_answerNum", entity.getCmAnswerNum());
                    searchCommentMap.put("cm_parentNum", entity.getCmParentNum());
                    searchCommentMap.put("board_id", entity.getBoard().getBdId());
                    searchCommentMap.put("totalElement", totalElements);
                    searchCommentMap.put("totalPage", totalPages);
                    
                    searchCommentList.add(searchCommentMap);
                });
            }
        }

        if(type.equals("대댓글")) {
            switch (category) {
                case "내용" :
                    checkComments = commentRepository.SearchBoardContent(sort, content, answerStep, pageable);
                    break;
                case "작성자" :
                    checkComments = commentRepository.SearchBoardWriter(sort, content, answerStep, pageable);
                    break;
                case "전체" :
                    if(content.equals("")) {
                        checkComments = commentRepository.findByCmDeletedAndCmStep(sort, answerStep, pageable);
                    } else {
                        checkComments = commentRepository.searchComment(content, sort, answerStep, pageable);
                    }
                    break;
                    default :
                        checkComments = null;
                        break;
            }

            if(checkComments != null) {

                long totalPages = checkComments.getTotalPages();
                long totalElements = checkComments.getTotalElements();

                checkComments.forEach(entity -> {

                    Map<String, Object> searchCommentMap = new LinkedHashMap<>();

                    searchCommentMap.put("cm_id", entity.getCmId());
                    searchCommentMap.put("cm_content", entity.getCmContent());
                    searchCommentMap.put("cm_writer", entity.getCmWriter());
                    searchCommentMap.put("cm_step", entity.getCmStep());
                    searchCommentMap.put("cm_deleted", entity.getCmDeleted());
                    searchCommentMap.put("cm_created", entity.getCmCreated());
                    searchCommentMap.put("cm_modify", entity.getCmModified());
                    searchCommentMap.put("cm_answerNum", entity.getCmAnswerNum());
                    searchCommentMap.put("cm_parentNum", entity.getCmParentNum());
                    searchCommentMap.put("board_id", entity.getBoard().getBdId());
                    searchCommentMap.put("totalElement", totalElements);
                    searchCommentMap.put("totalPage", totalPages);

                    searchCommentList.add(searchCommentMap);
                });
            }
        }

        return new ResCommentListDto(searchCommentList);
    }

    /* 관리자 댓글, 대댓글 삭제 */
    @Override
    public ResResultDto adminCommentDelete(UserInfo userInfo, Long bd_id, Long cm_id) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        Board checkBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        Board_Comment checkComment = commentRepository.findById(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        /* 댓글 삭제 */
        commentRepository.deleteById(checkComment.getCmId());

        /* 대댓도 같이 삭제 */
        commentRepository.deleteByCmParentNum(checkComment.getCmId().intValue());

        return new ResResultDto(checkComment.getCmId(),"댓글을 삭제 했습니다.");
    }

    @Override
    public ResResultDto adminCommentAnswerDelete(UserInfo userInfo, Long bd_id, Long cm_id, Long an_id) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        Board checkBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        Board_Comment checkComment = commentRepository.findById(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        Board_Comment checkAnswer = commentRepository.findByCmParentNumAndCmId(cm_id.intValue(), an_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("대댓글을 찾을 수 없습니다."));

        /* 대댓 삭제 */
        commentRepository.deleteById(checkAnswer.getCmId());

        //댓글의 대댓글 갯수 -1
        commentRepository.deleteAnswerNum(cm_id);

        return new ResResultDto(checkAnswer.getCmId(),"대댓글을 삭제 했습니다.");
    }


    /* ======================================= 상품 ======================================= */

    /* 관리자 상품 목록 */
    @Override
    public ResAdminProductListDto adminProductList(UserInfo userInfo, Pageable pageable, int page) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        List<Map<String,Object>> adminProductList = new ArrayList<>();

        pageable = PageRequest.of(page, 10, Sort.by("productUploadDate").descending());
        Page<Product> checkProductList = productRepository.findAll(pageable);

        checkProductList.forEach(product -> {

            /* 상품 아이디 값 하나씩 순회 하면서 평점 출력 */
            Product checkProduct = productRepository.findByProductIdAndCompanyEnabled(product.getProductId())
                    .orElseThrow(()->new CustomException.ResourceNotFoundException("상품 정보를 찾을 수 없습니다.")
            ) ;

            double productRating; // 상품 평점
            if(productRepository.selectProductRating(checkProduct) == null){
                productRating = 0;
            }else{
                productRating = Math.round(productRepository.selectProductRating(checkProduct)*10)/10.0;
            }

            Map<String,Object> adminProductMap = new LinkedHashMap<>();

            adminProductMap.put("p_id", product.getProductId());
            adminProductMap.put("p_brand", product.getProductBrand());
            adminProductMap.put("p_type", product.getProductType());
            adminProductMap.put("p_subtype", product.getProductSubType());
            adminProductMap.put("p_name", product.getProductName());
            adminProductMap.put("p_photo", product.getProductPhoto());
            adminProductMap.put("p_price", product.getProductPrice());
            adminProductMap.put("p_stock", product.getProductStock());
            adminProductMap.put("p_viewCount", product.getProductViewCount());
            adminProductMap.put("p_uploadDate", product.getProductUploadDate());
            adminProductMap.put("p_rating", productRating);
            adminProductMap.put("company_id", product.getProductCompanyId().getCompanyId());
            adminProductMap.put("company_name", product.getProductCompanyId().getCompanyName());
            adminProductMap.put("totalPage", checkProductList.getTotalPages());
            adminProductMap.put("totalElement", checkProductList.getTotalElements());

            adminProductList.add(adminProductMap);

        });

        return new ResAdminProductListDto(adminProductList);
    }
    
    /* 관리자 상품 검색 */
    @Override
    public ResAdminProductListDto adminProductSearch(UserInfo userInfo, Pageable pageable, int page, String category, String content) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        pageable = PageRequest.of(page, 10, Sort.by("productUploadDate").descending());
        Page<Product> productSearch;

        List<Map<String, Object>> adminProductList = new ArrayList<>();

        switch (category) {
            case "가게" :
                productSearch = productRepository.adminProductComName(content, pageable);
                break;
            case "상품" :
                productSearch = productRepository.adminProductName(content, pageable);
                break;
            case "브랜드" :
                productSearch = productRepository.adminProductBrand(content, pageable);
                break;
            default :
                productSearch = null;
                break;
        }

        if(productSearch != null) {

            long totalPages = productSearch.getTotalPages();
            long totalElements = productSearch.getTotalElements();

            productSearch.forEach(product -> {

                /* 상품 아이디 값 하나씩 순회 하면서 평점 출력 */
                Product checkProduct = productRepository.findByProductIdAndCompanyEnabled(product.getProductId())
                        .orElseThrow(()->new CustomException.ResourceNotFoundException("상품 정보를 찾을 수 없습니다.")
                ) ;

                double productRating; // 상품 평점
                if(productRepository.selectProductRating(checkProduct) == null){
                    productRating = 0;
                }else{
                    productRating = Math.round(productRepository.selectProductRating(checkProduct)*10)/10.0;
                }

                Map<String,Object> adminProductMap = new LinkedHashMap<>();

                adminProductMap.put("p_id", product.getProductId());
                adminProductMap.put("p_brand", product.getProductBrand());
                adminProductMap.put("p_type", product.getProductType());
                adminProductMap.put("p_subtype", product.getProductSubType());
                adminProductMap.put("p_name", product.getProductName());
                adminProductMap.put("p_photo", product.getProductPhoto());
                adminProductMap.put("p_price", product.getProductPrice());
                adminProductMap.put("p_stock", product.getProductStock());
                adminProductMap.put("p_viewCount", product.getProductViewCount());
                adminProductMap.put("p_uploadDate", product.getProductUploadDate());
                adminProductMap.put("p_rating", productRating);
                adminProductMap.put("company_id", product.getProductCompanyId().getCompanyId());
                adminProductMap.put("company_name", product.getProductCompanyId().getCompanyName());
                adminProductMap.put("totalPages", totalPages);
                adminProductMap.put("totalElements", totalElements);

                adminProductList.add(adminProductMap);
            });
        }
        return new ResAdminProductListDto(adminProductList);
    }
    
    /* 관리자 상품 삭제 */
    @Override
    public ResResultDto adminProductDelete(UserInfo userInfo, Long c_id, Long p_id) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        CompanyInfo checkCompanyInfo = companyRepository.findById(c_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("가게 정보를 찾을 수 없습니다."));

        Product checkProduct = productRepository.findByProductIdAndProductCompanyId(p_id, checkCompanyInfo)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("상품을 찾을 수 없습니다."));

        /* 상품 삭제 -> onDelete로 이미지, 후기, qna 모두 삭제 */
        productRepository.deleteByProductIdAndProductCompanyId(checkProduct.getProductId(), checkCompanyInfo);

        return new ResResultDto(checkProduct.getProductId(),"상품을 삭제 했습니다.");
    }

    
    /* ======================================= 후기 ======================================= */

    /* 관리자 후기 목록 조회 */
    @Override
    public ResReviewListDto adminReviewList(UserInfo userInfo, Pageable pageable, int page, String sort) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        pageable = PageRequest.of(page, 10, Sort.by("reCreated").descending());
        Page<Review> checkReviewList = reviewRepository.findByReDeleted(pageable, sort);

        List<Map<String, Object>> adminReviewList = new ArrayList<>();

        checkReviewList.forEach(review -> {

            Map<String, Object> adminReviewMap = new LinkedHashMap<>();

            adminReviewMap.put("re_id", review.getReId());
            adminReviewMap.put("re_content", review.getReContent());
            adminReviewMap.put("re_created", review.getReCreated());
            adminReviewMap.put("re_score", review.getReScore());
            adminReviewMap.put("re_writer", review.getReWriter());
            adminReviewMap.put("product_id", review.getProduct().getProductId());
            adminReviewMap.put("product_name", review.getProduct().getProductName());
            adminReviewMap.put("company_id", review.getProduct().getProductCompanyId().getCompanyId());
            adminReviewMap.put("company_name", review.getProduct().getProductCompanyId().getCompanyName());
            adminReviewMap.put("totalPages", checkReviewList.getTotalPages());
            adminReviewMap.put("totalElements", checkReviewList.getTotalElements());

            adminReviewList.add(adminReviewMap);
        });

        return new ResReviewListDto(adminReviewList);
    }
    
    /* 관리자 후기 검색 */

    @Override
    public ResReviewListDto adminReviewSearch(UserInfo userInfo, Pageable pageable, int page,
                                              String category, String sort, String content) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        pageable = PageRequest.of(page, 10, Sort.by("reCreated").descending());
        Page<Review> checkReviewList;

        switch (category) {
            case "내용" :
                checkReviewList = reviewRepository.SearchReviewContent(content, sort, pageable);
                break;
            case "상품" :
                checkReviewList = reviewRepository.SearchProductName(content, sort, pageable);
                break;
            case "작성자" :
                checkReviewList = reviewRepository.SearchReviewWriter(content, sort, pageable);
                break;
            default :
                checkReviewList = null;
                break;
        }

        List<Map<String, Object>> adminReviewList = new ArrayList<>();

        if(checkReviewList != null) {

            checkReviewList.forEach(review -> {

                Map<String, Object> adminReviewMap = new LinkedHashMap<>();

                adminReviewMap.put("re_id", review.getReId());
                adminReviewMap.put("re_content", review.getReContent());
                adminReviewMap.put("re_created", review.getReCreated());
                adminReviewMap.put("re_score", review.getReScore());
                adminReviewMap.put("re_writer", review.getReWriter());
                adminReviewMap.put("product_id", review.getProduct().getProductId());
                adminReviewMap.put("product_name", review.getProduct().getProductName());
                adminReviewMap.put("company_id", review.getProduct().getProductCompanyId().getCompanyId());
                adminReviewMap.put("company_name", review.getProduct().getProductCompanyId().getCompanyName());
                adminReviewMap.put("totalPages", checkReviewList.getTotalPages());
                adminReviewMap.put("totalElements", checkReviewList.getTotalElements());

                adminReviewList.add(adminReviewMap);
            });
        }

        return new ResReviewListDto(adminReviewList);
    }

    /* 관리자 후기 삭제 */
    @Override
    public ResResultDto adminReviewDelete(UserInfo userInfo, Long c_id, Long p_id, Long r_id) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        CompanyInfo checkCompanyInfo = companyRepository.findById(c_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("상점을 찾을 수 없습니다."));

        Product checkProduct = productRepository.findById(p_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("상품을 찾을 수 없습니다."));

        Review checkReview = reviewRepository.findById(r_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("후기를 찾을 수 없습니다."));

        /* 후기 삭제 */
        reviewRepository.deleteById(checkReview.getReId());

        return new ResResultDto(checkReview.getReId(),"후기를 삭제 했습니다.");
    }
}
