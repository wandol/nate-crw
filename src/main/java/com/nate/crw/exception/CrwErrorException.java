package com.nate.crw.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrwErrorException extends Throwable {
    public CrwErrorException(String s, Exception e) {
        log.debug("수집 error :: class - {}", e.getClass());
    }
}
