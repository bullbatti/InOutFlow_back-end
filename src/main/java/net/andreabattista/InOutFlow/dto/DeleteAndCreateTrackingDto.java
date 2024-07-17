package net.andreabattista.InOutFlow.dto;

public class DeleteAndCreateTrackingDto {
    private TrackingDto tracking;
    private EmployeeDto employee;

    public TrackingDto getTracking() {
        return tracking;
    }

    public void setTracking(TrackingDto tracking) {
        this.tracking = tracking;
    }

    public EmployeeDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDto employee) {
        this.employee = employee;
    }
}
