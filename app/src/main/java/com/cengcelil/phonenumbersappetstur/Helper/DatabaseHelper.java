package com.cengcelil.phonenumbersappetstur.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cengcelil.phonenumbersappetstur.Models.PersonModel;
import com.cengcelil.phonenumbersappetstur.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "persons.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Person";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String SURNAME_COLUMN = "surname";
    private static final String BIRTHDAY_COLUMN = "birthday";
    private static final String EMAIL_COLUMN = "email";
    private static final String COUNTRY_CODE_COLUMN = "country_code";
    private static final String NUMBER_COLUMN = "number";
    private static final String NOTE_COLUMN = "note";
    private static final String CREATED_AT_COLUMN = "created_at";
    private SQLiteDatabase database;

    String createTableQuery =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME_COLUMN + " TEXT, " +
                    SURNAME_COLUMN + " TEXT, " +
                    BIRTHDAY_COLUMN + " INTEGER, " +
                    COUNTRY_CODE_COLUMN + " TEXT, " +
                    EMAIL_COLUMN + " TEXT, " +
                    NUMBER_COLUMN + " TEXT, " +
                    NOTE_COLUMN + " TEXT, " +
                    CREATED_AT_COLUMN + " TEXT DEFAULT CURRENT_TIMESTAMP" +
                    ")";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public long addPersonWithPersonModel(PersonModel personModel)
    {
        database = this.getWritableDatabase();
        fillContentValuesWithPersonModel(personModel);
        ContentValues contentValues = fillContentValuesWithPersonModel(personModel);
        long result = database.insert(TABLE_NAME, null, contentValues);
        database.close();
        return result;
    }

    private ContentValues fillContentValuesWithPersonModel(PersonModel personModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COLUMN,personModel.getName());
        contentValues.put(SURNAME_COLUMN,personModel.getSurname());
        contentValues.put(BIRTHDAY_COLUMN,personModel.getBirth_date());
        contentValues.put(EMAIL_COLUMN,personModel.getEmail());
        contentValues.put(COUNTRY_CODE_COLUMN,personModel.getCountry_code());
        contentValues.put(NUMBER_COLUMN,personModel.getPhone_number());
        contentValues.put(NOTE_COLUMN,personModel.getNote());
        return contentValues;
    }

    public ArrayList<PersonModel> getPersonsOrderedWithFilter(String filtered){
        ArrayList<PersonModel> models = new ArrayList<>();
        database = this.getReadableDatabase();
        String[] select = {ID_COLUMN,NAME_COLUMN,SURNAME_COLUMN,BIRTHDAY_COLUMN,EMAIL_COLUMN,COUNTRY_CODE_COLUMN,NUMBER_COLUMN,NOTE_COLUMN};

        String where = NAME_COLUMN+" LIKE ? or "+SURNAME_COLUMN+" LIKE ?";

        String[] whereArgs = new String[]{"%"+filtered+"%","%"+filtered+"%"};
        String orderByWithName = NAME_COLUMN+" ASC";
        Cursor cursor = database.query(TABLE_NAME,select,where,whereArgs,null,null,orderByWithName);
        while (cursor.moveToNext()) {
            PersonModel personModel = new PersonModel();
            personModel.setId(cursor.getInt(cursor.getColumnIndex(ID_COLUMN)));
            personModel.setName(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
            personModel.setSurname(cursor.getString(cursor.getColumnIndex(SURNAME_COLUMN)));
            personModel.setBirth_date(new Date(cursor.getLong(cursor.getColumnIndex(BIRTHDAY_COLUMN))));
            personModel.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL_COLUMN)));
            personModel.setCountry_code(cursor.getString(cursor.getColumnIndex(COUNTRY_CODE_COLUMN)));
            personModel.setPhone_number(cursor.getString(cursor.getColumnIndex(NUMBER_COLUMN)));
            personModel.setNote(cursor.getString(cursor.getColumnIndex(NOTE_COLUMN)));
            models.add(personModel);
        }
        Log.d(TAG, "getPersonsOrderedWithFilter: ");
        return models;
    }
    public void deleteAllPersons(){
        database = getWritableDatabase();
        database.execSQL("delete from "+ TABLE_NAME);

    }

    public long updatePersonWithIdAndPersonModel(int id, PersonModel personModel) {
        database = getWritableDatabase();
        ContentValues contentValues = fillContentValuesWithPersonModel(personModel);
        return database.update(TABLE_NAME, contentValues, ID_COLUMN + "= ?", new String[] {String.valueOf(id)});

    }

    public long deletePersonWithId(int id) {
        return database.delete(TABLE_NAME, ID_COLUMN + "= ?", new String[] {String.valueOf(id)});
    }
}
