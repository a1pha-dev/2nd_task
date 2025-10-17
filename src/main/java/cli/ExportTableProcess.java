package cli;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ExportTableProcess {

    public static void run(db.RepositoryImpl repository) {
        if (repository.getTableName().isEmpty()) {
            System.out.println("Сначала подключитесь или создайте таблицу.");
            return;
        }
        String tableName = repository.getTableName();
        try {
            List<Map<String, Object>> data = repository.fetchAll();
            String response = excel.ExcelExporter.exportToExcel(tableName, data);
            System.out.printf("Данные успешно экспортированы в %s\n", response);
        } catch (SQLException e) {
            System.out.printf("Ошибка при получении данных из таблицы: %s\n", e.getMessage());
        }
    }
}
