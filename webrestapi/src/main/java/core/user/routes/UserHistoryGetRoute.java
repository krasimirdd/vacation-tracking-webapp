package core.user.routes;

import REST.App;
import core.user.User;
import core.user.UserServiceImpl;
import core.vacation.Vacation;
import spark.*;
import util.Messages;

import java.util.*;

import static spark.Spark.halt;
import static util.Messages.HTML.NEW_LINE;
import static util.Messages.Halt.FORBIDDEN;

public class UserHistoryGetRoute implements TemplateViewRoute {

	private final UserServiceImpl userService;

	public UserHistoryGetRoute() {
		this.userService = new UserServiceImpl();
	}

	@Override
	public ModelAndView handle(Request request, Response response) {

		User user = userService.getUserByName(request.params(":username"));
		User currUser = request.session(true).attribute("user");
		if (user == null || !currUser.getUsername().equals(user.getUsername())) {
			halt(401, FORBIDDEN);
		}
		Set<Vacation> vacations = user.getVacations();

		List<Vacation> vacationsSorted = new ArrayList<>(vacations);
		vacationsSorted.sort(Comparator.comparingLong(Vacation::getId));


		Map<String, String> model = new HashMap<>();
		model.put(Messages.HTML.FLASH_MESSAGE_KEY, App.getFlashMessage(request));
		if (!user.getUsername().equals("")) {
			StringBuilder history = new StringBuilder();
			vacationsSorted.forEach(v -> history.append(v.toString())
					.append(NEW_LINE)
					.append(NEW_LINE)
			);
			model.put("history", history.toString());
			return new ModelAndView(model, "history.hbs");
		} else {
			response.status(404); // 404 Not found
			return new ModelAndView(model, "not-found.hbs");
		}
	}
}
