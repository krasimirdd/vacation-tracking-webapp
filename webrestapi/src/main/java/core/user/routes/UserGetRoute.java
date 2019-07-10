package core.user.routes;

import core.user.User;
import spark.*;
import util.Messages;

import java.util.HashMap;
import java.util.Map;

import static REST.App.getFlashMessage;

public class UserGetRoute implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {

        User user = request.session(true).attribute("user");

        Map<String, String> model = new HashMap<>();
        model.put("username", user.getUsername());
        model.put("available", String.valueOf(user.getDays_available()));
        model.put("consumed", String.valueOf(user.getDays_consumed()));
        model.put("uid", "history/" + user.getUsername());
        model.put(Messages.HTML.FLASH_MESSAGE_KEY, getFlashMessage(request));
        return new ModelAndView(model, "user.hbs");
    }
}
