package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.Skill;
import com.example.HRMSAvisoft.service.SkillService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/skill")
@Transactional
public class SkillController {

    private final SkillService skillService;

    SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PreAuthorize("hasAnyAuthority('ADD_SKILL')")
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addSkill(@RequestBody Skill skill)throws IllegalArgumentException{
        Skill newSkill = skillService.addSkill(skill);
        return ResponseEntity.status(201).body(Map.of("success", true, "message", "Skill added successfully", "Skill", newSkill));
    }

    @PreAuthorize("hasAnyAuthority('GET_ALL_SKILL')")
    @GetMapping("")
    public ResponseEntity<Page<Skill>> getAllSkill(
            @RequestParam(defaultValue = "0" ) int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<Skill> skillListPage = skillService.getAllSkill(page, size);

        return ResponseEntity.status(200).body(skillListPage);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_SKILL')")
    @PatchMapping("/{skillId}")
    public ResponseEntity<Map<String, Object>> updateSkill(@RequestBody Skill skill, @PathVariable("skillId") Long skillId)throws EntityNotFoundException, IllegalArgumentException{
        skillService.updateSkill(skill, skillId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "skill updated successfully"));
    }

    @PreAuthorize("hasAnyAuthority('DELETE_SKILL')")
    @DeleteMapping("/{skillId}")
    public ResponseEntity<Map<String, Object>> deleteSkill(@PathVariable("skillId") Long skillId){
        skillService.deleteSkill(skillId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "skill deleted successfully"));
    }


    @ExceptionHandler({
            EntityNotFoundException.class,
            IllegalArgumentException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EntityNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if(exception instanceof IllegalArgumentException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else{
            message = "something went wrong";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}
