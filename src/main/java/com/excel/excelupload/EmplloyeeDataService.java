package com.excel.excelupload;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmplloyeeDataService {

    List<EmployeeData> getAllEmployees();

    int saveEmployeeData(List<EmployeeData> employeeData);

    public void uploadExcel(MultipartFile file);

    List<EmployeeData> retrieveAllEmployee();

}
