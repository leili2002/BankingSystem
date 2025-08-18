// Converter/UserConverter.java
package Converter;

import Logic.UserData;
import Repository.UserModel;

public class UserConverter {
    public static UserModel toModel(UserData userData) {
        return new UserModel(
                userData.getId(),
                userData.getName(),
                userData.getLastName(),
                userData.getPass()
        );
    }
    public static UserModel idToEntity(int id) {
        return new UserModel(id, null, null, null);
    }


    public static UserData toEntity(UserModel model) {
        return new UserData(
                model.getRepon_national_id(),
                model.get_Name(),
                model.getLastname(),
                model.getPassword()
        );
    }

}

