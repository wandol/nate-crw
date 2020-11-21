package com.nate.crw.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ErrorDto {

    private String errorSiteNm;

    private String errorArticleCate;

    private String errorMsg;

    @Builder
    public ErrorDto(String errorSiteNm, String errorArticleCate, String errorMsg) {
        this.errorSiteNm = errorSiteNm;
        this.errorArticleCate = errorArticleCate;
        this.errorMsg = errorMsg;
    }
}
