package internshipproject;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class Transaction {
    private double amount;
    private Date timestamp;

    public Transaction(double amount) {
        this.amount = amount;
        this.timestamp = new Date();
    }

    public double getAmount() {
        return amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}

class Account {
    private int accountNumber;
    private int pin;
    private double balance;
    private List<Transaction> transactionHistory;

    public Account(int accountNumber, int pin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public boolean validatePin(int enteredPin) {
        return pin == enteredPin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(new Transaction(amount));
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactionHistory.add(new Transaction(-amount));
            return true;
        } else {
            return false;
        }
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}

class User {
    private int userId;
    private int userPin;
    private Account account;

    public User(int userId, int userPin, Account account) {
        this.userId = userId;
        this.userPin = userPin;
        this.account = account;
    }

    public int getUserId() {
        return userId;
    }

    public int getUserPin() {
        return userPin;
    }

    public Account getAccount() {
        return account;
    }
}

public class ATM {
    private List<User> users;

    public ATM() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User login(int userId, int pin) {
        for (User user : users) {
            if (user.getUserId() == userId && user.getUserPin() == pin) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        Account account1 = new Account(12345, 1234, 10000.0);
        User user1 = new User(101, 1234, account1);
        atm.addUser(user1);
        
        Account account2 = new Account(96651, 1112, 20000.0);
        User user2 = new User(102, 1112, account2);
        atm.addUser(user2);
        
        Account account3 = new Account(81800, 9220, 50000.0);
        User user3 = new User(103, 9220, account3);
        atm.addUser(user3);
        
        Account account4 = new Account(82752, 2341 , 40000.0);
        User user4 = new User(104, 2341, account4);
        atm.addUser(user4);
        
        Account account5 = new Account(75593, 4567, 150000.0);
        User user5 = new User(105, 4567 , account5);
        atm.addUser(user5);

        Scanner scanner = new Scanner(System.in);
        User currentUser = null;

        while (true) {
            System.out.println("Welcome to the ATM! BANK OF BARODA");
            System.out.print("Enter your User ID: ");
            int userId = scanner.nextInt();
            System.out.print("Enter your PIN: ");
            int pin = scanner.nextInt();

            currentUser = atm.login(userId, pin);

            if (currentUser != null) {
                break;
            } else {
                System.out.println("Invalid User ID or PIN. Try again.");
            }
        }

        while (true) {
            System.out.println("ATM Menu:");
            System.out.println("1. View Balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Transaction History");
            System.out.println("6. Quit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Your balance is: $" + currentUser.getAccount().getBalance());
                    break;

                case 2:
                    System.out.print("Enter the amount to withdraw: $");
                    double withdrawAmount = scanner.nextDouble();
                    boolean success = currentUser.getAccount().withdraw(withdrawAmount);
                    if (success) {
                        System.out.println("Withdrawal successful.");
                    } else {
                        System.out.println("Insufficient funds.");
                    }
                    break;

                case 3:
                    System.out.print("Enter the amount to deposit: $");
                    double depositAmount = scanner.nextDouble();
                    currentUser.getAccount().deposit(depositAmount);
                    System.out.println("Deposit successful.");
                    break;

                case 4:
                    System.out.print("Enter the recipient's account number: ");
                    int recipientAccountNumber = scanner.nextInt();
                    User recipientUser = null;
                    for (User user : atm.users) {
                        if (user.getAccount().getAccountNumber() == recipientAccountNumber) {
                            recipientUser = user;
                            break;
                        }
                    }

                    if (recipientUser != null) {
                        System.out.print("Enter the amount to transfer: $");
                        double transferAmount = scanner.nextDouble();
                        if (currentUser.getAccount().withdraw(transferAmount)) {
                            recipientUser.getAccount().deposit(transferAmount);
                            System.out.println("Transfer successful.");
                        } else {
                            System.out.println("Insufficient funds for the transfer.");
                        }
                    } else {
                        System.out.println("Recipient account not found.");
                    }
                    break;

                case 5:
                    List<Transaction> transactions = currentUser.getAccount().getTransactionHistory();
                    System.out.println("Transaction History:");
                    for (Transaction transaction : transactions) {
                        System.out.println("Date: " + transaction.getTimestamp() + ", Amount: $" + transaction.getAmount());
                    }
                    break;

                case 6:
                    System.out.println("Thank you for using the ATM. Have a Good Day!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
}

