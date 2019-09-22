package com.rajnikanth.app.buckspy.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.rajnikanth.app.buckspy.CommonUtils;
import com.rajnikanth.app.buckspy.MainActivity;
import com.rajnikanth.app.buckspy.OldMainActivity;
import com.rajnikanth.app.buckspy.MultiDrawable;
import com.rajnikanth.app.buckspy.MyItemReader;
import com.rajnikanth.app.buckspy.R;
import com.rajnikanth.app.buckspy.database.DatabaseClient;
import com.rajnikanth.app.buckspy.entity.ExpenceLocationDetails;
import com.rajnikanth.app.buckspy.location.MyItem;
import com.rajnikanth.app.buckspy.location.Person;

import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlacesFragment extends Fragment implements OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<Person>,
        ClusterManager.OnClusterInfoWindowClickListener<Person>,
        ClusterManager.OnClusterItemClickListener<Person>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Person> {

    Activity context = null;
    private ClusterManager<MyItem> mClusterManager;
    private ClusterManager<Person> mPersonClusterManager;
    private GoogleMap mMap;
    private Random mRandom = new Random(1984);
    private List<ExpenceLocationDetails> expence_locations_List;

    public PlacesFragment(OldMainActivity oldMainActivity) {
        this.context = oldMainActivity;
    }

    public PlacesFragment(MainActivity mainActivity) {
        this.context = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(17.4337, 78.5016), 10));//51.503186, -0.126446
        /*mClusterManager = new ClusterManager<>(context, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        try {
            readItems();
        } catch (JSONException e) {
            Toast.makeText(context, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }*/
        mPersonClusterManager = new ClusterManager<Person>(context, mMap);
        mPersonClusterManager.setRenderer(new PersonRenderer());
        mMap.setOnCameraIdleListener(mPersonClusterManager);
        mMap.setOnMarkerClickListener(mPersonClusterManager);
        mMap.setOnInfoWindowClickListener(mPersonClusterManager);
        mPersonClusterManager.setOnClusterClickListener(this);
        mPersonClusterManager.setOnClusterInfoWindowClickListener(this);
        mPersonClusterManager.setOnClusterItemClickListener(this);
        mPersonClusterManager.setOnClusterItemInfoWindowClickListener(this);

//        addItems();
        new loadExpenceLocations().execute();
        mPersonClusterManager.cluster();
    }

    private void readItems() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        List<MyItem> items = new MyItemReader().read(inputStream);
        mClusterManager.addItems(items);
    }

    private class PersonRenderer extends DefaultClusterRenderer<Person> {
        private final IconGenerator mIconGenerator = new IconGenerator(context);
        private final IconGenerator mClusterIconGenerator = new IconGenerator(context);
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public PersonRenderer() {
            super(context, mMap, mPersonClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(context);
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(person.profilePhoto);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Person> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Person p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(p.profilePhoto);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<Person> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().name;
        Toast.makeText(context, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Person> cluster) {

    }

    @Override
    public boolean onClusterItemClick(Person item) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Person item) {

    }

    private LatLng position() {
        return new LatLng(random(20.056562, 96.299682), random(0.148271, -0.3514683));
    }

    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }

    private void addItems() {
        // http://www.flickr.com/photos/sdasmarchives/5036248203/
        mPersonClusterManager.addItem(new Person(new LatLng(17.4249062, 78.4459775), "Clothing", R.drawable.ic_clothing));

        // http://www.flickr.com/photos/usnationalarchives/4726917149/
        mPersonClusterManager.addItem(new Person(new LatLng(17.4968203, 78.3752823), "Fuel", R.drawable.ic_fuel));

        // http://www.flickr.com/photos/nypl/3111525394/
        mPersonClusterManager.addItem(new Person(new LatLng(17.4941962, 78.3568429), "Drinks", R.drawable.ic_drinks));

        // http://www.flickr.com/photos/smithsonian/2887433330/
        mPersonClusterManager.addItem(new Person(new LatLng(17.4941348, 78.3557959), "Education", R.drawable.ic_education));

        // http://www.flickr.com/photos/library_of_congress/2179915182/
        mPersonClusterManager.addItem(new Person(new LatLng(17.4381281, 78.4953904), "Health", R.drawable.ic_health));

        // http://www.flickr.com/photos/nationalmediamuseum/7893552556/
        mPersonClusterManager.addItem(new Person(new LatLng(17.4380146, 78.4931445), "Personal", R.drawable.ic_personal));

        // http://www.flickr.com/photos/sdasmarchives/5036231225/
        mPersonClusterManager.addItem(new Person(new LatLng(17.4969527, 78.3806408), "Drinks", R.drawable.ic_drinks));

        // http://www.flickr.com/photos/anmm_thecommons/7694202096/
        mPersonClusterManager.addItem(new Person(new LatLng(17.4969527, 78.3806408), "Food", R.drawable.ic_food));

        // http://www.flickr.com/photos/usnationalarchives/4726892651/
        mPersonClusterManager.addItem(new Person(new LatLng(17.4280486, 78.4696242), "personal", R.drawable.ic_personal));
    }

    public class loadExpenceLocations extends AsyncTask {

        @Override
        protected String doInBackground(Object[] objects) {
            try {
                expence_locations_List = DatabaseClient.getInstance(context).getAppDatabase().expenseDetailsDao().getExpenceLocations();
                return "S";
            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object str) {
            super.onPostExecute(str);
            if (str.equals("S")) {
                for (ExpenceLocationDetails locationDetails : expence_locations_List) {
                    String[] latlong = locationDetails.getExpence_location().split(",");
                    mPersonClusterManager.addItem(new Person(new LatLng(CommonUtils.stringToDoubleDefault(latlong[0],0.0), CommonUtils.stringToDoubleDefault(latlong[1],0.0)),
                            locationDetails.getCategory_name(), CommonUtils.stringToIntDefault(locationDetails.getImage_path(),0)));
                }
            }
        }
    }
}
