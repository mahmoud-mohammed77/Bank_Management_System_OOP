package model;

public class SavingAccount extends Account {
    public SavingAccount(int accountNumber, Customer customer, double balance) {
        super(accountNumber, customer, balance , AccountType.SAVING);
    }
    @Override
    public double calculateInterest() {
        return balance * 0.05;

    }

}
