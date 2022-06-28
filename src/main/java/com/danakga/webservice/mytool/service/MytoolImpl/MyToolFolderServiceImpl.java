package com.danakga.webservice.mytool.service.MytoolImpl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.mytool.model.MyToolFolder;
import com.danakga.webservice.mytool.repository.MyToolFolderRepository;
import com.danakga.webservice.mytool.service.MyToolFolderService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyToolFolderServiceImpl implements MyToolFolderService {
    private final UserRepository userRepository;
    private final MyToolFolderRepository myToolFolderRepository;

    //내 장비 폴더 생성
    @Override
    public Long MyToolFolderSave(UserInfo userInfo, String FolderName) {
        UserInfo folderUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        return myToolFolderRepository.save(
                MyToolFolder.builder()
                        .userInfo(folderUserInfo)
                        .myToolFolder(FolderName)
                        .build()
        ).getId();


    }
}
