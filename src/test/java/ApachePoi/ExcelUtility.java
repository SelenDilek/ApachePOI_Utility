package ApachePoi;

import io.cucumber.java.Scenario;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelUtility {
    public static  ArrayList<ArrayList<String>> getData(String path, String sheetName, int sutunSayisi) {

        ArrayList<ArrayList<String>> tablo = new ArrayList<>(); //satirlarin listesi gerekiyor bana. her satir kendi icinde
        //bir list o yuzden listin listi oluyor
        Sheet sheet=null;
        try {
            FileInputStream inputStream = new FileInputStream(path);
            Workbook workbook = WorkbookFactory.create(inputStream);
            sheet = workbook.getSheet(sheetName);
        } catch (IOException e) {
            System.out.println("e= " + e.getMessage());
        }

        //bir rowdaki iki satiri oku

        for (int i = 0; i <sheet.getPhysicalNumberOfRows() ; i++) { //her bir satiri //buraya satir sayisi yazilir
            ArrayList<String> satir = new ArrayList<>();
            for (int j = 0; j <sutunSayisi ; j++) { //buraya sutun sayisi yazilir(zaten bize sutun sayisini vermis)
                satir.add(sheet.getRow(i).getCell(j).toString());

            }

            tablo.add(satir);
        }

        return tablo;
    }


    public static void writeToExcel(String path, Scenario scenario) {

        File file = new File(path);

        if (!file.exists()) // dosya yok ise(ilk kez ve bi kez calisiyor)
        {
            //hafzada worbook oluştur, içinde hafızada sheet oluştur
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Sayfa1");

            //hafızada işlemlerini yap
            Row yeniSatir = sheet.createRow(0);
            Cell hucre = yeniSatir.createCell(0);
            hucre.setCellValue(scenario.getName());

            Cell hucre1 = yeniSatir.createCell(1);
            hucre1.setCellValue(scenario.getStatus().toString());

            //kaydet
            try {
                FileOutputStream outputStream = new FileOutputStream(path);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
            } catch (Exception ex) {
                System.out.println("ex.getMessage() = " + ex.getMessage());
            }
        } else {

            FileInputStream inputStream=null;
            Workbook workbook=null;
            Sheet sheet=null;

            try {
                inputStream = new FileInputStream(path);
                workbook = WorkbookFactory.create(inputStream); //hafizaya create et
                sheet = workbook.getSheetAt(0); //getAt index ister
            } catch (Exception ex) {
                System.out.println("ex.getMessage() = " + ex.getMessage());
            }

            int sonSatirIndex = sheet.getPhysicalNumberOfRows(); //son bos satirinn indexi
            Row yeniSatir = sheet.createRow(sonSatirIndex);

            //testin fail olup olmadigi icin yazdirma kismi (Hook class dan yardim aldim)
            Cell hucre = yeniSatir.createCell(0);
            hucre.setCellValue(scenario.getName());

            Cell hucre1 = yeniSatir.createCell(1);
            hucre1.setCellValue(scenario.getStatus().toString());


            try {
                inputStream.close(); //input stream i kapatiyoruz.
                FileOutputStream outputStream = new FileOutputStream(path);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
            } catch (Exception ex) {
                System.out.println("ex.getMessage() = " + ex.getMessage());
            }
        }

    }

}
