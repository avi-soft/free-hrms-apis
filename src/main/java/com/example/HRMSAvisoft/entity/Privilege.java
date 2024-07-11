package com.example.HRMSAvisoft.entity;


public enum Privilege
{
    ADD_EMPLOYEE,
    UPLOAD_EMPLOYEE_IMAGE,
    GET_ALL_EMPLOYEES,
    //    UPDATE_EMPLOYEE_PERSONAL_DETAILS,
    UPDATE_EMPLOYEE_COMPANY_DETAILS,
    FIND_EMPLOYEE_BY_ID,
    SEARCH_EMPLOYEE_BY_NAME,
    SEARCH_EMPLOYEE_BY_MANAGER_ID,

    ADD_ADDRESS,
    REMOVE_ADDRESS,
    UPDATE_ADDRESS,

    GETALL_DEPARTMENTS,
    ADD_DEPARTMENT,
    UPDATE_DEPARTMENT,
    DELETE_DEPARTMENT,

    GET_EMPLOYEE_EMERGENCY_CONTACT,
    ADD_EMERGENCY_CONTACT,
    UPDATE_EMERGENCY_CONTACT,
    DELETE_EMERGENCY_CONTACT,

    ADD_ACCOUNT,
    REMOVE_ACCOUNT,

    GET_ALL_PERFORMANCE_OF_EMPLOYEE,
    ADD_EMPLOYEE_PERFORMANCE,
    GET_ALL_PERFORMANCES,
    GET_PERFORMANCE_BY_REVIEWER,
    DELETE_PERFORMANCE_RECORD,

    SAVE_USER,
    CREATE_NEW_USER,
    DELETE_EMPLOYEE,
    GET_ALL_USERS,

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

    CREATE_ROLE,
    UPDATE_ROLE
}

