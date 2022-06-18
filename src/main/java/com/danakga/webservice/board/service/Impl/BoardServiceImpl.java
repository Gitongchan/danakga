package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqBoardDto;
import com.danakga.webservice.board.dto.request.ReqDeletedFileDto;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Files;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.board.repository.FileRepository;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.board.service.FilesService;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final FilesService filesService;
    private final UserRepository userRepository;

    //게시판 목록
    @Override
    public ResBoardListDto boardList(Pageable pageable, String board_type, int page) {

        final String deleted = "N";

        pageable = PageRequest.of(page, 10, Sort.by("bdCreated").descending());
        Page<Board> boards = boardRepository.findAllByBdDeletedAndBdType(deleted, board_type, pageable);

        List<Board> boardList = boards.getContent();

        List<Map<String, Object>> mapPosts = new ArrayList<>();

        ResBoardListDto resBoardListDto = new ResBoardListDto();

        boardList.forEach(entity -> {
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("bd_id", entity.getBdId());
            postMap.put("bd_title", entity.getBdTitle());
            postMap.put("bd_writer", entity.getBdWriter());
            postMap.put("bd_created", entity.getBdCreated());
            postMap.put("bd_views", entity.getBdViews());
            postMap.put("bd_deleted", entity.getBdDeleted());
            postMap.put("totalElement", boards.getTotalElements());
            postMap.put("totalPage", boards.getTotalPages());
            mapPosts.add(postMap);
        });
        resBoardListDto.setLists(mapPosts);

        return resBoardListDto;
    }

    //게시글 조회
    @Override
    public ResBoardPostDto getPost(Long id, HttpServletRequest request, HttpServletResponse response) {

        //게시글 존재 여부 확인
        Board boardWrapper = boardRepository.findByBdId(id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("해당 게시글을 찾을 수 없습니다."));

        //게시글의 파일 조회
        List<Board_Files> files = fileRepository.findByBoard(boardWrapper);

        //조회수 증가, 쿠키로 중복 증가 방지
        //쿠키가 있으면 그 쿠키가 해당 게시글 쿠키인지 확인하고 아니라면 조회수 올리고 setvalue로 해당 게시글의 쿠키 값도 넣어줘야함
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();

        //기존에 쿠키를 가지고 있다면 해당 쿠키를 oldCookie에 담아줌
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("boardView")) {
                    oldCookie = cookie;
                }
            }
        }

        //oldCookie가 쿠키를 가지고 있으면 oldCookie의 value값에 id가 없다면 조회수 증가
        //그리고 해당 게시글 id에 대한 쿠키를 다시 담아서 보냄
        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + id.toString() + "]")) {
                boardRepository.updateView(id);
                oldCookie.setValue(oldCookie.getValue() + "[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60);
                response.addCookie(oldCookie);
            }
        } else {
            boardRepository.updateView(id);
            Cookie postCookie = new Cookie("boardView", "[" + id + "]");
            postCookie.setPath("/");
            //쿠키 사용시간 1시간 설정
            postCookie.setMaxAge(60 * 60);
            response.addCookie(postCookie);
        }

        //파일, 게시글 정보 담을 List
        List<Map<String, Object>> mapFiles = new ArrayList<>();

        //값 담아줄 Dto 객체 생성
        ResBoardPostDto resBoardPostDto = new ResBoardPostDto();

        //게시글 값 (게시글은 단일 값이기 때문에 List 까지 씌울 필요는 없음)
        Map<String, Object> postMap = new HashMap<>();

        postMap.put("bd_id", boardWrapper.getBdId());
        postMap.put("bd_writer", boardWrapper.getBdWriter());
        postMap.put("bd_title", boardWrapper.getBdTitle());
        postMap.put("bd_content", boardWrapper.getBdContent());
        postMap.put("bd_created", boardWrapper.getBdCreated());
        postMap.put("bd_modified", boardWrapper.getBdModified());
        postMap.put("bd_views", boardWrapper.getBdViews());

        //file 정보 값
        files.forEach(entity -> {
            Map<String, Object> filesMap = new HashMap<>();
            filesMap.put("file_name", entity.getFileSaveName());
            filesMap.put("file_path", entity.getFilePath());
            mapFiles.add(filesMap);
        });
        
        //각 파일, 댓글 List<Map>에 set
        resBoardPostDto.setFiles(mapFiles);
        resBoardPostDto.setPost(postMap);

        return resBoardPostDto;
    }

    //게시글 작성
    @Transactional
    @Override
    public Long postWrite(ReqBoardDto reqBoardDto,
                          UserInfo userInfo, List<MultipartFile> files) {

        if (userRepository.findById(userInfo.getId()).isEmpty()) {
            return -1L;
        }
        UserInfo recentUserInfo = userRepository.findById(userInfo.getId()).get();

        Board board;

        //들어온 파일이 없다면 게시글만 작성
        //파일이 있다면 게시글 작성 후 파일 업로드
        if (CollectionUtils.isEmpty(files)) {
            boardRepository.save(
                    board = Board.builder()
                            .bdType(reqBoardDto.getBdType())
                            .bdWriter(recentUserInfo.getUserid())
                            .bdTitle(reqBoardDto.getBdTitle())
                            .bdContent(reqBoardDto.getBdContent())
                            .userInfo(recentUserInfo)
                            .build()
            );
            return board.getBdId();
        } else
            for (MultipartFile multipartFile : files) {

                String originFileName = multipartFile.getOriginalFilename().toLowerCase();

                //List에 값이 있으면 saveFileUpload 실행
                if (originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {
                    boardRepository.save(
                            board = Board.builder()
                                    .bdType(reqBoardDto.getBdType())
                                    .bdWriter(recentUserInfo.getUserid())
                                    .bdTitle(reqBoardDto.getBdTitle())
                                    .bdContent(reqBoardDto.getBdContent())
                                    .userInfo(recentUserInfo)
                                    .build()
                    );
                    if (!filesService.saveFileUpload(files, board).equals(1L)) {
                        return -2L;
                    }
                    return board.getBdId();
                }
            }
        return -1L;
    }


    //게시글 수정
    @Transactional
    @Override
    public Long postEdit(Long id, UserInfo userInfo, ReqBoardDto reqBoardDto, ReqDeletedFileDto reqDeletedFileDto, List<MultipartFile> files) {

        //유저 db 최신화
        if (userRepository.findById(userInfo.getId()).isEmpty()) {
            return -1L;
        }
        UserInfo recentUserInfo = userRepository.findById(userInfo.getId()).get();

        //게시판 db 최신화
        if (boardRepository.findByBdId(id).isEmpty()) {
            return -1L;
        }

        Board board = boardRepository.findByBdId(id).get();
        
        //삭제된 파일이 있는경우 해당 파일의 저장된 파일이름을 받아서 삭제 후 
        //새로 업로드 할 파일이 있다면 글 수정 후 파일 업로드, 업로드 할 파일이 없다면 글만 수정
        if(reqDeletedFileDto != null) {
            //삭제 파일명을 담아주기 위한 List
            List<String> fileNameList = new ArrayList<>();
            //dto에서 삭제 파일명을 담아주는 List, Map
            List<Map<String, Object>> fileNameMap = reqDeletedFileDto.getDeletedFiles();

            //List<Map> 값을 1씩 증가시켜서 List<String>에 담아줌
            for (Map<String, Object> valueMap : fileNameMap) {
                fileNameList.add(valueMap.get("value").toString());
            }

            //List<String>값을 반복문으로 파일명 빼서 삭제
            for(String deleteFile : fileNameList) {
                File deletedFiles = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files\\" + deleteFile);
                if(deletedFiles.delete()){
                    fileRepository.deleteByBoardAndFileSaveName(board, deleteFile);
                }

            }

        }


        //파일 없이 제목, 게시글만 들어오면 그대로 수정
        //else 파일 같이 들어오면 게시글 수정 후 파일 업로드
        if (CollectionUtils.isEmpty(files)) {
            boardRepository.save(
                    board = Board.builder()
                            .bdId(id)
                            .bdType(reqBoardDto.getBdType())
                            .bdWriter(recentUserInfo.getUserid())
                            .bdTitle(reqBoardDto.getBdTitle())
                            .bdContent(reqBoardDto.getBdContent())
                            .bdDeleted(board.getBdDeleted())
                            .bdCreated(board.getBdCreated())
                            .userInfo(recentUserInfo)
                            .build()
            );
            return board.getBdId();

        } else if (!CollectionUtils.isEmpty(files)) {

            for (MultipartFile multipartFile : files) {

                //확장자 체크 위해서 게시글 원본 이름 가져옴
                String originFileName = multipartFile.getOriginalFilename().toLowerCase();

                //파일 경로 + 파일명으로 파일 있는지 검사 후 삭제
                if (originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {
                    boardRepository.save(
                            board = Board.builder()
                                    .bdId(id)
                                    .bdType(reqBoardDto.getBdType())
                                    .bdWriter(recentUserInfo.getUserid())
                                    .bdTitle(reqBoardDto.getBdTitle())
                                    .bdContent(reqBoardDto.getBdContent())
                                    .bdCreated(board.getBdCreated())
                                    .bdDeleted(board.getBdDeleted())
                                    .userInfo(recentUserInfo)
                                    .build()
                    );
                    if (!filesService.saveFileUpload(files, board).equals(1L)) {
                        return -2L;
                    }
                    return board.getBdId();
                }
            }
        }
        return -1L;
    }

    //게시글 삭제 여부 변경
    @Transactional
    @Override
    public Long postDelete(Long id, UserInfo userInfo) {

        //해당 id의 null값 체크 후 값이 있으면  deleted 값 변경
        if (boardRepository.findByBdId(id).isPresent()) {

            Board board = boardRepository.findByBdId(id).get();

            //deleted 값 변경
            boardRepository.updateDeleted(id);

            return board.getBdId();
        }
        return -1L;
    }

}