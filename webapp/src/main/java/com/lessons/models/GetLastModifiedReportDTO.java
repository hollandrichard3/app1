package com.lessons.models;

import java.sql.Timestamp;

public class GetLastModifiedReportDTO {

    private String username;
    private Timestamp lastModifiedDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
