package cli;

import db.RepositoryImpl;
import models.Matrix;

import java.sql.SQLException;
import java.util.Scanner;

public class AddMatricesProcess {

    public static void run_input(Scanner scanner, RepositoryImpl repository) {
        if (requireTable(repository)) return;
        Matrix m1 = inputMatrix(scanner, "A");
        Matrix m2 = inputMatrix(scanner, "B");
        writeToRepository(repository, m1);
        writeToRepository(repository, m2);
    }

    public static void run_multiply(Scanner scanner, RepositoryImpl repository) {
        if (requireTable(repository)) return;
        System.out.println("Введите номера матрицы (A и B), произведение произодится справа: ");
        try {
            Matrix m1 = repository.fetchById(scanner.nextInt());
            Matrix m2 = repository.fetchById(scanner.nextInt());
            scanner.nextLine();
            Matrix result = new Matrix(m1.prod_matrix(m2));
            repository.insert(result);
            System.out.println("Результат:\n" + result);
        } catch (SQLException e) {
            System.out.printf("Ошибка загрузки матрицы %s\n", e.getMessage());
        }
    }

    private static Matrix inputMatrix(Scanner scanner, String name) {
        System.out.printf("Введите размер матрицы %s (строки и столбцы): ", name);
        int m = scanner.nextInt();
        int n = scanner.nextInt();
        scanner.nextLine();

        int[][] arr = new int[m][n];
        System.out.println("Введите элементы матрицы:");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++)
                arr[i][j] = scanner.nextInt();
            scanner.nextLine();
        }
        return new Matrix(arr);
    }

    private static void writeToRepository(RepositoryImpl repository, Matrix data) {
        try {
            repository.insert(data);
            System.out.printf("Матрица %s успешно добавлена в таблицу.\n", data);
        } catch (SQLException e) {
            System.out.printf("Ошибка при добавлении матрицы: %s\n", e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Сначала подключитесь или создайте таблицу.");
        }
    }

    private static boolean requireTable(RepositoryImpl repository) {
        if (repository.getTableName().isEmpty()) {
            System.out.println("Сначала подключитесь или создайте таблицу.");
            return true;
        }
        return false;
    }
}
