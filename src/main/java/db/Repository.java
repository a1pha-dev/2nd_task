package db;

import models.Matrix;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

interface Repository {

    Connection getConnection() throws SQLException;
    List<String> getAllTableNames() throws SQLException;
    String connectOrCreateTable() throws SQLException;
    void insert(Matrix data) throws SQLException;
    List<Map<String, Object>> fetchAll() throws SQLException;
    Matrix fetchById(int id) throws SQLException;
}
