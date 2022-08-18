package com.danakga.webservice.user.service.Impl;

import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Comment;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.board.repository.CommentRepository;
import com.danakga.webservice.company.dto.request.CompanyInfoDto;
import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.company.repository.CompanyRepository;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.review.dto.response.ResReviewListDto;
import com.danakga.webservice.review.model.Review;
import com.danakga.webservice.review.repository.ReviewRepository;
import com.danakga.webservice.user.dto.request.UpdateUserInfoDto;
import com.danakga.webservice.user.dto.request.UserAdapter;
import com.danakga.webservice.user.dto.request.UserInfoDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;


    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        UserInfo userInfo = userRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException((userid)));

        return new UserAdapter(userInfo);
    }

    //회원가입
    @Override
    public Long join(UserInfoDto userInfoDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawPassword = userInfoDto.getPassword();
        userInfoDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));

        //중복 id,email 검증
        Integer idCheckResult = userIdCheck(userInfoDto.getUserid());
        Integer emailCheckResult = emailCheck(userInfoDto.getEmail());

        if(idCheckResult.equals(-1)||emailCheckResult.equals(-1)) {
            return -1L;
        }
        return userRepository.save(
                UserInfo.builder()
                        .userid(userInfoDto.getUserid())
                        .password(userInfoDto.getPassword())
                        .name(userInfoDto.getName())
                        .phone(userInfoDto.getPhone())
                        .email(userInfoDto.getEmail())
                        .role(UserRole.ROLE_USER)//임시로 권한 USER로 지정
                        .userAdrNum(userInfoDto.getUserAdrNum())
                        .userLotAdr(userInfoDto.getUserLotAdr())
                        .userStreetAdr(userInfoDto.getUserStreetAdr())
                        .userDetailAdr(userInfoDto.getUserDetailAdr())
                        .userEnabled(true)
                        .build()
        ).getId();
    }

    //유저 아이디 중복 체크
    @Override
    public Integer userIdCheck(String userid) {
        //.isPresent , Optional객체가 있으면 true null이면 false 반환
        if (userRepository.findByUserid(userid).isPresent()) {
            return -1; //같은 userid있으면 -1반환
        }
        return 1;
    }

    //이메일 중복 체크
    @Override
    public Integer emailCheck(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return -1; //같은 이메일 존재할 때
        }
        return 1; // 같은 이메일 없을 때
    }

    //회원 정보 수정
    @Transactional
    @Override
    public Long update(UserInfo userInfo, UpdateUserInfoDto updateUserInfoDto) {
        //로그인 사용자 검증 이후 동작함
        if(userRepository.findById(userInfo.getId()).isEmpty()) {
            return -1L;
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserInfo modifyUser = userRepository.findById(userInfo.getId()).get();

        String rawCheckPassword = updateUserInfoDto.getCheckPassword();

        if(bCryptPasswordEncoder.matches(rawCheckPassword,modifyUser.getPassword())){

            String rawPassword = updateUserInfoDto.getPassword();
            updateUserInfoDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));

            userRepository.save(
                    UserInfo.builder()
                            .id(modifyUser.getId()) //로그인 유저 키값을 받아옴
                            .userid(modifyUser.getUserid()) //그대로 유지
                            .password(updateUserInfoDto.getPassword())
                            .name(updateUserInfoDto.getName())
                            .phone(updateUserInfoDto.getPhone())
                            .email(modifyUser.getEmail())
                            .role(modifyUser.getRole())
                            .userAdrNum(updateUserInfoDto.getUserAdrNum())
                            .userLotAdr(updateUserInfoDto.getUserLotAdr())
                            .userStreetAdr(updateUserInfoDto.getUserStreetAdr())
                            .userDetailAdr(updateUserInfoDto.getUserDetailAdr())
                            .userEnabled(modifyUser.isUserEnabled())
                            .build()
            );
            return userInfo.getId();
        }
        return -2L;
        }

    //회원 탈퇴
    @Override
    @Transactional
    public Long userDeleted(UserInfo userInfo, String password) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserInfo deleteUser = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("등록된 회원이 없습니다."));

        if(!deleteUser.getRole().equals(UserRole.ROLE_USER)) return -2L; //사업자 탈퇴먼저 진행

        if(bCryptPasswordEncoder.matches(password,deleteUser.getPassword())) {
            userRepository.save(
                    UserInfo.builder()
                            .id(deleteUser.getId()) //로그인 유저 키값을 받아옴
                            .userid(deleteUser.getUserid()) //그대로 유지
                            .password(deleteUser.getPassword())
                            .name(deleteUser.getName())
                            .phone(deleteUser.getPhone())
                            .email(deleteUser.getEmail())
                            .role(deleteUser.getRole())
                            .userAdrNum(deleteUser.getUserAdrNum())
                            .userStreetAdr(deleteUser.getUserStreetAdr())
                            .userLotAdr(deleteUser.getUserLotAdr())
                            .userDetailAdr(deleteUser.getUserDetailAdr())
                            .userDeletedDate(LocalDateTime.now()) //현재시간
                            .userEnabled(false)//사용자 이용 중지
                            .build()
            );
            return userInfo.getId();
        }
        else
            return -1L; // 비밀번호 매칭 실패시
        }

    //회원 정보 조회
    @Override
    public UserInfo userInfoCheck(UserInfo userInfo) {

        if(userRepository.findById(userInfo.getId()).isPresent()) {
            return  userRepository.findById(userInfo.getId()).get();
        }
        return null;
    }

    //사업자 등록
    //일반회원으로 등록된 사용자의 사업자 등록
    @Override
    public Long companyRegister(UserInfo userInfo, UserInfoDto userInfoDto, CompanyInfoDto companyInfoDto) {

        if(companyRepository.findByUserInfo(userInfo).isPresent()){
            return -1L;//가입된 유저 정보 있으면 -1L
        }

        if(userRepository.findById(userInfo.getId()).isPresent()){

            UserInfo registerUserInfo = userRepository.findById(userInfo.getId()).get();


            userRepository.save(
                    UserInfo.builder()
                            .id(registerUserInfo.getId())
                            .userid(registerUserInfo.getUserid())
                            .password(registerUserInfo.getPassword())
                            .name(registerUserInfo.getName())
                            .phone(registerUserInfo.getPhone())
                            .email(registerUserInfo.getEmail())
                            .role(UserRole.ROLE_MANAGER)
                            .userAdrNum(registerUserInfo.getUserAdrNum())
                            .userStreetAdr(registerUserInfo.getUserStreetAdr())
                            .userLotAdr(registerUserInfo.getUserLotAdr())
                            .userDetailAdr(registerUserInfo.getUserDetailAdr())
                            .userEnabled(registerUserInfo.isUserEnabled())
                            .build()
            );
            companyRepository.save(
                    CompanyInfo.builder()
                            .companyId(companyInfoDto.getCompanyId())
                            .userInfo(registerUserInfo)
                            .companyName(companyInfoDto.getCompanyName())
                            .companyNum(companyInfoDto.getCompanyNum())
                            .companyAdrNum(companyInfoDto.getCompanyAdrNum())
                            .companyLotAdr(companyInfoDto.getCompanyLotAdr())
                            .companyStreetAdr(companyInfoDto.getCompanyStreetAdr())
                            .companyDetailAdr(companyInfoDto.getCompanyDetailAdr())
                            .companyBankName(companyInfoDto.getCompanyBankName())
                            .companyBanknum(companyInfoDto.getCompanyBanknum())
                            .companyEnabled(true)
                            .build()
            );            return registerUserInfo.getId();

        }
        return -1L;
    }

    //탈퇴한 사업자 복구
    @Override
    public Long companyRestore(UserInfo userInfo,String password) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (userRepository.findById(userInfo.getId()).isEmpty()) {
            return -1L; //사용자 정보 없음
        }

        UserInfo restoreUserInfo = userRepository.findById(userInfo.getId()).get();

        if (companyRepository.findByUserInfoAndCompanyEnabled(restoreUserInfo,false).isEmpty()) {
            return -1L; //로그인된 사용자가 사업자 등록이 안되어있거나,이용중지된 사업자가 아님
        }

        if(!bCryptPasswordEncoder.matches(password,restoreUserInfo.getPassword())) {
            return -2L; //비밀번호 확인 실패시
        }

        CompanyInfo restoreComUserInfo = companyRepository.findByUserInfoAndCompanyEnabled(restoreUserInfo,false).get();


            userRepository.save(
                    UserInfo.builder()
                            .id(restoreUserInfo.getId())
                            .userid(restoreUserInfo.getUserid())
                            .password(restoreUserInfo.getPassword())
                            .name(restoreUserInfo.getName())
                            .phone(restoreUserInfo.getPhone())
                            .email(restoreUserInfo.getEmail())
                            .role(UserRole.ROLE_MANAGER)
                            .userAdrNum(restoreUserInfo.getUserAdrNum())
                            .userStreetAdr(restoreUserInfo.getUserStreetAdr())
                            .userLotAdr(restoreUserInfo.getUserLotAdr())
                            .userDetailAdr(restoreUserInfo.getUserDetailAdr())
                            .userEnabled(restoreUserInfo.isUserEnabled())
                            .build()
            );
            companyRepository.save(
                    CompanyInfo.builder()
                            .companyId(restoreComUserInfo.getCompanyId())
                            .userInfo(restoreUserInfo)
                            .companyName(restoreComUserInfo.getCompanyName())
                            .companyNum(restoreComUserInfo.getCompanyNum())
                            .companyAdrNum(restoreComUserInfo.getCompanyAdrNum())
                            .companyLotAdr(restoreComUserInfo.getCompanyLotAdr())
                            .companyStreetAdr(restoreComUserInfo.getCompanyStreetAdr())
                            .companyDetailAdr(restoreComUserInfo.getCompanyDetailAdr())
                            .companyBankName(restoreComUserInfo.getCompanyBankName())
                            .companyBanknum(restoreComUserInfo.getCompanyBanknum())
                            .companyEnabled(true)
                            .companyDeletedDate(null)
                            .build()
            );
            return restoreUserInfo.getId();
    }

    @Override
    public String useridFind(UserInfoDto userInfoDto) {
        if(userRepository.findByEmailAndPhone(userInfoDto.getEmail(),userInfoDto.getPhone()).isPresent()){
            UserInfo findUserInfo = userRepository.findByEmailAndPhone(userInfoDto.getEmail(),userInfoDto.getPhone()).get();
            return findUserInfo.getUserid();
        }
        return null;
    }

    @Override
    public String passwordFind(UserInfoDto userInfoDto) {
        if(userRepository.findByUseridAndEmailAndPhone(userInfoDto.getUserid(),userInfoDto.getEmail(),userInfoDto.getPhone()).isPresent()){
            UserInfo findUserInfo = userRepository.findByEmailAndPhone(userInfoDto.getEmail(),userInfoDto.getPhone()).get();
            return findUserInfo.getPassword();
        }
        return null;
    }

    /**               작성한 게시글, 댓글, 후기 조회 작업 (진모)               **/

    //작성한 게시글 목록 조회
    @Override
    public ResBoardListDto myPostList(UserInfo userInfo, String boardType, Pageable pageable, int page) {

        //회원 조회
        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원을 찾을 수 없습니다."));

        //pagedble로 db 조회
        pageable = PageRequest.of(page, 10, Sort.by("bdCreated").descending());
        Page<Board> boards = boardRepository.findAllByUserInfoAndBdType(recentUserInfo, boardType, pageable);

        //return할 dto 객체 생성
        ResBoardListDto resBoardListDto = new ResBoardListDto();

        //dto에 담아줄 List<Map> 생성
        List<Map<String, Object>> postList = new ArrayList<>();

        //List로 반환된 db 반복문으로 Map으로 get
        boards.forEach(entity -> {

            //List<Map>에 당아줄 Map객체 생성 후 put
            Map<String, Object> mapPost = new HashMap<>();
            mapPost.put("bd_id", entity.getBdId());
            mapPost.put("bd_title", entity.getBdTitle());
            mapPost.put("bd_writer", entity.getBdWriter());
            mapPost.put("bd_created", entity.getBdCreated());
            mapPost.put("bd_views", entity.getBdViews());
            mapPost.put("bd_deleted", entity.getBdDeleted());
            mapPost.put("totalElement", boards.getTotalElements());
            mapPost.put("totalPage", boards.getTotalPages());

            //dto에 담아줄 List<Map>에 담기
            postList.add(mapPost);
        });

        //dto에 List<Map>값 set
        resBoardListDto.setLists(postList);

        return resBoardListDto;
    }

    //마이페이지 댓글의 게시글 조회
    @Override
    public ResBoardListDto myCommentsPost(UserInfo userInfo, String boardType, Pageable pageable, int page) {

        //회원 조회
        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원을 찾을 수 없습니다."));

        //삭제 여부 변수
        final String deleted = "N";

        //게시판 목록을 불러오기 위한 List
        pageable = PageRequest.of(page, 10, Sort.by("bdCreated").descending());
        Page<Board> boards = boardRepository.myCommentsPost(recentUserInfo.getUserid(), boardType, deleted, pageable);

        //return해줄 dto 객체 생성
        ResBoardListDto resBoardListDto = new ResBoardListDto();

        //dto에 담아줄 List<Map> 생성
        List<Map<String, Object>> data = new ArrayList<>();

        //List로 반환된 db 반복문으로 Map으로 get
        boards.forEach(entity -> {

            //List<Map>에 당아줄 Map객체 생성 후 put
            Map<String, Object> mapComments = new HashMap<>();
            mapComments.put("bd_id", entity.getBdId());
            mapComments.put("bd_title", entity.getBdTitle());
            mapComments.put("bd_writer", entity.getBdWriter());
            mapComments.put("bd_created", entity.getBdCreated());
            mapComments.put("bd_views", entity.getBdViews());
            mapComments.put("totalElement", boards.getTotalElements());
            mapComments.put("totalPage", boards.getTotalPages());

            //dto에 담아줄 List<Map>에 담기
            data.add(mapComments);
        });

        //dto에 List<Map>값 set
        resBoardListDto.setLists(data);

        return resBoardListDto;
    }

    //마이페이지 댓글 조회
    @Override
    public ResCommentListDto myCommentsList(UserInfo userInfo, String boardType, Pageable pageable, int page) {

        //회원 조회
        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원을 찾을 수 없습니다."));

        //삭제 여부 변수
        final String deleted = "N";

        //페이징 db 조회
        pageable = PageRequest.of(page, 10, Sort.by("cmCreated").descending());
        Page<Board_Comment> comments = commentRepository.myCommentsList(recentUserInfo.getUserid(), boardType, deleted, pageable);

        //return해줄 dto 객체 생성
        ResCommentListDto resCommentListDto = new ResCommentListDto();

        //dto에 담아줄 List<Map> 생성
        List<Map<String, Object>> data = new ArrayList<>();

        //List로 반환된 db 반복문으로 Map으로 get
        comments.forEach(entity -> {

            //List<Map>에 당아줄 Map객체 생성 후 put
            Map<String, Object> mapComments = new HashMap<>();
            mapComments.put("cm_id", entity.getCmId());
            mapComments.put("cm_content", entity.getCmContent());
            mapComments.put("cm_writer", entity.getCmWriter());
            mapComments.put("cm_created", entity.getCmCreated());
            mapComments.put("cm_modify", entity.getCmModified());
            mapComments.put("bd_id", entity.getBoard().getBdId());
            mapComments.put("totalElement", comments.getTotalElements());
            mapComments.put("totalPage", comments.getTotalPages());

            //dto에 담아줄 List<Map>에 담기
            data.add(mapComments);
        });

        //dto에 값 set
        resCommentListDto.setComments(data);

        return resCommentListDto;
    }

    //마이페이지 후기 목록 조회
    @Override
    public ResReviewListDto myReviewList(UserInfo userInfo, Pageable pageable, int page) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원을 찾을 수 없습니다."));

        pageable = PageRequest.of(page, 10, Sort.by("reCreated").descending());
        Page<Review> checkReview = reviewRepository.findByReWriter(userInfo.getUserid(), pageable);

        List<Map<String,Object>> myReviewList = new ArrayList<>();

        Map<String,Object> myReviewMap = new LinkedHashMap<>();

        checkReview.forEach(review -> {

            myReviewMap.put("p_name", review.getProduct().getProductName());
            myReviewMap.put("p_brand", review.getProduct().getProductBrand());
            myReviewMap.put("p_price", review.getProduct().getProductPrice());
            myReviewMap.put("re_writer", review.getReWriter());
            myReviewMap.put("re_score", review.getReScore());
            myReviewMap.put("re_created", review.getReCreated());
            myReviewMap.put("totalPages", checkReview.getTotalPages());
            myReviewMap.put("totalElements", checkReview.getTotalElements());
        });

        myReviewList.add(myReviewMap);

        return new ResReviewListDto(myReviewList);
    }
}
