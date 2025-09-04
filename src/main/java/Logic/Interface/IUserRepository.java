package Logic.Interface;

import Logic.UserData;
import Repository.UserModel;

import java.util.List;

public interface IUserRepository {
    /**
     * Adds a new user to the database.
     * @param userData User data object containing ID, getName, lastname, and password.
     * @return 1 if successful, 0 otherwise.
     */
    int addUser(UserData userData);

    /**
     * Removes a user from the database.
     * @param model User model containing the national ID to delete.
     * @return 1 if successful, 0 otherwise.
     */
    int removeUser(UserModel model);

    /**
     * Retrieves a user by their national ID.
     * @param national_id The user's national ID.
     * @return A UserData object if found, otherwise null.
     */
    UserData getUserById(int national_id);

    /**
     * Retrieves all users from the database.
     * @return A list of UserModel objects.
     */
    List<UserModel> getAllUsers();
}
