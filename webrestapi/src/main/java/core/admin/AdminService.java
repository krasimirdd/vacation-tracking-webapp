package core.admin;

import core.team.Team;
import core.user.User;
import core.vacation.Vacation;
import enumerate.Status;

public interface AdminService {
	void changeVacationStatus(String adminName, long requestId, Status newStatus);

	Admin getAdminByUsername(String username);

	Admin getAdminByTeamId(long teamId);

	boolean checkIfAdminExists(String username, String encryptedPassword);

	boolean isAuthorized(Admin admin, Team team);

	String generateVacationReport(long daysAvailable, long daysConsumed, Admin admin);

	String generateUserReport(long employeesCount, long totalEmployees, Admin admin);

	String tryToApprove(long requestId, String reason, Vacation vacation, Team team, User user, Admin admin);

}
