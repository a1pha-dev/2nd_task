package excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class ExcelExporter {
    public static String exportToExcel(String tableName, List<Map<String, Object>> rows) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(tableName);

            Row header = sheet.createRow(0);
            int cellNum = 0;
            for (String key : rows.getFirst().keySet())
                header.createCell(cellNum++).setCellValue(key);

            int rowNum = 1;
            for (Map<String, Object> rowData : rows) {
                Row row = sheet.createRow(rowNum++);
                cellNum = 0;
                for (Object value : rowData.values())
                    row.createCell(cellNum++).setCellValue(value == null ? "" : value.toString());
            }

            String fileName = tableName + ".xlsx";
            try (FileOutputStream out = new FileOutputStream(fileName)) {
                workbook.write(out);
                return fileName;
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
