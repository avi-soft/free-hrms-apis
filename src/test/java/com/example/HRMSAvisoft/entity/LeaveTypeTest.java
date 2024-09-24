//package com.example.HRMSAvisoft.entity;
//
//import jakarta.persistence.Column;
//import org.junit.Test;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//
//public class LeaveTypeTest {
//
//    Long leaveTypeId;
//
//    String name;
//
//    String description;
//
//    Integer allowedDaysPerYear;
//
//    Boolean isPaidLeave;
//
//    @BeforeEach
//    void setUp(){
//         Long leaveTypeId = 1L;
//
//         String name = "Sick Leave";
//
//        String description = "Leave for the sick.";
//
//        Integer allowedDaysPerYear = 13;
//
//        Boolean isPaidLeave = true;
//    }
//
//    @Test
//    @DisplayName("testLeaveTypeConstructor")
//    public void testLeaveTypeConstructor(){
//        LeaveType leaveType = new LeaveType(leaveTypeId, name, description, allowedDaysPerYear, isPaidLeave);
//
//        Assertions.assertEquals(leaveTypeId, leaveType.getLeaveTypeId());
//        Assertions.assertEquals(name, leaveType.getName());
//        Assertions.assertEquals(description, leaveType.getDescription());
//        Assertions.assertEquals(allowedDaysPerYear, leaveType.getAllowedDaysPerYear());
//        Assertions.assertEquals(isPaidLeave, leaveType.getIsPaidLeave());
//    }
//
//    @Test
//    @DisplayName("testLeaveTypeGetterSetters")
//    public void testLeaveTypeGetterSetters(){
//        LeaveType leaveType = new LeaveType();
//        leaveType.setLeaveTypeId(leaveTypeId);
//        leaveType.setName(name);
//        leaveType.setDescription(description);
//        leaveType.setIsPaidLeave(isPaidLeave);
//        leaveType.setAllowedDaysPerYear(allowedDaysPerYear);
//
//        Assertions.assertEquals(leaveTypeId, leaveType.getLeaveTypeId());
//        Assertions.assertEquals(name, leaveType.getName());
//        Assertions.assertEquals(description, leaveType.getDescription());
//        Assertions.assertEquals(allowedDaysPerYear, leaveType.getAllowedDaysPerYear());
//        Assertions.assertEquals(isPaidLeave, leaveType.getIsPaidLeave());
//    }
//
//}
