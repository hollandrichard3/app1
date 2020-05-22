package com.lessons.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportDTO {
    private String name;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("report_source")
    private Integer reportSource;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getReportSource() {
        return reportSource;
    }

    public void setReportSource(Integer reportSource) {
        this.reportSource = reportSource;
    }
}
