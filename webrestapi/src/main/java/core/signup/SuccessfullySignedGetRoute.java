package core.signup;

import spark.*;

public class SuccessfullySignedGetRoute implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) throws Exception {
        return new ModelAndView(null, "successfullySigned.hbs");
    }
}
