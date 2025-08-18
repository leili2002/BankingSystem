package Logic.Interface;

import Repository.AccountModel;
import Repository.UserModel;

import java.util.List;

public interface IAccountRepository {
    int addAccount(AccountModel entity);

    int removeAccount(AccountModel entity);

    int withdraw(AccountModel entity, int amount);

    int deposit(AccountModel entity, int amount);

    int transfer(AccountModel entity_src, AccountModel entity_des, int amount);

    float checkBalance(AccountModel entity);

    List<AccountModel> printAccountById(UserModel entity);

    List<AccountModel> printAllAccounts();

    UserModel printUserData(UserModel entity);
}
