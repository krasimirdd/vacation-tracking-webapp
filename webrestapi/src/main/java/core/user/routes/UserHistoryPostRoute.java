package core.user.routes;

import REST.App;
import core.user.User;
import core.user.UserServiceImpl;
import core.vacation.Vacation;
import core.vacation.VacationServiceImpl;
import spark.*;
import util.Messages;

public class UserHistoryPostRoute implements TemplateViewRoute {

	private final UserServiceImpl userService;
	private final VacationServiceImpl vacationService;

	public UserHistoryPostRoute() {
		this.userService = new UserServiceImpl();
		this.vacationService = new VacationServiceImpl();
	}

	@Override
	public ModelAndView handle(Request request, Response response) {

		long vacationId = Long.parseLong(request.queryParams("cancel"));
		String username = request.params(":username");

		User user = userService.getUserByName(username);
		Vacation vacation = vacationService.getVacationById(vacationId);
		if (vacation == null) {
			App.setFlashMessage(request, Messages.UserPage.INVALID_REQUEST);
			response.redirect("/user/history/" + username);
			return null;
		}
		boolean isCancelable = vacationService.ifCancelable(vacation);

		if (isCancelable) {
			Vacation canceled = vacationService.cancelVacation(vacation);
			if (canceled != null) {
				userService.updateVacationInfo(user, canceled, false);
				App.setFlashMessage(request, Messages.UserPage.CANCELED);
			}
		} else {
			App.setFlashMessage(request, Messages.UserPage.UNABLE_TO_CANCEL);
		}

		response.redirect("/user/history/" + username);
		return new ModelAndView(null, "history.hbs");
	}
}
