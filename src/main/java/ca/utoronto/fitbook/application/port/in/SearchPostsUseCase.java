package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.application.port.in.command.SearchCommand;
import ca.utoronto.fitbook.application.port.out.response.PostResponse;
import ca.utoronto.fitbook.application.port.out.response.SearchResponse;

import java.util.List;

public interface SearchPostsUseCase
{
    SearchResponse search(SearchCommand command);
}
