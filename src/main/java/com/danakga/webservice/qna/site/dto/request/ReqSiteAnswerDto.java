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
public class ReqSiteAnswerDto {

    private Long siteAId;

    @NotBlank(message = "내용은 필수로 입력해야 합니다.")
    private String siteAContent;
}
