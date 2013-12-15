package domain;

import domain.Query;
import org.junit.Test;

import static domain.Query.emptyQuery;
import static domain.Query.newQuery;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class QueryTest {

    @Test
    public void query_for_keywords() {
        Query result = newQuery("keywords");

        assertThat(result.queryString().value, is(
           ".*keywords.*"
        ));
    }

    @Test
    public void empty_query() {
        Query result = emptyQuery();

        assertThat(result.queryString().value, is(
           ""
        ));
    }
}
