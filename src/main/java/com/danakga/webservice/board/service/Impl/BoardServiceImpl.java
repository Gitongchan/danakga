package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.exception.CustomException;
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

        Optional<Board> boardWrapper = boardRepository.findById(id);
        
        //id가 없는 값이 들어와서 boardWrapper가 비어있는 경우 Exception 처리
        if(boardWrapper.isEmpty()) {
            throw new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다.");
        }

        Board board = boardWrapper.get();
        List<Board_Files> files = fileRepository.findByBoard(board);

        //Map을 List에 넣어서 여러개를 받을 수 있게 함
        List<Map<String, Object>> mapFiles = new ArrayList<>();
        List<ResBoardPostDto> postDto = new ArrayList<>();

        //개별 게시글 값 set
        ResBoardPostDto resBoardPostDto = new ResBoardPostDto();
        resBoardPostDto.setBd_id(board.getBd_id());
        resBoardPostDto.setBd_writer(board.getBd_writer());
        resBoardPostDto.setBd_title(board.getBd_title());
        resBoardPostDto.setBd_content(board.getBd_content());
        resBoardPostDto.setBd_created(board.getBd_created());
        resBoardPostDto.setBd_modified(board.getBd_modified());
        resBoardPostDto.setBd_views(board.getBd_views());

        //file 정보 값 set
        //Map의 put은 키값마다 1개씩만 담기기 때문에 map 생성자를 밖으로 빼면 가장 마지막으로 들어온 값만 저장됨 (결국 1개만 저장)
        //Map.put() == List.add() 와 같은 기능
        //그래서 map 생성자도 반복문 안으로 넣어줘서 List<Map>에 한번 담고 다시 생성돼서 돌아가는 식
        files.forEach(entity -> {
            Map<String, Object> filesmap = new HashMap<>();
            filesmap.put("file_name",entity.getF_savename());
            filesmap.put("file_path",entity.getF_path());
            mapFiles.add(filesmap);
            resBoardPostDto.setFiles(mapFiles);
        });
        postDto.add(resBoardPostDto);
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
