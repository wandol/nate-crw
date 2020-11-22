package com.nate.crw.repository;

import com.nate.crw.domain.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SrcRepository extends JpaRepository<Source,String> {

    Source findBySiteNmAndArticleCategoryAndUseYn(String naver, String headline, String y);

    @Transactional
    @Modifying
    @Query(value=" UPDATE potal_source_tb s"
            + " SET s.crw_status_msg = :errorMsg, s.crw_status ='ERROR', up_Dt = now()"
            + " WHERE s.article_category = :errorArticleCate and s.site_nm = :errorSiteNm ", nativeQuery = true)
    void updateErrorValue(@Param("errorSiteNm") String errorSiteNm, @Param("errorArticleCate") String errorArticleCate, @Param("errorMsg") String errorMsg);

    @Transactional
    @Modifying
    @Query(value=" UPDATE potal_source_tb s"
            + " SET s.crw_status ='SUCCESS', s.up_dt = now()"
            + " WHERE s.article_category = :cateName and s.site_nm = :siteNm ", nativeQuery = true)
    void updateSuccessComplate(@Param("siteNm") String siteNm, @Param("cateName") String cateName);
}