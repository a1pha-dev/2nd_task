package cli;

import db.RepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class PrintTableProcess {

    public static void run(RepositoryImpl repository) {
        try {
            List<String> tableNames = repository.getAllTableNames();
            if (tableNames.isEmpty()) {
                System.out.println("В базе данных нет таблиц.");
                return;
            }
            System.out.println("Существующие таблицы в базе данных:");
            for (String name : tableNames) {
                System.out.println("- " + name);
            }
        } catch (SQLException e) {
            System.out.printf("Ошибка при получении списка таблиц: %s\n", e.getMessage());
        }
    }
}
