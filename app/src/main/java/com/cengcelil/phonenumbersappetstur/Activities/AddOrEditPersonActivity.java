package com.cengcelil.phonenumbersappetstur.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.cengcelil.phonenumbersappetstur.Helper.DatabaseHelper;
import com.cengcelil.phonenumbersappetstur.Helper.MaskHelper;
import com.cengcelil.phonenumbersappetstur.Models.PersonModel;
import com.cengcelil.phonenumbersappetstur.R;
import com.cengcelil.phonenumbersappetstur.Utils;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddOrEditPersonActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddOrEditPersonActivity";
    private EditText
            et_name,
            et_surname,
            et_birthday,
            et_email,
            et_number,
            et_note;
    private TextInputLayout
            til_name,
            til_surname,
            til_birthday,
            til_email;
    private TextView tv_number_error;
    private LinearLayout ll_number;

    private Button bt_confirm;
    private RelativeLayout bt_turn_back;
    private DatabaseHelper databaseHelper;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth;
    private Spinner spinner;
    private MaskHelper maskHelper;
    private PersonModel existPersonModel;
    private final String[] codes = {"+1", "+90"};
    private ArrayAdapter<String> country_code_spinner_adapter;
    private Dialog dialog;
    private boolean selection_flag = false;

    private void getExtrasIfAvailable() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            existPersonModel = (PersonModel) bundle.getSerializable("person");

        } else
            Log.d(TAG, "getExtrasIfAvailable: null");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addor_edit_person);
        bindViews();
        initComponents();
        getExtrasIfAvailable();
        fillEmtpyFieldsWithPersonModel(existPersonModel);


    }

    private void bindViews() {
        et_name = findViewById(R.id.name_edittext);
        et_surname = findViewById(R.id.surname_edittext);
        et_birthday = findViewById(R.id.birthday_edittext);
        et_email = findViewById(R.id.email_edittext);
        et_number = findViewById(R.id.number_edittext);
        et_note = findViewById(R.id.note_edittext);

        bt_confirm = findViewById(R.id.confirm_add_person);
        bt_turn_back = findViewById(R.id.btn_turn_back);
        til_name = findViewById(R.id.name_input_layout);
        til_surname = findViewById(R.id.surname_input_layout);
        til_birthday = findViewById(R.id.birthday_input_layout);
        til_email = findViewById(R.id.email_input_layout);
        ll_number = findViewById(R.id.number_layout);
        tv_number_error = findViewById(R.id.errorTextPhoneLayout);
    }

    private void initComponents() {
        databaseHelper = new DatabaseHelper(this);
        initCalendar();
        initDatePickerDialog();
        et_birthday.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);
        bt_turn_back.setOnClickListener(this);
        initCountryCodeSpinner();
        initNumberMask();
        setAllTypeFaces();
        initCustomDialog();

/*
TODO
Make Shadow Drawable for Background of Cardview
 */
    }

    private void initCustomDialog() {
        dialog = new Dialog(this,R.style.CustomDialogMainBackground);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
    }

    private void initDatePickerDialog() {
        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Date date = new Date();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);
                        updateBirthdayValue(calendar.getTime());
                    }
                }, year, month, dayOfMonth);
    }

    private void initDatePickerDialog(Long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(milliseconds);
        calendar.setTime(date);
        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Date date = new Date();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);
                        updateBirthdayValue(calendar.getTime());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setAllTypeFaces() {
        Typeface rubik = ResourcesCompat.getFont(getApplicationContext(), R.font.rubik_regular);
        til_name.setTypeface(rubik);
        til_surname.setTypeface(rubik);
        til_birthday.setTypeface(rubik);
        til_email.setTypeface(rubik);

    }

    private void resetValidationFields() {
        isValidNameOrSurnameField(et_name, til_name);
        isValidNameOrSurnameField(et_surname, til_surname);
        isValidDateField(et_birthday, til_birthday);
        isValidEmailField(et_email, til_email);
        isValidNumberField();
    }

    private void initCalendar() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void initCountryCodeSpinner() {
        spinner = findViewById(R.id.spinner_phone_number);
        country_code_spinner_adapter = new ArrayAdapter<String>(this, R.layout.country_code_spinner_layout, codes);

        country_code_spinner_adapter.setDropDownViewResource(R.layout.country_code_spinner_dropdown_layout);
        spinner.setAdapter(country_code_spinner_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setPhoneNumberMask(country_code_spinner_adapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void initNumberMask() {
        maskHelper = MaskHelper.setMask(spinner.getSelectedItem().toString());
        et_number.addTextChangedListener(maskHelper);
        et_number.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(maskHelper.getMaskLength())
        });
    }


    private void fillEmtpyFieldsWithPersonModel(PersonModel personModel) {
        if (personModel != null) {
            et_name.setText(personModel.getName());
            et_surname.setText(personModel.getSurname());
            et_birthday.setText(Utils.sdf.format(new Date(personModel.getBirth_date())));
            initDatePickerDialog(personModel.getBirth_date());
            et_email.setText(personModel.getEmail());
            spinner.setSelection(country_code_spinner_adapter.getPosition(personModel.getCountry_code()));
            selection_flag = false;
            et_number.setText(personModel.getPhone_number());
            Log.d(TAG, "fillEmtpyFieldsWithPersonModel: " + personModel.getPhone_number());
            et_note.setText(personModel.getNote());
        }
    }

    private void setPhoneNumberMask(String selected) {
        maskHelper.changeMask(selected);
        if (selection_flag) {
            et_number.setText("");
        } else
            selection_flag = true;

        et_number.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(maskHelper.getMaskLength())
        });
    }

    private void updateBirthdayValue(Date date) {
        et_birthday.setText(Utils.sdf.format(date));
    }

    private String getClearString(EditText e) {
        return e.getText().toString().trim();
    }


    private boolean isValidNumberField() {
        String str_number = getClearString(et_number);
        switch (maskHelper.compareLength(str_number)) {
            case Utils
                    .NO_CHARACTER_CODE: {
                setError(getString(R.string.validation_required));
                return false;

            }
            case Utils.NEED_CHARACTER_CODE: {
                setError(getString(R.string.validation_fill_empty_field));
                return false;
            }
            case Utils.OK_CHARACTER_CODE: {
                hideError();
                return true;
            }
            default:
                setError(getString(R.string.validation_unknown_error));
                return false;
        }
    }

    private boolean isValidDateField(EditText et_birthday, TextInputLayout til_birthday) {
        String string = getClearString(et_birthday);

        if (TextUtils.isEmpty(string)) {
            setError(et_birthday, til_birthday, getString(R.string.validation_required));
            return false;
        } else {
            hideError(et_birthday, til_birthday);
            return true;
        }
    }

    private boolean isValidEmailField(EditText et_email, TextInputLayout til_email) {
        String string = getClearString(et_email);
        if (TextUtils.isEmpty(string)) {
            setError(et_email, til_email, getString(R.string.validation_required));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(string).matches()) {
            setError(et_email, til_email, getString(R.string.validation_not_correct_format));
            return false;
        } else if (string.length() > 60) {
            setError(et_email, til_email, getString(R.string.validation_more_60_char));
            return false;
        } else {
            hideError(et_email, til_email);
            return true;
        }
    }

    private boolean isValidNameOrSurnameField(EditText edittext, TextInputLayout textInputLayout) {
        String string = getClearString(edittext);
        if (string.length() < 2) {
            setError(edittext, textInputLayout, getResources().getString(R.string.validation_less_2_char));
            return false;

        } else if (string.length() > 20) {
            setError(edittext, textInputLayout, getResources().getString(R.string.validation_more_20_char));
            return false;
        } else if (!string.matches("[a-zA-Z ]+")) {
            setError(edittext, textInputLayout, getResources().getString(R.string.validation_only_text));
            return false;
        } else {
            hideError(edittext, textInputLayout);
            return true;
        }
    }

    private void setError(EditText edittext, TextInputLayout textInputLayout, String str) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            edittext.setBackground(ContextCompat.getDrawable(this, R.drawable.main_input_background_error));
        } else
            edittext.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.main_input_background_error, null));
        edittext.requestFocus();
        textInputLayout.setError(str);
    }

    private void setError(String string) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ll_number.setBackground(ContextCompat.getDrawable(this, R.drawable.main_input_background_error));
        } else
            ll_number.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.main_input_background_error, null));
        tv_number_error.setText(string);
        tv_number_error.setVisibility(View.VISIBLE);
    }

    private void hideError(EditText editText, TextInputLayout textInputLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setBackground(ContextCompat.getDrawable(this, R.drawable.main_input_background));
            textInputLayout.setErrorEnabled(false);
        } else
            editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.main_input_background, null));

    }

    private void hideError() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ll_number.setBackground(ContextCompat.getDrawable(this, R.drawable.main_input_background));
        } else
            ll_number.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.main_input_background, null));
        tv_number_error.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        //For Edittext Birth Date
        switch (view.getId()) {
            case R.id.birthday_edittext:
                datePickerDialog.show();
                break;
            case R.id.confirm_add_person:
                resetValidationFields();
                if (isValidNameOrSurnameField(et_name, til_name) &&
                        isValidNameOrSurnameField(et_surname, til_surname) &&
                        isValidDateField(et_birthday, til_birthday) &&
                        isValidEmailField(et_email, til_email) &&
                        isValidNumberField()

                ) {
                    PersonModel personModel = new PersonModel();
                    personModel.setName(getClearString(et_name));
                    personModel.setSurname(getClearString(et_surname));
                    try {
                        personModel.setBirth_date(Utils.sdf.parse(getClearString(et_birthday)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    personModel.setEmail(getClearString(et_email));
                    personModel.setCountry_code(spinner.getSelectedItem().toString());
                    personModel.setPhone_number(getClearString(et_number));
                    personModel.setNote(getClearString(et_note));
                    if (existPersonModel==null)
                        savePersonWithPersonModel(personModel);
                    else
                        updatePersonWithIdAndPersonModel(existPersonModel.getId(),personModel);
                }
                break;
            case R.id.btn_turn_back:
                onBackPressed();

        }
    }

    private void updatePersonWithIdAndPersonModel(int id, PersonModel personModel) {
        long result = databaseHelper.updatePersonWithIdAndPersonModel(id,personModel);
        if (result==1)
            showDialogWithLayout(R.layout.success_dialog_layout,getString(R.string.update_success_message));
        else
            showDialogWithLayout(R.layout.error_dialog_layout,getString(R.string.update_error_message));
    }

    private void showDialogWithLayout(int layout, String string) {
        dialog.setContentView(layout);
        ((TextView)dialog.findViewById(R.id.tv_message)).setText(string);
        dialog.show();
    }

    public void savePersonWithPersonModel(PersonModel personModel)
    {
        long result = databaseHelper.addPersonWithPersonModel(personModel);
        if (result!=-1)
            showDialogWithLayout(R.layout.success_dialog_layout,getString(R.string.save_success_message));
        else
            showDialogWithLayout(R.layout.error_dialog_layout,getString(R.string.save_error_message));
    }


}