package com.example.HRMSAvisoft.service;


import com.example.HRMSAvisoft.controller.AttendanceLocationController;
import com.example.HRMSAvisoft.entity.AttendanceLocation;
import com.example.HRMSAvisoft.repository.AttendanceLocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class AttendanceLocationService {

    private final AttendanceLocationRepository attendanceLocationRepository;

    public AttendanceLocationService(AttendanceLocationRepository attendanceLocationRepository){
        this.attendanceLocationRepository = attendanceLocationRepository;
    }

    public AttendanceLocation addAttendanceLocation(AttendanceLocation attendanceLocation){
        return attendanceLocationRepository.save(attendanceLocation);
    }

    public List<AttendanceLocation> getAllAttendanceLocation(){
        return attendanceLocationRepository.findAll();
    }

    public AttendanceLocation updateAttendanceLocation(Long attendanceLocationId, AttendanceLocation attendanceLocation)throws EntityNotFoundException{
        AttendanceLocation attendanceLocationToUpdate = attendanceLocationRepository.findById(attendanceLocationId).orElseThrow(()-> new EntityNotFoundException("Location not found."));

        if(Objects.nonNull(attendanceLocation.getLatitude())){
            attendanceLocationToUpdate.setLatitude(attendanceLocation.getLatitude());
        }
        if(Objects.nonNull(attendanceLocation.getLongitude())){
            attendanceLocationToUpdate.setLongitude(attendanceLocation.getLongitude());
        }
        if(Objects.nonNull(attendanceLocation.getElevation())){
            attendanceLocationToUpdate.setElevation(attendanceLocation.getElevation());
        }

        return attendanceLocationRepository.save(attendanceLocation);
    }

    public void deleteAttendanceLocation(Long attendanceLocationId, AttendanceLocation attendanceLocation)throws EntityNotFoundException{
        AttendanceLocation attendanceLocationToUpdate = attendanceLocationRepository.findById(attendanceLocationId).orElseThrow(()-> new EntityNotFoundException("Location not found."));

        attendanceLocationRepository.delete(attendanceLocation);
    }





}
