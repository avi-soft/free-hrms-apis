package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.ShiftDurationDTO;
import com.example.HRMSAvisoft.entity.ShiftDuration;
import com.example.HRMSAvisoft.repository.ShiftDurationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ShiftDurationService {

    private final ShiftDurationRepository shiftDurationRepository;

    public ShiftDurationService(ShiftDurationRepository shiftDurationRepository){
        this.shiftDurationRepository = shiftDurationRepository;
    }

    public ShiftDuration saveShiftDuration(ShiftDurationDTO shiftDurationDTO) throws IllegalStateException{

        List<ShiftDuration> existingShiftDurationList = shiftDurationRepository.findAll();
        if(existingShiftDurationList.size() >= 1){
            throw new IllegalStateException("Duration exists, You must update or delete it.");
        }
        Duration duration = Duration.ofHours(shiftDurationDTO.getShiftDurationHours()).plusMinutes(shiftDurationDTO.getShiftDurationMinutes());

        ShiftDuration newShiftDuration = new ShiftDuration();
        newShiftDuration.setShiftDuration(duration);

        return shiftDurationRepository.save(newShiftDuration);
    }

    public List<ShiftDuration> getShiftDurations(){
        List<ShiftDuration> shiftDurationList = shiftDurationRepository.findAll();

        return shiftDurationList;
    }

    public ShiftDuration updateShiftDuration(ShiftDurationDTO shiftDurationDTO, Long shiftDurationId)throws EntityNotFoundException{
        ShiftDuration shiftDurationToUpdate = shiftDurationRepository.findById(shiftDurationId).orElseThrow(() -> new EntityNotFoundException("Shift Duration not found"));

        Duration updatedShiftDuration = Duration.ZERO;

        if(Objects.nonNull(shiftDurationDTO.getShiftDurationHours())){
            updatedShiftDuration = updatedShiftDuration.plusHours(shiftDurationDTO.getShiftDurationHours());
        }
        if(Objects.nonNull(shiftDurationDTO.getShiftDurationMinutes())){
            updatedShiftDuration = updatedShiftDuration.plusMinutes(shiftDurationDTO.getShiftDurationMinutes());
        }

        shiftDurationToUpdate.setShiftDuration(updatedShiftDuration);

        return shiftDurationRepository.save(shiftDurationToUpdate);
    }

    public void deleteShiftDuration(long shiftDurationId){
        shiftDurationRepository.deleteById(shiftDurationId);
    }
}
