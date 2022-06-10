package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqBoardDto;
import com.danakga.webservice.board.dto.request.ReqDeletedFileDto;
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
    private Board board;

    //게시판 목록
    @Override
    public List<ResBoardListDto> boardList(Pageable pageable, String board_type, int page) {

        //deleted 컬럼에 N값인 컬럼만 모두 List에 담아줌
        final String deleted = "N";
        pageable = PageRequest.of(page, 10, Sort.by("bdId").descending());
        Page<Board> boards = boardRepository.findAllByBdDeletedAndBdType(deleted, board_type, pageable);
        List<Board> boardList = boards.getContent();

        List<ResBoardListDto> boardListDto = new ArrayList<>();

        boardList.forEach(entity -> {
            ResBoardListDto listDto = new ResBoardListDto();
            listDto.setBdId(entity.getBdId());
            listDto.setBdTitle(entity.getBdTitle());
            listDto.setBdWriter(entity.getBdWriter());
            listDto.setBdCreated(entity.getBdCreated());
            listDto.setBdViews(entity.getBdViews());
            listDto.setBdDeleted(entity.getBdDeleted());
            listDto.setTotalElement(boards.getTotalElements());
            listDto.setTotalPage(boards.getTotalPages());
            boardListDto.add(listDto);
        });
        return boardListDto;
    }

    //게시글 조회
    @Override
    public ResBoardPostDto getPost(Long id, HttpServletRequest request, HttpServletResponse response) {

        Board boardWrapper = boardRepository.findByBdId(id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("해당 게시글을 찾을 수 없습니다."));

        List<Board_Files> files = fileRepository.findByBoard(boardWrapper);

        //조회수 증가, 쿠키로 중복 증가 방지
        //쿠키가 있으면 그 쿠키가 해당 게시글 쿠키인지 확인하고 아니라면 조회수 올리고 setvalue로 해당 게시글의 쿠키 값도 넣어줘야함
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();

        //기존에 쿠키를 가지고 있다면 해당 쿠키를 oldCookie에 담아줌
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
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
            Cookie postCookie = new Cookie("postView", "[" + id + "]");
            postCookie.setPath("/");
            //쿠키 사용시간 1시간 설정
            postCookie.setMaxAge(60 * 60);
            System.out.println("쿠키 이름 : " + postCookie.getValue());
            response.addCookie(postCookie);
        }


        //Map을 List에 넣어서 여러개를 받을 수 있게 함
        List<Map<String, Object>> mapFiles = new ArrayList<>();
        ResBoardPostDto resBoardPostDto = new ResBoardPostDto();

        //개별 게시글 값 set
        resBoardPostDto.setBdId(boardWrapper.getBdId());
        resBoardPostDto.setBdWriter(boardWrapper.getBdWriter());
        resBoardPostDto.setBdTitle(boardWrapper.getBdTitle());
        resBoardPostDto.setBdContent(boardWrapper.getBdContent());
        resBoardPostDto.setBdCreated(boardWrapper.getBdCreated());
        resBoardPostDto.setBdModified(boardWrapper.getBdModified());
        resBoardPostDto.setBdViews(boardWrapper.getBdViews());

        //file 정보 값 set
        files.forEach(entity -> {
            Map<String, Object> filesmap = new HashMap<>();
            filesmap.put("file_name", entity.getFileSaveName());
            filesmap.put("file_path", entity.getFilePath());
            mapFiles.add(filesmap);
        });
        resBoardPostDto.setFiles(mapFiles);

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
        
        //파일 먼저 삭제하고 CollectionUtil로 파일 유무 확인 후 없으면 board만 수정, 있으면 file도 다시 업로드
        //웹페이지에서 x눌러서 지운 파일은 어차피 값이 들어오지 않기 때문에 삭제 후 업로드 되지 않음(들어온 파일만 업로드)

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
        List<Board_Files> boardFiles = fileRepository.findByBoard(board);


        if(reqDeletedFileDto != null) {
            //삭제 파일명을 담아주기 위한 List
            List<String> fileNameList = new ArrayList<>();
            //dto에서 삭제 파일명을 담아주는 List, Map
            List<Map<String, Object>> fileNameMap = reqDeletedFileDto.getDeletedFiles();

            //List<Map> 값을 1씩 증가시켜서 List<String>에 담아줌
            for(int i = 0; i < fileNameMap.size(); i++) {
                fileNameList.add(fileNameMap.get(i).get("value").toString());
            }

            //List<String>값을 반복문으로 파일명 빼서 삭제
            for(String deleteFile : fileNameList) {
                File deletedFiles = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\js\\board\\files\\" + deleteFile);
                if(deletedFiles.delete()){}
                fileRepository.deleteByBoardAndFileSaveName(board, deleteFile);
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