package com.gear.sqlite.mapper;

import com.gear.sqlite.db.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemMapper extends JpaRepositoryImplementation<ItemEntity, Integer> {
}
