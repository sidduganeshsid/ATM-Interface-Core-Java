import java.util.ArrayList;
import java.util.Scanner;

class AccountHolder {
    private String userId;
    private String userPin;

    public AccountHolder(String userId, String userPin) {
        this.userId = userId;
        this.userPin = userPin;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPin() {
        return userPin;
    }
}

class Account {
    private String accountId;
    private double balance;
    private ArrayList<String> transactionHistory;

    public Account(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposit: +" + amount);
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactionHistory.add("Withdraw: -" + amount);
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public void transfer(Account recipient, double amount) {
        if (balance >= amount) {
            balance -= amount;
            recipient.deposit(amount);
            transactionHistory.add("Transfer: -" + amount + " to " + recipient.getAccountId());
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public void showTransactionHistory() {
        System.out.println("Transaction History for Account: " + accountId);
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
        System.out.println();
    }
}

class Bank {
    private ArrayList<Account> accounts;

    public Bank() {
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public Account getAccount(String accountId) {
        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }
}

class ATM {
    private AccountHolder accountHolder;
    private Bank bank;
    private Scanner scanner;

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Welcome to the ATM!");
        System.out.print("User ID: ");
        String userId = scanner.nextLine();
        System.out.print("PIN: ");
        String userPin = scanner.nextLine();

        accountHolder = authenticateUser(userId, userPin);

        if (accountHolder != null) {
            System.out.println("Authentication successful.");
            showMenu();
        } else {
            System.out.println("Authentication failed. Exiting...");
        }
    }

    private AccountHolder authenticateUser(String userId, String userPin) {
       //authentication
        if (userId.equals("user123") && userPin.equals("1234")) {
            return new AccountHolder(userId, userPin);
        } else {
            return null;
        }
    }

    private void showBalance() {
        Account account = getAccountFromInput();
        if (account != null) {
            double balance = account.getBalance();
            System.out.println("Current Balance: " + balance);
        }
    }

    private void showTransactionHistory() {
        Account account = getAccountFromInput();
        if (account != null) {
            account.showTransactionHistory();
        }
    }

    private void performWithdrawal() {
        Account account = getAccountFromInput();
        if (account != null) {
            System.out.print("Enter amount to withdraw: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); 
            account.withdraw(amount);
        }
    }

    private void performDeposit() {
        Account account = getAccountFromInput();
        if (account != null) {
            System.out.print("Enter amount to deposit: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); 
            account.deposit(amount);
        }
    }

    private void performTransfer() {
        Account sourceAccount = getAccountFromInput("Enter source account ID: ");
        if (sourceAccount != null) {
            Account recipientAccount = getAccountFromInput("Enter recipient account ID: ");
            if (recipientAccount != null) {
                System.out.print("Enter amount to transfer: ");
                double amount = scanner.nextDouble();
                scanner.nextLine(); 
                sourceAccount.transfer(recipientAccount, amount);
            }
        }
    }

    private void showMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== ATM Menu ===");
            System.out.println("1. Show Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Show Balance");
            System.out.println("6. Quit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:showTransactionHistory();
                        break;
                case 2:performWithdrawal();
                         break;
                case 3:performDeposit();
                        break;
                case 4:performTransfer();
                        break;
                case 5:showBalance();
                        break;
                case 6:exit = true;
                        break;
                default:System.out.println("Invalid choice. Try again.");
                        break;
            }
        }
    }

    private Account getAccountFromInput() {
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        return bank.getAccount(accountId);
    }

    private Account getAccountFromInput(String message) {
        System.out.print(message);
        String accountId = scanner.nextLine();
        return bank.getAccount(accountId);
    }
}

class ATMmain {
    public static void main(String[] args) {
        // Creating the Bank
        Bank bank = new Bank();

        // Creating some accounts
        Account account1 = new Account("AC001", 15000);
        Account account2 = new Account("AC002", 10000);
        Account account3 = new Account("AC003", 5000);

        // Adding the accounts to the bank
        bank.addAccount(account1);
        bank.addAccount(account2);
        bank.addAccount(account3);

        // Starting the ATM
        ATM atm = new ATM(bank);
        atm.run();
    }
}
