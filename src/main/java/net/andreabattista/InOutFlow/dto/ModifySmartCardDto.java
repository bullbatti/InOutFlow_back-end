package net.andreabattista.InOutFlow.dto;

public class ModifySmartCardDto {
    private EmployeeDto employee;
    private String id;

    public EmployeeDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDto employee) {
        this.employee = employee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
