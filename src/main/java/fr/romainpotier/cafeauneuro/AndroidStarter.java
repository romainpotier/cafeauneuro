package fr.romainpotier.cafeauneuro;

import java.util.HashMap;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.romainpotier.cafeauneuro.beans.ApiResult;
import fr.romainpotier.cafeauneuro.beans.Fields;
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

    private ApiResult result;

    private final static int VISIBLE_MARKER = 50;

    // State values
    private boolean updatedMap = false;
    private boolean markerClicked = false;

    // Map for marker's informations
    private Map<Marker, String> markersInformations;

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

        // Reload markers
        mMap.setOnCameraChangeListener(new OnCameraChangeListener() {

            @Override
            public void onCameraChange(final CameraPosition arg0) {
                if (result != null && !markerClicked) {
                    addMarkerToMap();
                }
                markerClicked = false;
            }
        });

        // Don't reload markers on click
        mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(final Marker marker) {
                markerClicked = true;
                showMarkerInformations(marker);
                return false;
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
        result = coffeeService.getCoffees();
        updateMap();
    }

    @UiThread
    void updateMap() {

        if (progress.isShowing()) {
            progress.dismiss();
        }

        addMarkerToMap();

    }

    private void addMarkerToMap() {

        if (updatedMap) {
            return;
        }

        updatedMap = true;

        mMap.clear();

        // Init params
        final LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        double latitude = Double.MAX_VALUE;
        double longitude = Double.MAX_VALUE;
        int rang = 0;
        MarkerOptions marker = null;
        markersInformations = new HashMap<>();
        Fields fields = null;

        for (Record record : result.getRecords()) {

            fields = record.getFields();

            if (fields != null && fields.getGeo_latitude() != null) {

                latitude = record.getFields().getGeo_latitude().get(0);
                longitude = record.getFields().getGeo_latitude().get(1);

                if (bounds.contains(new LatLng(latitude, longitude))) {

                    marker = new MarkerOptions() //
                            .position(new LatLng(latitude, longitude)) //
                            .title(record.getFields().getNom());

                    Marker addedMarker = mMap.addMarker(marker);

                    markersInformations.put(addedMarker, getMarkerInformations(fields));

                    if (rang++ == VISIBLE_MARKER) {
                        break;
                    }

                }
            }
        }

        updatedMap = false;

    }

    private String getMarkerInformations(final Fields fields) {
        StringBuilder builder = new StringBuilder("");
        if (fields.getPrix_comptoir() != 0) {
            builder.append("Prix comptoir : " + fields.getPrix_comptoir() + "€ ");
        }
        if (fields.getPrix_salle() != 0) {
            builder.append("Prix salle : " + fields.getPrix_salle() + "€ ");
        }
        if (fields.getPrix_terasse() != 0) {
            builder.append("Prix terasse : " + fields.getPrix_terasse() + "€ ");
        }
        return builder.toString();
    }

    private void showMarkerInformations(final Marker marker) {
        if (markersInformations.containsKey(marker)) {
            Toast.makeText(this, markersInformations.get(marker), Toast.LENGTH_SHORT).show();
        }
    }

}
