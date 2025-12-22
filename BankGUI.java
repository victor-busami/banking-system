import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Minimal Swing GUI for the Bank ATM MVP.
 * - Tabs: Create account, Login (deposit/withdraw), List all accounts
 * - Uses existing `DBHelper` and `Account` classes in this project
 */
public class BankGUI extends JFrame {
    private final DBHelper db = new DBHelper();

    // Create tab components
    private final JTextField createName = new JTextField(15);
    private final JTextField createPin = new JTextField(6);
    private final JTextField createType = new JTextField("checking", 10);
    private final JTextField createInitial = new JTextField("0.0", 10);

    // Login tab components
    private final JTextField loginName = new JTextField(15);
    private final JTextField loginPin = new JTextField(6);
    private final JLabel loggedAccountLabel = new JLabel("No account");
    private Account currentAccount = null;
    private final JTextField depositAmount = new JTextField(8);
    private final JTextField withdrawAmount = new JTextField(8);

    // (no admin/list tab in minimalist GUI)

    public BankGUI() {
        super("Minimal Bank ATM - GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Create", buildCreatePanel());
        tabs.addTab("Login", buildLoginPanel());

        add(tabs, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildCreatePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;

        int row = 0;
        c.gridy = row++;
        p.add(new JLabel("Customer name:"), c);
        c.gridx = 1; p.add(createName, c);
        c.gridx = 0;

        c.gridy = row++;
        p.add(new JLabel("PIN:"), c);
        c.gridx = 1; p.add(createPin, c);
        c.gridx = 0;

        c.gridy = row++;
        p.add(new JLabel("Account type:"), c);
        c.gridx = 1; p.add(createType, c);
        c.gridx = 0;

        c.gridy = row++;
        p.add(new JLabel("Initial deposit:"), c);
        c.gridx = 1; p.add(createInitial, c);
        c.gridx = 0;

        c.gridy = row++;
        JButton createBtn = new JButton("Create Account");
        createBtn.addActionListener(e -> onCreateAccount());
        c.gridwidth = 2;
        p.add(createBtn, c);

        return p;
    }

    private JPanel buildLoginPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Name:")); top.add(loginName);
        top.add(new JLabel("PIN:")); top.add(loginPin);
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> onLogin());
        top.add(loginBtn);
        p.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;
        int row = 0;

        c.gridy = row++; c.gridx = 0; center.add(new JLabel("Account:"), c);
        c.gridx = 1; center.add(loggedAccountLabel, c);

        c.gridy = row++; c.gridx = 0; center.add(new JLabel("Deposit:"), c);
        c.gridx = 1; center.add(depositAmount, c);
        c.gridx = 2; JButton depBtn = new JButton("Deposit"); depBtn.addActionListener(e -> onDeposit()); center.add(depBtn, c);

        c.gridy = row++; c.gridx = 0; center.add(new JLabel("Withdraw:"), c);
        c.gridx = 1; center.add(withdrawAmount, c);
        c.gridx = 2; JButton wBtn = new JButton("Withdraw"); wBtn.addActionListener(e -> onWithdraw()); center.add(wBtn, c);

        p.add(center, BorderLayout.CENTER);
        return p;
    }

    // Create account action
    private void onCreateAccount() {
        String name = createName.getText().trim();
        String pinText = createPin.getText().trim();
        String type = createType.getText().trim();
        String initialText = createInitial.getText().trim();

        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Name required"); return; }
        int pin;
        double initial;
        try { pin = Integer.parseInt(pinText); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid PIN"); return; }
        try { initial = Double.parseDouble(initialText); if (initial < 0) throw new NumberFormatException(); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid initial deposit"); return; }

        int customerId = db.createCustomer(name, pin);
        if (customerId == -1) { JOptionPane.showMessageDialog(this, "Failed to create customer"); return; }
        int accountId = db.createAccount(customerId, type.isEmpty() ? "checking" : type, initial);
        if (accountId == -1) { JOptionPane.showMessageDialog(this, "Failed to create account"); return; }
        JOptionPane.showMessageDialog(this, "Account created. ID: " + accountId);
        // clear fields
        createName.setText(""); createPin.setText(""); createInitial.setText("0.0");
    }

    // Login action
    private void onLogin() {
        String name = loginName.getText().trim();
        String pinText = loginPin.getText().trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Name required"); return; }
        int pin;
        try { pin = Integer.parseInt(pinText); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid PIN"); return; }
        Account acc = db.getAccountByCustomerNameAndPin(name, pin);
        if (acc == null) { JOptionPane.showMessageDialog(this, "Authentication failed or no account found"); return; }
        currentAccount = acc;
        updateLoggedAccountDisplay();
        // Minimal GUI: show balance popup after login
        JOptionPane.showMessageDialog(this, String.format("Account #%d balance: %.2f", currentAccount.getId(), currentAccount.getBalance()));
    }

    private void updateLoggedAccountDisplay() {
        if (currentAccount == null) loggedAccountLabel.setText("No account");
        else loggedAccountLabel.setText(String.format("#%d (%s) Balance: %.2f", currentAccount.getId(), currentAccount.getType(), currentAccount.getBalance()));
    }

    private void onDeposit() {
        if (currentAccount == null) { JOptionPane.showMessageDialog(this, "Login first"); return; }
        String txt = depositAmount.getText().trim();
        double amt;
        try { amt = Double.parseDouble(txt); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid amount"); return; }
        try {
            currentAccount.deposit(amt);
            boolean ok = db.updateAccountBalance(currentAccount.getId(), currentAccount.getBalance());
            if (ok) { JOptionPane.showMessageDialog(this, "Deposit successful"); updateLoggedAccountDisplay(); }
            else JOptionPane.showMessageDialog(this, "Failed to persist deposit");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onWithdraw() {
        if (currentAccount == null) { JOptionPane.showMessageDialog(this, "Login first"); return; }
        String txt = withdrawAmount.getText().trim();
        double amt;
        try { amt = Double.parseDouble(txt); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid amount"); return; }
        try {
            currentAccount.withdraw(amt);
            boolean ok = db.updateAccountBalance(currentAccount.getId(), currentAccount.getBalance());
            if (ok) { JOptionPane.showMessageDialog(this, "Withdrawal successful"); updateLoggedAccountDisplay(); }
            else JOptionPane.showMessageDialog(this, "Failed to persist withdrawal");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void refreshList() {
        // removed in minimalist GUI
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankGUI g = new BankGUI();
            g.setVisible(true);
        });
    }
}
