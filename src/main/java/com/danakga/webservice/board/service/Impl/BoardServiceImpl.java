package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqBoardWriteDto;
import com.danakga.webservice.board.dto.request.ReqFileUploadDto;
import com.danakga.webservice.board.dto.response.ResBoardUpdateDto;
import com.danakga.webservice.board.dto.response.ResBoardWriteDto;
import com.danakga.webservice.board.dto.response.ResPostDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.board.service.FilesService;
import com.danakga.webservice.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    @Autowired private final BoardRepository boardRepository;
    @Autowired private final FilesService filesService;
    //선언해서 쓰는지, 파라미터로 가져와서 넣어주는지 찾아봐야함
    private Board board;

    //게시판 목록
    @Override
    public List<Board> list() {
        return boardRepository.findAll();
    }

    //게시글 아이디 찾기
    @Override
    public Board bd_IdCheck(Long bd_id) {
        return boardRepository.findById(bd_id).orElseThrow(() -> { throw new IllegalArgumentException("게시글 없음");}) ;
    }

    //게시글 보기
    @Override
    public ResPostDto post() {
        return null;
    }

    //jpa는 id값만 확인하기 때문에 외래키로 설정한 값에 그대로 넣어주면 DB 테이블에 id값 들어옴
    @Override
    public ResBoardWriteDto write(ReqBoardWriteDto reqBoardWriteDto, UserInfo userInfo,
                                  ReqFileUploadDto reqFileUploadDto, List<MultipartFile> files) {
        final Long bd_id = boardRepository.save(
                  board = Board.builder()
                        .bd_type(reqBoardWriteDto.getBd_type())
                        .bd_views(reqBoardWriteDto.getBd_views())
                        .bd_writer(userInfo.getUserid())
                        .bd_title(reqBoardWriteDto.getBd_title())
                        .bd_content(reqBoardWriteDto.getBd_content())
                        .userInfo(userInfo)
                        .build()
                  ).getBd_id();

        //List에 값이 있으면 saveFileUpload 실행
        if(!CollectionUtils.isEmpty(files)) {
            filesService.saveFileUpload(reqFileUploadDto, files, board);
        }
        return new ResBoardWriteDto(bd_id);
    }


    //게시글 수정
//    @Override
//    public ResBoardUpdateDto edit(UserInfo userInfo, Board board) {
//        return null;
//    }


}
