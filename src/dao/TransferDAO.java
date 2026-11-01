package dao;
import dao.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransferDAO {

    public void addTransfer(int fromAccountId, int toAccountId, double amount) {
        String sql = "INSERT INTO Transfers (FromAccountID, ToAccountID, Amount, TransferDate) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, fromAccountId);
            pstmt.setInt(2, toAccountId);
            pstmt.setDouble(3, amount);
            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Transfer recorded in Transfers table.");
            } else {
                System.out.println("Failed to record transfer.");
            }

        } catch (SQLException e) {
            System.out.println("Database Error saving transfer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}