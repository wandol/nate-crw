package com.nate.crw.repository;

import com.nate.crw.domain.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SrcRepository extends JpaRepository<Source,String> {

    Source findBySiteNmAndArticleCategoryAndUseYn(String naver, String headline, String y);

}