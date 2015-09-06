package com.dynamicobjx.visitorapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.adapters.SpinnerAdapter;
import com.dynamicobjx.visitorapp.helpers.ParseHelper;
import com.dynamicobjx.visitorapp.models.Visitor;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegistrationActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    @InjectView(R.id.fragment_container) FrameLayout fragmentContainer;
    @InjectView(R.id.basic_layout) LinearLayout llBasicLayout;
    @InjectView(R.id.info_layout) LinearLayout llInfoLayout;
    @InjectView(R.id.btnBasic) Button btnBasic;
    @InjectView(R.id.btnMore) Button btnMore;

    /* widgets on basic information */
    @InjectView(R.id.etFirstName) EditText etFirstName;
    @InjectView(R.id.etLastName) EditText etLastName;
    @InjectView(R.id.etCompany) EditText etCompany;
    @InjectView(R.id.etPosition) EditText etPosition;
    @InjectView(R.id.etContactNo) EditText etContactNo;
    @InjectView(R.id.etEmailAddress) EditText etEmailAddress;
    @InjectView(R.id.etAddress) EditText etAddress;
    @InjectView(R.id.etPassword) EditText etPassword;

    /* widgets on basic information */
    @InjectView(R.id.etCountry) AutoCompleteTextView etCountry;
    @InjectView(R.id.etZipCode) EditText etZipCode;
    @InjectView(R.id.etMobileNumber) EditText etMobileNumber;
    @InjectView(R.id.etHomeNumber) EditText etHomeNumber;
    @InjectView(R.id.etOfficeNumber) EditText etOfficeNumber;
    @InjectView(R.id.etFax) EditText etFax;
    @InjectView(R.id.spinner_age) Spinner spinner_age;


    @InjectView(R.id.spinner_gender) Spinner spinner_gender;
    @InjectView(R.id.spinner_civil_status) Spinner spinner_civil;
    @InjectView(R.id.spinner_job) MultiSelect spinner_job;
    @InjectView(R.id.spinner_type) MultiSelect spinner_type;
    @InjectView(R.id.spinner_interest) MultiSelect spinner_interest;
    @InjectView(R.id.spinner_purpose) MultiSelect spinner_purpose;
    @InjectView(R.id.spinner_size) Spinner spinner_size;
    @InjectView(R.id.spinner_role) MultiSelect spinner_role;
    @InjectView(R.id.spinner_how_did) MultiSelect spinner_how_did;

    private ParseHelper parseHelper;
    private Visitor visitor;
    List<String> list1 = new ArrayList<String>();
    List<String> list2 = new ArrayList<String>();
    List<String> list3 = new ArrayList<String>();

    String[] arrayJob = {"","None","Transportation Science Society of the Philippines", "National Committee on Transport Engineering [NCTE]", "Cambodia Society for Transportation Studies",
            "Society of Transportation and Logistics Studies","Hong Kong Society for Transportation Studies","Indonesia Transportation Society","Korean Society of Transportation","Lao - EASTS",
            "Transportation Science Society of Malaysia","Mongolian Transport Research Society","Committee on Myanmar Transportation Studies","Society of Transport Engineers, Nepal (SOTEN)","EASTS New Zealand","Centre for Transportation Research/Inst of Engineers Singapore","Chinese Institute of Transportation",
            "Thai Society for Transportation and Traffic Studies","Transportation Studies Society of Vietnam"};

    String[] arrayTypeOfBusiness = {"Agent/Dealership","Building/Civil Contacting","Construction Service Provider",
            "Distributionship", "Educational Institution","Electrical/Mechanical System Service Provider","Exporter","Fire and Security Service Provider",
            "Government/Private Institution/Association","Importer","IT Service Provider","Manufacturing","Mass Media Service Provider",
            "Wholesaler/Retailer","Others"};

    String[] arrayMainProductInterest = {"Building Materials, Equipment & Services","Construction Promotion (Mass Media Service Provider)",
            "Government/Private Institutions","Information Technology & Telecommunications","Interior Design & Renovation Products, Services and Technology",
            "Mechanical & Electrical Engineering Systems","Security & Fire Equipment","Others"};

    String [] arrayPurpose = {"Look for Companies","Gather Information","Evaluate Show for Future Participation",
            "Visit Exhibitors/Suppliers","Update on Technologies","Attend WORLDBEX Seminars","Attend Product Forum", "Source of new Products and Services",
            "Meet with Exhibiting Company","Network with New Business Partners","Others"};

    String [] arraySizeOfOrg = {"1-10","11-50","51-100","101-300","301-500","Above 500 persons"};

    String [] arrayRole = {"Decision maker","Specifier","Recommendation Provider","Information Gatherer","Others"};

    String [] arrayHowDidYouLearn = {"Direct Mail of Event Brochure","Newspaper Advertisement","Exhibitor",
            "Business Associate/Colleague","Website","Email","Friends","Poster","Obtained Event Brochure","Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.inject(this);
        initActionBar("Registration").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                animateToRight(RegistrationActivity.this);
            }
        });
        llBasicLayout.setVisibility(View.VISIBLE);
        btnMore.setBackgroundColor(getResources().getColor(R.color.other_gray));
        llInfoLayout.setVisibility(View.GONE);
        ParseObject.registerSubclass(Visitor.class);
        setGroupFont(llBasicLayout,getTfSegoe());
        setGroupFont(llInfoLayout,getTfSegoe());
        parseHelper = new ParseHelper();


        list1.add("18-30");
        list1.add("31-40");
        list1.add("41-50");
        list1.add("51 & above");
        list2.add("Male");
        list2.add("Female");
        list3.add("Single");
        list3.add("Married");
        list3.add("Widow/er");
        list3.add("Separated");

        int itemLayout = android.R.layout.simple_spinner_item;
        int dropDownLayout = android.R.layout.simple_spinner_dropdown_item;

        SpinnerAdapter ageAdapter = new SpinnerAdapter(this,itemLayout,list1,getTfSegoe(), Color.BLACK);
        SpinnerAdapter genderAdapter = new SpinnerAdapter(this,itemLayout,list2,getTfSegoe(), Color.BLACK);
        SpinnerAdapter civilAdapter = new SpinnerAdapter(this,itemLayout,list3,getTfSegoe(), Color.BLACK);
        SpinnerAdapter countryAdapter = new SpinnerAdapter(this,itemLayout,getCountries(),getTfSegoe(), Color.BLACK);

        ageAdapter.setDropDownViewResource(dropDownLayout);
        genderAdapter.setDropDownViewResource(dropDownLayout);
        civilAdapter.setDropDownViewResource(dropDownLayout);
        countryAdapter.setDropDownViewResource(dropDownLayout);

        ArrayAdapter<String> dataAdaptersize = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,arraySizeOfOrg);
        dataAdaptersize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_age.setAdapter(ageAdapter);
        spinner_gender.setAdapter(genderAdapter);
        spinner_civil.setAdapter(civilAdapter);
        spinner_size.setAdapter(dataAdaptersize);
        etCountry.setAdapter(countryAdapter);
        spinner_job.setItems(arrayJob);
        spinner_type.setItems(arrayTypeOfBusiness);
        spinner_interest.setItems(arrayMainProductInterest);
        spinner_purpose.setItems(arrayPurpose);
        spinner_role.setItems(arrayRole);
        spinner_how_did.setItems(arrayHowDidYouLearn);
        addListenerOnSpinnerItemSelection();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("spinner", "na click");
        switch (adapterView.getId()) {
            case R.id.spinner_age:
                Log.d("spinner","age --> " + i);
                break;
            case R.id.spinner_gender:
                Log.d("spinner","gender --> " + i);
                break;
            case R.id.spinner_civil_status:
                Log.d("spinner","civil status --> " + i);
                break;
            case R.id.spinner_job:
                Log.d("spinner","job --> " + adapterView.getItemAtPosition(i).toString());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void addListenerOnSpinnerItemSelection(){
        spinner_age.setOnItemSelectedListener(this);
        spinner_gender.setOnItemSelectedListener(this);
        spinner_civil.setOnItemSelectedListener(this);
        spinner_size.setOnItemSelectedListener(this);
        spinner_job.setOnItemSelectedListener(this);
        spinner_type.setOnItemSelectedListener(this);
        spinner_interest.setOnItemSelectedListener(this);
        spinner_purpose.setOnItemSelectedListener(this);
        spinner_role.setOnItemSelectedListener(this);
        spinner_how_did.setOnItemSelectedListener(this);
    }

    @OnClick(R.id.btnMore)
    public void btnMoreOnClick() {
        if (llInfoLayout.getVisibility() == View.GONE) {
            if (isBasicFormComplete()) {
                if (Patterns.EMAIL_ADDRESS.matcher(etEmailAddress.getText()).matches()) {
                    llInfoLayout.setVisibility(View.VISIBLE);
                    llBasicLayout.setVisibility(View.GONE);
                    btnBasic.setBackgroundColor(getResources().getColor(R.color.other_gray));
                    btnMore.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    showToast("Invalid Email Address!");
                }
            } else {
                showCustomDialog("Warning", "Please fill all fields!");
            }
        }
    }

    @OnClick(R.id.btnBasic)
    public void btnBasicOnClick() {
        if (llBasicLayout.getVisibility() == View.GONE) {
            llInfoLayout.setVisibility(View.GONE);
            llBasicLayout.setVisibility(View.VISIBLE);
            btnMore.setBackgroundColor(getResources().getColor(R.color.other_gray));
            btnBasic.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    @OnClick(R.id.btnNext)
    public void btnNextOnClick() {
        if (!Patterns.EMAIL_ADDRESS.matcher(etEmailAddress.getText()).matches()) {
            showToast("Invalid Email Address!");
        } else {
            if (isBasicFormComplete()) {
                llBasicLayout.setVisibility(View.GONE);
                llInfoLayout.setVisibility(View.VISIBLE);
                btnBasic.setBackgroundColor(getResources().getColor(R.color.other_gray));
                btnMore.setBackgroundColor(getResources().getColor(R.color.white));
            } else {
                showCustomDialog("Warning", "Please fill all fields!");
            }
        }
    }

    @OnClick(R.id.btnSubmit)
    public void btnSubmitOnClick() {


            if (Patterns.EMAIL_ADDRESS.matcher(etEmailAddress.getText()).matches()) {
                if (isNetworkAvailable()) {
                    showProgressDialog("Registration", "Registering new user, Please wait...");

                    ParseUser newUser = new ParseUser();
                    newUser.setEmail(etEmailAddress.getText().toString());
                    newUser.setUsername(etEmailAddress.getText().toString());
                    newUser.setPassword(etPassword.getText().toString());
                    newUser.put("userType","visitor");
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("reg","visitor successfully created!");
                                setValuesToVisitor();
                                ParseUser.getCurrentUser().put("visitor",visitor);
                                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        dismissProgressDialog();
                                        if (e == null) {
                                            Log.d("reg","user successfully created!");
                                            finish();
                                            startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                                            animateToRight(RegistrationActivity.this);
                                        } else {
                                            Log.d("reg","Error --------->  " + e.toString());
                                        }
                                    }
                                });
                            } /*else if (e.getCode() == ParseException.EMAIL_TAKEN) {
                                dismissProgressDialog();
                                showToast("Email already taken!");
                                Log.d("reg","email already taken");
                            }
                            */else if (e.getCode() == ParseException.TIMEOUT) {
                                dismissProgressDialog();
                                showToast("Connection timeout, Please try again!");
                            } else {
                                dismissProgressDialog();
                                showToast("An error occurred while creating new user, Please try again!");
                                Log.d("reg","ERROR --> " + e.getCode() + "   msg ->  " + e.toString());
                            }
                        }
                    });
                } else {
                    showToast("Please turn on your WiFi/Data connection!");
                }
            } else {
                Log.d("reg", "invalid email address");
                showToast("Invalid Email Address!");
            }
        }



    private boolean isBasicFormComplete() {
        boolean result = false;
        if (!etFirstName.getText().toString().trim().isEmpty() &&
                !etLastName.getText().toString().trim().isEmpty() &&
                !etContactNo.getText().toString().trim().isEmpty() &&
                !etEmailAddress.getText().toString().trim().isEmpty() &&
                !etCompany.getText().toString().trim().isEmpty() &&
                !etPosition.getText().toString().trim().isEmpty()) {
            result = true;
        }
        return result;
    }

    private boolean isMoreFormComplete() {
        boolean result = false;
        if (!etMobileNumber.getText().toString().trim().isEmpty() ||
                !etHomeNumber.getText().toString().trim().isEmpty() ||
                !etOfficeNumber.getText().toString().trim().isEmpty()) {
            result = true;
        }
        return result;
    }


    public void setValuesToVisitor() {

        visitor = new Visitor();
        visitor.setFname(etFirstName.getText().toString());
        visitor.setLname(etLastName.getText().toString());
        visitor.setAddress(etAddress.getText().toString());
        visitor.setCompany(etCompany.getText().toString());
        visitor.setContactNo(etContactNo.getText().toString());
        visitor.setCountry(etCountry.getText().toString());
        visitor.setEmail(etEmailAddress.getText().toString());
        visitor.setFaxNo(etFax.getText().toString());
        visitor.setHomeNo(etHomeNumber.getText().toString());
        visitor.setMobileNo(etMobileNumber.getText().toString());
        visitor.setOfficeNo(etOfficeNumber.getText().toString());
        visitor.setZipCode(etZipCode.getText().toString());
        visitor.setPosition(etPosition.getText().toString());
        visitor.setStatus(spinner_civil.getSelectedItem().toString());
        visitor.setAge(spinner_age.getSelectedItem().toString());
        visitor.setGender(spinner_gender.getSelectedItem().toString());

        JSONArray jsnHow = new JSONArray(spinner_how_did.getSelectedIndicies());
        if (spinner_how_did.getValue() != null) {
            jsnHow.put(spinner_how_did.getValue());
        }
        visitor.setHowDid(jsnHow);

        JSONArray jsnJob = new JSONArray(spinner_job.getSelectedIndicies());
        if (spinner_job.getValue() != null) {
            jsnJob.put(spinner_job.getValue());
        }
        visitor.setJobFunction(jsnJob);

        JSONArray jsnProduct = new JSONArray(spinner_interest.getSelectedIndicies());
        if (spinner_interest.getValue() != null) {
            jsnProduct.put(spinner_interest.getValue());
        }
        visitor.setInterest(jsnProduct);

        JSONArray jsnPurpose = new JSONArray(spinner_purpose.getSelectedIndicies());
        if (spinner_purpose.getValue() != null) {
            jsnPurpose.put(spinner_purpose.getValue());
        }
        visitor.setPurpose(jsnPurpose);

        JSONArray jsnRole = new JSONArray(spinner_role.getSelectedIndicies());
        if (spinner_role.getValue() != null) {
            jsnRole.put(spinner_role.getValue());
        }
        visitor.setRole(jsnRole);

        JSONArray jsnBusiness = new JSONArray(spinner_type.getSelectedIndicies());
        if (spinner_type.getValue() != null) {
            jsnBusiness.put(spinner_type.getValue());
        }
        visitor.setBusiness(jsnBusiness);
        visitor.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    updateProgressDialog("Creating new visitor, Almost done");
                    Log.d("reg","visitor successfully created!");
                } else {
                    Log.d("reg","error in creating visitor  -> " + e.toString());
                }
            }
        });
        Log.d("done", "registered");

    }
}
