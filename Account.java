//class
public class Account {
    // data types & variables(id, customerId, type, balance)
    // encapsulation(use of private fields)
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

    //encapsulated getters/setters
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

 
    //input validation(only positive amounts for deposits)
    //error handling(IllegalArgumentException for invalid deposits)
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive");
        balance += amount;
    }

    //input validation(only positive amounts and sufficient funds for withdrawals)
    //error handling(IllegalArgumentException for invalid withdrawals)
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
