package com.danakga.webservice.mytool.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailSaveDto {
    private Long productId;
    private Long folderId;
}
