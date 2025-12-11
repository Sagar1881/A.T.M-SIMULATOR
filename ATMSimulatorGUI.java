import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

// 🔹 Abstract class for core ATM operations
abstract class ATMOperations {
    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);
    public abstract double checkBalance();
}

// 🔹 Interface for transaction messages
interface TransactionHandler {
    void displayMessage(String message);
}

// 🔹 User class (Encapsulation + Serializable)
class User implements Serializable {
    private String name;
    private int pin;

    public User(String name, int pin) {
        this.name = name;
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public int getPin() {
        return pin;
    }
}

// 🔹 ATM class implementing abstraction, interface, and polymorphism
class ATM extends ATMOperations implements TransactionHandler, Serializable {
    private double balance;
    private User user;
    private ArrayList<String> history = new ArrayList<>();

    public ATM(User user, double initialBalance) {
        this.user = user;
        this.balance = initialBalance;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Invalid deposit amount!");
        balance += amount;
        history.add("Deposited ₹" + amount);
        if (history.size() > 5) history.remove(0);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Invalid withdrawal amount!");
        if (amount > balance)
            throw new IllegalArgumentException("Insufficient balance!");
        balance -= amount;
        history.add("Withdrew ₹" + amount);
        if (history.size() > 5) history.remove(0);
    }

    @Override
    public double checkBalance() {
        return balance;
    }

    public String getUserName() {
        return user.getName();
    }

    public String getTransactionHistory() {
        if (history.isEmpty()) return "No transactions yet.";
        StringBuilder sb = new StringBuilder("Last Transactions for " + user.getName() + ":\n");
        for (String h : history) sb.append("- ").append(h).append("\n");
        return sb.toString();
    }

    @Override
    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}

// 🔹 Main ATM GUI Class
public class ATMSimulatorGUI extends JFrame implements ActionListener {

    // Store all users (PIN → ATM object)
    private HashMap<Integer, ATM> users = new HashMap<>();
    private ATM currentUser;

    private JTextField amountField;
    private JLabel balanceLabel, userLabel;
    private JButton depositButton, withdrawButton, checkBalanceButton, historyButton, logoutButton;

    // File name for saving data
    private static final String DATA_FILE = "users.dat";

    // 🔸 Constructor (loads data on startup)
    public ATMSimulatorGUI() {
        loadData(); // Load previous data if available

        // Default users if file is empty
        if (users.isEmpty()) {
            users.put(1234, new ATM(new User("Shivang Chauhan", 1234), 10000));
            users.put(5678, new ATM(new User("Utkarsh Sharma", 5678), 6000));
            users.put(5678, new ATM(new User("Tushar Singh", 2468), 8000));
            saveData();
        }

        showWelcomeScreen();
    }

    // 🔹 Save all user data to file
    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // 🔹 Load user data from file
    @SuppressWarnings("unchecked")
    private void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            users = (HashMap<Integer, ATM>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            users = new HashMap<>(); // if file not found or empty
        }
    }

    // 🔹 Welcome Screen
    private void showWelcomeScreen() {
        getContentPane().removeAll();
        setTitle("ATM System - Welcome");
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel title = new JLabel("🏦 Welcome to ATM Simulator", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Create New Account");

        loginButton.addActionListener(e -> loginScreen());
        registerButton.addActionListener(e -> registerScreen());

        add(title);
        add(loginButton);
        add(registerButton);

        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // 🔹 Login Screen
    private void loginScreen() {
        getContentPane().removeAll();
        setTitle("ATM Login");
        setLayout(new GridLayout(4, 1, 5, 5));

        JLabel label = new JLabel("Enter your 4-digit PIN:", SwingConstants.CENTER);
        JPasswordField pinField = new JPasswordField(10);
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        loginButton.addActionListener(e -> {
            try {
                int pin = Integer.parseInt(new String(pinField.getPassword()));
                if (users.containsKey(pin)) {
                    currentUser = users.get(pin);
                    mainMenu();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Invalid PIN! Try again.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Please enter numeric PIN only.");
            }
        });

        backButton.addActionListener(e -> showWelcomeScreen());

        add(label);
        add(pinField);
        add(loginButton);
        add(backButton);

        setSize(300, 220);
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    // 🔹 Registration Screen
    private void registerScreen() {
        getContentPane().removeAll();
        setTitle("Create New Account");
        setLayout(new GridLayout(6, 1, 5, 5));

        JLabel nameLabel = new JLabel("Enter Your Name:", SwingConstants.CENTER);
        JTextField nameField = new JTextField();

        JLabel pinLabel = new JLabel("Create a 4-digit PIN:", SwingConstants.CENTER);
        JPasswordField pinField = new JPasswordField(10);

        JLabel amountLabel = new JLabel("Enter Initial Deposit Amount:", SwingConstants.CENTER);
        JTextField amountField = new JTextField();

        JButton createButton = new JButton("Create Account");
        JButton backButton = new JButton("Back");

        createButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int pin = Integer.parseInt(new String(pinField.getPassword()));
                double initial = Double.parseDouble(amountField.getText().trim());

                if (name.isEmpty() || initial <= 0 || String.valueOf(pin).length() != 4) {
                    JOptionPane.showMessageDialog(this, "⚠️ Please enter valid details.");
                    return;
                }
                if (users.containsKey(pin)) {
                    JOptionPane.showMessageDialog(this, "⚠️ PIN already exists. Choose another.");
                    return;
                }

                User newUser = new User(name, pin);
                ATM newATM = new ATM(newUser, initial);
                users.put(pin, newATM);
                saveData(); // ✅ Save data after adding user

                JOptionPane.showMessageDialog(this, "✅ Account created successfully!");
                showWelcomeScreen();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Please enter valid numeric values.");
            }
        });

        backButton.addActionListener(e -> showWelcomeScreen());

        add(nameLabel);
        add(nameField);
        add(pinLabel);
        add(pinField);
        add(amountLabel);
        add(amountField);
        add(createButton);
        add(backButton);

        setSize(400, 350);
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    // 🔹 Main ATM Menu
    private void mainMenu() {
        getContentPane().removeAll();
        setTitle("ATM Simulator - " + currentUser.getUserName());
        setLayout(new GridLayout(8, 1, 10, 10));

        userLabel = new JLabel("Welcome, " + currentUser.getUserName() + "!", SwingConstants.CENTER);
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel = new JLabel("Your Current Balance: ₹" + currentUser.checkBalance(), SwingConstants.CENTER);
        amountField = new JTextField();

        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        checkBalanceButton = new JButton("Check Balance");
        historyButton = new JButton("Transaction History");
        logoutButton = new JButton("Logout");

        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);

        checkBalanceButton.addActionListener(e -> {
            balanceLabel.setText("Your Current Balance: ₹" + currentUser.checkBalance());
            currentUser.displayMessage("Your Current Balance: ₹" + currentUser.checkBalance());
        });

        historyButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, currentUser.getTransactionHistory(), "Transaction History", JOptionPane.INFORMATION_MESSAGE)
        );

        logoutButton.addActionListener(e -> {
            currentUser = null;
            showWelcomeScreen();
        });

        add(userLabel);
        add(balanceLabel);
        add(amountField);
        add(depositButton);
        add(withdrawButton);
        add(checkBalanceButton);
        add(historyButton);
        add(logoutButton);

        setSize(420, 400);
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    // 🔹 Handle Deposit and Withdraw actions
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (e.getSource() == depositButton) {
                currentUser.deposit(amount);
                currentUser.displayMessage("✅ Deposit Successful!");
            } else if (e.getSource() == withdrawButton) {
                currentUser.withdraw(amount);
                currentUser.displayMessage("✅ Withdrawal Successful!");
            }
            saveData(); // ✅ Save data after every transaction
            balanceLabel.setText("Your Current Balance: ₹" + currentUser.checkBalance());
            amountField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter a valid amount.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // 🔹 Main Method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ATMSimulatorGUI::new);
    }
}