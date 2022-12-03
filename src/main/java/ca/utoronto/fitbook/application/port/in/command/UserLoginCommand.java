package ca.utoronto.fitbook.application.port.in.command;

import lombok.NonNull;
import lombok.Value;

@Value
public class UserLoginCommand
{
    @NonNull
    String name;
    @NonNull
    String password;
}
