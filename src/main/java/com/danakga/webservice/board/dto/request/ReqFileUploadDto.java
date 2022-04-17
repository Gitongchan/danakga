package com.danakga.webservice.board.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReqFileUploadDto {
    private Long f_id;
    private String f_originName;
    private String f_name;
    private String f_path;

}
