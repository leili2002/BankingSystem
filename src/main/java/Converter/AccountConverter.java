package Converter;

import Logic.AccountData;
import Repository.AccountModel;

import java.util.UUID;


public class AccountConverter {
    public static AccountModel toEntity(AccountData accountData) {
        return new AccountModel(
                UUID.randomUUID(),     // generate a new UUID
                0,
                accountData.getName(),
                accountData.getLastName(),
                accountData.getType(),
                accountData.getBalance()
        );
    }


    public static AccountModel idToEntity(int id) {
        return new AccountModel(null, id, null, null, null, null);
    }


    public static AccountData toDomain(AccountModel entity) {
        return new AccountData(
                entity.getUuid(),
                entity.getId(),
                entity.get_Name(),
                entity.getLastname(),
                entity.gettype(),
                entity.getbalance()   // match your method name here
        );
    }

}
