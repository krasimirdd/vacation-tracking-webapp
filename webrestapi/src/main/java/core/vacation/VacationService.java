package core.vacation;

import core.user.User;
import dto.RequestDTO;

import java.util.List;

public interface VacationService {
	Vacation addVacation(User user, String from, String to, String isPaid);

	Vacation cancelVacation(Vacation vacation);

	Vacation getVacationById(long vacationId);

	List<Vacation> listRequestedVacations(long userID, long teamID);

	RequestDTO ifAcceptableRequest(User user, String from, String to, String isPaid);

	boolean ifCancelable(Vacation vacation);
}
