package model;

import java.util.ArrayList;

public abstract class Account {
    protected int AccountID;
    protected int AccountNumber;
    protected Customer customer;
    protected double balance;
    protected AccountType accountType;
    private ArrayList<Transaction> transactions;

    public Account(int accountID, int AccountNUmber, Customer customer, double balance , AccountType accountType) {
        this.AccountID=accountID;
        this.AccountNumber=AccountNUmber;
        this.customer=customer;
        this.balance=balance;
        this.accountType=accountType;
        this.transactions=new ArrayList<>();
    }

    public Account( int AccountNUmber,Customer customer,double balance , AccountType accountType) {
        this.AccountNumber=AccountNUmber;
        this.customer=customer;
        this.balance=balance;
        this.accountType=accountType;
        this.transactions=new ArrayList<>();
    }
    public int getAccountID(){
        return this.AccountID;
    }
    public void setAccountID(int accountID){
        this.AccountID=accountID;
    }
    public int getAccountNumber(){
        return this.AccountNumber;

    }
    public Customer getcostomer() {

        return this.customer;
    }
    public double getBalance() {
        return this.balance;

    }
    public double deposit(double number){
        if(number > 0 ) {
           this.balance+=number;
           transactions.add(new Transaction(TransactionType.DEPOSIT,number));
        }
        return this.balance;

    }
    public boolean withdraw(double amount) {

        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactions.add(new Transaction(TransactionType.WITHDRAW,amount));
            return true;
        }

        return false;
    }
    @Override
    public String toString() {
        return "model.Account Number : " +AccountNumber + ", Name : " + customer + ", Balance :" +balance;

    }
    public abstract double calculateInterest();
    public AccountType getAccountType() {
        return accountType;
    }
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    public void ShowTransaction(){
        for(Transaction transaction : transactions) {
            System.out.println("Type : "+transaction.getType());
            System.out.println("Amount : " + transaction.getAmount());
            System.out.println("Date : " + transaction.getData());
        }
    }


}
