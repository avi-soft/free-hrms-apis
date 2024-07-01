package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Zipcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZipCodeRepository extends JpaRepository<Zipcode,Long> {
}
