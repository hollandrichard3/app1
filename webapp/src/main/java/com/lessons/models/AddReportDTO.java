package com.lessons.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddReportDTO {

    @NotBlank
    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("reference_source")
    private Integer referenceSource;

    @NotBlank
    private String description;

    private Integer priority;

    @NotNull
    @JsonProperty("is_custom_report")
    private Boolean isCustomReport;

    public Boolean isValidDto(){
        if(StringUtils.isBlank(displayName) ||
                StringUtils.isBlank(description) || isCustomReport == null){
            return false;
        }
        return true;
    }

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
}
