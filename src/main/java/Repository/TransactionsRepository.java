package Repository;

import DataBaseConnection.DatabaseConnection;
import Logic.*;
import Logic.Interface.ITransactionsRepository;

import java.sql.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionsRepository implements ITransactionsRepository {
    public int addTransaction(TransactionData transactionData) {
        String sql = "INSERT INTO Transactions (amount, date) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transactionData.getAmount());
            Timestamp timestamp = Timestamp.valueOf(transactionData.getDate());
            stmt.setTimestamp(2, timestamp);

            stmt.executeUpdate();
            return 1;
            // System.out.println("✅ User added to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<TransactionData> printTransactions(int id_account) {
        List<TransactionData> transactionDataList = new ArrayList<>();

        String sql = "SELECT amount, date FROM Transactions WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id_account);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int amount = rs.getInt("amount");
                    Timestamp timestamp = rs.getTimestamp("date");
                    LocalDateTime date = timestamp.toLocalDateTime();

                    TransactionData transactionData = new TransactionData(amount, date);
                    transactionDataList.add(transactionData);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactionDataList;
    }


    public AccountData printAccountData(int id) {
        String sql = "SELECT * FROM Accounts WHERE id= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new AccountData(
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


