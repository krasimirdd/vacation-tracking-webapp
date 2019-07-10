package core.login;

import spark.*;

import java.util.HashMap;
import java.util.Map;

import static REST.App.getFlashMessage;
import static util.Messages.HTML.FLASH_MESSAGE_KEY;

public class LoginGetRoute implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
        Map<String, String> model = new HashMap<>();
        model.put(FLASH_MESSAGE_KEY, getFlashMessage(request));
        return new ModelAndView(model, "login.hbs");
    }
}
