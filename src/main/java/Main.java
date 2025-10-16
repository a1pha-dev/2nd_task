import db.DatabaseUtil;
import excel.ExcelExporter;
import models.Matrix;

import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String tableName = null;

        while (true) {
            System.out.println("""
                        Выберите действие:
                        1. Показать все таблицы
                        2. Подключиться или создать таблицу
                        3. Ввести две матрицы
                        4. Перемножить матрицы
                        5. Экспортировать в Excel
                        0. Выйти
                    """);

            switch (scanner.nextLine().trim()) {
                case "1" -> DatabaseUtil.printAllTables();
                case "2" -> tableName = createConnectTable(scanner);
                case "3" -> {
                    if (requireTable(tableName)) addMatrices(scanner, tableName);
                }
                case "4" -> {
                    if (requireTable(tableName)) productMatrices(scanner, tableName);
                }
                case "5" -> ExcelExporter.exportToExcel(tableName, DatabaseUtil.getAllRows(tableName));
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private static void productMatrices(Scanner scanner, String tableName) {
        System.out.println("Введите номера матрицы (A и B), произведение произодится справа: ");
        try {
            Matrix m1 = DatabaseUtil.loadMatrix(tableName, scanner.nextInt());
            Matrix m2 = DatabaseUtil.loadMatrix(tableName, scanner.nextInt());
            scanner.nextLine();
            Matrix result = new Matrix(m1.prod_matrix(m2));
            DatabaseUtil.insertMatrix(tableName, result);
            System.out.println("Результат:\n" + result);
        } catch (SQLException e) {
            System.out.printf("Ошибка загрузки матрицы %s\n", e.getMessage());
        }
    }

    private static void addMatrices(Scanner scanner, String tableName) {
        Matrix m1 = inputMatrix(scanner, "A");
        Matrix m2 = inputMatrix(scanner, "B");
        DatabaseUtil.insertMatrix(tableName, m1);
        DatabaseUtil.insertMatrix(tableName, m2);
        System.out.printf("Матрицы:\n%s и \n%s сохранены в таблице %s\n", m1, m2, tableName);
    }

    private static boolean requireTable(String table) {
        if (table == null) {
            System.out.println("Сначала подключитесь или создайте таблицу.");
            return false;
        }
        return true;
    }

    private static String createConnectTable(Scanner scanner) {
        System.out.print("Введите имя новой таблицы: ");
        return DatabaseUtil.connectOrCreateTable(scanner);
    }

    private static Matrix inputMatrix(Scanner sc, String name) {
        System.out.print("Введите размер матрицы " + name + " (строки и столбцы): ");
        int m = sc.nextInt();
        int n = sc.nextInt();
        sc.nextLine();
        int[][] arr = new int[m][n];
        System.out.println("Введите элементы матрицы " + name + ":");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++)
                arr[i][j] = sc.nextInt();
            sc.nextLine();
        }
        return new Matrix(arr);
    }
}
