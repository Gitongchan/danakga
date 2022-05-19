package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.board.service.FilesService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    @Autowired private final BoardRepository boardRepository;
    @Autowired private final FilesService filesService;
    @Autowired private final UserRepository userRepository;
    private Board board;

    //게시판 목록
    @Override
    public List<ResBoardListDto> boardList(Pageable pageable) {

        //deleted 컬럼에 N값인 컬럼만 모두 List에 담아줌
        String deleted = "N";
        List<Board> boards = boardRepository.findAllByBdDeleted(deleted, pageable).getContent();
        List<ResBoardListDto> boardListDto = new ArrayList<>();

        boards.forEach(entity -> {
            ResBoardListDto listDto = new ResBoardListDto();
                listDto.setBd_id(entity.getBd_id());
                listDto.setBd_title(entity.getBd_title());
                listDto.setBd_writer(entity.getBd_writer());
                listDto.setBd_created(entity.getBd_created());
                listDto.setBd_views(entity.getBd_views());
                listDto.setBd_deleted(entity.getBdDeleted());
                boardListDto.add(listDto);
        });

        return boardListDto;
    }

    //게시글 아이디 찾기
    @Override
    public Board bd_IdCheck(Long bd_id) {
        return boardRepository.findById(bd_id).orElseThrow(() -> {
            throw new IllegalArgumentException("게시글 없음");
        });
    }

    //개별 게시글 보기
    @Override
    public ResBoardListDto post() {
        return null;
    }

    //게시글 작성
    @Override
    public Long write(ReqBoardWriteDto reqBoardWriteDto,
                      UserInfo userInfo, List<MultipartFile> files) {

        if(userRepository.findById(userInfo.getId()).isEmpty()){
            return -1L;
        }
        UserInfo recentUserInfo = userRepository.findById(userInfo.getId()).get();

        if(CollectionUtils.isEmpty(files)) {
            boardRepository.save(
                    board = Board.builder()
                            .bd_type(reqBoardWriteDto.getBd_type())
                            .bd_views(reqBoardWriteDto.getBd_views())
                            .bd_writer(recentUserInfo.getUserid())
                            .bd_title(reqBoardWriteDto.getBd_title())
                            .bd_content(reqBoardWriteDto.getBd_content())
                            .userInfo(recentUserInfo)
                            .build()
            );
            return board.getBd_id();
        }
        else if(!CollectionUtils.isEmpty(files)) {

            for(MultipartFile multipartFile : files) {

                String originFileName = multipartFile.getOriginalFilename().toLowerCase();

                //List에 값이 있으면 saveFileUpload 실행
                if (!CollectionUtils.isEmpty(files)) {
                    if(originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {
                        boardRepository.save(
                                board = Board.builder()
                                        .bd_type(reqBoardWriteDto.getBd_type())
                                        .bd_views(reqBoardWriteDto.getBd_views())
                                        .bd_writer(recentUserInfo.getUserid())
                                        .bd_title(reqBoardWriteDto.getBd_title())
                                        .bd_content(reqBoardWriteDto.getBd_content())
                                        .userInfo(recentUserInfo)
                                        .build()
                        );

                        filesService.saveFileUpload(files, board);
                        return board.getBd_id();
                    }
                }
            }
        }
        return -1L;
    }


//게시글 수정
//    @Override
//    public ResBoardUpdateDto edit(UserInfo userInfo, Board board) {
//        return null;
//    }


}
