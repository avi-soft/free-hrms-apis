package com.example.HRMSAvisoft.scheduler;

import com.example.HRMSAvisoft.entity.LeaveRequest;
import com.example.HRMSAvisoft.repository.LeaveRequestRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;

public class leaveRequestScheduler {
    private final LeaveRequestRepository leaveRequestRepository;
    public leaveRequestScheduler(LeaveRequestRepository leaveRequestRepository){
        this.leaveRequestRepository=leaveRequestRepository;
    }
    @Transactional
    @Scheduled(cron = "0 0 0 1 */2 *")
    public void deleteOldLeaveRequests() {
        //logger.info("Starting deletion of old leave requests...");
        LocalDate currentMonthStart = YearMonth.now().atDay(1);
        leaveRequestRepository.deleteOldLeaveRequests(currentMonthStart);
        //logger.info("Old leave requests have been deleted.");
    }
}
