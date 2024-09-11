package com.example.HRMSAvisoft.entity;

import com.example.HRMSAvisoft.dto.CreateBranchDTO;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.service.BranchService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public enum Privilege {
    //Employee privileges
    ADD_EMPLOYEE,
    UPLOAD_EMPLOYEE_IMAGE,
    GET_ALL_EMPLOYEES,
    UPDATE_EMPLOYEE_COMPANY_DETAILS,
    FIND_EMPLOYEE_BY_ID,
    SEARCH_EMPLOYEE_BY_NAME,
    SEARCH_EMPLOYEE_BY_MANAGER_ID,

    CREATE_EMPLOYEE_ATTRIBUTE,
    UPDATE_EMPLOYEE_ATTRIBUTE,
    DELETE_EMPLOYEE_ATTRIBUTE,
    GET_ALL_EMPLOYEE_ATTRIBUTES,

    //Address privileges
    ADD_ADDRESS,
    REMOVE_ADDRESS,
    UPDATE_ADDRESS,

    //Emergency Contact privileges
    GET_EMPLOYEE_EMERGENCY_CONTACT,
    ADD_EMERGENCY_CONTACT,
    UPDATE_EMERGENCY_CONTACT,
    DELETE_EMERGENCY_CONTACT,

    //Account privileges
    ADD_ACCOUNT,
    REMOVE_ACCOUNT,

    //Performance privileges
    GET_ALL_PERFORMANCE_OF_EMPLOYEE,
    ADD_EMPLOYEE_PERFORMANCE,
    GET_ALL_PERFORMANCES,
    GET_PERFORMANCE_BY_REVIEWER,
    DELETE_PERFORMANCE_RECORD,

    //User privileges
    SAVE_USER,
    CREATE_NEW_USER,
    DELETE_EMPLOYEE,
    GET_ALL_USERS,

    //Leave privileges
    CREATE_LEAVE_REQUEST,
    GET_ALL_LEAVE_REQUESTS,
    DECLINE_LEAVE_REQUEST,
    APPROVE_LEAVE_REQUEST,
    GET_LEAVE_REQUEST_OF_EMPLOYEE,
    GET_PENDING_LEAVE_REQUEST_OF_EMPLOYEE,
    GET_APPROVED_REQUEST_OF_EMPLOYEE,
    GET_DECLINED_REQUEST_OF_EMPLOYEE,
    ADD_LEAVE_TYPE,
    REMOVE_LEAVE_TYPE,
    UPDATE_LEAVE_TYPE,
    GET_ALL_LEAVE_TYPES,
    GET_LEAVE_TYPE,
    GET_LEAVE_BALANCE_FOR_ALL_EMPLOYEES,

    //Role privileges
    CREATE_ROLE,
    UPDATE_ROLE,
    DELETE_ROLE,
    CHANGE_USER_ROLE,
    GET_ALL_ROLES,
    ASSIGN_ROLE_TO_USER,

    //Personal Profile
    ADD_DESIGNATION,
    UPDATE_DESIGNATION,
    DELETE_DESIGNATION,
    GET_ALL_DESIGNATION,

    ADD_SKILL,
    UPDATE_SKILL,
    DELETE_SKILL,
    GET_ALL_SKILL,

    //Organization Privileges
    GET_ALL_ORGANIZATION_ATTRIBUTES,
    CREATE_ORGANIZATION_ATTRIBUTE,
    UPDATE_ORGANIZATION_ATTRIBUTE,
    DELETE_ORGANIZATION_ATTRIBUTE,
    CREATE_ORGANIZATION,
    UPDATE_ORGANIZATION,
    DELETE_ORGANIZATION,
    GET_ALL_ORGANIZATIONS,
    UPLOAD_ORGANIZATION_IMAGE,
    GET_DEPARTMENTS_OF_ORGANIZATION,
    GET_BRANCHES_OF_ORGANIZATION,

    //Department Privileges
    GET_ALL_DEPARTMENT_ATTRIBUTES,
    CREATE_DEPARTMENT_ATTRIBUTE,
    UPDATE_DEPARTMENT_ATTRIBUTE,
    DELETE_DEPARTMENT_ATTRIBUTE,
    GETALL_DEPARTMENTS,
    ADD_DEPARTMENT,
    UPDATE_DEPARTMENT,
    DELETE_DEPARTMENT,
    GET_EMPLOYEES_OF_DEPARTMENT,
    ASSIGN_DEPARTMENT_TO_ORGANIZATION,
    REMOVE_DEPARTMENT_FROM_ORGANIZATION,
    ASSIGN_DEPARTMENT_TO_EMPLOYEE,
    REMOVE_DEPARTMENT_FROM_EMPLOYEE,
    //Branch privileges
    ADD_BRANCH,
    GET_ALL_BRANCH,
    GET_DEPARTMENTS_OF_BRANCH,
    UPDATE_BRANCH,
    DELETE_BRANCH,
    ASSIGN_BRANCH,
    REMOVE_BRANCH,
    ASSIGN_DEPARTMENT_TO_BRANCH,
    REMOVE_DEPARTMENT_FROM_BRANCH,

    //Attendance privileges
    GET_ALL_ATTENDANCE,
    UPDATE_ATTENDANCE_RECORD,
    ADD_ATTENDANCE_LOCATION,
    GET_ALL_ATTENDANCE_LOCATION,
    UPDATE_ATTENDANCE_LOCATION,
    DELETE_ATTENDANCE_LOCATION,
    ADD_SHIFT_DURATION,
    GET_SHIFT_DURATION,
    UPDATE_SHIFT_DURATION,
    DELETE_SHIFT_DURATION


}

