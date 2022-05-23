package com.danakga.webservice.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResBoardListDto {

    private Long bdId;
    private String bdWriter;
    private String bdTitle;
    private LocalDateTime bdCreated;
    private int bdViews;
    private String bdDeleted;
}
