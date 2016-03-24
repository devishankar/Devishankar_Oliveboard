package in.oliveboard.devishankar.handlers.response;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import in.oliveboard.devishankar.listeners.IHttpResponseListener;
import in.oliveboard.devishankar.utils.Logger;


/**
 * @author Devishankar
 */
public class ApiResponseHandler extends AsyncHttpResponseHandler {
    private static final String TAG = "ApiResponseHandler";
    private final IHttpResponseListener listener;

    public ApiResponseHandler(Context context) {
        listener = (IHttpResponseListener) context;
    }

    @Override
    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
        String resp = new String(responseBody);
        Logger.i(TAG, "headers " + Arrays.toString(headers));
        Logger.json(TAG, "api resp received " + resp);
        if (!resp.equals("")) {
            try {
                JSONObject obj = new JSONObject(resp);
                listener.onSuccess(obj);

            } catch (JSONException e) {
                Logger.getStackTraceString(e);
                listener.onJsonParseError();
            }
        } else {
            listener.onMessage("Something unexpected happened, please try again!");
        }
    }

    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
        String resp = "";
        if (responseBody != null)
            resp = new String(responseBody);
        Logger.json(TAG, "api resp failed " + resp);
        listener.onFailure(resp, error);
    }

    @Override
    public void onRetry(int retryNo) {
        Logger.d(TAG, "Request is retried, retry no. %d " + retryNo);
    }
}
