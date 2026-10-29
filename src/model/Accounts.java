package model;

import java.time.LocalDateTime;


public class Accounts {
    private int AccountID;
    private int AccountNumber;
    private int CustomerID;
    private AccountType AccountType;
    private Double Balance;
    private LocalDateTime CreatedAT;
    private String  Status;
    public Accounts(int accountID , int accountNumber , int customerID , AccountType accountType , Double balance, LocalDateTime createdAT , String status )
    {
        super();
        this.AccountID=accountID;
        this.AccountNumber=accountNumber;
        this.AccountType=accountType;
        this.Balance=balance;
        this.CreatedAT=createdAT;
        this.CustomerID=customerID;
        this.Status=status;
    }
    public void setAccountID(int id){
        this.AccountID=id;
    }
    public void setAccountNumber(int accountNumber){
        this.AccountNumber=accountNumber;
    }
    public void setCustomerID(int customerID){
        this.CustomerID=customerID;
    }
    public void setAccountType(AccountType accountType){
        this.AccountType=accountType;
    }
    public void setBalance(Double balance){
        this.Balance=balance;
    }
    public void setCreatedAT(LocalDateTime createdAT){
        this.CreatedAT= LocalDateTime.from(createdAT.toLocalTime());
    }
    public void setStatus(String status)
    {
        this.Status=status;
    }

}
