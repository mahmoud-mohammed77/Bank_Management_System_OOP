package model;

import dao.AccountDAO;
import dao.TransactionDAO;

import java.util.ArrayList;
import java.util.Scanner;

public class Bank {

    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    public Bank() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }

    // ========== إدارة الحسابات ==========

    public void addAccount(Account account) {
        accountDAO.addAccount(account);
    }

    public void ShowAccounts() {
        ArrayList<Account> accounts = accountDAO.getAllAccounts();

        if (accounts.isEmpty()) {
            System.out.println("No accounts found in the database.");
            return;
        }

        for (Account account : accounts) {
            System.out.println("Account Number : " + account.getAccountNumber());
            System.out.println("Customer Name  : " + account.getcostomer().getFullName());
            System.out.println("Balance        : " + account.getBalance());
            System.out.println("accountType    : " + account.getAccountType());
            System.out.println("---------------------------------");
        }
    }

    public Account findAccount(int accountNumber) {
        return accountDAO.getAccountByNumber(accountNumber);
    }

    public boolean DeleteAccount(int accountNumber) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            return false;
        }
        accountDAO.DeleteAccount(accountNumber);
        return true;
    }

    // ========== العمليات البنكية ==========

    public boolean deposit(int accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            return false;
        }

        // 1. تحديث الرصيد في الكائن
        account.deposit(amount);

        // 2. تحديث الرصيد في الداتابيز
        accountDAO.updateBalance(accountNumber, account.getBalance());

        // 3. تسجيل العملية في جدول Transactions
        transactionDAO.addTransaction(account.getAccountID(), "DEPOSIT", amount, "SUCCESS");

        return true;
    }

    public boolean withdraw(int accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            return false;
        }

        // 1. محاولة السحب في الكائن
        boolean success = account.withdraw(amount);

        if (success) {
            // 2. تحديث الرصيد في الداتابيز لو نجح
            accountDAO.updateBalance(accountNumber, account.getBalance());

            // 3. تسجيل العملية
            transactionDAO.addTransaction(account.getAccountID(), "WITHDRAW", amount, "SUCCESS");
        } else {
            // تسجيل محاولة سحب فاشلة (اختياري)
            transactionDAO.addTransaction(account.getAccountID(), "WITHDRAW", amount, "FAILED");
        }

        return success;
    }

    public boolean transfer(int fromAccountNumber, int toAccountNumber, double amount) {
        Account fromAccount = findAccount(fromAccountNumber);
        Account toAccount = findAccount(toAccountNumber);

        // التحقق من وجود الحسابات
        if (fromAccount == null || toAccount == null) {
            return false;
        }
        // منع التحويل لنفس الحساب
        if (fromAccount == toAccount) {
            return false;
        }
        // التحقق من كفاية الرصيد
        if (amount <= 0 || amount > fromAccount.getBalance()) {
            return false;
        }

        // 1. خصم وإضافة في الكائنات
        fromAccount.balance -= amount;
        toAccount.balance += amount;

        // 2. تحديث الداتابيز للحسابين
        accountDAO.updateBalance(fromAccountNumber, fromAccount.getBalance());
        accountDAO.updateBalance(toAccountNumber, toAccount.getBalance());

        // 3. تسجيل العمليات في جدول Transactions
        transactionDAO.addTransaction(fromAccount.getAccountID(), "TRANSFER_OUT", amount, "SUCCESS");
        transactionDAO.addTransaction(toAccount.getAccountID(), "TRANSFER_IN", amount, "SUCCESS");

        return true;
    }

    // ========== طرق قراءة المدخلات (Helpers) ==========

    public static int readAccountNumber() {
        while (true) {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter Account Number :");
            String accountnumber = input.nextLine();
            try {
                return Integer.parseInt(accountnumber);
            } catch (NumberFormatException e) {
                System.out.println("Please enter numbers only!");
            }
        }
    }

    public static int readAccountbalance() {
        while (true) {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter Account Balance : ");
            String accountbalnce = input.nextLine();
            try {
                return Integer.parseInt(accountbalnce);
            } catch (NumberFormatException e) {
                System.out.println("Please enter numbers only!");
            }
        }
    }
}