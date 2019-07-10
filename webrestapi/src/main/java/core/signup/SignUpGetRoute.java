package core.signup;

import spark.*;

import java.util.HashMap;
import java.util.Map;

import static REST.App.getFlashMessage;
import static util.Messages.HTML.FLASH_MESSAGE_KEY;

public class SignUpGetRoute implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) throws Exception {
        Map<String, String> model = new HashMap<>();
        model.put(FLASH_MESSAGE_KEY, getFlashMessage(request));
        return new ModelAndView(model, "sign-up.hbs");
    }
}
