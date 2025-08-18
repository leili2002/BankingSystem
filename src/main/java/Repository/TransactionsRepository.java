package Repository;

import DataBaseConnection.DatabaseConnection;
import Logic.*;

import java.sql.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionsRepository {
    public static int AddTransaction(Transaction transaction) {
        String sql = "INSERT INTO Transactions (amount, date) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transaction.getAmount());
            Timestamp timestamp = Timestamp.valueOf(transaction.getdate());
            stmt.setTimestamp(2, timestamp);

            stmt.executeUpdate();
            return 1;
            // System.out.println("✅ User added to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static List<Transaction> printTransactions(int id_account) {
        List<Transaction> transactionList = new ArrayList<>();

        String sql = "SELECT amount, date FROM Transactions WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id_account);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int amount = rs.getInt("amount");
                    Timestamp timestamp = rs.getTimestamp("date");
                    LocalDateTime date = timestamp.toLocalDateTime();

                    Transaction transaction = new Transaction(amount, date);
                    transactionList.add(transaction);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactionList;
    }


    public Account printAccountData(int id) {
        String sql = "SELECT * FROM Accounts WHERE id= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getString("serials"),
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("lastname"),
                        rs.getString("type"),
                        rs.getFloat("balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}


