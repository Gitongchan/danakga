package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqFileUploadDto;
import com.danakga.webservice.board.dto.response.ResBoardDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Files;
import com.danakga.webservice.board.repository.FileRepository;
import com.danakga.webservice.board.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Autowired private final FileRepository fileRepository;

    @Override
    public ResBoardDto.ResFileUploadDto saveFile(ReqFileUploadDto reqFileUploadDto, MultipartFile files, Board board) {
        //service로 로직 옮김
        try {
            //UUID로 파일명 중복되지 않게 유일한 식별자로 파일명 저장
            UUID uuid = UUID.randomUUID();
            //실제 첨부파일로 등록된 파일명
            String originFileName = files.getOriginalFilename();
            //db에 저장 할 파일명
            String fileName = uuid + "__" + originFileName;
            //파일 저장 경로
            String savepath = System.getProperty("user.dir") + "\\files";

            //파일 저장되는 폳더 없으면 생성
            if(!new File(savepath).exists()) {
                try{
                    new File(savepath).mkdir();
                } catch (Exception e2) {
                    e2.getStackTrace();
                }
            }
            //File로 저장 경로와 저장 할 파일명 합쳐서 multipartfile의 transferTo() 사용하여 업로드하려는 파일을 해당 경로로 저장
            String filepath = savepath + "\\" + fileName;
            files.transferTo(new File(filepath));

            //set으로 설정한 파일명, 경로 값 넘겨주기
            reqFileUploadDto.setF_originName(originFileName);
            reqFileUploadDto.setF_name(fileName);
            reqFileUploadDto.setF_path(filepath);

        } catch (Exception e1) {
            e1.getStackTrace();
        }
            final Long f_id = fileRepository.save(
                    Files.builder()
                            .f_id(reqFileUploadDto.getF_id())
                            .f_name(reqFileUploadDto.getF_name())
                            .f_origin(reqFileUploadDto.getF_originName())
                            .f_path(reqFileUploadDto.getF_path())
                            .board(board)
                            .build()
            ).getF_id();
            return new ResBoardDto.ResFileUploadDto(f_id);
        }
}

