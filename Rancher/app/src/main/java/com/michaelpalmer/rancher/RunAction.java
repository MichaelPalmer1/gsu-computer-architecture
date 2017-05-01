package com.michaelpalmer.rancher;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.michaelpalmer.rancher.schema.BaseSchema;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RunAction {
    private Context context;
    private String action;
    private Thread thread;
    private Handler handler;
    private ProgressDialog progressDialog;
    private BaseSchema resource;
    private String url;
    private ActionsAPI actionApi;
    private Class pollApi;
    private RunActionCallback actionCallback;

    public static final String ACTION_START = "start", ACTION_STOP = "stop", ACTION_RESTART = "restart";

    public interface RunActionCallback {
        void onActionComplete();
    }

    public static abstract class ActionsAPI extends AsyncTask<String, Void, JSONObject> {}

    public RunAction(Context context, BaseSchema resource, String action, String url,
                     ActionsAPI actionApi, Class pollApi, RunActionCallback actionCallback) {
        // Validate action
        switch (action) {
            case "start": break;
            case "stop": break;
            case "restart": break;
            default: throw new IllegalArgumentException("Invalid action");
        }

        // Save data
        this.context = context;
        this.resource = resource;
        this.action = action;
        this.url = url;
        this.actionApi = actionApi;
        this.pollApi = pollApi;
        this.actionCallback = actionCallback;

        // Create thread
        thread = new Thread(runnable);

        // Create progress dialog
        progressDialog = new ProgressDialog(context);

        // Set progress dialog icon
        switch (action) {
            case ACTION_START:
                progressDialog.setIcon(R.drawable.ic_start_black);
                progressDialog.setTitle("Starting " + resource.getName());
                break;

            case ACTION_STOP:
                progressDialog.setIcon(R.drawable.ic_stop_black);
                progressDialog.setTitle("Stopping " + resource.getName());
                break;

            case ACTION_RESTART:
                progressDialog.setIcon(R.drawable.ic_restart_black);
                progressDialog.setTitle("Restarting " + resource.getName());
                break;
        }
    }

    /**
     * Start the thread
     */
    public void start() {
        progressDialog.show();
        thread.start();
        handler = new Handler(callback);
    }

    /**
     * Start action handler
     *
     * @param state Current state
     * @return True if complete, otherwise false
     */
    private boolean handleStart(String state) {
        switch (state) {
            case "stopped":
                progressDialog.setProgress(25);
                break;

            case "starting":
                progressDialog.setProgress(50);
                break;

            case "running":
                Log.d("RunActionHandler", "Resource has completed " + action);
                progressDialog.setProgress(100);
                progressDialog.dismiss();
                if (actionCallback != null) {
                    actionCallback.onActionComplete();
                }
                return true;
        }
        return false;
    }

    private boolean handleStop(String state) {
        switch (state) {
            case "running":
                if (progressDialog.getProgress() < 25) {
                    progressDialog.setProgress(25);
                }
                break;

            case "stopping":
                progressDialog.setProgress(50);
                break;

            case "stopped":
                Log.d("RunActionHandler", "Resource has completed " + action);
                progressDialog.setProgress(100);
                progressDialog.dismiss();
                if (actionCallback != null) {
                    actionCallback.onActionComplete();
                }
                return true;
        }
        return false;
    }

    private boolean handleRestart(String state) {
        switch (state) {
            case "restarting":
                progressDialog.setProgress(10);
                break;

            case "stopping":
                progressDialog.setProgress(25);
                break;

            case "stopped":
                progressDialog.setProgress(50);
                break;

            case "starting":
                progressDialog.setProgress(75);
                break;

            case "running":
                Log.d("RunActionHandler", "Resource has completed " + action);
                progressDialog.setProgress(100);
                progressDialog.dismiss();
                if (actionCallback != null) {
                    actionCallback.onActionComplete();
                }
                return true;
        }
        return false;
    }

    private Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle data = msg.getData();

            // Check for errors
            if (data.getBoolean("error")) {
                Log.e("RunActionHandler", "Received error message");
                progressDialog.cancel();
                return true;
            }

            // Check for exit
            if (data.getBoolean("exit")) {
                Log.d("RunActionHandler", "Received exit message");
                progressDialog.cancel();
                return true;
            }

            // Get the state
            String state = msg.getData().getString("state");
            Log.d("RunActionHandler", "Got container state: " + state);
            if (state == null) {
                return false;
            }

            // Update message
            progressDialog.setMessage(state.toUpperCase());

            // Update progress
            switch (action) {
                case "start":
                    return handleStart(state);

                case "stop":
                    return handleStop(state);

                case "restart":
                    return handleRestart(state);

                default:
                    Log.e("RunActionHandler", "Invalid action \"" + action + "\".");
                    return true;
            }
        }
    };

    private Runnable runnable = new Runnable() {
        private final int SLEEP_TIME = 500;
        private final int MAX_CALLS = 240;

        private boolean doExitCheck(String state) {
            switch (action) {
                case "start":
                    return state.equals("running");

                case "stop":
                    return state.equals("stopped") || state.equals("starting") ||
                            (state.equals("running") && progressDialog.getProgress() > 25);

                case "restart":
                    return state.equals("running");

                default:
                    return false;
            }
        }

        private void exitThread() {
            Message exitMsg = new Message();
            exitMsg.getData().putBoolean("exit", true);
            handler.sendMessage(exitMsg);
        }

        @Override
        public void run() {
            int numCalls = 0;
            Log.d("RunActionRunnable", "Performing " + action + " of resource " + resource.getName());
            try {
                String actionUrl = resource.getActions().optString(action);

                if (actionUrl == null) {
                    new AlertDialog.Builder(context)
                            .setMessage("Failed to " + action + " container.")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                    return;
                }

                ActionsAPI api = (ActionsAPI) actionApi.execute(actionUrl, action);
                if (api == null) {
                    return;
                }

                // Get the updated object
                Log.d("RunActionRunnable", "Fetching API object...");
                api.get(10, TimeUnit.SECONDS);
                Log.d("RunActionRunnable", "Received API object.");

            } catch (InterruptedException e) {
                Log.e("RunActionRunnable", "Container interrupted: " + e.getMessage());
                Message msg = new Message();
                msg.getData().putBoolean("error", true);
                handler.sendMessage(msg);
                return;
            } catch (ExecutionException e) {
                Log.e("RunActionRunnable", "Container execution exception: " + e.getMessage());
                Message msg = new Message();
                msg.getData().putBoolean("error", true);
                handler.sendMessage(msg);
                return;
            } catch (TimeoutException e) {
                Log.e("RunActionRunnable", "Container timed out: " + e.getMessage());
                Message msg = new Message();
                msg.getData().putBoolean("error", true);
                handler.sendMessage(msg);
                return;
            }

            while (numCalls <= MAX_CALLS) {
                numCalls++;
                try {
                    // Sleep
                    Log.d("RunActionPoller", "Thread sleeping for " + SLEEP_TIME + "ms...");
                    Thread.sleep(SLEEP_TIME);
                    Log.d("RunActionPoller", "Thread done sleeping");

                    // Get the status
                    Log.d("RunActionPoller", "Requesting resource status");
                    JSONObject object = ((ActionsAPI) pollApi.newInstance()).execute(url).get(10, TimeUnit.SECONDS);
                    Log.d("RunActionPoller", "Received resource status");

                    // Verify container was retrieved
                    if (object == null) {
                        Log.d("RunActionPoller", "Resource was null");
                        continue;
                    }

                    // Get state
                    String state = object.optString("state");

                    // Update handler
                    Log.d("RunActionPoller", "Resource state is " + state);
                    Message msg = new Message();
                    msg.getData().putString("state", state);
                    handler.sendMessage(msg);

                    if (doExitCheck(state)) {
                        exitThread();
                        return;
                    }
                } catch (InterruptedException e) {
                    Log.e("RunActionPoller", "Task interrupted: " + e.getMessage());
                    numCalls++;
                } catch (ExecutionException e) {
                    Log.e("RunActionPoller", "Task execution failed: " + e.getMessage());
                    numCalls++;
                } catch (TimeoutException e) {
                    Log.e("RunActionPoller", "Task timed out: " + e.getMessage());
                    numCalls++;
                } catch (java.lang.InstantiationException e) {
                    Log.e("RunActionPoller", "Failed to instantiate the polling class: " + e.getMessage());
                    exitThread();
                    return;
                } catch (IllegalAccessException e) {
                    Log.e("RunActionPoller", "Illegal access exception: " + e.getMessage());
                    exitThread();
                    return;
                }
            }
        }
    };
}
