package Logic.Dto;

import com.google.gson.annotations.SerializedName;

public record AdminLogginResult(
        @SerializedName("national_id") int nationalId,
        boolean success,
        String name
) {
    public int getNationalId() {
        return nationalId;
    }

    public boolean isSuccess() {
        return success;
    }
    public String name() {
        return name;
    }
}


