package com.lessons.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class GetReportDTO {

    private Integer id;
    private Integer version;
    private String description;

    @JsonProperty("display_name")
    private String displayName;

    private Boolean reviewed;

    @JsonProperty("reference")
    private Integer referenceSource;

    private Integer priority;

    @JsonProperty("created_date")
    private Timestamp createdDate;

    @JsonProperty("last_modified_date")
    private Timestamp lastModifiedDate;

    @JsonProperty("is_custom_report")
    private Boolean isCustomReport;

    private Boolean reserved;

    @JsonProperty("reserved_by")
    private String reservedBy;

    public GetReportDTO(){

    }

    public GetReportDTO(Integer id, Integer version, String description, String displayName, Boolean reviewed, Integer referenceSource, Integer priority, Timestamp createdDate, Timestamp lastModifiedDate, Boolean isCustomReport, Boolean reserved, String reservedBy) {
        this.id = id;
        this.version = version;
        this.description = description;
        this.displayName = displayName;
        this.reviewed = reviewed;
        this.referenceSource = referenceSource;
        this.priority = priority;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.isCustomReport = isCustomReport;
        this.reserved = reserved;
        this.reservedBy = reservedBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getReviewed() {
        return reviewed;
    }

    public void setReviewed(Boolean reviewed) {
        this.reviewed = reviewed;
    }

    public Integer getReferenceSource() {
        return referenceSource;
    }

    public void setReferenceSource(Integer referenceSource) {
        this.referenceSource = referenceSource;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getIsCustomReport() {
        return isCustomReport;
    }

    public void setIsCustomReport(Boolean customReport) {
        isCustomReport = customReport;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public String getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(String reservedBy) {
        this.reservedBy = reservedBy;
    }
}
