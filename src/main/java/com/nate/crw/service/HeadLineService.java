package com.nate.crw.service;


import com.nate.crw.domain.Contents;
import com.nate.crw.domain.Source;

import java.util.List;

public interface HeadLineService {

    int updatePostEndDt(List<String> pks,String articleArea, String siteNm);

    List<Contents> saveAll(List<Contents> contentsList);

    Source findBySiteNmAndArticleCategoryAndUseYn(String sitem, String toString1, String y);

    Contents findFirstBySiteNmAndArticlePkAndDelYn(String toString, String pk_v, String n);
}
