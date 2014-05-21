package fr.romainpotier.cafeauneuro;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Simple activity for testing REST webservice
 * 
 * @author Romain Potier
 */
@EActivity(R.layout.activity_main)
public class AndroidStarter extends Activity {

    // @ViewById
    // TextView textView;
    //
    // @ViewById
    // ListView listView;
    //
    // @RestService
    // MoviesService moviesService;

    private GoogleMap mMap;

    @AfterViews
    void init() {
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.8353182, 2.2949952999999823)).title("Domicile"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.165661, -1.50709)).title("Parents"));
        mMap.setMyLocationEnabled(true);
    }

    // @Click
    // void callRestService() {
    // callMoviesService();
    // }
    // @Background
    // void callMoviesService() {
    // // Call service
    // List<Movie> movies = moviesService.getMovies();
    // updateListView(movies);
    // }
    //
    // @UiThread
    // void updateListView(final List<Movie> movies) {
    // // Create custom adapter
    // final ArrayAdapter<Movie> arrayAdapter = new ArrayAdapter<Movie>(this, android.R.layout.simple_list_item_1,
    // movies);
    // listView.setAdapter(arrayAdapter);
    // }

}
