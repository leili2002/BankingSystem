package Logic;

import Converter.UserConverter;
import Logic.Interface.IAccountRepository;
import Logic.Interface.IUserRepository;
import Repository.*;

import java.util.List;

import Logic.Dto.*;


public class BankingService {
    private IUserRepository iUserRepository;

    public BankingService(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    public int register(UserData newUserData) {
        // Convert User (logic) → UserEntity (repository)
        UserModel entity = UserConverter.toModel(newUserData);
        return iUserRepository.addUser(entity);
    }

    public LoginResult login(int id, String password) {
        UserData userData = iUserRepository.getUserById(id);
        boolean success = userData != null && (userData.getPass() == null ? password == null : userData.getPass().equals(password));
        return new LoginResult(id, success);
    }

    public static AdminLoginResult Admin_login(int id, String password) {
        boolean success = (id == 12345) && "adminsystem".equals(password);
        return new AdminLoginResult(id, success);
    }

    public int removeUser(int id) {
        // Convert id → UserEntity
        UserModel entity = UserConverter.idToEntity(id);
        return iUserRepository.removeUser(entity);
    }

    public List<UserData> displayAllUsers() {
        List<UserModel> entities = iUserRepository.getAllUsers();
        // Convert each UserEntity → User (logic model)
        return entities.stream()
                .map(UserConverter::toEntity)
                .toList();
    }


}