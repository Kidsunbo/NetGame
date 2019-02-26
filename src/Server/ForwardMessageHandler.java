package Server;

import org.json.JSONObject;

/**
 * Created by bxs863 on 26/02/19.
 */
public class ForwardMessageHandler extends MessageHandler {
    public ForwardMessageHandler(String message) {
        super(message);
    }

    @Override
    String process() {
        JSONObject response = new JSONObject();
        //TO BE FINISHED
        return response.toString();
    }
}
