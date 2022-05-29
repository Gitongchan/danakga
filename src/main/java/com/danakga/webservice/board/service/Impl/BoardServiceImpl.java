package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqBoardDto;
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

    @Autowired private final BoardRepository boardRepository;
    @Autowired private final FileRepository fileRepository;
    @Autowired private final FilesService filesService;
    @Autowired private final UserRepository userRepository;
    private Board board;

    //게시판 목록
    @Override
    public List<ResBoardListDto> boardList(Pageable pageable, String board_type, int page) {

        //deleted 컬럼에 N값인 컬럼만 모두 List에 담아줌
        final String deleted = "N";
        pageable = PageRequest.of(page, 10, Sort.by("bdId").descending());
        List<Board> boards = boardRepository.findAllByBdDeletedAndBdType(deleted, board_type, pageable).getContent();

        List<ResBoardListDto> boardListDto = new ArrayList<>();

        boards.forEach(entity -> {
            ResBoardListDto listDto = new ResBoardListDto();
                listDto.setBdId(entity.getBdId());
                listDto.setBdTitle(entity.getBdTitle());
                listDto.setBdWriter(entity.getBdWriter());
                listDto.setBdCreated(entity.getBdCreated());
                listDto.setBdViews(entity.getBdViews());
                listDto.setBdDeleted(entity.getBdDeleted());
                boardListDto.add(listDto);
        });
        return boardListDto;
    }

    //게시글 조회
    @Override
    public ResBoardPostDto getPost(Long id, HttpServletRequest request, HttpServletResponse response) {

        Board boardWrapper = boardRepository.findById(id)
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
        if(oldCookie != null) {
            if(!oldCookie.getValue().contains("[" + id.toString() + "]")) {
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
        //Map의 put은 키값마다 1개씩만 담기기 때문에 map 생성자를 밖으로 빼면 가장 마지막으로 들어온 값만 저장됨 (결국 1개만 저장)
        //그래서 map 생성자도 반복문 안으로 넣어줘서 List<Map>에 한번 담고 다시 생성돼서 돌아가는 식
        //Map.put() == List.add() 와 같은 기능
        //Map에 담긴 값을 Dto에 선언했던 Lise<Map<?,?>>에 담아줌
        //for(Board_Files board_files : files) {} 으로도 가능
            files.forEach(entity -> {
                Map<String, Object> filesmap = new HashMap<>();
                filesmap.put("file_name",entity.getFSaveName());
                filesmap.put("file_path",entity.getFPath());
                mapFiles.add(filesmap);
            });
        resBoardPostDto.setFiles(mapFiles);

        return resBoardPostDto;
    }

    //게시글 작성
    @Override
    public Long postWrite(ReqBoardDto reqBoardDto,
                      UserInfo userInfo, List<MultipartFile> files) {

        if(userRepository.findById(userInfo.getId()).isEmpty()){
            return -1L;
        }
        UserInfo recentUserInfo = userRepository.findById(userInfo.getId()).get();

        if(CollectionUtils.isEmpty(files)) {
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
        } else if(!CollectionUtils.isEmpty(files)) {

            for(MultipartFile multipartFile : files) {

                String originFileName = multipartFile.getOriginalFilename().toLowerCase();

                //List에 값이 있으면 saveFileUpload 실행
                if (!CollectionUtils.isEmpty(files)) {
                    if(originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {
                        boardRepository.save(
                                board = Board.builder()
                                        .bdType(reqBoardDto.getBdType())
                                        .bdWriter(recentUserInfo.getUserid())
                                        .bdTitle(reqBoardDto.getBdTitle())
                                        .bdContent(reqBoardDto.getBdContent())
                                        .userInfo(recentUserInfo)
                                        .build()
                        );
                        if(!filesService.saveFileUpload(files, board).equals(1L)) {
                            return -2L;
                        }
                        return board.getBdId();
                    }
                }
            }
        }
        return -1L;
    }



    //게시글 수정
    @Transactional
    @Override
    public Long postEdit(Long id, UserInfo userInfo, ReqBoardDto reqBoardDto, List<MultipartFile> files) {

        if(userRepository.findById(userInfo.getId()).isEmpty()){
            return -1L;
        }
        UserInfo recentUserInfo = userRepository.findById(userInfo.getId()).get();

        if(boardRepository.findById(id).isEmpty()) {
            return -1L;
        }
        Board board = boardRepository.findById(id).get();
        List<Board_Files> boardFiles = fileRepository.findByBoard(board);

        //제목, 내용만 있는 게시글 수정
        if(CollectionUtils.isEmpty(files)) {
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

            } else if(!CollectionUtils.isEmpty(files)) {

            //파일까지 있는 게시글 수정, 제목내용만 있는 게시글에 파일 추가 한 게시글 수정
            //db와 files에 있는 사진을 삭제 후 다시 업로드 하는 방식
            List<String> saveFileName = new ArrayList<>();

            for(Board_Files board_Files : boardFiles) {
                saveFileName.add(board_Files.getFSaveName());
                System.out.println(saveFileName);
            }

            for(MultipartFile multipartFile : files) {

                //확장자 체크를 위해 있어야 함
                String originFileName = multipartFile.getOriginalFilename().toLowerCase();
                
                //파일 경로 + 파일명으로 파일 있는지 검사 후 삭제
                for (String sFileName : saveFileName) {

                    File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files\\" + sFileName);

                    //폴더 내 파일 있는지 검사 후 파일 삭제(db, files)하고 재 업로드
                    if (file.exists()) {
                        System.out.println("파일 있음 = " + file);
                        if (file.delete()) {
                            fileRepository.deleteAllByBoard(board);
                        } else {
                            if(originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {
                                System.out.println("파일 없음 = " + file);
                                boardRepository.save(
                                        board = Board.builder()
                                                .bdType(reqBoardDto.getBdType())
                                                .bdWriter(recentUserInfo.getUserid())
                                                .bdTitle(reqBoardDto.getBdTitle())
                                                .bdContent(reqBoardDto.getBdContent())
                                                .userInfo(recentUserInfo)
                                                .build()
                                );
                                if(!filesService.saveFileUpload(files, board).equals(1L)) {
                                    return -2L;
                                }
                                return board.getBdId();
                            }
                        }
                    }
                }
            }
        }
        return -1L;
    }

    //게시글 삭제 여부 변경
    @Override
    public Long postDelete(Long id, UserInfo userInfo) {

        //해당 id의 null값 체크 후 값이 있으면  deleted 값 변경
        if(boardRepository.findById(id).isPresent()) {

            Board board = boardRepository.findById(id).get();

            //deleted 값 변경
            boardRepository.updateDeleted(id);

            return board.getBdId();
        }
        return -1L;
    }

}
