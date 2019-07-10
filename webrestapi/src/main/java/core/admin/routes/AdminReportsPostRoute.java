package core.admin.routes;

import core.admin.Admin;
import core.admin.AdminServiceImpl;
import core.team.Team;
import core.team.TeamServiceImpl;
import core.user.UserServiceImpl;
import spark.*;

import java.util.HashMap;
import java.util.Map;

public class AdminReportsPostRoute implements TemplateViewRoute {

	private final UserServiceImpl userService;
	private final AdminServiceImpl adminService;
	private final TeamServiceImpl teamService;

	public AdminReportsPostRoute() {

		userService = new UserServiceImpl();
		adminService = new AdminServiceImpl();
		teamService = new TeamServiceImpl();
	}

	@Override
	public ModelAndView handle(Request request, Response response) {

		Map<String, String> model = new HashMap<>();
		String vacationReport = request.queryParams("vacation_report");
		String userReport = request.queryParams("user_report");

		Admin admin = request.session(true).attribute("user");
		Team team = teamService.getTeamById(admin.getTeam().getId());

		long adminID = admin.getId();
		long daysAvailable = userService.getDaysAvailable(adminID);
		long daysConsumed = userService.getDaysConsumed(adminID);
		long employeesWithDaysAvailable = userService.getEmployeesWithDaysLeft(adminID);
		long totalEmployees = team.getUsers().size();

		String vacationLink = "";
		String userLink = "";
		if (vacationReport != null) {
			vacationLink = adminService.generateVacationReport(daysAvailable, daysConsumed, admin);
		}
		model.put("vacationLink", vacationLink);
		if (userReport != null) {
			userLink = adminService.generateUserReport(employeesWithDaysAvailable, totalEmployees, admin);
		}
		model.put("userLink", userLink);

		return new ModelAndView(model, "generated.hbs");
	}
}
