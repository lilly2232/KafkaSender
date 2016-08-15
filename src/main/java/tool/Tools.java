package tool;


import org.json.JSONObject;

/**
 * Created by 1002718 on 2016. 8. 15..
 */
public class Tools {

    public static String stringToJson(String topic, String message) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("topic",topic);
        jsonObj.put("message",message);
        return jsonObj.toString();
    }

    public static String jsonToString(String json){

        return "";
    }

}
