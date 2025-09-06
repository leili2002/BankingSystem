package Logic;
import Logic.Interface.IAccountRepository;
import java.util.List;
import java.util.UUID;

public class AccountService {

    private final IAccountRepository iAccountRepository;


    public AccountService(IAccountRepository iAccountRepository) {
        this.iAccountRepository = iAccountRepository;
    }


    public Exception addAccount(AccountData accountData) {
        return iAccountRepository.addAccount(accountData);
    }

    public int removeAccount(int accountID) {
        return iAccountRepository.removeAccount(accountID);
    }

    public int Withdraw(int accountID, int amount) {
        return iAccountRepository.withdraw(accountID, amount);
    }

    public int Deposit(int accountID, int amount) {
        return iAccountRepository.deposit(accountID, amount);
    }

    public int Transfer(int sourceAccountID, int destinationAccountID, int amount) {
        return iAccountRepository.transfer(sourceAccountID, destinationAccountID, amount);
    }

    public float CheckBalance(int accountID) {
        return iAccountRepository.checkBalance(accountID);
    }

    public List<AccountData> GetUserAccounts(int userNationalID) {
        return iAccountRepository.getUserAccounts(userNationalID);

    }

    public List<AccountData> GetAllAccounts() {
        return iAccountRepository.getAllAccounts();
    }

   public Exception CreateUserAccount(int nationalId,String accountType) {
        UserData userData = iAccountRepository.getUserData(nationalId);
       int balance = 0;
       AccountData newAccount = new AccountData(
               // id (auto, so can be 0 or ignored if constructor handles it)
               userData.getName(),
               userData.getLastName(),
               accountType,
               balance
       );

       return this.addAccount(newAccount);
    }
}
