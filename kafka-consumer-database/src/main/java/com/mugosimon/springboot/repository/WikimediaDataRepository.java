package com.mugosimon.springboot.repository;

import com.mugosimon.springboot.entity.WikimediaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WikimediaDataRepository
        extends JpaRepository<WikimediaData, Long> {

}
