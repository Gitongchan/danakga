package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqBoardDto;
import com.danakga.webservice.board.dto.request.ReqDeletedFileDto;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Files;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.board.repository.CommentRepository;
import com.danakga.webservice.board.repository.BoardFileRepository;
import com.danakga.webservice.board.service.BoardService;
import com.danakga.webservice.board.service.BoardFileService;
import com.danakga.webservice.exception.CustomException;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository fileRepository;
    private final BoardFileService boardFilesService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    //게시판 검색
    @Override
    public ResBoardListDto boardSearch(Pageable pageable, String category, String content, String boardType, int page) {

        //DB 조회를 위한 deleted 값
        final String deleted = "N";

        //pageable 값 설정
        pageable = PageRequest.of(page, 10, Sort.by("bdCreated").descending());

        Page<Board> boards;

        List<Map<String, Object>> searchList = new ArrayList<>();

        //switch case로 제목, 내용, 작성자, 전체 게시글 목록 검색 조회, default 값은 필수
        switch (category) {
            case "제목": //게시글 제목
                boards = boardRepository.SearchBoardTitle(deleted, content, boardType, pageable);
                break;
            case "내용": //게시글 내용
                boards = boardRepository.SearchBoardContent(deleted, content, boardType, pageable);
                break;
            case "작성자": //작성자 아이디
                boards = boardRepository.SearchBoardWriter(deleted, content, boardType, pageable);
                break;
            case "전체":
                if (content.equals("")) {
                    boards = boardRepository.findAllByBdDeletedAndBdType(deleted, boardType, pageable);
                } else {
                    boards = boardRepository.searchBoard(content, deleted, boardType, pageable);
                }
                break;
            default:
                boards = null;
        }

        if (boards != null) {

            boards.forEach(entity -> {

                Map<String, Object> searchMap = new LinkedHashMap<>();

                searchMap.put("bd_id", entity.getBdId());
                searchMap.put("bd_title", entity.getBdTitle());
                searchMap.put("bd_writer", entity.getBdWriter());
                searchMap.put("bd_created", entity.getBdCreated());
                searchMap.put("bd_views", entity.getBdViews());
                searchMap.put("bd_deleted", entity.getBdDeleted());
                searchMap.put("totalElement", boards.getTotalElements());
                searchMap.put("totalPage", boards.getTotalPages());
                searchList.add(searchMap);
            });
        }

        return new ResBoardListDto(searchList);
    }

    //게시판 목록
    @Override
    public ResBoardListDto boardList(Pageable pageable, String boardType, int page) {

        final String deleted = "N";

        pageable = PageRequest.of(page, 10, Sort.by("bdCreated").descending());
        Page<Board> boards = boardRepository.findAllByBdDeletedAndBdType(deleted, boardType, pageable);

        List<Map<String, Object>> postList = new ArrayList<>();

        boards.forEach(entity -> {
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("bd_id", entity.getBdId());
            postMap.put("bd_title", entity.getBdTitle());
            postMap.put("bd_writer", entity.getBdWriter());
            postMap.put("bd_created", entity.getBdCreated());
            postMap.put("bd_views", entity.getBdViews());
            postMap.put("bd_deleted", entity.getBdDeleted());
            postMap.put("totalElement", boards.getTotalElements());
            postMap.put("totalPage", boards.getTotalPages());
            postList.add(postMap);
        });

        return new ResBoardListDto(postList);
    }

    //게시글 조회
    @Override
    public ResBoardPostDto getPost(Long bd_id, HttpServletRequest request, HttpServletResponse response) {

        //게시글 존재 여부 확인
        Board checkBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("해당 게시글을 찾을 수 없습니다."));

        //게시글의 파일 조회
        List<Board_Files> files = fileRepository.findByBoard(checkBoard);

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
            if (!oldCookie.getValue().contains("[" + bd_id + "]")) {
                boardRepository.updateView(bd_id);
                oldCookie.setValue(oldCookie.getValue() + "[" + bd_id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60);
                response.addCookie(oldCookie);
            }
        } else {
            boardRepository.updateView(bd_id);
            Cookie postCookie = new Cookie("boardView", "[" + bd_id + "]");
            postCookie.setPath("/");
            //쿠키 사용시간 1시간 설정
            postCookie.setMaxAge(60 * 60);
            response.addCookie(postCookie);
        }

        //파일 값 List
        List<Map<String, Object>> fileList = new ArrayList<>();

        //게시글 값 List
        List<Map<String, Object>> postList = new ArrayList<>();

        //file 정보 값
        files.forEach(entity -> {
            Map<String, Object> filesMap = new HashMap<>();
            filesMap.put("file_name", entity.getFileSaveName());
            filesMap.put("file_path", entity.getFilePath());
            fileList.add(filesMap);
        });

        //게시글 정보 담을 Map
        Map<String, Object> postMap = new LinkedHashMap<>();

        postMap.put("bd_id", checkBoard.getBdId());
        postMap.put("bd_writer", checkBoard.getBdWriter());
        postMap.put("bd_title", checkBoard.getBdTitle());
        postMap.put("bd_content", checkBoard.getBdContent());
        postMap.put("bd_created", checkBoard.getBdCreated());
        postMap.put("bd_modified", checkBoard.getBdModified());
        postMap.put("bd_views", checkBoard.getBdViews());
        postMap.put("files", fileList);
        postList.add(postMap);

        return new ResBoardPostDto(postList);
    }

    //게시글 작성
    @Transactional
    @Override
    public ResResultDto postWrite(ReqBoardDto reqBoardDto, UserInfo userInfo, List<MultipartFile> files) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        //들어온 파일이 없다면 게시글만 작성
        //파일이 있다면 게시글 작성 후 파일 업로드
        if (CollectionUtils.isEmpty(files)) {
            Board board = boardRepository.save(
                    Board.builder()
                            .bdType(reqBoardDto.getBdType())
                            .bdWriter(checkUserInfo.getUserid())
                            .bdTitle(reqBoardDto.getBdTitle())
                            .bdContent(reqBoardDto.getBdContent())
                            .userInfo(checkUserInfo)
                            .build()
            );
            return new ResResultDto(board.getBdId(), "게시글을 작성 했습니다.");
        } else
            for (MultipartFile multipartFile : files) {

                String originFileName = multipartFile.getOriginalFilename().toLowerCase();

                //List에 값이 있으면 saveFileUpload 실행
                if (originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {
                    Board board = boardRepository.save(
                            Board.builder()
                                    .bdType(reqBoardDto.getBdType())
                                    .bdWriter(checkUserInfo.getUserid())
                                    .bdTitle(reqBoardDto.getBdTitle())
                                    .bdContent(reqBoardDto.getBdContent())
                                    .userInfo(checkUserInfo)
                                    .build()
                    );
                    if (!boardFilesService.saveFileUpload(files, board).equals(1L)) {
                        return new ResResultDto(-2L, "게시글 작성에 실패 했습니다(이미지 업로드 오류)");
                    }
                    return new ResResultDto(board.getBdId(), "게시글을 작성 했습니다.");
                }
            }
        return new ResResultDto(-1L, "게시글 작성에 실패 했습니다.");
    }


    //게시글 수정
    @Transactional
    @Override
    public ResResultDto postEdit(Long bd_id, UserInfo userInfo, ReqBoardDto reqBoardDto, ReqDeletedFileDto reqDeletedFileDto, List<MultipartFile> files) {

        //유저 정보, 게시글 정보 조회
        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Board checkBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        //삭제된 파일이 있는경우 해당 파일의 저장된 파일이름을 받아서 삭제 후 
        //새로 업로드 할 파일이 있다면 글 수정 후 파일 업로드, 업로드 할 파일이 없다면 글만 수정
        if (reqDeletedFileDto != null) {
            //삭제 파일명을 담아주기 위한 List
            List<String> fileNameList = new ArrayList<>();
            //dto에서 삭제 파일명을 담아주는 List, Map
            List<Map<String, Object>> fileNameMap = reqDeletedFileDto.getDeletedFiles();

            //List<Map> 값을 1씩 증가시켜서 List<String>에 담아줌
            for (Map<String, Object> valueMap : fileNameMap) {
                fileNameList.add(valueMap.get("value").toString());
            }

            //List<String>값을 반복문으로 파일명 빼서 삭제
            for (String deleteFile : fileNameList) {
                File deletedFiles = new File(System.getProperty("user.dir") + "//src//main//resources//static//files//" + deleteFile);
                if (deletedFiles.delete()) {
                    fileRepository.deleteByBoardAndFileSaveName(checkBoard, deleteFile);
                }
            }
        }


        //파일 없이 제목, 게시글만 들어오면 그대로 수정
        //else 파일 같이 들어오면 게시글 수정 후 파일 업로드
        if (CollectionUtils.isEmpty(files)) {
            checkBoard = boardRepository.save(
                    Board.builder()
                            .bdId(checkBoard.getBdId())
                            .bdType(reqBoardDto.getBdType())
                            .bdWriter(checkUserInfo.getUserid())
                            .bdTitle(reqBoardDto.getBdTitle())
                            .bdContent(reqBoardDto.getBdContent())
                            .bdDeleted(checkBoard.getBdDeleted())
                            .bdCreated(checkBoard.getBdCreated())
                            .userInfo(checkUserInfo)
                            .build()
            );
            return new ResResultDto(checkBoard.getBdId(), "게시글을 수정 했습니다.");

        } else if (!CollectionUtils.isEmpty(files)) {

            for (MultipartFile multipartFile : files) {

                //확장자 체크 위해서 게시글 원본 이름 가져옴
                String originFileName = multipartFile.getOriginalFilename().toLowerCase();

                //파일 경로 + 파일명으로 파일 있는지 검사 후 삭제
                if (originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {
                    checkBoard = boardRepository.save(
                            Board.builder()
                                    .bdId(checkBoard.getBdId())
                                    .bdType(reqBoardDto.getBdType())
                                    .bdWriter(checkUserInfo.getUserid())
                                    .bdTitle(reqBoardDto.getBdTitle())
                                    .bdContent(reqBoardDto.getBdContent())
                                    .bdCreated(checkBoard.getBdCreated())
                                    .bdDeleted(checkBoard.getBdDeleted())
                                    .userInfo(checkUserInfo)
                                    .build()
                    );
                    if (!boardFilesService.saveFileUpload(files, checkBoard).equals(1L)) {
                        return new ResResultDto(-2L, "게시글 수정에 실패 했습니다(이미지 업로드 오류)");
                    }
                    return new ResResultDto(checkBoard.getBdId(), "게시글을 수정 했습니다.");
                }
            }
        }
        return new ResResultDto(-1L, "게시글 수정에 실패 했습니다.");
    }

    //게시글 삭제 여부 변경
    @Transactional
    @Override
    public ResResultDto postDelete(Long bd_id, UserInfo userInfo) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Board checkBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        //deleted 값 변경
        boardRepository.updateBdDeleted(bd_id);

        //댓글과 대댓글 포함해서 삭제 상태 변경
        commentRepository.deleteBoardComment(bd_id);

        return new ResResultDto(checkBoard.getBdId(), "게시글을 삭제 했습니다.");
    }
}