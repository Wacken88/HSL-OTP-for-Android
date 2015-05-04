package edu.usf.cutr.opentripplanner.android.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.usf.cutr.opentripplanner.android.model.AddressModel;

/**
 * Created by Radek on 3/21/15.
 */
public class FavouriteAddressDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "favouriteAddresses";

    private static final String TABLE_ADDRESSES = "addresses";

    public static final String KEY_ID = "_id";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LONG = "longitude";
    public static final String KEY_LAT_BOOL = "latitude_bool";
    public static final String KEY_LONG_BOOL = "longitude_bool";

    private static FavouriteAddressDB mInstance;

    public FavouriteAddressDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ADDRESS_TABLE = "CREATE TABLE " + TABLE_ADDRESSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ADDRESS + " TEXT" + " UNIQUE, "
                + KEY_LAT + " REAL, " + KEY_LONG + " REAL, " + KEY_LAT_BOOL + " INTEGER ," + KEY_LONG_BOOL + " INTEGER)";
        db.execSQL(CREATE_ADDRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESSES);

        // Create tables again
        onCreate(db);
    }

    public static FavouriteAddressDB getInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new FavouriteAddressDB(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public boolean addAddress(AddressModel address) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ADDRESS, address.getAddress());
        values.put(KEY_LAT, address.getLatitude());
        values.put(KEY_LONG, address.getLongitude());
        values.put(KEY_LAT_BOOL, address.hasLat());
        values.put(KEY_LONG_BOOL, address.hasLong());

        Long res = db.insert(TABLE_ADDRESSES, null, values);
        db.close(); // Closing database connection
        return res != -1;
    }

    public boolean isAddressStored(AddressModel address){
        List<AddressModel> addressList = new ArrayList<AddressModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESSES + " WHERE " + KEY_ADDRESS + " = '" + address.getAddress() + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor.getCount() != 0;
    }

    public AddressModel getAddress(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ADDRESSES, new String[] { KEY_ID,
                        KEY_ADDRESS, KEY_LAT, KEY_LONG, KEY_LAT_BOOL, KEY_LONG_BOOL}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new AddressModel(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getInt(4) == 1, cursor.getInt(5) == 1);
    }

    public List<AddressModel> getAllAddresses() {
        List<AddressModel> addressList = new ArrayList<AddressModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESSES + " ORDER BY " + KEY_ADDRESS + " ASC;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AddressModel address = new AddressModel();
                address.setId(Integer.parseInt(cursor.getString(0)));
                address.setAddress(cursor.getString(1));
                address.setLatitude(cursor.getDouble(2));
                address.setLongitude(cursor.getDouble(3));
                address.setLatitudeBool(cursor.getInt(4) == 1);
                address.setLongitudeBool(cursor.getInt(5) == 1);

                addressList.add(address);
            } while (cursor.moveToNext());
        }

        return addressList;
    }

    public void deleteAddressById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADDRESSES, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}
