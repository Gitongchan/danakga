package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqFileUploadDto;
import com.danakga.webservice.board.dto.response.ResFileUploadDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Files;
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
    public ResFileUploadDto saveFileUpload(ReqFileUploadDto reqFileUploadDto, List<MultipartFile> files, Board board) {

            Long f_id = null;
            //파일 저장 경로
            String savepath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files\\";

            //파일 저장되는 폳더 없으면 생성
            if(!new File(savepath).exists()) {
                try{
                    new File(savepath).mkdir();
                } catch (Exception e2) {
                    e2.getStackTrace();
                }
            }

            // 다중 파일 처리
            // multipartfile : files  files에서 더 꺼낼게 없을 때 까지 multipartfile에 담아줌
            for(MultipartFile multipartFile : files) {

                //파일명 소문자로 추출
                String originFileName = multipartFile.getOriginalFilename().toLowerCase();

                //확장자 제한
                if(originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {

                    //UUID로 파일명 중복되지 않게 유일한 식별자 + 확장자로 저장
                    UUID uuid = UUID.randomUUID();
                    String saveFileName = uuid + "__" + originFileName;

                    //File로 저장 경로와 저장 할 파일명 합쳐서 transferTo() 사용하여 업로드하려는 파일을 해당 경로로 저장
                    String filepath = savepath + "\\" + saveFileName;
                    try {
                        multipartFile.transferTo(new File(filepath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //설정한 파일명, 경로 값 넘겨주기
                    reqFileUploadDto.setF_originName(originFileName);
                    reqFileUploadDto.setF_savename(saveFileName);
                    reqFileUploadDto.setF_path(filepath);

                    //board의 id값 들어가는거 확인
                    //나머지 파일 관련 값은 안들어감
                    f_id = fileRepository.save(
                            Files.builder()
                                    .f_savename(reqFileUploadDto.getF_savename())
                                    .f_origin(reqFileUploadDto.getF_originName())
                                    .f_path(reqFileUploadDto.getF_path())
                                    .board(board)
                                    .build()
                    ).getF_id();
                } else {
                    System.out.println("jpg, png, jpeg 형식의 파일만 업로드 가능합니다.");
                    break;
                }
            }
        return new ResFileUploadDto(f_id);
    }
}
