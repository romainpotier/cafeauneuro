package fr.romainpotier.cafeauneuro.service;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.web.client.RestClientException;

import android.content.Context;
import android.widget.Toast;
import fr.romainpotier.cafeauneuro.R;

@EBean
public class RestServiceErrorHandler implements RestErrorHandler {

    @RootContext
    Context context;

    @Override
    public void onRestClientExceptionThrown(final RestClientException e) {
        showErrorMessage();
    }

    @UiThread
    void showErrorMessage() {
        Toast.makeText(context, context.getText(R.string.errorService), Toast.LENGTH_SHORT).show();
    }
}