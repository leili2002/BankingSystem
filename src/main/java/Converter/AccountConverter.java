package Converter;
import Repository.AccountEntity;
import Logic.Account;
import Repository.UserEntity;

import java.util.UUID;


public class AccountConverter {
        public static AccountEntity toEntity(Account account) {
            return new AccountEntity(
                    UUID.randomUUID(),     // generate a new UUID
                    0,
                    account.getName(),
                    account.getLastName(),
                    account.getType(),
                    account.getbalance()
            );
    }


    public static AccountEntity idToEntity(int id) {
        return new AccountEntity(null, id, null, null,null,null);
    }





    public static Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getUuid(),
                entity.getId(),
                entity.get_Name(),
                entity.getLastname(),
                entity.gettype(),
                entity.getbalance()   // match your method name here
        );
    }

}
