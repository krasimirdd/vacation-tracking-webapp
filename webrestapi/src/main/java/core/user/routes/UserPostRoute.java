package core.user.routes;

import REST.App;
import core.admin.Admin;
import core.admin.AdminServiceImpl;
import core.user.User;
import core.user.UserServiceImpl;
import core.vacation.Vacation;
import core.vacation.VacationServiceImpl;
import dto.RequestDTO;
import spark.*;
import util.Messages;

import static REST.App.emailUtil;

public class UserPostRoute implements TemplateViewRoute {

	private final AdminServiceImpl adminService;
	private final UserServiceImpl userService;
	private final VacationServiceImpl vacationService;

	public UserPostRoute() {
		this.adminService = new AdminServiceImpl();
		this.userService = new UserServiceImpl();
		this.vacationService = new VacationServiceImpl();
	}

	@Override
	public ModelAndView handle(Request request, Response response) {

		String from = request.queryParams("from");
		String to = request.queryParams("to");
		String isPaid = request.queryParams("type");
		if (isPaid == null) {
			isPaid = "";
		}
		User user = request.session(true).attribute("user");

		Admin reeferAdmin = adminService.getAdminByTeamId(user.getTeam().getId());

		RequestDTO requestDTO = vacationService.ifAcceptableRequest(user, from, to, isPaid);
		boolean acceptable = requestDTO.isAcceptable();

		if (acceptable) {
			App.setFlashMessage(request, Messages.UserPage.ACCEPTED_REQUEST);
			Vacation vacation = vacationService.addVacation(user, requestDTO.getFrom().toString(),
					requestDTO.getTo().toString(), isPaid);
			//true for adding | false for removing
			userService.updateVacationInfo(user, vacation, true);
			emailUtil.sendEmailTo(reeferAdmin.getEmail());
		} else {
			App.setFlashMessage(request, Messages.UserPage.INVALID_REQUEST);
		}

		response.redirect("/user/");
		return new ModelAndView(null, "user.hbs");
	}
}
