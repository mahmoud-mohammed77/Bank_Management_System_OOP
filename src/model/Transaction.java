package model;

import java.time.LocalDateTime;

public class Transaction {
    private int id;                  // ضفنا الـ ID
    private int accountId;           // ضفنا الـ AccountID
    private double amount;
    private LocalDateTime date;
    private TransactionType type;
    private int targetAccountNumber;
    private String status;           // ضفنا الـ Status

    // 1. Constructor للعمليات الجديدة (زي deposit و withdraw) من غير ID
    public Transaction(TransactionType type, double amount) {
        this.type = type;
        this.amount = amount;
        this.date = LocalDateTime.now();
        this.targetAccountNumber = -1;
        this.status = "SUCCESS";
    }

    // 2. Constructor للتحويلات (Transfer) من غير ID
    public Transaction(TransactionType type, double amount, int targetAccountNumber) {
        this.type = type;
        this.amount = amount;
        this.date = LocalDateTime.now();
        this.targetAccountNumber = targetAccountNumber;
        this.status = "SUCCESS";
    }

    // 3. Constructor عشان لما نقرأ من الداتابيز (مع ID و AccountID و Status)
    public Transaction(int id, int accountId, TransactionType type, double amount, LocalDateTime date, String status) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.targetAccountNumber = -1;
    }

    // 4. Constructor عشان لو حبينا نعمل Transaction بتاريج معين (زي ما إنت كنت عامل)
    public Transaction(TransactionType type, double amount, LocalDateTime date) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.targetAccountNumber = -1;
        this.status = "SUCCESS";
    }

    // ========== Getters ==========
    public int getId() { return id; }
    public int getAccountId() { return accountId; }
    public double getAmount() { return amount; }
    public LocalDateTime getData() { return date; }
    public TransactionType getType() { return type; }
    public String getStatus() { return status; }
    public int getTargetAccountNumber() { return targetAccountNumber; }

    // ========== Setters ==========
    public void setDate(LocalDateTime date) { this.date = date; }
    public void setStatus(String status) { this.status = status; }
}