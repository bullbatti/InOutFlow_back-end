package net.andreabattista.InOutFlow.dto;

import java.time.LocalDateTime;

public class TrackingDto {
    private LocalDateTime date;
    private String nfcReader;
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public String getNfcReader() {
        return nfcReader;
    }
    
    public void setNfcReader(String nfcReader) {
        this.nfcReader = nfcReader;
    }
}
