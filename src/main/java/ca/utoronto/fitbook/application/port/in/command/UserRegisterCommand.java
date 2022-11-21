package ca.utoronto.fitbook.application.port.in.command;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class UserRegisterCommand
{
    @NonNull
    String name;
    @NonNull
    String password;
    @NonNull
    String repeatedPassword;
}
