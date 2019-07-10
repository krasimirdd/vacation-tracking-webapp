package core.admin.routes;

import spark.*;

public class AdminReportsGetRoute implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
        return new ModelAndView(null, "reports.hbs");
    }
}
