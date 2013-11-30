package data.factory;

import data.dao.FacewallDAO;
import data.mapper.MutablePerson;
import data.mapper.MutableTeam;
import data.mapper.PersonMapper;
import domain.Person;
import org.neo4j.graphdb.Node;

import java.util.List;

import static data.factory.DefaultMutablePerson.newMutablePersonInTeam;

public class LazyMutableTeamFactory {
    private final FacewallDAO dao;
    private final PersonMapper personMapper;

    public LazyMutableTeamFactory(FacewallDAO dao, PersonMapper personMapper) {
        this.dao = dao;
        this.personMapper = personMapper;
    }

    public MutableTeam createLazyMutableTeam() {
        return new LazyMutableTeam();
    }

    private class LazyMutableTeam extends MutableTeam {

        MembersFactory membersFactory = new MembersFactory(personMapper, new DefaultMutablePersonFactory());

        @Override public List<Person> members() {
            List<Node> memberNodes = dao.fetchTeamMembers(id);

            return membersFactory.createMembers(memberNodes);
        }

        private class DefaultMutablePersonFactory implements MutablePersonFactory {
            @Override public MutablePerson createMutablePerson() {
                return newMutablePersonInTeam(LazyMutableTeam.this);
            }
        }
    }
}