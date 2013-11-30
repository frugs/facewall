package data.factory.team;

import data.dao.FacewallDAO;
import data.datatype.PersonId;
import data.factory.LazyMutablePersonFactory;
import data.factory.LazyMutableTeamFactory;
import data.mapper.MutablePerson;
import data.mapper.MutableTeam;
import data.mapper.TeamMapper;
import domain.Team;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.Node;

import static data.datatype.PersonId.newPersonId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LazyMutablePersonFactoryTest {

    @Mock FacewallDAO mockDAO;
    @Mock LazyMutableTeamFactory mockLazyMutableTeamFactory;
    @Mock TeamMapper mockTeamMapper;
    private LazyMutablePersonFactory lazyMutablePersonFactory;

    @Before
    public void setUp() throws Exception {
        lazyMutablePersonFactory = new LazyMutablePersonFactory(mockDAO, mockLazyMutableTeamFactory, mockTeamMapper);
    }

    @Test
    public void lazy_mutable_persons_team_delegates_to_teamMapper() {
        Team expectedTeam = mock(Team.class);
        when(mockTeamMapper.map(any(MutableTeam.class), any(Node.class)))
            .thenReturn(expectedTeam);

        Team result = lazyMutablePersonFactory.createMutablePerson().team();

        assertThat(result, is(sameInstance(expectedTeam)));
    }

    @Test
    public void lazy_mutable_persons_team_fetches_teamNode_using_dao() {
        PersonId expectedPersonId = newPersonId("expected person id");

        Node expectedTeamNode = mock(Node.class);
        when(mockDAO.fetchTeamForPerson(any(PersonId.class)))
            .thenReturn(expectedTeamNode);

        MutablePerson person = lazyMutablePersonFactory.createMutablePerson();
        person.setId(expectedPersonId);

        person.team();

        verify(mockDAO).fetchTeamForPerson(expectedPersonId);
        verify(mockTeamMapper).map(any(MutableTeam.class), eq(expectedTeamNode));
    }

    @Test
    public void lazy_mutable_persons_team_creates_mutable_team_using_factory() {
        MutableTeam expectedMutableTeam = mock(MutableTeam.class);
        when(mockLazyMutableTeamFactory.createLazyMutableTeam())
            .thenReturn(expectedMutableTeam);

        lazyMutablePersonFactory.createMutablePerson().team();

        verify(mockLazyMutableTeamFactory).createLazyMutableTeam();
        verify(mockTeamMapper).map(eq(expectedMutableTeam), any(Node.class));
    }
}