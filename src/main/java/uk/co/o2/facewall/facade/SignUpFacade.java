package uk.co.o2.facewall.facade;

import uk.co.o2.facewall.data.PersonRepository;
import uk.co.o2.facewall.data.TeamRepository;
import uk.co.o2.facewall.data.dao.AdminDAO;
import uk.co.o2.facewall.data.dto.PersonInformation;
import uk.co.o2.facewall.domain.Person;
import uk.co.o2.facewall.domain.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

public class SignUpFacade {

    private final AdminDAO adminDAO;
    private final PersonRepository personRepository;
    private final TeamRepository teamRepository;


    public SignUpFacade(AdminDAO adminDAO, PersonRepository personRepository, TeamRepository teamRepository) {
        this.adminDAO = adminDAO;
        this.personRepository = personRepository;
        this.teamRepository = teamRepository;
    }

    public void registerPerson(PersonInformation personInformation, Team team) {
        adminDAO.savePersonInformation(personInformation);

        Person person = personRepository.findPersonById(personInformation.getId());
        team.addMember(person);
    }

    public List<Team> getSortedAvailableTeams() {
        List<Team> availableTeams = getAvailableTeams();
        Collections.sort(availableTeams, new Comparator<Team>() {
            @Override
            public int compare(Team team1, Team team2) {
                int result = 0;
                if (team1.name() != null && team2.name() != null) {
                    result = CASE_INSENSITIVE_ORDER.compare(team1.name(), team2.name());
                }
                return result;
            }
        });
        return availableTeams;
    }

    public List<String> getSortedAvailableTeamNames() {
        List<String> teamNamesList = new ArrayList<>();
        for ( Team team : getSortedAvailableTeams()) {
            teamNamesList.add(team.name());
        }
        return teamNamesList;
    }

    private List<Team> getAvailableTeams() {
        return teamRepository.listTeams();
    }
}