package core.admin.routes;

import core.admin.Admin;
import core.admin.AdminServiceImpl;
import core.team.Team;
import core.team.TeamServiceImpl;
import core.user.User;
import core.user.UserServiceImpl;
import core.vacation.Vacation;
import core.vacation.VacationServiceImpl;
import enumerate.Status;
import spark.*;
import util.Messages;

import static REST.App.emailUtil;
import static REST.App.setFlashMessage;

public class AdminPostRoute implements TemplateViewRoute {

	private final VacationServiceImpl vacationService;
	private final TeamServiceImpl teamService;
	private final UserServiceImpl userService;
	private final AdminServiceImpl adminService;

	public AdminPostRoute() {

		vacationService = new VacationServiceImpl();
		teamService = new TeamServiceImpl();
		userService = new UserServiceImpl();
		adminService = new AdminServiceImpl();
	}

	@Override
	public ModelAndView handle(Request request, Response response) {

		String approval = request.queryParams("approval");
		String reason = request.queryParams("reason");
		long requestId = Long.parseLong(request.queryParams("requestId"));

		Vacation vacation = vacationService.getVacationById(requestId);
		if (vacation == null) {
			setFlashMessage(request, Messages.AdminPage.NO_VACATION);
			response.redirect("/admin/");
			return new ModelAndView(null, "admin.hbs");
		}
		Team team = teamService.getTeamByVacation(vacation);
		User user = userService.getUserByVacationId(vacation.getId());
		if (user == null) {
			setFlashMessage(request, Messages.AdminPage.NO_REQUESTS);
			response.redirect("/admin/");
			return new ModelAndView(null, "admin.hbs");
		}

		Admin admin = request.session(true).attribute("user");

		boolean isAuthorized = adminService.isAuthorized(admin, team);

		if (vacation.getStatus().equals(Status.PENDING) && isAuthorized) {

			if (approval.equalsIgnoreCase("approve")) {
				String resultMsg = adminService.tryToApprove(requestId, reason, vacation, team, user, admin);
				if(resultMsg.equalsIgnoreCase(Messages.Email.OVERBOOKED_FLASH)){
					userService.updateVacationInfo(user, vacation, false);
				}
				setFlashMessage(request, resultMsg);
			} else if (approval.equalsIgnoreCase("reject")) {
				adminService.changeVacationStatus(admin.getUsername(), requestId, Status.REJECTED);
				userService.updateVacationInfo(user, vacation, false);
				emailUtil.sendEmailTo(Status.REJECTED, user.getEmail(), reason);
				setFlashMessage(request, Messages.Email.EMAIL_SENDING);
			}
		} else {
			setFlashMessage(request, Messages.UserPage.INVALID_REQUEST);
		}

		response.redirect("/admin/");
		return new ModelAndView(null, "admin.hbs");
	}
}