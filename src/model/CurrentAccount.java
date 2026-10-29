package model;

public class CurrentAccount  extends Account {
    public CurrentAccount(int accountNumber, Customer customer, double balance) {
        super(accountNumber,customer,balance , AccountType.CURRENT);
    }
    @Override
    public double calculateInterest(){
        return balance * 0.02;
    }
}
