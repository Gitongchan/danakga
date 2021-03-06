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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;


    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        UserInfo userInfo = userRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException((userid)));

        return new UserAdapter(userInfo);
    }

    //????????????
    @Override
    public Long join(UserInfoDto userInfoDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawPassword = userInfoDto.getPassword();
        userInfoDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));

        //?????? id,email ??????
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
                        .role(UserRole.ROLE_USER)//????????? ?????? USER??? ??????
                        .userAdrNum(userInfoDto.getUserAdrNum())
                        .userLotAdr(userInfoDto.getUserLotAdr())
                        .userStreetAdr(userInfoDto.getUserStreetAdr())
                        .userDetailAdr(userInfoDto.getUserDetailAdr())
                        .userEnabled(true)
                        .build()
        ).getId();
    }

    //?????? ????????? ?????? ??????
    @Override
    public Integer userIdCheck(String userid) {
        //.isPresent , Optional????????? ????????? true null?????? false ??????
        if (userRepository.findByUserid(userid).isPresent()) {
            return -1; //?????? userid????????? -1??????
        }
        return 1;
    }

    //????????? ?????? ??????
    @Override
    public Integer emailCheck(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return -1; //?????? ????????? ????????? ???
        }
        return 1; // ?????? ????????? ?????? ???
    }

    //?????? ?????? ??????
    @Transactional
    @Override
    public Long update(UserInfo userInfo, UpdateUserInfoDto updateUserInfoDto) {
        //????????? ????????? ?????? ?????? ?????????
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
                            .id(modifyUser.getId()) //????????? ?????? ????????? ?????????
                            .userid(modifyUser.getUserid()) //????????? ??????
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

    //?????? ??????
    @Override
    @Transactional
    public Long userDeleted(UserInfo userInfo, String password) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserInfo deleteUser = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("????????? ????????? ????????????."));

        if(!deleteUser.getRole().equals(UserRole.ROLE_USER)) return -2L; //????????? ???????????? ??????

        if(bCryptPasswordEncoder.matches(password,deleteUser.getPassword())) {
            userRepository.save(
                    UserInfo.builder()
                            .id(deleteUser.getId()) //????????? ?????? ????????? ?????????
                            .userid(deleteUser.getUserid()) //????????? ??????
                            .password(deleteUser.getPassword())
                            .name(deleteUser.getName())
                            .phone(deleteUser.getPhone())
                            .email(deleteUser.getEmail())
                            .role(deleteUser.getRole())
                            .userAdrNum(deleteUser.getUserAdrNum())
                            .userStreetAdr(deleteUser.getUserStreetAdr())
                            .userLotAdr(deleteUser.getUserLotAdr())
                            .userDetailAdr(deleteUser.getUserDetailAdr())
                            .userDeletedDate(LocalDateTime.now()) //????????????
                            .userEnabled(false)//????????? ?????? ??????
                            .build()
            );
            return userInfo.getId();
        }
        else
            return -1L; // ???????????? ?????? ?????????
        }

    //?????? ?????? ??????
    @Override
    public UserInfo userInfoCheck(UserInfo userInfo) {

        if(userRepository.findById(userInfo.getId()).isPresent()) {
            return  userRepository.findById(userInfo.getId()).get();
        }
        return null;
    }

    //????????? ??????
    //?????????????????? ????????? ???????????? ????????? ??????
    @Override
    public Long companyRegister(UserInfo userInfo, UserInfoDto userInfoDto, CompanyInfoDto companyInfoDto) {

        if(companyRepository.findByUserInfo(userInfo).isPresent()){
            return -1L;//????????? ?????? ?????? ????????? -1L
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

    //????????? ????????? ??????
    @Override
    public Long companyRestore(UserInfo userInfo,String password) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (userRepository.findById(userInfo.getId()).isEmpty()) {
            return -1L; //????????? ?????? ??????
        }

        UserInfo restoreUserInfo = userRepository.findById(userInfo.getId()).get();

        if (companyRepository.findByUserInfoAndCompanyEnabled(restoreUserInfo,false).isEmpty()) {
            return -1L; //???????????? ???????????? ????????? ????????? ??????????????????,??????????????? ???????????? ??????
        }

        if(!bCryptPasswordEncoder.matches(password,restoreUserInfo.getPassword())) {
            return -2L; //???????????? ?????? ?????????
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

    //????????? ????????? ?????? ??????
    @Override
    public ResBoardListDto myPostList(UserInfo userInfo, String boardType, Pageable pageable, int page) {

        //?????? ??????
        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("????????? ????????? ????????????."));

        //pagedble??? db ??????
        pageable = PageRequest.of(page, 10, Sort.by("bdCreated").descending());
        Page<Board> boards = boardRepository.findAllByUserInfoAndBdType(recentUserInfo, boardType, pageable);

        //page<>??? List??? ??????
        List<Board> myPost = boards.getContent();

        //return??? dto ?????? ??????
        ResBoardListDto resBoardListDto = new ResBoardListDto();

        //dto??? ????????? List<Map> ??????
        List<Map<String, Object>> postList = new ArrayList<>();

        //List??? ????????? db ??????????????? Map?????? get
        myPost.forEach(entity -> {

            //List<Map>??? ????????? Map?????? ?????? ??? put
            Map<String, Object> mapPost = new HashMap<>();
            mapPost.put("bd_id", entity.getBdId());
            mapPost.put("bd_title", entity.getBdTitle());
            mapPost.put("bd_writer", entity.getBdWriter());
            mapPost.put("bd_created", entity.getBdCreated());
            mapPost.put("bd_views", entity.getBdViews());
            mapPost.put("bd_deleted", entity.getBdDeleted());
            mapPost.put("totalElement", boards.getTotalElements());
            mapPost.put("totalPage", boards.getTotalPages());

            //dto??? ????????? List<Map>??? ??????
            postList.add(mapPost);
        });

        //dto??? List<Map>??? set
        resBoardListDto.setLists(postList);

        return resBoardListDto;
    }

    //??????????????? ????????? ????????? ??????
    @Override
    public ResBoardListDto myCommentsPost(UserInfo userInfo, String boardType, Pageable pageable, int page) {

        //?????? ??????
        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("????????? ????????? ????????????."));

        //?????? ?????? ??????
        final String deleted = "N";

        //????????? ????????? ???????????? ?????? List
        pageable = PageRequest.of(page, 10, Sort.by("bdCreated").descending());
        Page<Board> boards = boardRepository.myCommentsPost(recentUserInfo.getUserid(), boardType, deleted, pageable);

        //return?????? dto ?????? ??????
        ResBoardListDto resBoardListDto = new ResBoardListDto();

        //dto??? ????????? List<Map> ??????
        List<Map<String, Object>> data = new ArrayList<>();

        //List??? ????????? db ??????????????? Map?????? get
        boards.forEach(entity -> {

            //List<Map>??? ????????? Map?????? ?????? ??? put
            Map<String, Object> mapComments = new HashMap<>();
            mapComments.put("bd_id", entity.getBdId());
            mapComments.put("bd_title", entity.getBdTitle());
            mapComments.put("bd_writer", entity.getBdWriter());
            mapComments.put("bd_created", entity.getBdCreated());
            mapComments.put("bd_views", entity.getBdViews());
            mapComments.put("totalElement", boards.getTotalElements());
            mapComments.put("totalPage", boards.getTotalPages());

            //dto??? ????????? List<Map>??? ??????
            data.add(mapComments);
        });

        //dto??? List<Map>??? set
        resBoardListDto.setLists(data);

        return resBoardListDto;
    }

    //??????????????? ?????? ??????
    @Override
    public ResCommentListDto myCommentsList(UserInfo userInfo, String boardType, Pageable pageable, int page) {

        //?????? ??????
        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("????????? ????????? ????????????."));

        //?????? ?????? ??????
        final String deleted = "N";

        //????????? db ??????
        pageable = PageRequest.of(page, 10, Sort.by("cmCreated").descending());
        Page<Board_Comment> comments = commentRepository.myCommentsList(recentUserInfo.getUserid(), boardType, deleted, pageable);

        List<Board_Comment> commentsList = comments.getContent();

        //return?????? dto ?????? ??????
        ResCommentListDto resCommentListDto = new ResCommentListDto();

        //dto??? ????????? List<Map> ??????
        List<Map<String, Object>> data = new ArrayList<>();

        //List??? ????????? db ??????????????? Map?????? get
        commentsList.forEach(entity -> {

            //List<Map>??? ????????? Map?????? ?????? ??? put
            Map<String, Object> mapComments = new HashMap<>();
            mapComments.put("cm_id", entity.getCmId());
            mapComments.put("cm_content", entity.getCmContent());
            mapComments.put("cm_writer", entity.getCmWriter());
            mapComments.put("cm_created", entity.getCmCreated());
            mapComments.put("cm_modify", entity.getCmModified());
            mapComments.put("bd_id", entity.getBoard().getBdId());
            mapComments.put("totalElement", comments.getTotalElements());
            mapComments.put("totalPage", comments.getTotalPages());

            //dto??? ????????? List<Map>??? ??????
            data.add(mapComments);
        });

        //dto??? ??? set
        resCommentListDto.setComments(data);

        return resCommentListDto;
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

}
