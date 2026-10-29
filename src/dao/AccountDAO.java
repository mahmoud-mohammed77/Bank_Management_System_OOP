package dao;

import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountDAO {
    public void addAccount(Account account) {

        String sql = "INSERT INTO Accounts (AccountNumber, CustomerID, AccountTypeID, Balance, CreatedAt, Status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setInt(1, account.getAccountNumber());
            pstmt.setInt(2, account.getcostomer().getId());

            int typeId = (account.getAccountType() == AccountType.SAVING) ? 1 : 2;
            pstmt.setInt(3, typeId);

            pstmt.setDouble(4, account.getBalance());
            pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            pstmt.setString(6, "Active");


            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("model.Account added to database successfully!");
            } else {
                System.out.println("Failed to add account.");
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Account getAccountByNumber(int accountNumber) {
        String sql = "SELECT AccountID, AccountNumber, CustomerID, AccountTypeID, Balance FROM Accounts WHERE AccountNumber = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    CustomerDAO customerDAO = new CustomerDAO();
                    Customer customer = customerDAO.getCustomerById(rs.getInt("CustomerID"));

                    AccountType type = (rs.getInt("AccountTypeID") == 1) ? AccountType.SAVING : AccountType.CURRENT;

                    Account account;
                    if (type == AccountType.SAVING) {
                        account = new SavingAccount(
                                rs.getInt("AccountNumber"),
                                customer,
                                rs.getDouble("Balance")
                        );
                    } else {
                        account = new CurrentAccount(
                                rs.getInt("AccountNumber"),
                                customer,
                                rs.getDouble("Balance")
                        );
                    }
                    account.setAccountID(rs.getInt("AccountID"));

                    return account;
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Database Error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
        public void updateBalance(int accountNumber , double Newbalance){
            String sql = "Update Accounts Set Balance = ? Where AccountNumber=?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement pass = con.prepareStatement(sql)){
                pass.setDouble(1,Newbalance);
                pass.setInt(2,accountNumber);
                int rowAffect=pass.executeUpdate();
                if(rowAffect > 0){
                    System.out.println("Balance updated Successfully...");
                }else {
                    System.out.println("model.Account not found..!");
                }

            }catch (SQLException e){
                System.out.println("Database Error :" +e.getMessage());
                e.printStackTrace();
            }

        }
        public void DeleteAccount(int accountNumber){
            String sql="Delete from Accounts where AccountNumber=?";

            try (Connection con= DBConnection.getConnection();
                 PreparedStatement pass =con.prepareStatement(sql)){
                pass.setInt(1,accountNumber);
                int rowAffected=pass.executeUpdate();
                if(rowAffected > 0){
                    System.out.println("model.Account Delete Successfully");
                }else {
                    System.out.println("model.Account not found");
                }

            }catch (SQLException e){
                System.out.println("Database Error :"+e.getMessage());
                e.printStackTrace();
            }
        }
    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        String sql = "SELECT AccountID, AccountNumber, CustomerID, AccountTypeID, Balance FROM Accounts";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            CustomerDAO customerDAO = new CustomerDAO();

            while (rs.next()) {
                Customer customer = customerDAO.getCustomerById(rs.getInt("CustomerID"));
                AccountType type = (rs.getInt("AccountTypeID") == 1) ? AccountType.SAVING : AccountType.CURRENT;

                Account account;
                if (type == AccountType.SAVING) {
                    account = new SavingAccount(
                            rs.getInt("AccountNumber"),
                            customer,
                            rs.getDouble("Balance")
                    );
                } else {
                    account = new CurrentAccount(
                            rs.getInt("AccountNumber"),
                            customer,
                            rs.getDouble("Balance")
                    );
                }

                account.setAccountID(rs.getInt("AccountID")); // مهم جداً
                accounts.add(account);
            }

        } catch (SQLException e) {
            System.out.println("❌ Database Error: " + e.getMessage());
            e.printStackTrace();
        }

        return accounts;
    }


}
