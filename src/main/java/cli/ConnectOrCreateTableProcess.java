package cli;

import db.RepositoryImpl;

import java.sql.SQLException;
import java.util.Scanner;

public class ConnectOrCreateTableProcess {

    public static void run(Scanner scanner, RepositoryImpl repository) {
        System.out.print("Введите имя таблицы: ");
        String tableName = scanner.nextLine().trim();
        repository.setTableName(tableName);

        try {
            String result = repository.connectOrCreateTable();
            System.out.printf("Успешное подключение к таблице: %s\n", result);
        } catch (SQLException e) {
            System.out.printf("Ошибка при подключении или создании таблицы: %s\n", e.getMessage());
        } catch (Exception e) {
            System.out.printf("Произошла ошибка: %s\n", e.getMessage());
        }
    }
}
