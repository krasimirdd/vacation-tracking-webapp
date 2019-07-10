package core.logout;

import spark.*;

public class LogoutGetRoute implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) throws Exception {
        request.session(false).attribute("user",null);

//        response.header("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
//        response.header("Pragma", "no-cache"); // HTTP 1.0.
//        response.header("Expires", "0"); // Proxies.

        response.status(200);
        response.redirect("/");
        return new ModelAndView(null, "login.hbs");
    }
}
