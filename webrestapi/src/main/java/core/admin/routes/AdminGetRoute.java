package core.admin.routes;

import REST.App;
import core.admin.Admin;
import core.user.User;
import core.user.UserServiceImpl;
import core.vacation.Vacation;
import core.vacation.VacationServiceImpl;
import enumerate.Status;
import spark.*;

import java.util.*;

import static util.Messages.AdminPage.NO_REQUESTS;
import static util.Messages.HTML.FLASH_MESSAGE_KEY;
import static util.Messages.HTML.NEW_LINE;

public class AdminGetRoute implements TemplateViewRoute {

	private final UserServiceImpl userService;
	private final VacationServiceImpl vacationService;

	public AdminGetRoute() {
		userService = new UserServiceImpl();
		vacationService = new VacationServiceImpl();
	}

	@Override
	public ModelAndView handle(Request request, Response response) {

		Map<String, String> model = new HashMap<>();

		Admin admin = request.session(true).attribute("user");
		long teamID = admin.getTeam().getId();

		List<User> users = userService.getUsersByTeamID(teamID);
		if (users == null) {
			model.put("requestList", NO_REQUESTS);
			return new ModelAndView(model, "admin.hbs");
		}

		List<Vacation> vacations = new ArrayList<>();
		for (User user : users) {
			vacations.addAll(vacationService.listRequestedVacations(user.getId(), teamID));
		}

		if (vacations.size() != 0) {
			StringBuilder result = new StringBuilder();
			vacations.stream().filter(v -> v.getStatus().equals(Status.PENDING))
					.forEach(v -> result.append(v.toString())
							.append(NEW_LINE)
							.append(NEW_LINE)
					);
			model.put("requestList", result.toString());
		}
		model.put(FLASH_MESSAGE_KEY, App.getFlashMessage(request));
		return new ModelAndView(model, "admin.hbs");
	}
}

