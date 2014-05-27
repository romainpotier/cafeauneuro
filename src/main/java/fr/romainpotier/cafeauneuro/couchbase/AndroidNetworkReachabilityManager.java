package fr.romainpotier.cafeauneuro.couchbase;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.couchbase.lite.Context;
import com.couchbase.lite.NetworkReachabilityManager;

public class AndroidNetworkReachabilityManager extends NetworkReachabilityManager {

    private Context context;
    private boolean listening;
    private final android.content.Context wrappedContext;
    private final ConnectivityBroadcastReceiver receiver;
    private State state;

    public enum State {
        UNKNOWN,

        /** This state is returned if there is connectivity to any network **/
        CONNECTED,
        /**
         * This state is returned if there is no connectivity to any network. This is set to true under two
         * circumstances:
         * <ul>
         * <li>When connectivity is lost to one network, and there is no other available network to attempt to switch
         * to.</li>
         * <li>When connectivity is lost to one network, and the attempt to switch to another network fails.</li>
         */
        NOT_CONNECTED
    }

    public AndroidNetworkReachabilityManager(final AndroidContext context) {
        this.context = context;
        this.wrappedContext = context.getWrappedContext();
        this.receiver = new ConnectivityBroadcastReceiver();
        this.state = State.UNKNOWN;
    }

    @Override
    public synchronized void startListening() {
        if (!listening) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            wrappedContext.registerReceiver(receiver, filter);
            listening = true;
        }
    }

    @Override
    public synchronized void stopListening() {
        if (listening) {
            try {
                wrappedContext.unregisterReceiver(receiver);
            } catch (Exception e) {
            }
            context = null;
            listening = false;
        }
    }

    private class ConnectivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final android.content.Context context, final Intent intent) {
            String action = intent.getAction();

            if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION) || listening == false) {
                return;
            }

            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            if (noConnectivity) {
                state = State.NOT_CONNECTED;
            } else {
                state = State.CONNECTED;
            }

            if (state == State.NOT_CONNECTED) {
                notifyListenersNetworkUneachable();
            }

            if (state == State.CONNECTED) {
                notifyListenersNetworkReachable();
            }

        }
    };

}