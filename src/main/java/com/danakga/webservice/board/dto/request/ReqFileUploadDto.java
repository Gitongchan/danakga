package com.danakga.webservice.board.dto.request;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReqFileUploadDto {
    private Long f_id;
    private String f_originName;
    private String f_savename;
    private String f_path;
}
