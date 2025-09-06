package Logic.Dto;

import com.google.gson.annotations.SerializedName;
// dto = data transfer object
public record LoginResult(
        @SerializedName("national_id") int nationalId,
        boolean success,
        String getName,
        String accessToken
) {
    public int getNationalId() {
        return nationalId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getName() {
        return getName;
    }

    public String getAccessToken() {
        return accessToken;
    }
}


