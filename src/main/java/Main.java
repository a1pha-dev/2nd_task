import cli.AddMatricesProcess;
import cli.ConnectOrCreateTableProcess;
import cli.ExportTableProcess;
import cli.PrintTableProcess;
import db.RepositoryImpl;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RepositoryImpl repository = RepositoryImpl.getInstance();
        repository.setTableName(null);

        while (true) {
            System.out.print("""
                    Выберите действие:
                        1. Показать все таблицы
                        2. Подключиться или создать таблицу
                        3. Ввести две матрицы
                        4. Перемножить матрицы
                        5. Экспортировать в Excel
                        0. Выйти
                    """);

            switch (scanner.nextLine().trim()) {
                case "1" -> PrintTableProcess.run(repository);
                case "2" -> ConnectOrCreateTableProcess.run(scanner, repository);
                case "3" -> AddMatricesProcess.run_input(scanner, repository);
                case "4" -> AddMatricesProcess.run_multiply(scanner, repository);
                case "5" -> ExportTableProcess.run(repository);
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }
}
