import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Handler.Dto.LoginFailedException;
import Logic.BankingService;
import Logic.Dto.AdminLoginResult;
import Logic.Dto.LoginResult;
import Logic.Interface.IUserRepository;
import Logic.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankingServiceTest {

    private IUserRepository repoMock;
    private BankingService bankingService;

    @BeforeEach
    void setUp() {
        repoMock = mock(IUserRepository.class);
        bankingService = new BankingService(repoMock);
    }
//-------------------log in------------------------------
    @Test
    void testLogin_Success() throws LoginFailedException {
        int nationalId = 123456;
        String password = "mypassword";
        String name = "Alice";
        String lastname="azadeh";

        // Mocking repo to return a user
        UserData user = new UserData(nationalId,name,lastname,password);

        when(repoMock.getUserById(nationalId)).thenReturn(user);

        // Call login
        LoginResult result = bankingService.login(nationalId, password);

        // Assertions
        assertNotNull(result);
        assertEquals(nationalId, result.getNationalId());
        assertTrue(result.isSuccess());
        assertEquals(name, result.getName());
        assertNotNull(result.getAccessToken());
    }

    @Test
    void testLogin_Failure_InvalidPassword() {
        int nationalId = 123456;
        String correctPassword = "correct";
        String wrongPassword = "wrong";
        String lastname="azadeh";


        UserData user = new UserData(nationalId,"bob",lastname,correctPassword);

        when(repoMock.getUserById(nationalId)).thenReturn(user);

        // Should throw LoginFailedException
        assertThrows(LoginFailedException.class, () -> {
            bankingService.login(nationalId, wrongPassword);
        });
    }

    @Test
    void testLogin_Failure_UserNotFound() {
        int nationalId = 999999;
        String password = "password";

        when(repoMock.getUserById(nationalId)).thenReturn(null);

        assertThrows(LoginFailedException.class, () -> {
            bankingService.login(nationalId, password);
        });
    }
     //------------------------------sign_up-----------------------------------------------

    @Test
    void testRegister_Success() {
        int nationalId = 123456;
        String name = "Alice";
        String lastname = "Azadeh";
        String password = "mypassword";

        UserData newUser = new UserData(nationalId, name, lastname, password);

        // Simulate repo.addUser succeeds (returns 1)
        when(repoMock.addUser(newUser)).thenReturn(1);

        int result = bankingService.register(newUser);

        // Assertions
        assertEquals(1, result);
        verify(repoMock).addUser(newUser);
    }

    @Test
    void testRegister_Failure_UserAlreadyExists() {
        int nationalId = 123456;
        String name = "Bob";
        String lastname = "Azadeh";
        String password = "secret";

        UserData existingUser = new UserData(nationalId, name, lastname, password);

        // Simulate repo.addUser fails (returns 0)
        when(repoMock.addUser(existingUser)).thenReturn(0);

        int result = bankingService.register(existingUser);

        // Assertions
        assertEquals(0, result);
        verify(repoMock).addUser(existingUser);
    }
    //-------------------------admin login-------------------
    @Test
    void testAdminLogin_Success() throws LoginFailedException {
        int adminId = 12345;
        String password = "adminsystem";

        AdminLoginResult result = bankingService.Admin_login(adminId, password);

        assertNotNull(result);
        assertEquals(adminId, result.admin_id());
        assertTrue(result.isSuccess());
    }

    @Test
    void testAdminLogin_Failure_WrongPassword() {
        int adminId = 12345;
        String wrongPassword = "wrongpass";

        assertThrows(LoginFailedException.class, () -> {
            bankingService.Admin_login(adminId, wrongPassword);
        });
    }

    @Test
    void testAdminLogin_Failure_WrongId() {
        int wrongId = 11111;
        String password = "adminsystem";

        assertThrows(LoginFailedException.class, () -> {
            bankingService.Admin_login(wrongId, password);
        });
    }
}




