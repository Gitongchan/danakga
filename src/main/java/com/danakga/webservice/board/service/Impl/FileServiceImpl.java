package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Files;
import com.danakga.webservice.board.repository.FileRepository;
import com.danakga.webservice.board.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FilesService {

    @Autowired private final FileRepository fileRepository;

    @Override
    public Long saveFileUpload(List<MultipartFile> files, Board board) {

            //파일 저장 경로
            String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

            //파일 저장되는 폳더 없으면 생성
            if(!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                } catch (Exception e2) {
                    e2.getStackTrace();
                }
            }

            // 다중 파일 처리
            // multipartfile : files  files에서 더 꺼낼게 없을 때 까지 multipartfile에 담아줌
            for(MultipartFile multipartFile : files) {

                final long maxRequestSize = 30 * 1024 * 1024;
                final long maxFileSize = 15 * 1024 * 1024;

                //파일명 소문자로 추출
                String originFileName = multipartFile.getOriginalFilename().toLowerCase();

                //UUID로 파일명 중복되지 않게 유일한 식별자 + 확장자로 저장
                UUID uuid = UUID.randomUUID();
                String saveFileName = uuid + "__" + originFileName;

                //File로 저장 경로와 저장 할 파일명 합쳐서 transferTo() 사용하여 업로드하려는 파일을 해당 경로로 저장
                String filePath = savePath + "\\" + saveFileName;
                try {
                    multipartFile.transferTo(new File(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                 fileRepository.save(
                        Board_Files.builder()
                                .fSavename(saveFileName)
                                .fOrigin(originFileName)
                                .fPath(filePath)
                                .board(board)
                                .build()
                );
            }
        return 1L;
    }
}
