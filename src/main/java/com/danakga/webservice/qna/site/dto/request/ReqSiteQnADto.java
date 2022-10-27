package com.danakga.webservice.qna.site.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ReqSiteQnADto {

    @NotBlank(message = "유형은 필수로 선택해야 합니다.")
    private String siteQType;

    @NotBlank(message = "제목은 필수로 입력해야 합니다.")
    private String siteQTitle;

    @NotBlank(message = "내용은 필수로 입력해야 합니다.")
    private String siteQContent;

}
