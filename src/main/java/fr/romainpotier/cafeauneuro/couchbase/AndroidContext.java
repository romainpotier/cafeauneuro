package fr.romainpotier.cafeauneuro.couchbase;

import java.io.File;

import com.couchbase.lite.Context;
import com.couchbase.lite.NetworkReachabilityManager;

public class AndroidContext implements Context {

    private final android.content.Context wrappedContext;
    private NetworkReachabilityManager networkReachabilityManager;

    public AndroidContext(final android.content.Context wrappedContext) {
        this.wrappedContext = wrappedContext;
    }

    @Override
    public File getFilesDir() {
        return wrappedContext.getFilesDir();
    }

    @Override
    public void setNetworkReachabilityManager(final NetworkReachabilityManager networkReachabilityManager) {
        this.networkReachabilityManager = networkReachabilityManager;
    }

    @Override
    public NetworkReachabilityManager getNetworkReachabilityManager() {
        if (networkReachabilityManager == null) {
            networkReachabilityManager = new AndroidNetworkReachabilityManager(this);
        }
        return networkReachabilityManager;
    }

    public android.content.Context getWrappedContext() {
        return wrappedContext;
    }

}