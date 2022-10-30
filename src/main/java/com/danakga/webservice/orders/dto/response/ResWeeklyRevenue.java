package com.danakga.webservice.orders.dto.response;

public interface ResWeeklyRevenue {
    String getStartDate();
    String getEndDate();
    String getWeek();
    Long getPrice();
}
