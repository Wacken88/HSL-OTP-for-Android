package edu.usf.cutr.opentripplanner.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.List;

import edu.usf.cutr.opentripplanner.android.model.AddressModel;
import edu.usf.cutr.opentripplanner.android.sqlite.FavouriteAddressDB;
import edu.usf.cutr.opentripplanner.android.util.FavListAdapter;

import static android.view.View.OnClickListener;

/**
 * Created by Radek on 3/22/15.
 */
public class AddressFavActivity extends Activity {

    private FavouriteAddressDB db;

    public static final String FAVOURITE_ADDRESS_RESULT_PARAM_NAME = "fav_name";
    public static final String FAVOURITE_ADDRESS_RESULT_PARAM_LAT = "fav_latitude";
    public static final String FAVOURITE_ADDRESS_RESULT_PARAM_LONG = "fav_longitude";
    public static final double FAVOURITE_ADDRESS_RESULT_PARAM_ERROR_DOUBLE = -666;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        db = FavouriteAddressDB.getInstance(this);
        // We'll define a custom screen layout here (the one shown above), but
        // typically, you could just use the standard ListActivity layout.
        setContentView(R.layout.favourite);

        List<AddressModel> addressList = db.getAllAddresses();

        final FavListAdapter adapter = new FavListAdapter(this, android.R.layout.simple_list_item_1, addressList);
        ListView listView = (ListView) findViewById(R.id.favListView);
        listView.setAdapter(adapter);

        EditText filter = (EditText) findViewById(R.id.favFilter);
        filter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                adapter.getFilter().filter(s.toString());
            }
        });

        ImageButton backBtn = (ImageButton) findViewById(R.id.imageButtonFavBack);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
