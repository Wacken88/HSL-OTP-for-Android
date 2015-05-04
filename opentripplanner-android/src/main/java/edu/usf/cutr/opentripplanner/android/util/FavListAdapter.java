package edu.usf.cutr.opentripplanner.android.util;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import edu.usf.cutr.opentripplanner.android.AddressFavActivity;
import edu.usf.cutr.opentripplanner.android.R;
import edu.usf.cutr.opentripplanner.android.model.AddressModel;
import edu.usf.cutr.opentripplanner.android.sqlite.FavouriteAddressDB;

/**
 * Created by Radek on 3/24/15.
 */
public class FavListAdapter extends ArrayAdapter<AddressModel> {

    private final Activity context;
    private final List<AddressModel> addresses;

    public FavListAdapter(Activity context, int resource, List<AddressModel> objects) {
        super(context, resource, objects);

        this.context = context;
        this.addresses = objects;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final AddressModel address = addresses.get(position);
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);

        final TextView txtTitle = (TextView) rowView.findViewById(R.id.favLineTextView);
        txtTitle.setText(address.getAddress());
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(AddressFavActivity.FAVOURITE_ADDRESS_RESULT_PARAM_NAME, address.getAddress());
                if(address.hasLat())
                    result.putExtra(AddressFavActivity.FAVOURITE_ADDRESS_RESULT_PARAM_LAT, address.getLatitude());
                else
                    result.putExtra(AddressFavActivity.FAVOURITE_ADDRESS_RESULT_PARAM_LAT, AddressFavActivity.FAVOURITE_ADDRESS_RESULT_PARAM_ERROR_DOUBLE);
                if(address.hasLong())
                    result.putExtra(AddressFavActivity.FAVOURITE_ADDRESS_RESULT_PARAM_LONG, address.getLongitude());
                else
                    result.putExtra(AddressFavActivity.FAVOURITE_ADDRESS_RESULT_PARAM_LONG, AddressFavActivity.FAVOURITE_ADDRESS_RESULT_PARAM_ERROR_DOUBLE);
                context.setResult(Activity.RESULT_OK, result);
                context.finish();
            }
        });

        ImageButton imageView = (ImageButton) rowView.findViewById(R.id.favLineImageButton);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavouriteAddressDB.getInstance(context).deleteAddressById(address.getId());
                addresses.remove(address);
                context.findViewById(R.id.favListView).requestLayout();
                v.postInvalidate();
            }
        });

        return rowView;
    }
}
