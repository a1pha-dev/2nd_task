package models;

public class ArrayPI {

    protected int[][] matrixData;

    public ArrayPI(int[][] matrixData) {
        this.matrixData = matrixData;
    }

    public int[][] get_data() {
        return matrixData;
    }
}
