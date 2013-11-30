package data.factory.person;

import data.dto.PersonDTO;
import data.mapper.MutableTeam;
import data.mapper.PersonMapper;
import data.mapper.TeamMapper;
import domain.Person;
import domain.Team;

import java.util.ArrayList;
import java.util.List;

import static data.factory.person.DefaultMutablePerson.newMutablePersonInTeam;

public class PersonFactory {

    private final PersonMapper personMapper;
    private final TeamMapper teamMapper;
    private final LazyMutableTeamFactory lazyMutableTeamFactory;

    public PersonFactory(PersonMapper personMapper, TeamMapper teamMapper, LazyMutableTeamFactory lazyMutableTeamFactory) {
        this.personMapper = personMapper;
        this.teamMapper = teamMapper;
        this.lazyMutableTeamFactory = lazyMutableTeamFactory;
    }

    public List<Person> createPersons(List<PersonDTO> personDTOs) {
        List<Person> persons = new ArrayList<>();

        for (PersonDTO dto : personDTOs) {
            persons.add(createPerson(dto));
        }
        return persons;
    }

    private Person createPerson(PersonDTO dto) {
        MutableTeam mutableTeam = lazyMutableTeamFactory.createLazyMutableTeam();
        Team lazyTeam = teamMapper.map(mutableTeam, dto.teamNode);

        DefaultMutablePerson defaultMutablePerson = newMutablePersonInTeam(lazyTeam);

        return personMapper.map(defaultMutablePerson, dto.personNode);
    }
}
