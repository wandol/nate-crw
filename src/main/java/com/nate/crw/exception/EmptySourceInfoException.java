package com.nate.crw.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * User: wandol
 * Date: 2020/10/23
 * Time: 8:06 오후
 * Desc:    수집원이 없을때
 */
@Slf4j
public class EmptySourceInfoException extends Throwable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmptySourceInfoException(String s) {
		log.info("");
    }
}
