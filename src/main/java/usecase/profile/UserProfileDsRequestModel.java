package usecase.profile;

import jakarta.validation.constraints.NotEmpty;

public class UserProfileDsRequestModel {
    @NotEmpty
    private final String id;

    public UserProfileDsRequestModel(String id) {
        this.id = id;
    }
    public String getId(){
        return id;
    }
}
