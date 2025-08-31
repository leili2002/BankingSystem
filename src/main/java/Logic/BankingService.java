package Logic;

import Logic.Dto.AdminLoginResult;
import Logic.Dto.LoginFailedException;
import Logic.Dto.AdminLogginResult;
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

    public AdminLogginResult login(int national_id, String password)throws LoginFailedException {
        UserData userData = repo.getUserById(national_id);
        boolean success = userData != null && (userData.getPass() == null ? password == null : userData.getPass().equals(password));
        if (!success) {
            throw new LoginFailedException("Invalid national ID or password");
        }
        String name=userData.getName();
        return new AdminLogginResult(national_id, true,name);
    }

    public   AdminLoginResult Admin_login(int id, String password)  throws LoginFailedException{
        boolean success = (id == 12345) && "adminsystem".equals(password);
        if (!success) {
            throw new LoginFailedException("Invalid national ID or password");
        }
        return new AdminLoginResult(id, true);
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