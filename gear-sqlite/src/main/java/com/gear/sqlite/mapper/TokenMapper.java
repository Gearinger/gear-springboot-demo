package com.gear.sqlite.mapper;

import com.gear.sqlite.db.TokenEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenMapper extends JpaRepositoryImplementation<TokenEntity, Integer> {
}
