package fr.romainpotier.cafeauneuro.service;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import fr.romainpotier.cafeauneuro.beans.ApiResult;

/**
 * REST webservice example
 * 
 * @author Romain Potier
 */
@Rest(rootUrl = "http://opendata.paris.fr/api/records/1.0", converters = { MappingJacksonHttpMessageConverter.class })
public interface CoffeeRestService extends RestClientErrorHandling {

    @Get("/search?dataset=liste-des-cafes-a-un-euro&rows=10000")
    ApiResult getCoffees();
}
