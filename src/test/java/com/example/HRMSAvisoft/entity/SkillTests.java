package com.example.HRMSAvisoft.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class SkillTests {
    Long skillId;
    String skill;

    @BeforeEach
    void setUp(){
        skillId = 1L;
        skill = "JAVA";
    }

    @Test
    @DisplayName("testSkillConstructor")
    void testSkillConstructor(){
        Skill newSkill = new Skill(skillId, skill);

        assertEquals(skillId, newSkill.getSkillId());
        assertEquals(skill, newSkill.getSkill());
    }

    @Test
    @DisplayName("testSkillGettersAndSetters")
    void testSkillGettersAndSetters(){
        Skill newSkill = new Skill();

        newSkill.setSkillId(skillId);
        newSkill.setSkill(skill);

        assertEquals(skillId, newSkill.getSkillId());
        assertEquals(skill, newSkill.getSkill());    }
}
