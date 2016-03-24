package in.oliveboard.devishankar.listeners;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Devishankar
 */
public interface IHttpResponseListener {
    void onSuccess(JSONObject body) throws JSONException;

    void onMessage(String resp);

    void onFailure(String resp, Throwable throwable);

    void onJsonParseError();

}
