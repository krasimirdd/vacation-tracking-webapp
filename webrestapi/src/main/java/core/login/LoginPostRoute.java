package core.login;

import REST.App;
import core.admin.Admin;
import core.admin.AdminServiceImpl;
import core.user.User;
import core.user.UserServiceImpl;
import spark.*;
import util.CipherUtil;
import util.Messages;

import java.util.HashMap;
import java.util.Map;


public class LoginPostRoute implements TemplateViewRoute {
	private final UserServiceImpl userServiceImpl;
	private final AdminServiceImpl adminService;

	public LoginPostRoute() {
		this.userServiceImpl = new UserServiceImpl();
		this.adminService = new AdminServiceImpl();
	}

	@Override
	public ModelAndView handle(Request request, Response response) {

		String username = request.queryParams("username");
		String password = request.queryParams("password");

		String encryptPassword = CipherUtil.encrypt(password);

		boolean ifExistsUser = userServiceImpl.checkIfExists(username, encryptPassword);
		boolean ifExistsAdmin = adminService.checkIfAdminExists(username, encryptPassword);

		if (ifExistsUser) {
			User user = userServiceImpl.getUserByName(username);
			request.session(true).attribute("user", user);
			response.redirect("/user/");
			Map<String, String> model = new HashMap<>();
			model.put("username", username);
			return new ModelAndView(model, "user.hbs");
		} else if (ifExistsAdmin) {
			Admin admin = adminService.getAdminByUsername(username);
			request.session(true).attribute("user", admin);
			response.redirect("/admin/");
			Map<String, String> model = new HashMap<>();
			model.put("username", username);
			return new ModelAndView(model, "admin.hbs");
		} else {
			App.setFlashMessage(request, Messages.Credentials.WRONG_CREDENTIALS);
			response.redirect("/login/");
			return new ModelAndView(null, "login.hbs");
		}
	}
}
