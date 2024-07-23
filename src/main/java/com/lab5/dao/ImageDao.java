package com.lab5.dao;

import com.lab5.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ImageDao extends JpaRepository<Image, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Image p WHERE p.filename = :filename")
    void deleteByName(@Param("filename") String filename);


}
