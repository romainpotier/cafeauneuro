package fr.romainpotier.cafeauneuro.service;

import static android.text.TextUtils.isEmpty;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.rest.RestService;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import fr.romainpotier.cafeauneuro.beans.ApiResult;

@EBean
public class CoffeeService {

    @RestService
    CoffeeRestService coffeeRestService;

    @Bean
    RestServiceErrorHandler restServiceErrorHandler;

    @RootContext
    Context context;

    private final Gson gson = new Gson();

    // Static vars
    private static final String PREFS_FILE = "fr.romainpotier";
    private static final String COFFEE_VAR = "coffees";

    @AfterInject
    void afterInject() {
        coffeeRestService.setRestErrorHandler(restServiceErrorHandler);
    }

    public ApiResult loadCoffees() {

        final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        final String coffees = prefs.getString(COFFEE_VAR, "");

        ApiResult result = null;

        if (isEmpty(coffees)) {
            result = coffeeRestService.getCoffees();
            if (result != null) {
                // Save in prefs
                prefs.edit().putString("coffees", gson.toJson(result)).commit();
            }
        } else {
            result = gson.fromJson(coffees, ApiResult.class);
        }

        return result;

    }

}
