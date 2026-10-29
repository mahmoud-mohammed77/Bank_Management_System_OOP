package dao;

import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAO {

    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO Customers (FullName, Phone, Email, CreatedAt) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getFullName());
            pstmt.setString(2, customer.getPhone());
            pstmt.setString(3, customer.getEmail());
            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(customer.getCreatedAt()));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Error in Database");
            } else {
                System.out.println("Invalid Add  User");
            }

        } catch (SQLException e) {
            System.out.println("Error in Database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Customer getCustomerById(int id) {
        String sql = "SELECT CustomerID, FullName, Phone, Email, CreatedAt FROM Customers WHERE CustomerID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer(
                            rs.getInt("CustomerID"),
                            rs.getString("FullName"),
                            rs.getString("Phone"),
                            rs.getString("Email"),
                            rs.getTimestamp("CreatedAt").toLocalDateTime()
                    );
                    return customer;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error in Database " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    public void printCustomerById(int id) {

        Customer customer = getCustomerById(id);


        if (customer != null) {
            System.out.println("************ Found model.Account ***********");
            System.out.println("ID: " + customer.getId());
            System.out.println("Name: " + customer.getFullName());
            System.out.println("Phone: " + customer.getPhone());
            System.out.println("Email: " + customer.getEmail());
            System.out.println("Created At: " + customer.getCreatedAt());
            System.out.println("************ End info model.Account ***********");
        } else {
            System.out.println("No model.Account has this ID");
        }
    }

    public ArrayList<Customer> getAllCustomer() {
        ArrayList<Customer> customers = new ArrayList<>();
        String sql = "SELECT customerID , FullName , Phone , Email , CreatedAt  from Customers ";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("CustomerID"),
                        rs.getString("FullName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getTimestamp("CreatedAt").toLocalDateTime()
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Error in Database Connection :" + e.getMessage());
            e.printStackTrace();
        }
        return customers;
    }

    public void UpdateAccount(Customer customer) {
        String sql = "UPDATE CUSTOMERS SET FullName = ? , Phone = ? , Email = ? Where CustomerID= ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pass = con.prepareStatement(sql)) {
            pass.setString(1, customer.getFullName());
            pass.setString(2, customer.getPhone());
            pass.setString(3, customer.getEmail());
            pass.setInt(4, customer.getId());

            int rowAffected = pass.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Successfully Update....");
            } else {
                System.out.println("Invalid Update.........");
            }
        } catch (SQLException e) {
            System.out.println("AN Error in Database  : " + e.getMessage());
            e.printStackTrace();
        }

    }


    public void DelateCustomer(int customerID) {
        String sql = "DELETE FROM Customers WHERE CustomerID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pass = conn.prepareStatement(sql)) {
            pass.setInt(1, customerID);
            int rowAffect = pass.executeUpdate();
            if (rowAffect > 0) {
                System.out.println("Successful delete User");
            } else {
                System.out.println("Invalid ID");
            }

        } catch (SQLException e) {
            System.out.println("Error in Database " + e.getMessage());
            e.printStackTrace();
        }
    }
}




















