package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Attendance;
import com.example.HRMSAvisoft.repository.AttendanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository){
        this.attendanceRepository = attendanceRepository;
    }

    public Attendance startClockIn(Long userId, Double latitude, Double longitude, Double elevation) throws IllegalArgumentException, AlreadyClockedInException {

        List<Location> allowedLocations = List.of(
                new Location(37.7749, -122.4194, 30.0), // Example: San Francisco
                new Location(40.7128, -74.0060, 10.0)  // Example: New York
                // Add more allowed locations as needed
        );

        boolean isLocationValid = allowedLocations.stream()
                .anyMatch(location -> isWithinAllowedDistance(location, latitude, longitude, elevation));

        if (!isLocationValid) {
            throw new IllegalArgumentException("You are not at any allowed location.");
        }

        Attendance alreadyExistingClockinAttendanceForCurrentDate = attendanceRepository.findByUserIdAndDate(userId, LocalDate.now()).orElse(null);

        if(alreadyExistingClockinAttendanceForCurrentDate != null){
            throw new AlreadyClockedInException("You are already clockedIn for the day");
        }

        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setDate(LocalDate.now());
        attendance.setClockInTime(LocalDateTime.now());
        attendance.setLatitude(latitude);
        attendance.setLongitude(longitude);
        attendance.setElevation(elevation);
        return attendanceRepository.save(attendance);
    }


    private boolean isWithinAllowedDistance(Location allowedLocation, Double latitude, Double longitude, Double elevation) {
        // Calculate distance using Haversine formula for latitude and longitude
        double distance = calculateHaversineDistance(allowedLocation.getLatitude(), allowedLocation.getLongitude(), latitude, longitude);
        // Check if the elevation difference is within acceptable range (e.g., 10 meters)
        double elevationDifference = Math.abs(allowedLocation.getElevation() - elevation);
        return distance <= 100 && elevationDifference <= 10; // Example distance and elevation range
    }

    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000; // Convert to meters
    }

    public Attendance stopClockOut(Long userId, Double latitude, Double longitude, Double elevation)throws InsufficientTimeForAttendanceException, EntityNotFoundException {
        Attendance optionalAttendance = attendanceRepository.findByUserIdAndClockOutTimeIsNull(userId).orElseThrow(() -> new EntityNotFoundException("No clock in time found for user"));

        List<Location> allowedLocations = List.of(
                new Location(37.7749, -122.4194, 30.0), // Example: San Francisco
                new Location(40.7128, -74.0060, 10.0)  // Example: New York
                // Add more allowed locations as needed
        );

        boolean isLocationValid = allowedLocations.stream()
                .anyMatch(location -> isWithinAllowedDistance(location, latitude, longitude, elevation));

        if (!isLocationValid) {
            throw new IllegalArgumentException("You are not at any allowed location.");
        }

        optionalAttendance.setClockOutTime(LocalDateTime.now());
        optionalAttendance.setLatitude(latitude);
        optionalAttendance.setLongitude(longitude);
        optionalAttendance.setElevation(elevation);

        // Check if the time between clock in and clock out is sufficient
        Duration shiftDuration = Duration.ofHours(1); // Example shift duration
        Duration workedDuration = Duration.between(optionalAttendance.getClockInTime(), optionalAttendance.getClockOutTime());

        // Mark attendance based on duration
        boolean isAttendanceMarked = workedDuration.compareTo(shiftDuration) >= 0;
        optionalAttendance.setAttendanceMarked(isAttendanceMarked);

        Duration timeLeft = shiftDuration.minus(workedDuration);

        long hoursLeft = timeLeft.toHours();
        long minutesLeft = timeLeft.toMinutes() % 60;

        String timeLeftFormatted = String.format("%02d hours and %02d minutes", hoursLeft, minutesLeft);

        if (!isAttendanceMarked) {
            throw new InsufficientTimeForAttendanceException(timeLeftFormatted + " left for shift to end.");
        }

        // Save and return attendance
        attendanceRepository.save(optionalAttendance);

        return optionalAttendance; // Return or handle the result as needed
    }

    public static class InsufficientTimeForAttendanceException extends RuntimeException{
        public InsufficientTimeForAttendanceException(String message){
            super(message);
        }
    }

    public static class AlreadyClockedInException extends RuntimeException{
        public AlreadyClockedInException(String message){
            super(message);
        }
    }

    public static class Location {

        private double latitude;
        private double longitude;
        private double elevation;

        public Location(double latitude, double longitude, double elevation) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.elevation = elevation;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getElevation() {
            return elevation;
        }
    }
}







