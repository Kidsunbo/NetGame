package Server;

import org.json.JSONObject;

/**
 * Created by bxs863 on 26/02/19.
 */
public class SignupMessageHandler extends MessageHandler {
    public SignupMessageHandler(String message) {
        super(message);
    }

    @Override
    String process() {
        JSONObject response = new JSONObject();
        try {
            response.put("type", "logout_response");
            signUp(jsonObject, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    private void signUp(JSONObject jsonObject, JSONObject response) {


    }
}
