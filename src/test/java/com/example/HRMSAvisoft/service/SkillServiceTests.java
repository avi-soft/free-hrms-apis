//package com.example.HRMSAvisoft.service;
//
//import com.example.HRMSAvisoft.entity.Skill;
//import com.example.HRMSAvisoft.repository.SkillRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertThrows;
//
//@ExtendWith(MockitoExtension.class)
//public class SkillServiceTests {
//    @InjectMocks
//    SkillService skillService;
//
//    @Mock
//    SkillRepository skillRepository;
//
//    // Skill with valid name is successfully added
//    @Test
//    @DisplayName("test_skill_with_valid_name_is_successfully_added")
//    public void test_skill_with_valid_name_is_successfully_added() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//        Skill skill = new Skill();
//        skill.setSkill("Java");
//
//        Mockito.when(skillRepository.save(skill)).thenReturn(skill);
//
//        Skill result = skillService.addSkill(skill);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals("Java", result.getSkill());
//    }
//
//    // SkillRepository's save method is called with the correct Skill object
//    @Test
//    @DisplayName("test_skillrepository_save_method_called_with_correct_skill_object")
//    public void test_skillrepository_save_method_called_with_correct_skill_object() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//        Skill skill = new Skill();
//        skill.setSkill("Java");
//
//        skillService.addSkill(skill);
//
//        Mockito.verify(skillRepository, Mockito.times(1)).save(skill);
//    }
//
//    // Skill object with null name throws IllegalArgumentException
//    @Test
//    @DisplayName("test_skill_with_null_name_throws_illegalargumentexception")
//    public void test_skill_with_null_name_throws_illegalargumentexception() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//        Skill skill = new Skill();
//        skill.setSkill(null);
//
//        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            skillService.addSkill(skill);
//        });
//    }
//
//    // Skill object with empty string name throws IllegalArgumentException
//    @Test
//    @DisplayName("test_skill_with_empty_string_name_throws_illegalargumentexception")
//    public void test_skill_with_empty_string_name_throws_illegalargumentexception() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//        Skill skill = new Skill();
//        skill.setSkill("");
//
//        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            skillService.addSkill(skill);
//        });
//    }
//
//    // Skill object with whitespace-only name throws IllegalArgumentException
//    @Test
//    public void test_skill_with_whitespace_only_name_throws_illegalargumentexception() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//        Skill skill = new Skill();
//        skill.setSkill("   ");
//
//        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            skillService.addSkill(skill);
//        });
//    }
//
//    // Retrieve all skills when the repository contains multiple skills
//    @Test
//    @DisplayName("test_retrieve_all_skills_when_repository_contains_multiple_skills")
//    public void test_retrieve_all_skills_when_repository_contains_multiple_skills() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//
//        List<Skill> skills = List.of(new Skill(1L, "Java"), new Skill(2L, "Python"));
//        Mockito.when(skillRepository.findAll()).thenReturn(skills);
//
//        List<Skill> result = skillService.getAllSkill();
//
//        Assertions.assertEquals(2, result.size());
//        Assertions.assertEquals("Java", result.get(0).getSkill());
//        Assertions.assertEquals("Python", result.get(1).getSkill());
//    }
//
//    // Retrieve an empty list when the repository contains no skills
//    @Test
//    @DisplayName("test_retrieve_empty_list_when_repository_contains_no_skills")
//    public void test_retrieve_empty_list_when_repository_contains_no_skills() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//
//        Mockito.when(skillRepository.findAll()).thenReturn(Collections.emptyList());
//
//        List<Skill> result = skillService.getAllSkill();
//
//        Assertions.assertTrue(result.isEmpty());
//    }
//
//    // Successfully update a skill when valid skill and skillId are provided
//    @Test
//    @DisplayName("test_update_skill_success")
//    public void test_update_skill_success() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//        Skill skill = new Skill();
//        skill.setSkill("Java");
//        Skill existingSkill = new Skill();
//        existingSkill.setSkillId(1L);
//        existingSkill.setSkill("Python");
//
//        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(existingSkill));
//
//        skillService.updateSkill(skill, 1L);
//
//        Mockito.verify(skillRepository).save(existingSkill);
//        assertEquals("Java", existingSkill.getSkill());
//    }
//
//    // Throw IllegalArgumentException when skill is null
//    @Test
//    @DisplayName("test_throw_illegal_argument_exception_when_skill_is_null")
//    public void test_throw_illegal_argument_exception_when_skill_is_null() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//        Skill skill = new Skill();
//        skill.setSkill(null);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            skillService.updateSkill(skill, 1L);
//        });
//    }
//
//    // Throw IllegalArgumentException when skill is an empty string
//    @Test
//    @DisplayName("test_throw_illegal_argument_exception_when_skill_is_empty_string")
//    public void test_throw_illegal_argument_exception_when_skill_is_empty_string() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//        Skill skill = new Skill();
//        skill.setSkill("");
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            skillService.updateSkill(skill, 1L);
//        });
//    }
//
//    // Throw IllegalArgumentException when skill is a string with only whitespace
//    @Test
//    @DisplayName("test_throw_illegal_argument_exception_when_skill_is_whitespace")
//    public void test_throw_illegal_argument_exception_when_skill_is_whitespace() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//        Skill skill = new Skill();
//        skill.setSkill("   ");
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            skillService.updateSkill(skill, 1L);
//        });
//    }
//
//    // Successfully delete an existing skill by valid skillId
//    @Test
//    @DisplayName("test_delete_skill_success")
//    public void test_delete_skill_success() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//        Skill skill = new Skill(1L, "Java");
//
//        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
//
//        skillService.deleteSkill(1L);
//
//        Mockito.verify(skillRepository).delete(skill);
//    }
//
//    // Throw EntityNotFoundException when skillId does not exist
//    @Test
//    @DisplayName("test_delete_skill_not_found")
//    public void test_delete_skill_not_found() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//
//        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(EntityNotFoundException.class, () -> {
//            skillService.deleteSkill(1L);
//        });
//    }
//
//    // Ensure no action is taken if skillId is invalid
//    @Test
//    @DisplayName("test_delete_skill_invalid_id")
//    public void test_delete_skill_invalid_id() {
//        SkillRepository skillRepository = Mockito.mock(SkillRepository.class);
//        SkillService skillService = new SkillService(skillRepository);
//
//        Assertions.assertThrows(EntityNotFoundException.class, () -> {
//            skillService.deleteSkill(-1L);
//        });
//
//        Mockito.verify(skillRepository, Mockito.never()).delete(Mockito.any(Skill.class));
//    }
//}
