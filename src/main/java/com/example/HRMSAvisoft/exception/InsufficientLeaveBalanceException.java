package com.example.HRMSAvisoft.exception;

public class InsufficientLeaveBalanceException extends Exception {
   public  InsufficientLeaveBalanceException(int availableLeaves){
       super("Leave Balance "+availableLeaves );
   }
}
