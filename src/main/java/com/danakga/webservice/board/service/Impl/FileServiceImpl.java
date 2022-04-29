package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqFileUploadDto;
import com.danakga.webservice.board.dto.response.ResFileUploadDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Files;
import com.danakga.webservice.board.repository.FileRepository;
import com.danakga.webservice.board.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Autowired private final FileRepository fileRepository;

    @Override
    public ResFileUploadDto savefile(ReqFileUploadDto reqFileUploadDto, Board board) {
        final Long f_id = fileRepository.save(
                Files.builder()
                        .f_id(reqFileUploadDto.getF_id())
                        .f_name(reqFileUploadDto.getF_name())
                        .f_origin(reqFileUploadDto.getF_originName())
                        .f_path(reqFileUploadDto.getF_path())
                        .board(board)
                        .build()
        ).getF_id();
        return new ResFileUploadDto(f_id);
    }
}
