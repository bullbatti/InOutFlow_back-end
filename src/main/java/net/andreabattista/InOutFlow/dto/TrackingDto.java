package net.andreabattista.InOutFlow.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.andreabattista.InOutFlow.business.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

public class TrackingDto {
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endDate;

    private String nfcReader;
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public String getNfcReader() {
        return nfcReader;
    }
    
    public void setNfcReader(String nfcReader) {
        this.nfcReader = nfcReader;
    }
}
