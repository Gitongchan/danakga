package com.danakga.webservice.mytool.service;

import com.danakga.webservice.mytool.dto.response.ResMyToolDetailDto;
import com.danakga.webservice.user.model.UserInfo;

import java.util.List;

public interface MyToolFolderService {
    Long MyToolFolderSave(UserInfo userInfo,String folderName);

    Long MyToolFolderNameUpdate(UserInfo userInfo,Long folderId,String folderName);


}
