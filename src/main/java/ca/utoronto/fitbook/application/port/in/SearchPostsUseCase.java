package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.application.port.in.command.SearchCommand;
import ca.utoronto.fitbook.application.port.out.response.SearchResponse;

public interface SearchPostsUseCase {

    SearchResponse search(SearchCommand searchCommand);

}
