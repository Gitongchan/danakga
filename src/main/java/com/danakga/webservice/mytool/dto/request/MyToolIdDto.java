package com.danakga.webservice.mytool.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyToolIdDto {

    private Long myToolId;

    private Long myToolFolderId;
}
