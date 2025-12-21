/**
 * Account model demonstrating encapsulation and methods (deposit/withdraw).
 *
 * Clear concept mapping (this file):
 * 1. Data types & variables: fields `id` (int), `customerId` (int), `type` (String), `balance` (double).
 * 2. Methods: constructor, getters/setters, `deposit`, `withdraw`, `toString`.
 * 4. Encapsulation: fields are `private` with public getters/setters.
 * 9. Input validation: `deposit` and `withdraw` check amounts (amount > 0, sufficient balance).
 * 10. Error handling: invalid operations throw `IllegalArgumentException` to signal errors to callers.
 */
public class Account {
    // 1. Data types & variables: fields below (`id`, `customerId`, `type`, `balance`).
    // 4. Encapsulation: fields are private; use getters/setters to access/modify.
    private int id;
    private int customerId;
    private String type;
    private double balance;

    public Account(int id, int customerId, String type, double balance) {
        this.id = id;
        this.customerId = customerId;
        this.type = type;
        this.balance = balance;
    }

    // Encapsulated getters/setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Business methods with validation
    // 9. Input validation: ensure deposit amount is positive.
    // 10. Error handling: throws IllegalArgumentException for invalid deposits.
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive");
        balance += amount;
    }

    // 9. Input validation: ensure withdrawal amount is positive and not greater than balance.
    // 10. Error handling: throws IllegalArgumentException for invalid withdrawals or insufficient funds.
    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdraw amount must be positive");
        if (amount > balance) throw new IllegalArgumentException("Insufficient funds");
        balance -= amount;
    }

    @Override
    public String toString() {
        return String.format("Account{id=%d, customerId=%d, type='%s', balance=%.2f}", id, customerId, type, balance);
    }
}
