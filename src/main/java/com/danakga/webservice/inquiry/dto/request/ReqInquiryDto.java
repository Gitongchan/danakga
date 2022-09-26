package com.danakga.webservice.inquiry.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ReqInquiryDto {

    @NotBlank(message = "유형은 필수로 선택해야 합니다.")
    private String inType;

    @NotBlank(message = "제목은 필수로 입력해야 합니다.")
    private String inTitle;

    @NotBlank(message = "내용은 필수로 입력해야 합니다.")
    private String inContent;

}
