package com.lessons.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class EditReportDTO {

    @NotNull
    private Integer id;

    @NotNull
    private Integer version;

    @NotBlank
    @JsonProperty("display_name")
    private String displayName;

    @NotBlank
    private String description;

    private Integer priority;

    @NotNull
    @JsonProperty("is_custom_report")
    private Boolean isCustomReport;

    @JsonProperty("reference_source")
    private Integer referenceSource;

    public Integer getReferenceSource() {
        return referenceSource;
    }

    public void setReferenceSource(Integer referenceSource) {
        this.referenceSource = referenceSource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getIsCustomReport() {
        return isCustomReport;
    }

    public void setIsCustomReport(Boolean customReport) {
        isCustomReport = customReport;
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
}
