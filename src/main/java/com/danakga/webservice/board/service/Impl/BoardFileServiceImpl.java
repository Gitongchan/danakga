package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Files;
import com.danakga.webservice.board.repository.BoardFileRepository;
import com.danakga.webservice.board.service.BoardFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardFileServiceImpl implements BoardFileService {

     private final BoardFileRepository fileRepository;

     //파일 저장
    @Override
    public Long saveFileUpload(List<MultipartFile> files, Board board) {

        //파일 저장 경로
        String savePath = System.getProperty("user.dir") + "//src//main//resources//static//files";

        //파일 저장되는 폳더 없으면 생성
        if (!new File(savePath).exists()) {
            try {
                new File(savePath).mkdir();
            } catch (Exception e2) {
                e2.getStackTrace();
            }
        }

        // 다중 파일 처리
        // multipartfile : files  files에서 더 꺼낼게 없을 때 까지 multipartfile에 담아줌
        for (MultipartFile multipartFile : files) {

            //파일명 소문자로 추출, 원본 파일명 공백 제거
            String originFileName = multipartFile.getOriginalFilename().toLowerCase().replace(" ", "");

            //UUID로 파일명 중복되지 않게 유일한 식별자 + 확장자로 저장
            UUID uuid = UUID.randomUUID();
            String saveFileName = uuid + "__" + originFileName;

            //File로 저장 경로와 저장 할 파일명 합쳐서 transferTo() 사용하여 업로드하려는 파일을 해당 경로로 저장
            String filePath = savePath + "//" + saveFileName;
            
            //DB에 저장되는 경로
            String dbFilePath = "../files/" + saveFileName;
            
            //.transferTo 사용하여 파일경로에 파일명으로 저장
            try {
                multipartFile.transferTo(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            fileRepository.save(
                    Board_Files.builder()
                            .fileSaveName(saveFileName)
                            .fileOrigin(originFileName)
                            .filePath(dbFilePath)
                            .board(board)
                            .build()
            );
        }
        return 1L;
    }
}
