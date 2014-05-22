package fr.romainpotier.cafeauneuro;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.romainpotier.cafeauneuro.beans.ApiResult;
import fr.romainpotier.cafeauneuro.beans.Record;
import fr.romainpotier.cafeauneuro.service.CoffeeService;

/**
 * Simple activity for testing REST webservice
 * 
 * @author Romain Potier
 */
@EActivity(R.layout.activity_main)
public class AndroidStarter extends Activity {

    @RestService
    CoffeeService coffeeService;

    private ProgressDialog progress;

    private GoogleMap mMap;

    @AfterViews
    void init() {
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);

        // My position
        mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(final Location arg0) {
                final LatLng latLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                mMap.setOnMyLocationChangeListener(null);
            }

        });

        progress = new ProgressDialog(this);
        progress.setMessage("Chargement des cafés");
        progress.show();

        // Load coffees
        loadCoffees();

    }

    @Background
    void loadCoffees() {
        final ApiResult result = coffeeService.getCoffees();
        updateMap(result);
    }

    @UiThread
    void updateMap(final ApiResult result) {

        if (progress.isShowing()) {
            progress.dismiss();
        }

        double latitude = Double.MAX_VALUE;
        double longitude = Double.MAX_VALUE;
        int rang = 0;
        MarkerOptions marker = null;

        for (Record record : result.getRecords()) {

            latitude = record.getFields().getGeo_latitude().get(0);
            longitude = record.getFields().getGeo_latitude().get(1);

            marker = new MarkerOptions() //
                    .position(new LatLng(latitude, longitude)) //
                    .title(record.getFields().getNom());

            mMap.addMarker(marker);

            if (rang++ == 10) {
                break;
            }
        }
    }

}
