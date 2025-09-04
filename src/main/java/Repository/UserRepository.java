package Repository;

import DataBaseConnection.DatabaseConnection;
//import Logic.Interface.IUserRepository;
import Logic.Interface.IUserRepository;
import Logic.UserData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {

    public int addUser(UserData userData) {
        String sql = "INSERT INTO Users (national_id,name , lastname, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userData.getNational_id());
            stmt.setString(2, userData.getName());
            stmt.setString(3, userData.getLastName());
            stmt.setString(4, userData.getPass());
            stmt.executeUpdate();
            return 1;
            // System.out.println("✅ User added to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int removeUser(UserModel model) {
        int id = model.getRepon_national_id();
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

    public UserData getUserById(int national_id) {
        String sql = "SELECT * FROM Users WHERE national_id= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, national_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserData(
                        rs.getInt("id"),
                        rs.getString("getName"),
                        rs.getString("lastname"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> userModelList = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                userModelList.add(new UserModel(
                        rs.getInt("id"),
                        rs.getString("getName"),
                        rs.getString("lastname"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userModelList;
    }
}
