package com.danakga.webservice.mytool.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResMyToolFolderDto {

    //내 장비 폴더 아이디
    private Long myToolFolderId;

    //내 장비 폴더명
    private String myToolFolderName;
}
