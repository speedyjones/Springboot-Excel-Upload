package com.excel.excelupload;

import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EmployeeDataServiceImpl implements EmplloyeeDataService {

    public String FilePath = "D:\\prabeer\\Data\\java\\ExcelUpload\\";
    public String savePath = "D:\\prabeer\\Data\\java\\ExcelUpload\\dummyData.xlsx";
    @Autowired
    private EmployeeDataRepository employeeDataRepository;
    private Workbook workbook;

    @Override
    public List<EmployeeData> getAllEmployees() {
        List<String> list = new ArrayList<String>();

        DataFormatter dataFormatter = new DataFormatter();

        try {
            File file = new File(FilePath);
            file.getName();
            workbook = WorkbookFactory.create(new File(savePath));

        } catch (EncryptedDocumentException | IOException e) {
            log.error("" + e);
        }

        log.info("WorkBook Has : " + workbook.getNumberOfSheets() + "' Sheets");
        Sheet sheet = workbook.getSheetAt(0);

        int noOfColoumns = sheet.getRow(0).getLastCellNum();
        log.info("Sheet Has : " + noOfColoumns + "' columns");
        String cellValue = "";
        for (Row row : sheet) {
            for (Cell cell : row) {
                try {
                    cellValue = dataFormatter.formatCellValue(cell);
                    list.add(cellValue);

                } catch (NumberFormatException e) {
                    log.error("Failed Records " + cell.getRow());
                }
            }
        }

        List<EmployeeData> employeeData = createList(list, noOfColoumns);

        try {
            workbook.close();
        } catch (IOException e) {
            log.error("" + e);
        }
        return employeeData;
    }

    private List<EmployeeData> createList(List<String> excelData, int noOfColumns) {
        ArrayList<EmployeeData> employeeData = new ArrayList<EmployeeData>();
        int i = noOfColumns;

        do {
            EmployeeData employeeData1 = new EmployeeData();
            employeeData1.setName(excelData.get(i));
            employeeData1.setAmount(Long.valueOf(excelData.get(i + 1)));
            employeeData1.setReason(excelData.get(i + 2));

            employeeData.add(employeeData1);
            i = i + (noOfColumns);

        }
        while (i < excelData.size());
        {
            return employeeData;
        }

    }


    @Override
    public int saveEmployeeData(List<EmployeeData> employeeData) {
        employeeData = employeeDataRepository.saveAll(employeeData);
        return employeeData.size();
    }

    @Override
    public void uploadExcel(MultipartFile file) {
        try {


            Path copyLocation = Paths.get(FilePath + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("" + e);
            throw new RuntimeException("Could Not Load File " + file.getOriginalFilename() + ". Please Try Again !!! ");
        }

    }

    @Override
    public List<EmployeeData> retrieveAllEmployee() {
        return employeeDataRepository.findAll();
    }
}
