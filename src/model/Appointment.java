/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Jacobi
 */
public class Appointment {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private int appointmentID;
    private int customerId;
    private int userId;
    private String startDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String meetingType;
    private String customerName;
    
    public Appointment(int appointmentID, int customerId, int userId, String startTime, String endTime, String meetingType, String customerName) throws SQLException {
        this.appointmentID = appointmentID;
        this.customerId = customerId;
        this.userId = userId;
        this.startDate = LocalDate.parse(startTime.substring(0,10),df).toString();
        this.startTime = LocalTime.parse(startTime.substring(11),tf);
        this.endTime = LocalTime.parse(endTime.substring(11),tf);
        this.startDateTime = LocalDateTime.parse(endTime, dtf);
        this.endDateTime = LocalDateTime.parse(endTime, dtf);
        this.meetingType = meetingType;
        this.customerName = customerName;
    }

    public DateTimeFormatter getDtf() {
        return dtf;
    }

    public void setDtf(DateTimeFormatter dtf) {
        this.dtf = dtf;
    }

    public DateTimeFormatter getDf() {
        return df;
    }

    public void setDf(DateTimeFormatter df) {
        this.df = df;
    }

    public DateTimeFormatter getTf() {
        return tf;
    }

    public void setTf(DateTimeFormatter tf) {
        this.tf = tf;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
    
    public String getStartDateTimeString() {
        return startDateTime.toString().replace("T"," ");
    }
    
    public String getEndDateTimeString() {
        return endDateTime.toString().replace("T"," ");
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
}
