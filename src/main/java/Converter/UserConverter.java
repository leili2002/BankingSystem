// Converter/UserConverter.java
package Converter;

import Logic.User;
import Repository.UserEntity;
public class UserConverter {
    public static UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getPass()
        );
    }
    public static UserEntity idToEntity(int id) {
        return new UserEntity(id, null, null, null);
    }


    public static User toDomain(UserEntity entity) {
        return new User(
                entity.getRepon_national_id(),
                entity.get_Name(),
                entity.getLastname(),
                entity.getPassword()
        );
    }

}

