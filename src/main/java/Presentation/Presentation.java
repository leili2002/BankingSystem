package Presentation;

import Logic.Account;
import Logic.BankingSystem;
import Logic.Dto.AdminLoginResult;
import Logic.Dto.LoginResult;
import Logic.User;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;


public class Presentation {
    public static void presentation() {
        HomeMenue.home_menu();
        Entrance.enterence();
    }
}

class HomeMenue {
    public static void home_menu() {
        System.out.println("*****    your welcome to the bank site*   ****");
        System.out.println("         ______________________________       ");

    }
}

class Entrance {
    public static void enterence() {
        Scanner scaneer = new Scanner(System.in);
        System.out.println("1. Sign Up");
        System.out.println("2. Log In");
        System.out.println("3. admin");
        int Enternce_choise = scaneer.nextInt();

        switch (Enternce_choise) {
            case 1 -> {
                int result_sighnUp = SignUp.Sign_Up();
                if (result_sighnUp == 1) {
                    System.out.println("Sign-up successful");
                } else {
                    System.out.println("Sign-up unsuccessful");
                }
            }
            case 2 -> LogIn.log_in();

            case 3 -> Admin_LogIn.log_in();

            default -> System.out.println("Invalid choice.");
        }
    }
}

class SignUp {
    public static int Sign_Up() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your id");
        int id = scanner.nextInt();
        System.out.println("enter your name");
        String name = scanner.next();
        System.out.println("enter your last name");
        String last_name = scanner.next();
        System.out.println("enter your password");
        String password = scanner.next();
        User new_user = new User(id, name, last_name, password);
        BankingSystem bankSystem = new BankingSystem();
        Entrance.enterence();
        return bankSystem.register(new_user);

    }
}

class LogIn {
    public static void log_in() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your national_id");
        int id = scanner.nextInt();
        System.out.println("enter your password");
        String password = scanner.next();
        LoginResult loginResult = BankingSystem.login(id, password);
        if (loginResult.isSuccess()) {
            System.out.println("Login successful for user ID: " + loginResult.getNationalId());
            user_Menu.menue(loginResult.getNationalId());
        } else {
            System.out.println("Login failed.");
        }


    }
}

class Admin_LogIn {
    public static void log_in() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your id");
        int id = scanner.nextInt();
        System.out.println("enter your password");
        String password = scanner.next();
        AdminLoginResult adminLoginResult = BankingSystem.Admin_login(id, password);
        if (adminLoginResult.isSuccess()) {
            System.out.println("Admin Login successful ");
            Admin_menue.admin_menue();
        } else {
            System.out.println("Login failed.");
        }


    }
}


class user_Menu {
    public static void menue(int national_id) {
        User user = new User();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    Menu:
                    1. Existing accounts
                    2. Add new account
                    3. Log out""");

            int menue_choice = scanner.nextInt();

            switch (menue_choice) {
                case 1: {
                    List<Account> account_list = user.PrintAccountById(national_id);
                    for (Account acc : account_list) {
                        System.out.println("ID = " + acc.getid());
                        System.out.println("Name = " + acc.getName());
                        System.out.println("Last name = " + acc.getLastName());
                        System.out.println("Type = " + acc.getType());
                        System.out.println("Balance = " + acc.getbalance());
                        System.out.println("------------");
                    }
                    System.out.println("Enter account ID to manage:");
                    int account_id = scanner.nextInt();
                    while (true) {
                        System.out.println(
                                """
                                        1. Withdrawal
                                        2. Deposit
                                        3. Transfer
                                        4. Check balance
                                        5. Back""");

                        int account_choice = scanner.nextInt();

                        switch (account_choice) {
                            case 1 -> {
                                System.out.println("Enter the amount:");
                                int withdraw_amount = scanner.nextInt();
                                int withdraw_result = user.Withdraw(account_id, withdraw_amount);
                                switch (withdraw_result) {
                                    case 1 -> System.out.println("✅ Withdrawal successful");
                                    case 2 -> System.out.println("❌ Insufficient funds.");
                                    case 3 -> System.out.println("❌ Account not found.");
                                }
                            }
                            case 2 -> {
                                System.out.println("Enter the amount:");
                                int deposit_amount = scanner.nextInt();
                                int deposit_result = user.Deposit(account_id, deposit_amount);
                                switch (deposit_result) {
                                    case 1 -> System.out.println("✅ Deposit successful");
                                    case 2 -> System.out.println("❌ Account not found.");
                                }
                            }
                            case 3 -> {
                                System.out.println("Enter the amount and target account ID:");
                                int transfer_amount = scanner.nextInt();
                                int transfer_account = scanner.nextInt();
                                int transfer_result = user.Transfer(account_id, transfer_account, transfer_amount);
                                switch (transfer_result) {
                                    case 1 -> System.out.println("✅ Transfer to account ID " + transfer_account + " successful");
                                    case 2 -> System.out.println("❌ Transfer unsuccessful");
                                }
                            }
                            case 4 -> {
                                float balance_result = user.CheckBalance(account_id);
                                System.out.println("Balance = " + balance_result);
                            }
                            case 5 -> System.out.println("Returning to main menu...");
                        }

                        if (account_choice == 5) break;
                    }
                }

                case 2: {
                    System.out.println("Enter account type:");
                    String type = scanner.next();
                    String serial = UUID.randomUUID().toString();
                    float balance = 0;
                    Account new_account = new Account(
                            serial,
                            0, // auto-increment in DB
                            user.getName(),
                            user.getLastName(),
                            type,
                            balance
                    );
                    int result =user.addAccount(new_account);
                    if(result==1){
                    System.out.println("account added");}
                    else {
                    System.out.println("account didnt add");}
                    break;
                }
                case 3: {
                    System.out.println("Goodbye!");
                    return; // ✅ exit whole method
                }
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}

class Admin_menue {
    public static void admin_menue() {
        Scanner scanner = new Scanner(System.in);
        BankingSystem bankingSystem = new BankingSystem();
        while (true) {
            System.out.println(
                    """
                            1.display users
                            2.display accounts
                            3.remove user
                            4.Exit""");
            int Admin_result = scanner.nextInt();
            switch (Admin_result) {
                case 1:
                    List<User> UsersList = bankingSystem.displayAllUsers();
                    for (User users : UsersList) {
                        System.out.println(" name: " + users.getName() + "last name= " + users.getLastName() + "id= " + users.getId());
                    }
                case 2:

                    List<Account> accountList = User.PrintAllAccounts();

                    for (Account account : accountList) {
                        System.out.println(" name: " + account.getName() + "last name= " +
                                "" + account.getLastName() + "type= " + account.getType() + "balance= " + account.getbalance());
                    }
                case 3:
                    System.out.println("enter the user national id");
                    int national_id = scanner.nextInt();
                    int result = bankingSystem.removeUser(national_id);
                    if (result == 1) {
                        System.out.println("✅ User removed from database.");
                    } else {
                        System.out.println("⚠ User not found.");
                    }

                case 4:
                    System.out.println("Goodbye!");
                    return; //  exit whole method
            }
        }
    }
}
