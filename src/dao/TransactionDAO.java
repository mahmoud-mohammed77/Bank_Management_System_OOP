package dao;

import model.Transaction;
import model.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionDAO {

    public void addTransaction (int accountid , String type , double Amount , String status){

        String sql ="Insert into Transactions (AccountID, TransactionType , Amount , TransactionDate,Status) values (? , ? , ? , ? ,? )";


        try (Connection con = DBConnection.getConnection();
             PreparedStatement pass = con.prepareStatement(sql)){
            pass.setInt(1,accountid);
            pass.setString(2,type);
            pass.setDouble(3,Amount);
            pass.setTimestamp(4,java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            pass.setString(5,status);

            int rowAffect=pass.executeUpdate();
            if(rowAffect > 0) {
                System.out.println("model.Transaction saved to database!");
            }else {
                System.out.println("Failed to save transaction.");
            }


        }catch (SQLException e){
            System.out.println("Database Error :" + e.getMessage());
            e.printStackTrace();
        }


    }
    public ArrayList<Transaction> getTransactionsByAccount(int accountId) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT TransactionType, Amount, TransactionDate FROM Transactions WHERE AccountID = ? ORDER BY TransactionDate DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String typeStr = rs.getString("model.TransactionType");
                    TransactionType type = TransactionType.valueOf(typeStr);

                    double amount = rs.getDouble("Amount");
                    java.time.LocalDateTime date = rs.getTimestamp("TransactionDate").toLocalDateTime();
                    Transaction t = new Transaction(type, amount);
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
