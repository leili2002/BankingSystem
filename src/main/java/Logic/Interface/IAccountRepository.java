package Logic.Interface;

import Logic.AccountData;
import Logic.UserData;

import java.util.List;

public interface IAccountRepository {
    Exception addAccount(AccountData accountData);

    int removeAccount(int accountID);

    int withdraw(int accountID, int amount);

    int deposit(int accountID, int amount);

    int transfer(int sourceAccount, int destinationAccountID, int amount);

    float checkBalance(int accountID);

    List<AccountData> getUserAccounts(int userNationalID);

    List<AccountData> getAllAccounts();

    UserData getUserData(int nationalID);
}
