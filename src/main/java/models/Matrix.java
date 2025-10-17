package models;


public final class Matrix extends ArrayPI {
    public Matrix(int[][] matrixData) {
        super(matrixData);
    }

    public int[][] prod_matrix(Matrix another_matrix) {
        int rows1 = matrixData.length;
        int cols1 = matrixData[0].length;

        int[][] anotherMatrixData = another_matrix.getData();
        int rows2 = anotherMatrixData.length;
        int cols2 = anotherMatrixData[0].length;

        if (cols1 != rows2) {
            throw new IllegalArgumentException("Матрицы нельзя перемножить: число столбцов первой != числу строк второй");
        }

        int[][] result = new int[rows1][cols2];

        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                for (int k = 0; k < cols1; k++) {
                    result[i][j] += matrixData[i][k] * anotherMatrixData[k][j];
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int[] row_ints : matrixData) {
            for (int col_int : row_ints) {
                result.append(col_int).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
