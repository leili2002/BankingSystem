package Repository;

import DataBaseConnection.DatabaseConnection;
import Logic.AccountData;
import Logic.Interface.IAccountRepository;
import Logic.UserData;
import Repository.Converter.AccountConverter;
import Repository.Converter.UserConverter;

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


    public Exception addAccount(AccountData accountData) {

        AccountConverter accountConverter = new AccountConverter();
        AccountModel accountModel = accountConverter.toModel(accountData);

        String sql = "INSERT INTO Accounts (serial, id, name , lastname,type,balance) VALUES (?, ?, ?, ?, ?, ?)";
        try (
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountModel.getUuid().toString());
            stmt.setInt(2, accountModel.getRepo_id());
            stmt.setString(3, accountModel.get_Name());
            stmt.setString(4, accountModel.getLastname());
            stmt.setString(5, accountModel.gettype());
            stmt.setFloat(6, accountModel.getbalance());
            stmt.executeUpdate();
            return null;
            // System.out.println("✅ User added to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            return e;
        }
    }

    public int removeAccount(int accountID) {
        String sql = "DELETE FROM Accounts WHERE id = ?";
        try (
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountID);
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

    public int withdraw(int accountID, int amount) {
        String selectSql = "SELECT balance FROM Accounts WHERE id = ?";
        String updateSql = "UPDATE Accounts SET balance = balance - ? WHERE id = ?";

        try (
                PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setInt(1, accountID);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                float currentBalance = rs.getFloat("balance");

                if (currentBalance >= amount) {
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setFloat(1, amount);
                        updateStmt.setInt(2, accountID);
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

    public int deposit(int accountID, int amount) {
        String selectSql = "SELECT balance FROM Accounts WHERE id = ?";
        String updateSql = "UPDATE Accounts SET balance = balance + ? WHERE id = ?";
        try (
                PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setInt(1, accountID);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                float currentBalance = rs.getFloat("balance");
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setFloat(1, amount);
                    updateStmt.setInt(2, accountID);
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

    public int transfer(int sourceAccount, int destinationAccountID, int amount) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            AccountRepository accountRepo = new AccountRepository(conn);

            int result1 = accountRepo.withdraw(sourceAccount, amount);
            int result2 = accountRepo.deposit(destinationAccountID, amount);

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

    public float checkBalance(int accountID) {
        String sql = "select balance from Accounts where id=?";
        try (
                PreparedStatement selectStmt = conn.prepareStatement(sql)) {

            selectStmt.setInt(1, accountID);
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


    public   List<AccountData> getUserAccounts(int userNationalID) {
        String sql = "SELECT a.* " +
                "FROM Accounts a " +
                "JOIN users_accounts ua ON a.id = ua.account_id " +
                "JOIN Users u ON ua.user_id = u.id " +
                "WHERE u.national_id = ?;";
        List<AccountModel> Account_list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userNationalID);
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

        AccountConverter accountConverter = new AccountConverter();
        List<AccountData> accountDataList = new ArrayList<>();
        for (AccountModel model : Account_list) {
            accountDataList.add(accountConverter.toData(model));
        }

        return accountDataList;
    }

    public List<AccountData> getAllAccounts() {
        String sql = "SELECT a.* FROM Accounts a";

        List<AccountModel> accountList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    accountList.add(new AccountModel(
                            UUID.fromString(rs.getString("serial")),
                            rs.getInt("id"),
                            rs.getString("getName"),
                            rs.getString("lastname"),
                            rs.getString("type"),
                            rs.getFloat("balance")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AccountConverter accountConverter = new AccountConverter();
        List<AccountData> accountDataList = new ArrayList<>();
        for (AccountModel model : accountList) {
            accountDataList.add(accountConverter.toData(model));
        }

        return accountDataList;
    }




    public UserData getUserData(int nationalID) {
        String sql = "SELECT * FROM Users WHERE national_id= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nationalID);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.println("Column " + i + ": " + meta.getColumnName(i));
            }
            if (rs.next()) {
                UserModel fetchedUserModel= new UserModel(
                        rs.getInt("id"),
                        rs.getString("getName"),
                        rs.getString("lastname"),
                        rs.getString("password")
                );
                UserConverter userConverter = new UserConverter();
                return userConverter.toData(fetchedUserModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}








