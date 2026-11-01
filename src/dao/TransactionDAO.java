package dao;

import model.Transaction;
import model.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TransactionDAO {

    public void addTransaction(int accountId, String type, double amount, String status) {
        String sql = "INSERT INTO Transactions (AccountID, TransactionType, Amount, TransactionDate, Status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(5, status);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Transaction saved to database!");
            } else {
                System.out.println("❌ Failed to save transaction.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<Transaction> getTransactionsByAccount(int accountId) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        // جبنا كل الأعمدة عشان نعرضها صح
        String sql = "SELECT TransactionID, AccountID, TransactionType, Amount, TransactionDate, Status FROM Transactions WHERE AccountID = ? ORDER BY TransactionDate DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {

                    TransactionType type = TransactionType.valueOf(rs.getString("TransactionType").toUpperCase());


                    LocalDateTime date = rs.getTimestamp("TransactionDate").toLocalDateTime();


                    Transaction t = new Transaction(
                            rs.getInt("TransactionID"),
                            rs.getInt("AccountID"),
                            type,
                            rs.getDouble("Amount"),
                            date,
                            rs.getString("Status")
                    );
                    transactions.add(t);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Database Error: " + e.getMessage());
            e.printStackTrace();
        }

        return transactions;
    }
}