import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DBHelper handles SQLite connection and CRUD operations.
 * Creates two tables: customers and accounts.
 *
 * Clear concept mapping (this file):
 * 1. Data types & variables: `DB_URL` (String), local variables for SQL fields and results.
 * 2. Methods: `initDB`, `getConnection`, `createCustomer`, `createAccount`, `getAccount`, etc.
 * 4. Encapsulation: `DB_URL` is `private static final` and `getConnection()` is `private`.
 * 8. Database Support: uses JDBC (`Connection`, `PreparedStatement`, `ResultSet`) and SQL statements to create/query/update tables.
 * 10. Error handling: `try/catch (SQLException)` in CRUD methods; errors logged to `System.err`.
 */
public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:bank.db"; // uses local file bank.db

    public DBHelper() {
        initDB();
    }

    // Initialize DB and tables
    public void initDB() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Customers table: id, name, pin
            stmt.execute("CREATE TABLE IF NOT EXISTS customers ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "pin INTEGER NOT NULL"
                    + ");");

            // Accounts table: id, customer_id, type, balance
            stmt.execute("CREATE TABLE IF NOT EXISTS accounts ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "customer_id INTEGER NOT NULL,"
                    + "type TEXT NOT NULL,"
                    + "balance REAL NOT NULL DEFAULT 0,"
                    + "FOREIGN KEY(customer_id) REFERENCES customers(id)"
                    + ");");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }

    // Get a DB connection
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // Driver not present on classpath — runtime must include sqlite-jdbc jar
        }
        return DriverManager.getConnection(DB_URL);
    }

    // Create a customer and return generated id
    public int createCustomer(String name, int pin) {
        String sql = "INSERT INTO customers(name, pin) VALUES(?,?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setInt(2, pin);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating customer: " + e.getMessage());
        }
        return -1;
    }

    // Create an account for an existing customer
    public int createAccount(int customerId, String type, double initialBalance) {
        String sql = "INSERT INTO accounts(customer_id, type, balance) VALUES(?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customerId);
            ps.setString(2, type);
            ps.setDouble(3, initialBalance);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
        }
        return -1;
    }

    // Retrieve Account by id
    public Account getAccount(int accountId) {
        String sql = "SELECT id, customer_id, type, balance FROM accounts WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getInt("id"), rs.getInt("customer_id"), rs.getString("type"), rs.getDouble("balance"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching account: " + e.getMessage());
        }
        return null;
    }

    // Get customer's PIN given an account id (used for simple auth)
    public Integer getCustomerPinByAccount(int accountId) {
        String sql = "SELECT c.pin FROM customers c JOIN accounts a ON c.id = a.customer_id WHERE a.id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("pin");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching PIN: " + e.getMessage());
        }
        return null;
    }

    // Lookup account by customer name + pin (returns first matching account)
    public Account getAccountByCustomerNameAndPin(String name, int pin) {
        String sql = "SELECT a.id, a.customer_id, a.type, a.balance FROM accounts a JOIN customers c ON a.customer_id = c.id WHERE c.name = ? AND c.pin = ? LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, pin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getInt("id"), rs.getInt("customer_id"), rs.getString("type"), rs.getDouble("balance"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching account by name+PIN: " + e.getMessage());
        }
        return null;
    }

    // Update balance for an account
    public boolean updateAccountBalance(int accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountId);
            int updated = ps.executeUpdate();
            return updated == 1;
        } catch (SQLException e) {
            System.err.println("Error updating balance: " + e.getMessage());
        }
        return false;
    }

    // List all accounts (small MVP so no pagination)
    public List<Account> listAccounts() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT id, customer_id, type, balance FROM accounts";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Account(rs.getInt("id"), rs.getInt("customer_id"), rs.getString("type"), rs.getDouble("balance")));
            }
        } catch (SQLException e) {
            System.err.println("Error listing accounts: " + e.getMessage());
        }
        return list;
    }
}
