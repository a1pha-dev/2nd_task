package excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class ExcelExporter {
    public static void exportToExcel(String tableName, List<Map<String, Object>> rows) {
        if (rows.isEmpty()) {
            System.out.println("Нет данных для экспорта.");
            return;
        }

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
            }
            System.out.println("Экспортировано в " + fileName);
        } catch (Exception e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        }
    }
}
