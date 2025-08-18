package Logic;
import Converter.UserConverter;
import Repository.*;
import java.util.List;
import Logic.Dto.*;



public class BankingSystem {
    private final UserRepository userRepository = new UserRepository();

    public int register(User newUser) {
        // Convert User (logic) → UserEntity (repository)
        UserEntity entity = UserConverter.toEntity(newUser);
        return userRepository.addUser(entity);
    }

    public static LoginResult login(int id, String password) {
        User user = UserRepository.getUserById(id);
        boolean success = user != null && (user.getPass() == null ? password == null : user.getPass().equals(password));
        return new LoginResult(id, success);
    }

    public static AdminLoginResult Admin_login(int id, String password) {
        boolean success = (id == 12345) && "adminsystem".equals(password);
        return new AdminLoginResult(id, success);
    }

    public int removeUser(int id) {
        // Convert id → UserEntity
        UserEntity entity = UserConverter.idToEntity(id);
        return userRepository.removeUser(entity);
    }

    public List<User> displayAllUsers() {
        List<UserEntity> entities = userRepository.getAllUsers();
        // Convert each UserEntity → User (logic model)
        return entities.stream()
                .map(UserConverter::toDomain)
                .toList();
    }



}