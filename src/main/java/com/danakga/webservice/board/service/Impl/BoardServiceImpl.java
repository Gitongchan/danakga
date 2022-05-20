package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Files;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.board.repository.FileRepository;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.board.service.FilesService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    @Autowired private final BoardRepository boardRepository;
    @Autowired private final FileRepository fileRepository;
    @Autowired private final FilesService filesService;
    @Autowired private final UserRepository userRepository;
    private Board board;

    //게시판 목록
    @Override
    public List<ResBoardListDto> boardList(Pageable pageable) {

        //deleted 컬럼에 N값인 컬럼만 모두 List에 담아줌
        final String deleted = "N";
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

    //게시글 조회
    @Override
    public ResBoardPostDto getpost(Long id) {

        //.get()에서 경고뜨는 isPresent() 사용해서 해야함
        //isPresent()는 boolean 형으로 반환하기 때문에 return을 어떻게 해줘야 할지 고민
        //Optional로 가져오면 파일이 1개만 담겨서 옴
        Optional<Board> boardWrapper = boardRepository.findById(id);// 3
        Optional<Board_Files> filesWrapper = fileRepository.findById(id); //3

        Board board = boardWrapper.get();
        Board_Files board_Files = filesWrapper.get();

        //board_Files를 List로 변환
        List<Board_Files> files = Arrays.asList(board_Files);
        List<ResBoardPostDto> postDto = new ArrayList<>();

        //개별 게시글에 필요한 정보 담아서 보내주기
        ResBoardPostDto resBoardPostDto = new ResBoardPostDto();
        resBoardPostDto.setBd_id(board.getBd_id());
        resBoardPostDto.setBd_writer(board.getBd_writer());
        resBoardPostDto.setBd_title(board.getBd_title());
        resBoardPostDto.setBd_content(board.getBd_content());
        resBoardPostDto.setBd_created(board.getBd_created());
        resBoardPostDto.setBd_modified(board.getBd_modified());
        resBoardPostDto.setBd_views(board.getBd_views());

        if(board.getBd_id().equals(board_Files.getF_id())) {
            files.forEach(entity -> {
                resBoardPostDto.setFile_origin(board_Files.getF_origin());
                resBoardPostDto.setFile_path(board_Files.getF_path());
            });
        }
        return resBoardPostDto;
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
                        if(!filesService.saveFileUpload(files, board).equals(1L)) {
                            return -2L;
                        }

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
