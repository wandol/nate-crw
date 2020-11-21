package com.nate.crw.exception;

import com.nate.crw.dto.ErrorDto;
import com.nate.crw.repository.SrcRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CrwErrorException extends Throwable {

    @Autowired
    public SrcRepository srcRepository;

    public CrwErrorException(ErrorDto dto) {
        log.error(dto.toString());
        srcRepository.updateErrorValue(dto.getErrorSiteNm(),dto.getErrorArticleCate(),dto.getErrorMsg());
    }
}
