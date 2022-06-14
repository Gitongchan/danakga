package com.danakga.webservice.board.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReqDeletedFileDto {
     
     //삭제 할 파일명
     private List<Map<String, Object>> deletedFiles;
}
