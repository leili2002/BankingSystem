// Converter/UserConverter.java
package Repository.Converter;

import Logic.UserData;
import Repository.UserModel;

public class UserConverter {
    public UserModel toModel(UserData userData) {
        return new UserModel(
                userData.getId(),
                userData.getName(),
                userData.getLastName(),
                userData.getPass()
        );
    }

    public UserModel idToModel(int id) {
        return new UserModel(id, null, null, null);
    }


    public UserData toData(UserModel model) {
        return new UserData(
                model.getRepon_national_id(),
                model.get_Name(),
                model.getLastname(),
                model.getPassword()
        );
    }

}

