package net.andreabattista.InOutFlow.dto;

public class ModifyTrackingEmployeeDto {
    private TrackingDto oldTracking;
    private TrackingDto newTracking;
    private EmployeeDto employee;

    public TrackingDto getOldTracking() {
        return oldTracking;
    }

    public void setOldTracking(TrackingDto oldTracking) {
        this.oldTracking = oldTracking;
    }

    public TrackingDto getNewTracking() {
        return newTracking;
    }

    public void setNewTracking(TrackingDto newTracking) {
        this.newTracking = newTracking;
    }

    public EmployeeDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDto employee) {
        this.employee = employee;
    }
}
