package Logic;

import Converter.AccountConverter;
import Converter.UserConverter;
import Logic.Interface.IAccountRepository;
import Repository.AccountModel;
import Repository.UserModel;

import java.util.List;

public class AccountService {


    private IAccountRepository iAccountRepository;


    public AccountService(IAccountRepository iAccountRepository) {
        this.iAccountRepository = iAccountRepository;
    }


    public int addAccount(AccountData accountData) {

        AccountModel entity = AccountConverter.toEntity(accountData);
        return iAccountRepository.addAccount(entity);
    }

    public int removeAccount(int id_account) {
        AccountModel entity = AccountConverter.idToEntity(id_account);
        return iAccountRepository.removeAccount(entity);
    }

    public int Withdraw(int id_account, int amount) {
        AccountModel entity = AccountConverter.idToEntity(id_account);
        return iAccountRepository.withdraw(entity, amount);
    }

    public int Deposit(int id_account, int amount) {
        AccountModel entity = AccountConverter.idToEntity(id_account);
        return iAccountRepository.deposit(entity, amount);
    }

    public int Transfer(int Src_Id, int Des_Id, int amount) {
        AccountModel entity_src = AccountConverter.idToEntity(Src_Id);
        AccountModel entity_des = AccountConverter.idToEntity(Des_Id);
        return iAccountRepository.transfer(entity_src, entity_des, amount);
    }

    public float CheckBalance(int id_account) {
        AccountModel entity = AccountConverter.idToEntity(id_account);
        return iAccountRepository.checkBalance(entity);
    }

    public List<AccountData> PrintAccountById(int national_id) {
        UserModel entity = UserConverter.idToEntity(national_id);
        List<AccountModel> entities = iAccountRepository.printAccountById(entity);
        return entities.stream()
                .map(AccountConverter::toDomain)
                .toList();
    }

    public List<AccountData> PrintAllAccounts() {
        List<AccountModel> entities = iAccountRepository.printAllAccounts();
        return entities.stream()
                .map(AccountConverter::toDomain)
                .toList();
    }

    UserData PrintUsertData(int national_id) {
        UserModel entity = UserConverter.idToEntity(national_id);
        UserModel userModel = iAccountRepository.printUserData(entity);
        return UserConverter.toEntity(userModel);

    }
}
