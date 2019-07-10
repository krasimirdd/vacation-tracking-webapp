package core.team;

import core.vacation.Vacation;

public interface TeamService {

	Team getTeamById(long id);

	Team getTeamByVacation(Vacation vacation);
}
