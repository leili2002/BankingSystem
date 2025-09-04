package Repository.Converter;

import Logic.AccountData;
import Repository.AccountModel;


public class AccountConverter {
    public AccountModel toModel(AccountData accountData) {
        return new AccountModel(
               accountData.getSerial(),     // generate a new UUID
                accountData.getId(),
                accountData.getName(),
                accountData.getLastName(),
                accountData.getType(),
                accountData.getBalance()
        );
    }



    public AccountData toData(AccountModel entity) {
        return new AccountData(
                entity.getUuid(),
                entity.getId(),
                entity.get_Name(),
                entity.getLastname(),
                entity.gettype(),
                entity.getbalance()   // match your method getName here
        );
    }

}
