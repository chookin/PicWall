package com.cmri.pic.wall.server.repository;

import com.cmri.pic.wall.server.domain.Pic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by zhuyin on 1/6/16.
 */
public interface PicRepository extends CrudRepository<Pic, Long> {
    @Query("select e from Pic e where e.uploadTime >= ? order by e.id ")
    List<Pic> findSince(Timestamp time);
}