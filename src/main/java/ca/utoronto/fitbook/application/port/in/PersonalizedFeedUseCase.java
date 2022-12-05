package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.application.port.in.command.PersonalizedFeedCommand;
import ca.utoronto.fitbook.application.port.out.response.PersonalizedFeedResponse;

public interface PersonalizedFeedUseCase
{
    PersonalizedFeedResponse getFeed(PersonalizedFeedCommand command);
}
