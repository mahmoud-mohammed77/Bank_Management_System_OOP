import dao.CustomerDAO;
import model.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Bank bank = new Bank();
        boolean running = true;

        while (running) {

            printMenu();

            System.out.print("Choice : ");
            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    createAccount(input, bank);
                    break;
                case 2:
                    addCustomer(input);
                    break;
                case 3:
                    showAllCustomers();
                    break;
                case 4:
                    findCustomer(input);
                    break;
                case 5:
                    updateCustomer(input);
                    break;
                case 6:
                    deleteCustomer(input);
                    break;
                case 7:
                    deposit(input, bank);
                    break;
                case 8:
                    withdraw(input, bank);
                    break;
                case 9:
                    transfer(input, bank);
                    break;
                case 10:
                    showTransactionHistory(input, bank);
                    break;
                case 11:
                    System.out.println("Goodbye, see you soon!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        }

        input.close();
    }

    public static void printMenu() {
        System.out.println("================ model.Bank Management System =================");
        System.out.println("1) Create model.Account");
        System.out.println("2) Add model.Customer");
        System.out.println("3) Show All Customers");
        System.out.println("4) Find model.Customer");
        System.out.println("5) Update model.Customer");
        System.out.println("6) Delete model.Customer");
        System.out.println("7) Deposit");
        System.out.println("8) Withdraw");
        System.out.println("9) Transfer");
        System.out.println("10) Show model.Transaction History");
        System.out.println("11) Exit");
        System.out.println("==========================================================");
    }

    // ========== model.Account Methods ==========

    public static void createAccount(Scanner input, Bank bank) {
        int accountNumber = Bank.readAccountNumber();
        input.nextLine();

        System.out.print("Enter model.Customer ID: ");
        int customerId = input.nextInt();
        input.nextLine();

        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getCustomerById(customerId);

        if (customer == null) {
            System.out.println("model.Customer Not Found!");
            return;
        }

        double balance = Bank.readAccountbalance();
        input.nextLine();
        System.out.println("1) Saving model.Account");
        System.out.println("2) Current model.Account");
        System.out.print("Choose model.Account Type: ");
        int type = input.nextInt();
        input.nextLine();

        Account account;

        if (type == 1) {
            account = new SavingAccount(accountNumber, customer, balance);
        } else if (type == 2) {
            account = new CurrentAccount(accountNumber, customer, balance);
        } else {
            System.out.println("Invalid model.Account Type");
            return;
        }

        bank.addAccount(account);
        System.out.println("model.Account Created Successfully");
    }

    public static void deposit(Scanner input, Bank bank) {
        System.out.print("Enter model.Account Number: ");
        int accountNumber = input.nextInt();

        System.out.print("Enter Deposit Amount: ");
        double amount = input.nextDouble();

        boolean success = bank.deposit(accountNumber, amount);

        if (success) {
            System.out.println("Deposit Successful");
        } else {
            System.out.println("Deposit Failed");
        }
    }

    public static void withdraw(Scanner input, Bank bank) {
        System.out.print("Enter model.Account Number: ");
        int accountNumber = input.nextInt();

        System.out.print("Enter Withdraw Amount: ");
        double amount = input.nextDouble();

        boolean success = bank.withdraw(accountNumber, amount);

        if (success) {
            System.out.println("Withdraw Successful");
        } else {
            System.out.println("Withdraw Failed");
        }
    }

    public static void transfer(Scanner input, Bank bank) {
        System.out.print("From model.Account Number: ");
        int fromAccountNumber = input.nextInt();

        System.out.print("To model.Account Number: ");
        int toAccountNumber = input.nextInt();

        System.out.print("Enter Transfer Amount: ");
        double amount = input.nextDouble();

        boolean success = bank.transfer(fromAccountNumber, toAccountNumber, amount);

        if (success) {
            System.out.println("Transfer Successful");
        } else {
            System.out.println("Transfer Failed");
        }
    }

    public static void showTransactionHistory(Scanner input, Bank bank) {
        System.out.print("Enter model.Account Number: ");
        int accountNumber = input.nextInt();

        bank.showTransactionHistory(accountNumber);
    }

    // ========== model.Customer Methods ==========

    public static void addCustomer(Scanner input) {
        input.nextLine();

        System.out.print("Enter Full Name: ");
        String fullName = input.nextLine();

        System.out.print("Enter Phone: ");
        String phone = input.nextLine();

        System.out.print("Enter Email: ");
        String email = input.nextLine();

        Customer customer = new Customer(
                fullName,
                phone,
                email,
                java.time.LocalDateTime.now()
        );

        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.addCustomer(customer);
    }

    public static void showAllCustomers() {
        CustomerDAO customerDAO = new CustomerDAO();
        ArrayList<Customer> customers = customerDAO.getAllCustomer();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        System.out.println("Total Customers: " + customers.size());
        System.out.println("---------------------------------");

        for (Customer c : customers) {
            System.out.println("ID: " + c.getId());
            System.out.println("Name: " + c.getFullName());
            System.out.println("Phone: " + c.getPhone());
            System.out.println("Email: " + c.getEmail());
            System.out.println("Created At: " + c.getCreatedAt());
            System.out.println("---------------------------------");
        }
    }

    public static void findCustomer(Scanner input) {
        System.out.print("Enter model.Customer ID: ");
        int id = input.nextInt();

        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getCustomerById(id);

        if (customer != null) {
            System.out.println("model.Customer Found:");
            System.out.println("ID: " + customer.getId());
            System.out.println("Name: " + customer.getFullName());
            System.out.println("Phone: " + customer.getPhone());
            System.out.println("Email: " + customer.getEmail());
            System.out.println("Created At: " + customer.getCreatedAt());
        } else {
            System.out.println("model.Customer Not Found");
        }
    }

    public static void updateCustomer(Scanner input) {
        System.out.print("Enter model.Customer ID: ");
        int id = input.nextInt();

        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getCustomerById(id);

        if (customer == null) {
            System.out.println("model.Customer Not Found");
            return;
        }

        input.nextLine();

        System.out.print("Enter New Full Name: ");
        customer.setFullName(input.nextLine());

        System.out.print("Enter New Phone: ");
        customer.setPhone(input.nextLine());

        System.out.print("Enter New Email: ");
        customer.setEmail(input.nextLine());

        customerDAO.UpdateAccount(customer);
    }

    public static void deleteCustomer(Scanner input) {
        System.out.print("Enter model.Customer ID: ");
        int id = input.nextInt();

        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.DelateCustomer(id);
    }
}