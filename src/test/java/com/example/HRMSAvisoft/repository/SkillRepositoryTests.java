package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Designation;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Skill;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class SkillRepositoryTests {
    @Autowired
    SkillRepository skillRepository;


    String skillName = "Springboot";
    Skill newSkill;

    @BeforeEach
    void setup(){
        Skill skill = new Skill();
        skill.setSkill(skillName);

        newSkill = skillRepository.save(skill);
    }

    @Test
    @DisplayName("testSaveSkill")
    void saveSkill(){
        assertEquals(skillName, newSkill.getSkill());
    }

    @Test
    @DisplayName("testGetSkillById")
    void getSkillById(){

        Skill skillFoundById = skillRepository.findById(newSkill.getSkillId()).orElse(null);

        assertNotNull(skillFoundById);
        assertEquals(skillName, skillFoundById.getSkill());

    }

    @Test
    @DisplayName("testUpdateSkill")
    void updateSkill()throws EntityNotFoundException {

        Skill skillFoundById = skillRepository.findById(newSkill.getSkillId()).orElseThrow(()-> new EntityNotFoundException("Department not found"));

        assertNotNull(skillFoundById);

        String newSkillName = "MERN";

        skillFoundById.setSkill(newSkillName);

        Skill updatedSkill = skillRepository.save(skillFoundById);

        assertEquals(newSkillName, updatedSkill.getSkill());
    }

    @Test
    @DisplayName("testDeleteSkill")
    void deleteSkill(){
        skillRepository.delete(newSkill);

        Skill deletedSkill = skillRepository.findById(newSkill.getSkillId()).orElse((null));

        assertNull(deletedSkill);

    }

}
