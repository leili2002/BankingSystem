package Logic;

import Logic.Dto.AdminLoginResult;
import Logic.Dto.LoginResult;
import Repository.Converter.UserConverter;
import Repository.UserRepository;
import Repository.*;

import java.util.List;


public class BankingService {
    UserConverter userConverter=new UserConverter();
    //private IUserRepository iUserRepository;
    UserRepository repo = new UserRepository();
    public BankingService() {
       // this.iUserRepository = iUserRepository;
    }

    public int register(UserData newUserData) {
       return repo.addUser(newUserData);
    }

    public  LoginResult login(int id, String password) {
        UserData userData = repo.getUserById(id);
        boolean success = userData != null && (userData.getPass() == null ? password == null : userData.getPass().equals(password));
        return new LoginResult(id, success);
    }

    public   AdminLoginResult Admin_login(int id, String password) {
        boolean success = (id == 12345) && "admin system".equals(password);
        return new AdminLoginResult(id, success);
    }

    public int removeUser(int id) {
        // Convert id → UserEntity
        UserModel entity = userConverter.idToModel(id);
        return repo.removeUser(entity);
    }

    public List<UserData> displayAllUsers() {
        List<UserModel> entities = repo.getAllUsers();
        // Convert each UserEntity → User (logic model)
        return entities.stream()
                .map(userConverter::toData)
                .toList();
    }


}