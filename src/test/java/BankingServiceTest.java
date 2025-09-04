import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Handler.Dto.LoginFailedException;
import Logic.BankingService;
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
}
