package Logic.Dto;

public record AdminLoginResult(int admin_id, boolean success) {


    public boolean isSuccess() {
        return success;
    }
}