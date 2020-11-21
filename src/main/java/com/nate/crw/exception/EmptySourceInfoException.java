package com.nate.crw.exception;

import com.nate.crw.dto.ErrorDto;
import com.nate.crw.repository.SrcRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: wandol
 * Date: 2020/10/23
 * Time: 8:06 오후
 * Desc:    수집원이 없을때
 */
@Slf4j
public class EmptySourceInfoException extends Throwable {

	private static final long serialVersionUID = 1L;

	@Autowired
	public SrcRepository srcRepository;


	/**
	 * 	DB에서 수집원을 찾지 못한 에러.
	 * 	수집원 테이블에 상태 업데이트.
	 * @param dto
	 */
	public EmptySourceInfoException(ErrorDto dto) {
		log.error(dto.toString());
		srcRepository.updateErrorValue(dto.getErrorSiteNm(),dto.getErrorArticleCate(),dto.getErrorMsg());
    }
}
