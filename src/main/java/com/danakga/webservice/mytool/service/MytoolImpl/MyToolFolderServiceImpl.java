package com.danakga.webservice.mytool.service.MytoolImpl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.mytool.dto.response.ResMyToolDetailDto;
import com.danakga.webservice.mytool.dto.response.ResMyToolFolderDto;
import com.danakga.webservice.mytool.model.MyToolFolder;
import com.danakga.webservice.mytool.repository.MyToolFolderRepository;
import com.danakga.webservice.mytool.service.MyToolFolderService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyToolFolderServiceImpl implements MyToolFolderService {
    private final UserRepository userRepository;
    private final MyToolFolderRepository myToolFolderRepository;

    //내 장비 폴더 생성
    @Transactional
    @Override
    public Long MyToolFolderSave(UserInfo userInfo, String folderName) {
        UserInfo folderUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        return myToolFolderRepository.save(
                MyToolFolder.builder()
                        .userInfo(folderUserInfo)
                        .myToolFolder(folderName)
                        .build()
        ).getId();
    }

    //내 장비 폴더명 수정
    @Transactional
    @Override
    public Long MyToolFolderNameUpdate(UserInfo userInfo, Long folderId, String folderName) {
        UserInfo folderUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );
        MyToolFolder updateMyToolFolder = myToolFolderRepository.findByIdAndUserInfo(folderId,folderUserInfo).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("폴더 정보를 찾을 수 없습니다.")
        );

        return myToolFolderRepository.save(
                MyToolFolder.builder()
                        .id(updateMyToolFolder.getId())
                        .userInfo(folderUserInfo)
                        .myToolFolder(folderName)
                        .build()
        ).getId();
    }

    //내 장비 폴더 리스트
    @Override
    public List<ResMyToolFolderDto> myToolFolderList(UserInfo userInfo) {
        UserInfo detailUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        List<MyToolFolder> myToolFolderList = myToolFolderRepository.findByUserInfo(detailUserInfo);

        List<ResMyToolFolderDto> resMyToolFolderDtoList = new ArrayList<>();
        myToolFolderList.forEach(entity->{
                    ResMyToolFolderDto listDto = new ResMyToolFolderDto();
                    listDto.setMyToolFolderId(entity.getId());
                    listDto.setMyToolFolderName(entity.getMyToolFolder());
                    resMyToolFolderDtoList.add(listDto);
                }
        );
        return resMyToolFolderDtoList;
    }

}
