package Repository;

import DataBaseConnection.DatabaseConnection;
import Logic.Interface.IAccountRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AccountRepository implements IAccountRepository {
    private Connection conn;

    public AccountRepository(Connection conn) {
        this.conn = conn;
    }

    public AccountRepository() {
        try {
            this.conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int addAccount(AccountModel entity) {
        String sql = "INSERT INTO Accounts (serial, id, name, lastname,type,balance) VALUES (?, ?, ?, ?, ?, ?)";
        try (
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getUuid().toString());
            stmt.setInt(2, entity.getRepo_id());
            stmt.setString(3, entity.get_Name());
            stmt.setString(4, entity.getLastname());
            stmt.setString(5, entity.gettype());
            stmt.setFloat(6, entity.getbalance());
            stmt.executeUpdate();
            return 1;
            // System.out.println("✅ User added to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int removeAccount(AccountModel entity) {
        int id = entity.getRepo_id();
        String sql = "DELETE FROM Accounts WHERE id = ?";
        try (
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return 1;
                //System.out.println("✅ User removed from database.");
            } else {
                return -1;
                //System.out.println("⚠ User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int withdraw(AccountModel entity, int amount) {
        int id_account=entity.getRepo_id();
        String selectSql = "SELECT balance FROM Accounts WHERE id = ?";
        String updateSql = "UPDATE Accounts SET balance = balance - ? WHERE id = ?";

        try (
                PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setInt(1, id_account);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                float currentBalance = rs.getFloat("balance");

                if (currentBalance >= amount) {
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setFloat(1, amount);
                        updateStmt.setInt(2, id_account);
                        updateStmt.executeUpdate();
                        return 1;
                        // System.out.println("✅ Withdrawal successful. New balance: " + (currentBalance - amount));
                    }
                } else {
                    //  System.out.println("❌ Insufficient funds.");
                    return -1;
                }
            } else {
                //System.out.println("❌ Account not found.");
                return -2;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public int deposit(AccountModel entity, int amount) {
        int id_account=entity.getRepo_id();
        String selectSql = "SELECT balance FROM Accounts WHERE id = ?";
        String updateSql = "UPDATE Accounts SET balance = balance + ? WHERE id = ?";
        try (
                PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setInt(1, id_account);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                float currentBalance = rs.getFloat("balance");
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setFloat(1, amount);
                    updateStmt.setInt(2, id_account);
                    updateStmt.executeUpdate();
                    return 1;
                    //  System.out.println("✅ deposit successful. New balance: " + (currentBalance + amount));
                }
            } else {
                //  System.out.println("❌ Account not found.");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int transfer(AccountModel entity_src, AccountModel entity_des, int amount) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            AccountRepository accountRepo = new AccountRepository(conn);

            int result1 = accountRepo.withdraw(entity_src, amount);
            int result2 = accountRepo.deposit(entity_des, amount);

            if (result1 == 1 && result2 == 1) {
                conn.commit();
                return 1; // success
            } else {
                conn.rollback();
                return 0; // withdraw or deposit failed, rollback and return 0
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return -1; // error occurred
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public float checkBalance(AccountModel entity) {
        int id_account=entity.getRepo_id();
        String sql = "select balance from Accounts where id=?";
        try (
                PreparedStatement selectStmt = conn.prepareStatement(sql)) {

            selectStmt.setInt(1, id_account);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("balance");
            } else {
                //System.out.println("❌ Account not found.");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public   List<AccountModel> printAccountById(UserModel entity) {
        int national_id=entity.getRepon_national_id();
        String sql = "SELECT a.* " +
                "FROM Accounts a " +
                "JOIN users_accounts ua ON a.id = ua.account_id\n" +
                "JOIN Users u ON ua.user_id = u.id\n" +
                "WHERE u.national_id = ?;\n";
        List<AccountModel> Account_list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, national_id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Account_list.add(new AccountModel(
                            UUID.fromString(rs.getString("serial")),
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("lastname"),
                            rs.getString("type"),
                            rs.getFloat("balance")

                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Account_list;
    }


    public   List<AccountModel> printAllAccounts() {
        String sql = "SELECT a.* " +
                "FROM Accounts a ";

        List<AccountModel> Account_list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Account_list.add(new AccountModel(
                            UUID.fromString(rs.getString("serial")),
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("lastname"),
                            rs.getString("type"),
                            rs.getFloat("balance")

                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Account_list;
    }




    public UserModel printUserData(UserModel entity) {
        int id=entity.getRepon_national_id();
        String sql = "SELECT * FROM Users WHERE national_id= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserModel(
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


}








