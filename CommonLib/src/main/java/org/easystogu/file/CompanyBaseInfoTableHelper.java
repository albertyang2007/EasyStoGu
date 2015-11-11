package org.easystogu.file;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

//txt file to store all company base info
//export all data from easymoney software PC version in "分析"->"财务数据", select text file format
//the Table_CompanyBaseInfo.txt is saved in CommonLib\src\main\resources
public class CompanyBaseInfoTableHelper {
    private static Logger logger = LogHelper.getLogger(CompanyBaseInfoTableHelper.class);
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();
    private String resourcesFileName = "Table_CompanyBaseInfo.txt";
    private Workbook companyBaseInfoWorkbook = null;
    private static CompanyBaseInfoTableHelper instance = null;

    public static CompanyBaseInfoTableHelper getInstance() {
        if (instance == null) {
            instance = new CompanyBaseInfoTableHelper();
        }
        return instance;
    }

    public Workbook getCompanyBaseInfoWorkbook() {
        return this.companyBaseInfoWorkbook;
    }

    private CompanyBaseInfoTableHelper() {

        String resourcesFilePath = "classpath:/" + resourcesFileName;
        if (Strings.isNotEmpty(System.getProperty("easystogu.companyBaseInfoFile"))) {
            resourcesFilePath = System.getProperty("easystogu.companyBaseInfoFile");
        }

        logger.debug("Loading CompanyBaseInfo file from path:{}", resourcesFilePath);

        FileInputStream fis = null;
        try {
            Resource resource = resourceLoader.getResource(resourcesFilePath);
            fis = new FileInputStream(resource.getFile());
            companyBaseInfoWorkbook = new HSSFWorkbook(fis);
        } catch (IOException ex) {
            logger.error("Could not load data from path:{}, {} ", resourcesFilePath, ex.getMessage());
        } finally {
            if (fis != null) {
                try {
                    logger.debug("Close resource file.");
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Workbook wb = CompanyBaseInfoTableHelper.getInstance().getCompanyBaseInfoWorkbook();
        Sheet sheet1 = wb.getSheetAt(0);
        for (Row row : sheet1) {
          for (Cell cell : row) {
            System.out.print(cell.getStringCellValue() + "  ");
          }
          System.out.println();
        }
    }
}
