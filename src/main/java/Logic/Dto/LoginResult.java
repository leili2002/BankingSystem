package Logic.Dto;

public record LoginResult(int nationalId, boolean success) {

    public int getNationalId() {
        return nationalId;
    }

    public boolean isSuccess() {
        return success;
    }
}