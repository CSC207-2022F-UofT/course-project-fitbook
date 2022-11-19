package usecase.profile;

import jakarta.validation.constraints.NotEmpty;

public class UserProfileRequestModel {
    @NotEmpty
    private final String id;

    public UserProfileRequestModel(String id) {
        this.id = id;
    }
    public String getId(){
        return id;
    }
}
