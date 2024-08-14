package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.config.TestSecurityConfig;
import com.example.HRMSAvisoft.entity.Designation;
import com.example.HRMSAvisoft.entity.Skill;
import com.example.HRMSAvisoft.repository.SkillRepository;
import com.example.HRMSAvisoft.service.SkillService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkillController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
@ContextConfiguration(classes = TestSecurityConfig.class)
public class SkillControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SkillService skillService;

    @MockBean
    SkillRepository skillRepository;


    @Value("${getAndAddSkill.url}")
    String getAndAddSkillUrl;

    @Value("${updateAndDeleteSkill.url}")
    String updateAndDeleteSkillUrl;


    private String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    @Test
    @DisplayName("testGetAllSkill")
    void getAllSkills() throws Exception {
        List<Skill> skillList = new ArrayList<Skill>();

        skillList.add(new Skill(1L, "REACT"));
        skillList.add(new Skill(2L, "NODE"));

        when(skillService.getAllSkill()).thenReturn(skillList);
        this.mockMvc.perform(get(getAndAddSkillUrl))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("testAddSkill")
    void testAddSkill() throws Exception {

        String jsonPayload = readFileAsString("src/test/resources/payloads/CreateSkill.json");

        Skill mockSkill = new Skill();
        mockSkill.setSkill("Spring");

        when(skillService.addSkill(any(Skill.class))).thenReturn(mockSkill);

        this.mockMvc.perform(post(getAndAddSkillUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("testUpdateSkill")
    void testUpdateSkill() throws Exception {
        String jsonPayload = readFileAsString("src/test/resources/payloads/CreateSkill.json");

        Skill mockSkill = new Skill();
        mockSkill.setSkill("Spring");


        this.mockMvc.perform(patch(updateAndDeleteSkillUrl, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("testDeleteSkill")
    void testDeleteSkill() throws Exception {

        Skill mockSkill = new Skill(1L, "Node");
        when(skillRepository.findById(1L)).thenReturn(Optional.of(mockSkill));

        this.mockMvc.perform(delete(updateAndDeleteSkillUrl, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}
