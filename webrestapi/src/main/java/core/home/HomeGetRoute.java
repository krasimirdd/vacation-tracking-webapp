package core.home;

import core.admin.Admin;
import core.user.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class HomeGetRoute implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) throws Exception {

        Object user = request.session(true).attribute("user");
        if (user != null) {

            if (user instanceof User) {
                response.status(201);
                response.redirect("/user/");
                return new ModelAndView(null, "user.hbs");
            }else if(user instanceof Admin){
                response.status(201);
                response.redirect("/admin/");
                return new ModelAndView(null, "admin.hbs");
            }

        }



        return new ModelAndView(null, "login.hbs");
    }
}
