package com.danakga.webservice.mytool.service;

import com.danakga.webservice.user.model.UserInfo;

public interface MyToolFolderService {
    Long MyToolFolderSave(UserInfo userInfo,String folderName);

    Long MyToolFolderNameUpdate(UserInfo userInfo,Long folderId,String folderName);
}
