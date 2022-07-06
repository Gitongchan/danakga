package com.danakga.webservice.mytool.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.mytool.dto.request.MyToolIdDto;
import com.danakga.webservice.mytool.dto.request.DetailSaveDto;
import com.danakga.webservice.mytool.dto.request.FolderNameDto;
import com.danakga.webservice.mytool.dto.request.UpdateFolderNameDto;
import com.danakga.webservice.mytool.dto.response.ResMyToolDetailDto;
import com.danakga.webservice.mytool.dto.response.ResMyToolFolderDto;
import com.danakga.webservice.mytool.service.MyToolDetailService;
import com.danakga.webservice.mytool.service.MyToolFolderService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    
    //폴더명 변경
    @PutMapping("/folder")
    public ResResultDto folderUpdate(@LoginUser UserInfo userInfo,@RequestBody UpdateFolderNameDto updateFolderNameDto){
        Long result = myToolFolderService.MyToolFolderNameUpdate(userInfo, updateFolderNameDto.getFolderId(),updateFolderNameDto.getFolderName());
        return new ResResultDto(result,"폴더명이 변경 되었습니다.");
    }

    //내 장비 폴더명 조회
    @GetMapping("/folder")
    public List<ResMyToolFolderDto> folderList(@LoginUser UserInfo userInfo){
        return myToolFolderService.myToolFolderList(userInfo);
    }

    //내 장비 추가
    @PostMapping("/detail")
    public ResResultDto detailSave(@LoginUser UserInfo userInfo, @RequestBody List<DetailSaveDto> detailSaveDto){
        myToolDetailService.MyToolDetailSave(userInfo,detailSaveDto);
        return new ResResultDto(1L,"장비가 추가 되었습니다.");
    }

    //내 장비 삭제
    @DeleteMapping("/detail")
    public ResResultDto detailDelete(@LoginUser UserInfo userInfo, @RequestBody MyToolIdDto myToolIdDto){
        myToolDetailService.MyToolDelete(userInfo,myToolIdDto);
        return new ResResultDto(1L,"장비를 삭제 했습니다.");
    }

    //내 장비 조회
    @GetMapping("/detail/{myToolFolderId}")
    public List<ResMyToolDetailDto> MyToolList(@LoginUser UserInfo userInfo,@PathVariable("myToolFolderId") Long myToolFolderId){

        return myToolDetailService.myToolList(userInfo,myToolFolderId);
    }



}
