package core.signup;

import REST.App;
import core.team.Team;
import core.team.TeamServiceImpl;
import core.user.User;
import core.user.UserServiceImpl;
import spark.*;
import util.Messages;
import util.ValidationUtil;

import static util.Messages.Credentials.SAME_CREDENTIALS_ERROR;

public class SignUpPostRoute implements TemplateViewRoute {
	private final TeamServiceImpl teamService;
	private final UserServiceImpl userService;

	public SignUpPostRoute() {
		this.teamService = new TeamServiceImpl();
		this.userService = new UserServiceImpl();
	}

	@Override
	public ModelAndView handle(Request request, Response response) {

		String username = request.queryParams("username");
		boolean validUsername = ValidationUtil.validateUsername(username);
		String email = request.queryParams("email");
		String password = request.queryParams("password");
		boolean validPassword = ValidationUtil.validatePassword(password);
		String confirm_password = request.queryParams("confirm_password");
		String team = request.queryParams("team");

		if (!validPassword || !validUsername) {
			App.setFlashMessage(request, Messages.Credentials.WRONG_CREDENTIALS);
			response.redirect("/sign-up/");
			return new ModelAndView(null, "sign-up.hbs");
		}

		Team teamById = teamService.getTeamById(Long.parseLong(team));

		User user = new User(username, password, email, teamById);
		boolean isCreated = userService.addUser(user);
		if (!password.equals(confirm_password)) {
			App.setFlashMessage(request, Messages.Credentials.CONFIRM_PASSWORD_ERROR);
			response.redirect("/sign-up/");
			return new ModelAndView(null, "sign-up.hbs");
		}

		if (!isCreated) {
			App.setFlashMessage(request, SAME_CREDENTIALS_ERROR);
			response.redirect("/sign-up/");
			return new ModelAndView(null, "sign-up.hbs");
		}

		response.redirect("/successfullySigned/");
		return new ModelAndView(null, "successfullySigned.hbs");
	}
}
