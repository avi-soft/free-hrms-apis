package com.example.HRMSAvisoft.service;


import com.example.HRMSAvisoft.entity.Designation;
import com.example.HRMSAvisoft.repository.DesignationRepository;
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
public class DesignationService {

    private final DesignationRepository designationRepository;

    DesignationService(DesignationRepository designationRepository) {
        this.designationRepository = designationRepository;
    }

    public Designation addDesignation(Designation designation)throws IllegalArgumentException{
        Designation newDesignation = new Designation();
        if(designation.getDesignation() == null || designation.getDesignation().equals("") || designation.getDesignation().trim().equals("")){
            throw new IllegalArgumentException("Designation field cannot be null");
        }
        newDesignation.setDesignation(designation.getDesignation());

        return designationRepository.save(newDesignation);
    }

    public Page<Designation> getAllDesignations(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        List<Designation> designationsList = designationRepository.findAll();

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), designationsList.size());

        return new PageImpl<>(designationsList.subList(start, end), pageable, designationsList.size());
    }

    public void updateDesignation(Designation designation, Long designationId) throws EntityNotFoundException, IllegalArgumentException{
        if(designation.getDesignation() == null || designation.getDesignation().equals("") || designation.getDesignation().trim().equals("")){
            throw new IllegalArgumentException("Designation field cannot be null");
        }        Designation designationToUpdate = designationRepository.findById(designationId).orElseThrow(()-> new EntityNotFoundException("Designation not found"));

        if(designation.getDesignation() != null && !designation.getDesignation().equals(""))
            designationToUpdate.setDesignation(designation.getDesignation());

        designationRepository.save(designationToUpdate);

    }

    public void deleteDesignation(Long designationId) throws EntityNotFoundException{
        Designation designationToDelete = designationRepository.findById(designationId).orElseThrow(()-> new EntityNotFoundException("Designation not found"));

        designationRepository.delete(designationToDelete);
    }


}
