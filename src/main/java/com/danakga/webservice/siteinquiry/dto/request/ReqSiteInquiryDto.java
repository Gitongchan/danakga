package com.danakga.webservice.siteinquiry.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ReqSiteInquiryDto {

    @NotBlank(message = "유형은 필수로 선택해야 합니다.")
    private String sinType;

    @NotBlank(message = "제목은 필수로 입력해야 합니다.")
    private String sinTitle;

    @NotBlank(message = "내용은 필수로 입력해야 합니다.")
    private String sinContent;

}
