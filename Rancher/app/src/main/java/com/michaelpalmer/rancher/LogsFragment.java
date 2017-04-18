package com.michaelpalmer.rancher;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.michaelpalmer.rancher.schema.Container;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLogsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LogsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogsFragment extends Fragment {
    private static final String ARG_LOGS_URL = "logs-url", ARG_CONTAINER_ID = "container-id";
    private String mLogsUrl, mContainerId;
    private OnLogsFragmentInteractionListener mListener;
    private LogsClient logsClient;
    private TextView logOutput;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param container Container
     * @return A new instance of fragment LogsFragment.
     */
    public static LogsFragment newInstance(Container container) {
        LogsFragment fragment = new LogsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTAINER_ID, container.getId());

        try {
            String logsUrl = container.getActions().getString("logs");
            args.putString(ARG_LOGS_URL, logsUrl);
        } catch (JSONException e) {
            // Let it go
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLogsUrl = getArguments().getString(ARG_LOGS_URL);
            mContainerId = getArguments().getString(ARG_CONTAINER_ID);
        }

        if (mLogsUrl != null) {
            new LogsAPI().execute(mLogsUrl);
        } else {
            Toast.makeText(getContext(), "Failed to fetch logs", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logs, container, false);
        logOutput = (TextView) view.findViewById(R.id.log_output);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onLogsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLogsFragmentInteractionListener) {
            mListener = (OnLogsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLogsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
//        logsClient.close();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLogsFragmentInteractionListener {
        void onLogsFragmentInteraction(Uri uri);
    }

    private class LogsAPI extends AsyncTask<String, Void, JSONObject> {

        private static final String TAG = "LogsAPI";

        /**
         * Perform the API call
         *
         * @param params Parameters
         * @return API Response as string
         */
        @Override
        protected JSONObject doInBackground(String... params) {
            // Fetch the data
            String jsonString = API.POST(params[0]);
            Log.i(TAG, "Received JSON: " + jsonString);

            // Parse the data
            try {
                return new JSONObject(jsonString);
            } catch (JSONException e) {
                Log.e(TAG, "Error processing JSON: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            try {
                // Set headers
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + data.getString("token"));

                // Set URI
                URI uri = new URI(data.getString("url"));

                // Create client
                Log.d(TAG, "Creating websocket connection...");
                logsClient = new LogsClient(uri, headers);
                logsClient.connect();
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing JSON data: " + e.getMessage());
            } catch (URISyntaxException e) {
                Log.e(TAG, String.format(Locale.US, "Invalid URL \"%s\": %s", e.getInput(), e.getMessage()));
            }
        }
    }

    private class LogsClient extends RancherWSClient {

        LogsClient(URI serverUri, Map<String, String> headers) {
            super(serverUri, headers);
            Log.d("LogsFragment", "Created connection to '" + serverUri.toString() + "'");
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            Log.d("LogsFragment", "Opening connection: " + handshakedata.getHttpStatusMessage());
        }

        @Override
        public void onMessage(String message) {
            Log.d("LogsFragment", "Message received: " + message);
            if (logOutput != null) {
                logOutput.append(message);
            }
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.d("LogsFragment", "Closing connection with " + code + ": " + reason);
        }

        @Override
        public void onError(Exception ex) {
            Log.e("LogsFragment", "WebSocket error: " + ex.getMessage());
        }

    }
}
