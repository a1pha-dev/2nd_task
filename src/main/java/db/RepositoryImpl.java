package db;


import models.Matrix;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class RepositoryImpl implements Repository {
    private final String URL;
    private final String USER;
    private final String PASSWORD;

    private String tableName = null;

    private static final RepositoryImpl INSTANCE = getInstance();

    private RepositoryImpl() {
        this.URL = "jdbc:postgresql://localhost:5432/2nd_task";
        this.USER = "postgres";
        this.PASSWORD = "secret";
    }

    public void setTableName(String tableName) {
        this.tableName = sanitize(tableName);
    }

    public String getTableName() {
        return tableName;
    }

    public static RepositoryImpl getInstance() {
        if (INSTANCE == null) {
            return new RepositoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public List<String> getAllTableNames() throws SQLException {
        List<String> tables = new ArrayList<>();
        String sql = """
                    SELECT table_name
                    FROM information_schema.tables
                    WHERE table_schema = 'public'
                    ORDER BY table_name;
                """;

        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) tables.add(rs.getString("table_name"));

        return tables;
    }

    @Override
    public String connectOrCreateTable() throws SQLException, IllegalStateException {
        requireTable();
        List<String> existing = getAllTableNames();

        tableName = sanitize(tableName);
        if (existing.contains(tableName)) {
            return tableName;
        }

        String sql = String.format("""
                    CREATE TABLE %s (
                        id SERIAL PRIMARY KEY,
                        matrix INT[][]
                    );
                """, tableName);

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        return tableName;
    }

    @Override
    public void insert(Matrix matrix) throws SQLException, IllegalStateException {
        requireTable();

        String sql = "INSERT INTO " + sanitize(tableName) + " (matrix) VALUES (?)";
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setArray(1, conn.createArrayOf("integer", matrix.getData()));
        ps.executeUpdate();
    }

    @Override
    public List<Map<String, Object>> fetchAll() throws SQLException, IllegalStateException {
        requireTable();

        List<Map<String, Object>> rows = new ArrayList<>();
        String sql = "SELECT * FROM " + sanitize(tableName) + " ORDER BY id";

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columns; i++)
                row.put(md.getColumnName(i), rs.getObject(i));
            rows.add(row);
        }
        return rows;
    }

    @Override
    public Matrix fetchById(int id) throws SQLException {
        requireTable();

        String sql = "SELECT matrix FROM " + sanitize(tableName) + " WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Array array = resultSet.getArray("matrix");
                if (array != null) {
                    Integer[][] integerArray = (Integer[][]) array.getArray();
                    int[][] primitiveArray = new int[integerArray.length][];
                    for (int i = 0; i < integerArray.length; i++) {
                        primitiveArray[i] = new int[integerArray[i].length];
                        for (int j = 0; j < integerArray[i].length; j++) {
                            primitiveArray[i][j] = integerArray[i][j];
                        }
                    }
                    return new Matrix(primitiveArray);
                }
            }
        }
        return null;
    }


    private String sanitize(String name) {
        if (name == null) {
            return "";
        }
        return name.trim().toLowerCase().replaceAll("[^a-z0-9_]", "_");
    }

    private void requireTable() {
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalStateException("Table name is not set.");
        }
    }
}
