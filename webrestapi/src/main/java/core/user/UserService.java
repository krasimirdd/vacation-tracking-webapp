package core.user;

import core.vacation.Vacation;

import java.util.List;

public interface UserService {
	boolean addUser(User user);

	boolean checkIfExists(String username, String encryptedPassword);

	User getUserByName(String username);

	User getUserByVacationId(long vacationId);

	List<User> getUsersByTeamID(long id);

	void updateVacationInfo(User user, Vacation vacation, boolean add);

	long getDaysAvailable(long adminID);

	long getDaysConsumed(long adminID);

	long getEmployeesWithDaysLeft(long adminID);
}
