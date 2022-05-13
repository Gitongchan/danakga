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
    private final UserRepository userRepository;
    private Board board;

    //게시판 목록
    @Override
    public List<ResBoardListDto> boardList() {

        //게시글도 수정 후 다시 보여지는데 최신화해서 보여주는게 맞는지
        //Long타입의 메소드가 아니여서 return은 어떤거로 줄지
//        if(boardRepository.findById(board.getBd_id()).isEmpty()){
//            return null;
//        }
//        UserInfo recentBoard = userRepository.findById(board.getBd_id()).get();


        List<Board> boards = boardRepository.findAll();
        List<ResBoardListDto> boardListDto = new ArrayList<>();

        boards.forEach(entity -> {
            ResBoardListDto listDto = new ResBoardListDto();
            listDto.setBd_title(entity.getBd_title());
            listDto.setBd_writer(entity.getBd_writer());
            listDto.setBd_created(entity.getBd_created());
            listDto.setBd_views(entity.getBd_views());
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

    //게시글 보기
    @Override
    public ResBoardListDto post() {
        return null;
    }

    @Override
    public Long write(ReqBoardWriteDto reqBoardWriteDto,
                      UserInfo userInfo, List<MultipartFile> files) {

        if(userRepository.findById(userInfo.getId()).isEmpty()){
            return -1L;
        }
        UserInfo fileUserInfo = userRepository.findById(userInfo.getId()).get();

        if(CollectionUtils.isEmpty(files)) {
            boardRepository.save(
                    board = Board.builder()
                            .bd_type(reqBoardWriteDto.getBd_type())
                            .bd_views(reqBoardWriteDto.getBd_views())
                            .bd_writer(fileUserInfo.getUserid())
                            .bd_title(reqBoardWriteDto.getBd_title())
                            .bd_content(reqBoardWriteDto.getBd_content())
                            .userInfo(fileUserInfo)
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
                                        .bd_writer(fileUserInfo.getUserid())
                                        .bd_title(reqBoardWriteDto.getBd_title())
                                        .bd_content(reqBoardWriteDto.getBd_content())
                                        .userInfo(fileUserInfo)
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
