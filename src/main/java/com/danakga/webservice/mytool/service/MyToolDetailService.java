package com.danakga.webservice.mytool.service;

import com.danakga.webservice.mytool.dto.request.DetailSaveDto;
import com.danakga.webservice.user.model.UserInfo;

import java.util.List;

public interface MyToolDetailService {
    void MyToolDetailSave(UserInfo userInfo,List<DetailSaveDto> detailSaveDto);

}
