package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Skill;
import com.example.HRMSAvisoft.repository.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class SkillService {

    private final SkillRepository skillRepository;

    SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill addSkill(Skill skill)throws IllegalArgumentException {

        if(skill.getSkill() == null || skill.getSkill().equals("") || skill.getSkill().trim().equals("")){
            throw new IllegalArgumentException("Skill cannot be null");
        }
        return skillRepository.save(skill);
    }

    public Page<Skill> getAllSkill(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        List<Skill> skillList = skillRepository.findAll();
        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), skillList.size());
        return new PageImpl<>(skillList.subList(start, end), pageable, skillList.size());
    }

    public void updateSkill(Skill skill, Long skillId) throws EntityNotFoundException, IllegalArgumentException{
        if(skill.getSkill() == null || skill.getSkill().equals("") || skill.getSkill().trim().equals("")){
            throw new IllegalArgumentException("Skill cannot be null");
        }

        Skill skillToUpdate = skillRepository.findById(skillId).orElseThrow(()-> new EntityNotFoundException("Skill not found."));

        if(skill.getSkill() != null && !skill.getSkill().equals(""))
            skillToUpdate.setSkill(skill.getSkill());

        skillRepository.save(skillToUpdate);
    }

    public void deleteSkill(Long skillId)throws EntityNotFoundException{
        Skill skillToDelete = skillRepository.findById(skillId).orElseThrow(()-> new EntityNotFoundException("Skill not found."));

        skillRepository.delete(skillToDelete);
    }
}
