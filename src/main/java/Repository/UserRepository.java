package Repository;

import DataBaseConnection.DatabaseConnection;
import Logic.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public int addUser(UserEntity entity) {
        String sql = "INSERT INTO Users (national_id, name, lastname, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entity.getRepon_national_id());
            stmt.setString(2, entity.get_Name());
            stmt.setString(3, entity.getLastname());
            stmt.setString(4, entity.getPassword());
            stmt.executeUpdate();
            return 1;
            // System.out.println("✅ User added to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int removeUser(UserEntity entity) {
        int id = entity.getRepon_national_id();
        String sql = "DELETE FROM Users WHERE national_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return 1;//succses
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static User getUserById(int id) {
        String sql = "SELECT * FROM Users WHERE national_id= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("lastname"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserEntity> getAllUsers() {
        List<UserEntity> userList = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                userList.add(new UserEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("lastname"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
}
