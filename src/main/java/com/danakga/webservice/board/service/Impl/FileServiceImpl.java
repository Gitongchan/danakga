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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FilesService {

    @Autowired private final FileRepository fileRepository;

    //파일 최대 용량, 개당 용량 제한 propertis에서 걸어주기
    //Board의 id값 어떻게 받아와서 넣어줄지 생각하기
    @Override
    public ResFileUploadDto saveFileUpload(ReqFileUploadDto reqFileUploadDto, List<MultipartFile> files, Board board) {
        // 전달되어 온 파일이 존재할 경우
        // CollectionUtils로 List 타입의 데이터가 비어있는지 확인
        if(!CollectionUtils.isEmpty(files)) {
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
            //다중 파일 처리
            for(MultipartFile multipartFile : files) {
                String originalFilename;
                String contentType = multipartFile.getContentType();

                //확장자명 없으면 처리 x
                if(ObjectUtils.isEmpty(contentType)) {
                    break;
                }
                //jpg, png 두개만 처리
                else {
                    if(contentType.contains("image/jpg"))
                        originalFilename = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFilename = ".png";
                    else  // 다른 확장자일 경우 처리 x
                        break;
                }
                //UUID로 파일명 중복되지 않게 유일한 식별자로 파일명 저장
                UUID uuid = UUID.randomUUID();
                String fileName = uuid + "__" + originalFilename;

                //File로 저장 경로와 저장 할 파일명 합쳐서 multipartfile의 transferTo() 사용하여 업로드하려는 파일을 해당 경로로 저장
                String filepath = savepath + "\\" + fileName;
                try {
                    multipartFile.transferTo(new File(filepath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //set으로 설정한 파일명, 경로 값 넘겨주기
                reqFileUploadDto.setF_originName(originalFilename);
                reqFileUploadDto.setF_name(fileName);
                reqFileUploadDto.setF_path(filepath);
            }
        }

        final Long f_id = fileRepository.save(
                Files.builder()
                        .f_name(reqFileUploadDto.getF_name())
                        .f_origin(reqFileUploadDto.getF_originName())
                        .f_path(reqFileUploadDto.getF_path())
                        .board(board) //bd_id 인데 이걸 어떻게 넣어주느냐
                        .build()
        ).getF_id();

        return new ResFileUploadDto(f_id);
    }
}
