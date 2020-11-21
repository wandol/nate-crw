package com.nate.crw.util;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

/**
 *  여러 utils
 */
@Slf4j
public class CommonUtil {

    /**
     * 문자열을 MD-5 방식으로 암호화
     * @param txt 암호화 하려하는 문자열
     * @return String
     * @throws Exception
     */
    public String getEncMD5(String txt) throws Exception {

        StringBuffer sbuf = new StringBuffer();

        MessageDigest mDigest = MessageDigest.getInstance("MD5");
        mDigest.update(txt.getBytes());

        byte[] msgStr = mDigest.digest() ;

        for(int i=0; i < msgStr.length; i++){
            String tmpEncTxt = Integer.toHexString((int)msgStr[i] & 0x00ff) ;
            sbuf.append(tmpEncTxt) ;
        }
        return sbuf.toString() ;
    }

	/**
	* @Method	checkWriter
	* @Date  	2020. 11. 19.
	* @Writter  wandol
	* @EditHistory
	* @Discript	작성자 체크. 다음 포털의 경우 작성자에 입력으로 오는 경우가 있음.
	* @return 	String
	*/
	public String checkWriter(String writer) {
		
		String result = writer;
		
		if(	writer.contains("입력")) {
			result = "";
		}
		
		return result;
	}

	
	/**
	* @Method	checkDate
	* @Date  	2020. 11. 19.
	* @Writter  wandol
	* @EditHistory
	* @Discript	날짜 포멧 변경 및 체크.
	* @return 	LocalDateTime
	*/
	public LocalDateTime checkDate(String writeDtTag,String formatString) {
		
		//  기사 작성일 parse
        //  디폴트 값을 설정해놓음. properties
        //  기사 작성일 간혹 수정일이 포함되어 옴. ( 이에 배열 첫번째 요소로 parse )
        DateFormat dateParser = new SimpleDateFormat(formatString, Locale.ENGLISH);
        LocalDateTime regDt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        try {
            regDt = LocalDateTime.ofInstant(dateParser.parse(writeDtTag).toInstant(), ZoneId.of("Asia/Seoul"));
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("date parse error :: {}", e.getMessage());
        }
        
		return regDt;
	}
}
