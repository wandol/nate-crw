package com.nate.crw.repository;

import com.nate.crw.domain.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SrcRepository extends JpaRepository<Source,String> {

    Source findBySiteNmAndArticleCategoryAndUseYn(String naver, String headline, String y);

    @Transactional
    @Modifying
    @Query(value=" UPDATE potal_source_tb "
            + " SET crw_status_msg =  (:errorMsg), crw_status ='ERROR', up_Dt = now()"
            + " WHERE src_type = :errorArticleCate and site_nm = :errorSiteNm ", nativeQuery = true)
    void updateErrorValue(String errorSiteNm, String errorArticleCate, String errorMsg);
}