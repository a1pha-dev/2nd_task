package models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Matrix extends ArrayPI {
    public Matrix(int[][] data_first) {
        super(data_first);
    }

    public int[][] prod_matrix(Matrix data) {
        int rows1 = data_first.length;
        int cols1 = data_first[0].length;

        int[][] data_second = data.getData_first();
        int rows2 = data_second.length;
        int cols2 = data_second[0].length;

        if (cols1 != rows2) {
            throw new IllegalArgumentException("Матрицы нельзя перемножить: число столбцов первой != числу строк второй");
        }

        int[][] result = new int[rows1][cols2];

        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                for (int k = 0; k < cols1; k++) {
                    result[i][j] += data_first[i][k] * data_second[k][j];
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int[] row_ints : data_first) {
            for (int col_int : row_ints) {
                result.append(col_int).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(data_first);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации матрицы в JSON", e);
        }
    }

    public static Matrix fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            int[][] data = mapper.readValue(json, int[][].class);
            return new Matrix(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации JSON в матрицу", e);
        }
    }
}
