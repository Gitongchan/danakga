package com.danakga.webservice.mytool.service;

import com.danakga.webservice.mytool.dto.request.DetailSaveDto;
import com.danakga.webservice.mytool.dto.request.MyToolIdDto;
import com.danakga.webservice.user.model.UserInfo;

import java.util.List;

public interface MyToolDetailService {
    //목록 저장
    void MyToolDetailSave(UserInfo userInfo,List<DetailSaveDto> detailSaveDto);
    
    //목록에서 장비 제거
    void MyToolDelete(UserInfo userInfo , MyToolIdDto myToolIdDto);

}
