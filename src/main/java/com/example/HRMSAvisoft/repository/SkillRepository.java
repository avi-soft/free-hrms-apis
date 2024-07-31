package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findBySkill(String skill);
}
