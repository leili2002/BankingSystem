package Logic;
import Converter.AccountConverter;
import Converter.UserConverter;
import Repository.AccountEntity;
import Repository.AccountRepository;
import Repository.UserEntity;
import com.google.protobuf.Internal;

import java.util.List;


public class User {
    private int id;
    private String name;
    private String lastname;
    private String password;


    public User(int id, String name, String lastname, String password) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
    }

    public User() {
    }


    static AccountRepository AccountRepository = new AccountRepository();

    public int getId() {
        return id;
    }

    public String getPass() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastname;
    }


    public int addAccount(Account account) {

        AccountEntity entity=AccountConverter.toEntity(account);
       return AccountRepository.addaccount(entity);
    }

    public int removeAccount(int id_account) {
        AccountEntity entity=AccountConverter.idToEntity(id_account);
        return AccountRepository.removeAccount(entity);
    }

    public int Withdraw(int id_account, int amount) {
        AccountEntity entity=AccountConverter.idToEntity(id_account);
        return AccountRepository.Withdraw(entity,amount);
    }

    public int Deposit(int id_account, int amount) {
        AccountEntity entity=AccountConverter.idToEntity(id_account);
        return AccountRepository.Deposit(entity, amount);
    }

    public int Transfer(int Src_Id, int Des_Id, int amount) {
        AccountEntity entity_src=AccountConverter.idToEntity(Src_Id);
        AccountEntity entity_des=AccountConverter.idToEntity(Des_Id);
        return AccountRepository.Transfer(entity_src, entity_des, amount);
    }

    public float CheckBalance(int id_account) {
        AccountEntity entity=AccountConverter.idToEntity(id_account);
        return AccountRepository.CheckBalance(entity);
    }

    public List<Account> PrintAccountById(int national_id) {
        UserEntity entity= UserConverter.idToEntity(national_id);
   List<AccountEntity> entities =AccountRepository.PrintAccountById(entity);
        return entities.stream()
                .map(AccountConverter::toDomain)
                .toList();
    }

    public static List<Account> PrintAllAccounts() {
        List<AccountEntity> entities= AccountRepository.PrintAllAccounts();
        return entities.stream()
                .map(AccountConverter::toDomain)
                .toList();
    }

    User PrintUsertData(int national_id) {
        UserEntity entity= UserConverter.idToEntity(national_id);
        UserEntity user= AccountRepository.PrintUserData(entity);
        return UserConverter.toDomain(user);

    }
}