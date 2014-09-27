package fr.romainpotier.cafeauneuro.service;

import android.content.Context;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.rest.RestService;

import fr.romainpotier.cafeauneuro.beans.ApiResult;

@EBean
public class CoffeeService {

    @RestService
    CoffeeRestService coffeeRestService;

    @Bean
    RestServiceErrorHandler restServiceErrorHandler;

    @RootContext
    Context context;

    @AfterInject
    void afterInject() {
        coffeeRestService.setRestErrorHandler(restServiceErrorHandler);
    }

    public ApiResult loadCoffees() {

        return coffeeRestService.getCoffees();

    }

}
