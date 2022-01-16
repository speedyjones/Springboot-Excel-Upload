package com.excel.excelupload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@ControllerAdvice
@RequestMapping("/api")
public class MainController {

     @Autowired
     private EmployeeDataServiceImpl employeeDataServiceImpl;


    @PostMapping("/excel/upload")
    public String uploadExcel(@RequestParam("file")MultipartFile file)
    {
        employeeDataServiceImpl.uploadExcel(file);
        log.info("You're File Has Been Successfully Uploaded "+file.getOriginalFilename());
        String Success = "You're File Has Been Successfully Uploaded "+file.getOriginalFilename();
        return Success;
    }

    @GetMapping("/excel/save")
    public String saveData(Model model)
    {
        List<EmployeeData> employeeData1 = employeeDataServiceImpl.getAllEmployees();
        int noOfRecords = employeeDataServiceImpl.saveEmployeeData(employeeData1);
        model.addAttribute("No Of Records",noOfRecords);
        return "Success";
    }

    @GetMapping("/excel/getAll")
    public ResponseEntity<?> getAllData()
    {


        Map<String ,Object> map = new HashMap<>();
        map.put("employeeData",employeeDataServiceImpl.retrieveAllEmployee());
        return new ResponseEntity<>(map, HttpStatus.OK);

    }

}
