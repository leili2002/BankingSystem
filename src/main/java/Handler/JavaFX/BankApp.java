package Handler.JavaFX;

import Handler.Dto.LoginFailedException;
import Logic.AccountData;
import Logic.AccountService;
import Logic.BankingService;
import Logic.Dto.LoginResult;
import Logic.Interface.IUserRepository;
import Logic.UserData;
import Repository.AccountRepository;
import Repository.UserRepository;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class BankApp extends Application {

    private final UserRepository userRepository = new UserRepository();
    private final AccountRepository accountRepository = new AccountRepository();
    private final IUserRepository iUserRepository = new UserRepository();
    private final BankingService bankingService = new BankingService(iUserRepository);
    private final AccountService accountService = new AccountService(accountRepository);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // ---------------- Splash screen ----------------
        Text welcomeText = new Text("Welcome to the Bank Site");
        welcomeText.setFont(Font.font(20));
        VBox splashLayout = new VBox(welcomeText);
        splashLayout.setPadding(new Insets(50));
        Scene splashScene = new Scene(splashLayout, 500, 300);

        primaryStage.setScene(splashScene);
        primaryStage.setTitle("Bank Application");
        primaryStage.show();

        // ---------------- Entrance screen ----------------
        VBox entranceLayout = new VBox(20);
        entranceLayout.setPadding(new Insets(50));
        Text entranceTitle = new Text("Please select your entrance");
        Button signUpButton = new Button("Sign Up");
        Button logInButton = new Button("Log In");
        entranceLayout.getChildren().addAll(entranceTitle, signUpButton, logInButton);
        Scene entranceScene = new Scene(entranceLayout, 500, 300);

        // Show entrance after 2 seconds
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            javafx.application.Platform.runLater(() -> primaryStage.setScene(entranceScene));
        }).start();

        // ---------------- Sign Up screen ----------------
        VBox signUpLayout = new VBox(15);
        signUpLayout.setPadding(new Insets(30));
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your getName");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter your last getName");
        TextField idFieldSignUp = new TextField();
        idFieldSignUp.setPromptText("Enter your national ID");
        PasswordField passwordFieldSignUp = new PasswordField();
        passwordFieldSignUp.setPromptText("Enter your password");
        Button submitSignUp = new Button("Register");
        Text resultSignUp = new Text();
        Button backToEntrance1 = new Button("Back");
        signUpLayout.getChildren().addAll(new Text("Sign Up"), nameField, lastNameField, idFieldSignUp,
                passwordFieldSignUp, submitSignUp, resultSignUp, backToEntrance1);
        Scene signUpScene = new Scene(signUpLayout, 500, 400);

        backToEntrance1.setOnAction(e -> primaryStage.setScene(entranceScene));

        submitSignUp.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idFieldSignUp.getText());
                String name = nameField.getText();
                String lastName = lastNameField.getText();
                String password = passwordFieldSignUp.getText();
                UserData newUser = new UserData(id, name, lastName, password);
                int result = this.bankingService.register(newUser);
                if (result == 1) resultSignUp.setText("Sign-up successful!");
                else resultSignUp.setText("Sign-up unsuccessful!");
            } catch (NumberFormatException ex) {
                resultSignUp.setText("ID must be a number!");
            }
        });

        // ---------------- Login screen ----------------
        VBox loginLayout = new VBox(15);
        loginLayout.setPadding(new Insets(30));
        TextField idFieldLogin = new TextField();
        idFieldLogin.setPromptText("Enter your national ID");
        PasswordField passwordFieldLogin = new PasswordField();
        passwordFieldLogin.setPromptText("Enter your password");
        Button submitLogin = new Button("Login");
        Text resultLogin = new Text();
        Button backToEntrance2 = new Button("Back");
        loginLayout.getChildren().addAll(new Text("Log In"), idFieldLogin, passwordFieldLogin, submitLogin, resultLogin, backToEntrance2);
        Scene loginScene = new Scene(loginLayout, 500, 300);

        backToEntrance2.setOnAction(e -> primaryStage.setScene(entranceScene));

        submitLogin.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idFieldLogin.getText());
                String password = passwordFieldLogin.getText();
                LoginResult loginResult = bankingService.login(id, password);
                if (loginResult.isSuccess()) {
                    primaryStage.setScene(createMainMenu(primaryStage, id));
                } else resultLogin.setText("Login failed.");
            } catch (NumberFormatException ex) {
                resultLogin.setText("ID must be a number!");
            } catch (LoginFailedException ex) {
                ex.printStackTrace();
            }
        });

        // ---------------- Button actions ----------------
        signUpButton.setOnAction(e -> primaryStage.setScene(signUpScene));
        logInButton.setOnAction(e -> primaryStage.setScene(loginScene));
    }

    // ---------------- Main Menu ----------------
    private Scene createMainMenu(Stage primaryStage, int id) {
        VBox menuLayout = new VBox(20);
        menuLayout.setPadding(new Insets(30));
        Label title = new Label("Main Menu");
        Button existingAccountsBtn = new Button("Existing Accounts");
        Button addAccountBtn = new Button("Add New Account");
        Button logoutBtn = new Button("Log Out");

        menuLayout.getChildren().addAll(title, existingAccountsBtn, addAccountBtn, logoutBtn);
        Scene menuScene = new Scene(menuLayout, 500, 400);

        existingAccountsBtn.setOnAction(e -> primaryStage.setScene(createExistingAccountsMenu(primaryStage, id)));
        addAccountBtn.setOnAction(e -> primaryStage.setScene(createAddAccountMenu(primaryStage, id)));
        logoutBtn.setOnAction(e -> start(primaryStage));

        return menuScene;
    }

    // ---------------- Existing Accounts ----------------
    private Scene createExistingAccountsMenu(Stage primaryStage, int userId) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        Label title = new Label("Your Accounts");
        List<AccountData> accounts = accountService.GetUserAccounts(userId);
        VBox accountList = new VBox(10);

        for (AccountData acc : accounts) {
            Button accBtn = new Button(acc.getId() + " - " + acc.getType() + " (Balance: " + acc.getBalance() + ")");
            accBtn.setOnAction(e -> primaryStage.setScene(createAccountOperationsMenu(primaryStage, acc)));
            accountList.getChildren().add(accBtn);
        }

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> primaryStage.setScene(createMainMenu(primaryStage, userId)));
        layout.getChildren().addAll(title, accountList, backBtn);
        return new Scene(layout, 500, 400);
    }

    // ---------------- Account Operations ----------------
    private Scene createAccountOperationsMenu(Stage primaryStage, AccountData account) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        Label title = new Label("Manage Account ID: " + account.getId());

        Button withdrawBtn = new Button("Withdraw");
        Button depositBtn = new Button("Deposit");
        Button transferBtn = new Button("transfer");
        Button balanceBtn = new Button("balance");
        Button backBtn = new Button("Back");
        Text resultText = new Text();

        withdrawBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Enter amount to withdraw:");
            dialog.showAndWait().ifPresent(amountStr -> {
                int amount = Integer.parseInt(amountStr);
                accountService.Withdraw(account.getId(), amount);
                resultText.setText("Withdrawal successful. New balance: " + account.getBalance());
            });
        });

        transferBtn.setOnAction(e -> {
            TextInputDialog amountDialog = new TextInputDialog();
            amountDialog.setHeaderText("Enter amount to transfer:");

            TextInputDialog destDialog = new TextInputDialog();
            destDialog.setHeaderText("Enter destination account ID:");

            amountDialog.showAndWait().ifPresent(amountStr -> {
                destDialog.showAndWait().ifPresent(destStr -> {
                    try {
                        int amount = Integer.parseInt(amountStr);
                        int destAccountId = Integer.parseInt(destStr);

                        int result = accountService.Transfer(account.getId(), destAccountId, amount);

                        if (result == 1) {
                            resultText.setText("✅ Transfer successful! " + amount +
                                    " sent to account ID " + destAccountId);
                        } else {
                            resultText.setText("❌ Transfer failed.");
                        }
                    } catch (NumberFormatException ex) {
                        resultText.setText("❌ Please enter valid numbers.");
                    }
                });
            });
        });

        transferBtn.setOnAction(e -> {
            TextInputDialog Amount = new TextInputDialog();
            Amount.setHeaderText("Enterid:");
            Amount.showAndWait().ifPresent(amountStr -> {
                accountService.CheckBalance(account.getId());
                resultText.setText("balance: " + account.getBalance());
            });
        });
        depositBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Enter amount to deposit:");
            dialog.showAndWait().ifPresent(amountStr -> {
                int amount = Integer.parseInt(amountStr);
                accountService.Deposit(account.getId(), amount);
                resultText.setText("Deposit successful. New balance: " + account.getBalance());
            });
        });

        backBtn.setOnAction(e -> primaryStage.setScene(createExistingAccountsMenu(primaryStage, account.getId())));

        layout.getChildren().addAll(title, withdrawBtn, depositBtn, resultText, backBtn);
        return new Scene(layout, 500, 400);
    }

    // ---------------- Add Account ----------------
    private Scene createAddAccountMenu(Stage primaryStage, int userId) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        TextField typeField = new TextField();
        typeField.setPromptText("Enter account type");
        typeField.setStyle("-fx-prompt-text-fill: black;");
        Button addBtn = new Button("Add Account");
        Text result = new Text();
        Button backBtn = new Button("Back");

        addBtn.setOnAction(e -> {
            String type = typeField.getText();
            if (!type.isEmpty()) {
                Exception creatAccountException = accountService.CreateUserAccount(userId, type);
                if (creatAccountException == null) {
                    result.setText("Account added successfully!");
                    typeField.clear();
                } else {
                    result.setText("Account added unsuccessfully!");
                }
            } else {
                result.setText("Please enter account type!");
            }
        });

        backBtn.setOnAction(e -> primaryStage.setScene(createMainMenu(primaryStage, userId)));

        layout.getChildren().addAll(new Text("Add New Account"), typeField, addBtn, result, backBtn);
        return new Scene(layout, 500, 400);
    }
}

