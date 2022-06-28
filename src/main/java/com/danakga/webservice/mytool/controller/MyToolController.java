package com.danakga.webservice.mytool.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.mytool.dto.request.DetailSaveDto;
import com.danakga.webservice.mytool.dto.request.FolderNameDto;
import com.danakga.webservice.mytool.service.MyToolDetailService;
import com.danakga.webservice.mytool.service.MyToolFolderService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/myTool")
public class MyToolController {
    private final MyToolFolderService myToolFolderService;
    private final MyToolDetailService myToolDetailService;

    //내 장비 폴더 생성
    @PostMapping("/folder")
    public ResResultDto folderSave(@LoginUser UserInfo userInfo,@RequestBody FolderNameDto folderNameDto){
        Long result = myToolFolderService.MyToolFolderSave(userInfo,folderNameDto.getFolderName());
        return new ResResultDto(result,"폴더가 생성 되었습니다.");
    }

    //내 장비 추가
    @PostMapping("/detail")
    public ResResultDto folderSave(@RequestBody DetailSaveDto detailSaveDto){
        Long result = myToolDetailService.MyToolDetailSave(detailSaveDto.getProductId(), detailSaveDto.getFolderId());
        return new ResResultDto(result,"장비가 추가 되었습니다.");
    }

}
