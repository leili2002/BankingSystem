package Handler.Terminal;

import Logic.AccountData;
import Logic.BankingService;
import Logic.Dto.AdminLoginResult;
import Logic.Dto.LoginFailedException;
import Logic.UserData;
import Logic.AccountService;
import java.util.List;
import java.util.Scanner;
import Logic.Dto.AdminLogginResult;


public class Presentation {
    private final AccountService accountService;
    private final BankingService bankingService;


    public Presentation(AccountService accountService, BankingService bankingService) {
        this.accountService = accountService;
        this.bankingService = bankingService;
    }


    public void presentation() throws LoginFailedException {
        home_menu();
        enterence();
    }


    public void home_menu() {
        System.out.println("*****    your welcome to the bank site*   ****");
        System.out.println("         ______________________________       ");

    }


    public void enterence() throws LoginFailedException {
        Scanner scaneer = new Scanner(System.in);
        System.out.println("1. Sign Up");
        System.out.println("2. Log In");
        System.out.println("3. admin");
        int Enternce_choise = scaneer.nextInt();

        switch (Enternce_choise) {
            case 1 -> {
                int result_sighnUp = Sign_Up();
                if (result_sighnUp == 1) {
                    System.out.println("Sign-up successful");
                } else {
                    System.out.println("Sign-up unsuccessful");
                }
            }
            case 2 -> log_in();

            case 3 -> admin_log_in();

            default -> System.out.println("Invalid choice.");
        }
    }


    public int Sign_Up() throws LoginFailedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your id");
        int id = scanner.nextInt();
        System.out.println("enter your name");
        String name = scanner.next();
        System.out.println("enter your last name");
        String last_name = scanner.next();
        System.out.println("enter your password");
        String password = scanner.next();
        UserData new_userData = new UserData(id, name, last_name, password);
        enterence();
        return bankingService.register(new_userData);

    }


    public String log_in() throws LoginFailedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your national_id");
        int id = scanner.nextInt();
        System.out.println("enter your password");
        String password = scanner.next();

        AdminLogginResult loginResult = bankingService.login(id, password);
        if (loginResult.isSuccess()) {
          return   "Login successful for userModel ID: " + loginResult.getNationalId();
           //menue(loginResult.getNationalId());
        } else {
          return "Login failed.";
        }


    }

    public String admin_log_in() throws LoginFailedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your id");
        int id = scanner.nextInt();
        System.out.println("enter your password");
        String password = scanner.next();
        BankingService bank=new BankingService();
        AdminLoginResult adminLoginResult = bank.Admin_login(id, password);
        if (adminLoginResult.isSuccess()) {
             return "Admin Login successful ";
          //  admin_menue();
        } else {
           return"Login failed.";
        }


    }


    public void menue(int national_id) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    Menu:
                    1. Existing accounts
                    2. Add new accountData
                    3. Log out""");

            int menue_choice = scanner.nextInt();

            switch (menue_choice) {
                case 1: {
                    List<AccountData> account_Data_Entity_list = accountService.GetUserAccounts(national_id);
                    for (AccountData acc : account_Data_Entity_list) {
                        System.out.println("ID = " + acc.getId());
                        System.out.println("Name = " + acc.getName());
                        System.out.println("Last name = " + acc.getLastName());
                        System.out.println("Type = " + acc.getType());
                        System.out.println("Balance = " + acc.getBalance());
                        System.out.println("------------");
                    }
                    System.out.println("Enter accountData ID to manage:");
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
                                int withdraw_result = accountService.Withdraw(account_id, withdraw_amount);
                                switch (withdraw_result) {
                                    case 1 -> System.out.println("✅ Withdrawal successful");
                                    case 2 -> System.out.println("❌ Insufficient funds.");
                                    case 3 -> System.out.println("❌ Account not found.");
                                }
                            }
                            case 2 -> {
                                System.out.println("Enter the amount:");
                                int deposit_amount = scanner.nextInt();
                                int deposit_result = accountService.Deposit(account_id, deposit_amount);
                                switch (deposit_result) {
                                    case 1 -> System.out.println("✅ Deposit successful");
                                    case 2 -> System.out.println("❌ Account not found.");
                                }
                            }
                            case 3 -> {
                                System.out.println("Enter the amount and target accountData ID:");
                                int transfer_amount = scanner.nextInt();
                                int transfer_account = scanner.nextInt();
                                int transfer_result = accountService.Transfer(account_id, transfer_account, transfer_amount);
                                switch (transfer_result) {
                                    case 1 -> System.out.println("✅ Transfer to accountData ID " + transfer_account + " successful");
                                    case 2 -> System.out.println("❌ Transfer unsuccessful");
                                }
                            }
                            case 4 -> {
                                float balance_result = accountService.CheckBalance(account_id);
                                System.out.println("Balance = " + balance_result);
                            }
                            case 5 -> System.out.println("Returning to main menu...");
                        }

                        if (account_choice == 5) break;
                    }
                }

//                case 2: {
//                    System.out.println("Enter accountData type:");
//                    String type = scanner.next();
//                    String serial = UUID.randomUUID().toString();
//                    float balance = 0;
//                    AccountData new_accountData = new AccountData(
//                            serial,
//                            0, // auto-increment in DB
//                            userEntity.getName(),
//                            userEntity.getLastName(),
//                            type,
//                            balance
//                    );
//                    int result = accountService.addAccount(new_accountData);
//                    if (result == 1) {
//                        System.out.println("accountData added");
//                    } else {
//                        System.out.println("accountData didnt add");
//                    }
//                    break;
//                }
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


    public void admin_menue() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(
                    """
                            1.display users
                            2.display accounts
                            3.remove userModel
                            4.Exit""");
            int Admin_result = scanner.nextInt();
            switch (Admin_result) {
                case 1:
                    List<UserData> usersList = bankingService.displayAllUsers();
                    for (UserData users : usersList) {
                        System.out.println(" name: " + users.getName() + "last name= " + users.getLastName() + "id= " + users.getNational_id());
                    }
                case 2:

                    List<AccountData> accountDataList = accountService.GetAllAccounts();

                    for (AccountData accountData : accountDataList) {
                        System.out.println(" name: " + accountData.getName() + "last name= " +
                                "" + accountData.getLastName() + "type= " + accountData.getType() + "balance= " + accountData.getBalance());
                    }
                case 3:
                    System.out.println("enter the userModel national id");
                    int national_id = scanner.nextInt();
                    int result = bankingService.removeUser(national_id);
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


