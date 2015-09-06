package com.dynamicobjx.visitorapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.adapters.SpinnerAdapter;
import com.dynamicobjx.visitorapp.helpers.Prefs;
import com.dynamicobjx.visitorapp.listeners.ActionTabListener;
import com.dynamicobjx.visitorapp.models.Categories;
import com.dynamicobjx.visitorapp.models.Event;
import com.dynamicobjx.visitorapp.models.EventMap;
import com.dynamicobjx.visitorapp.models.EventSchedule;
import com.dynamicobjx.visitorapp.models.Exhibitor;
import com.dynamicobjx.visitorapp.models.MergedBookMarks;
import com.dynamicobjx.visitorapp.models.News;
import com.dynamicobjx.visitorapp.models.PhotoVideo;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by rsbulanon on 2/3/15.
 */
public class BaseActivity extends AppCompatActivity {

    private static Typeface TF_SEGOE;
    private static Typeface TF_BOOTSTRAP;
    private ProgressDialog progressDialog;
    private static JSONObject visitorJSON = new JSONObject();
    private static ArrayList<News> myNews = new ArrayList<>();
    private static ArrayList<Exhibitor> myExhibitors = new ArrayList<>();
    private static ArrayList<Categories> categoriesArrayList = new ArrayList<>();
    private static ArrayList<Event> myEvents = new ArrayList<>();
    private static ArrayList<String> myFavorites = new ArrayList<>();
    private static ArrayList<String> myProductFavorites = new ArrayList<>();
    private static ArrayList<ParseObject> myVisitor = new ArrayList<>();
    private static ArrayList<MergedBookMarks> myMergedBookMarks = new ArrayList<>();
    private static ArrayList<EventSchedule> myEventSchedule = new ArrayList<>();
    private static ArrayList<PhotoVideo> myPhotoVidoes = new ArrayList<>();
    private static ArrayList<EventMap> myEventMaps = new ArrayList<>();
    private static String hashTag = "";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm a");
    private static String officialFanPage = "";
    private static HashMap<String,ArrayList<Exhibitor>> categoriesExhibitor = new HashMap<>();

    public void resetLists() {
        Prefs.setMyStringPref(this,"visitor_id","");
        Prefs.setMyStringPref(this,"object_id","");
        Prefs.setMyStringPref(this,"fname","");
        Prefs.setMyStringPref(this,"lname","");
        Prefs.setMyStringPref(this,"contact_no","");
        Prefs.setMyStringPref(this,"email","");
        Prefs.setMyStringPref(this, "company_name", "");
        myNews.clear();
        myExhibitors.clear();
        categoriesArrayList.clear();
        myEvents.clear();
        myVisitor.clear();
        myFavorites.clear();
        myProductFavorites.clear();
        myMergedBookMarks.clear();
        myPhotoVidoes.clear();
        myEventSchedule.clear();
        myEventMaps.clear();
        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }
    }

    public void initFonts() {
        if (TF_SEGOE == null) {
            TF_SEGOE = Typeface.createFromAsset(getAssets(),"fonts/segoe-ui-light.ttf");
            TF_BOOTSTRAP = Typeface.createFromAsset(getAssets(),"fonts/fontawesome-webfont.ttf");
        }
    }

    public void initActionBar() {
        initFonts();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setCustomView(R.layout.custom_home_actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowHomeEnabled(false);
        setFont(TF_SEGOE,getBtnMyQr());
        setFont(TF_SEGOE,getBtnQrScanner());
        setFont(TF_BOOTSTRAP,getTvOptionsMenu());
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
    }

    public LinearLayout initActionBar(String header) {
        initFonts();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setCustomView(R.layout.custom_actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowHomeEnabled(false);
        setFont(TF_SEGOE, getTvHeader());
        setFont(TF_BOOTSTRAP, getTvBack());
        setFont(TF_BOOTSTRAP, getTvHome());
        getTvHeader().setText(header);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        return (LinearLayout)findViewById(R.id.llCustomActioBar);
    }

    public LinearLayout initActionBarWithSearch(String header) {
        initFonts();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setCustomView(R.layout.custom_actionbar_withsearch);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowHomeEnabled(false);
        setFont(TF_SEGOE, getTvHeader());
        setFont(TF_SEGOE, getEtSearch());
        setFont(TF_BOOTSTRAP, getTvBack());
        setFont(TF_BOOTSTRAP, getTvHome());
        setFont(TF_BOOTSTRAP, getTvSearch());
        getTvHeader().setText(header);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        return (LinearLayout)findViewById(R.id.llCustomActioBar);
    }

    public LinearLayout initActionBarWithSpinner(String header, ArrayList<String> maps) {
        initFonts();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setCustomView(R.layout.custom_action_bar_with_spinner);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowHomeEnabled(false);
        setFont(TF_SEGOE, getTvHeader());
        setFont(TF_BOOTSTRAP, getTvBack());
        setFont(TF_BOOTSTRAP, getTvHome());
        getTvHeader().setText(header);
        int itemLayout = android.R.layout.simple_spinner_item;
        int dropDownLayout = android.R.layout.simple_spinner_dropdown_item;
        SpinnerAdapter mapAdapter = new SpinnerAdapter(this,itemLayout,maps,getTfSegoe(),Color.WHITE);
        mapAdapter.setDropDownViewResource(dropDownLayout);
        getMapSpinner().setAdapter(mapAdapter);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        return (LinearLayout)findViewById(R.id.llCustomActioBar);
    }

    public Spinner getMapSpinner() { return (Spinner)findViewById(R.id.spiner_map);}

    /** check network availability */
    public boolean isNetworkAvailable() {
        boolean isConnected = false;
        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            isConnected = true;
        } else {
            NetworkInfo mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            boolean isDataEnabled = mData.isConnected();
            isConnected = isDataEnabled ? true : false;
        }
        return isConnected;
    }

    public  Bitmap trimDownImgReso(Resources res, int resId, int size) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = size;
        options.inDither=false;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public Bitmap resizeImage(byte[] data,int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length,options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length,options);
    }

    public void initActionBarTab(ArrayList<Fragment> fragments, ArrayList<String> tabName) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (int i = 0 ; i < tabName.size() ; i++) {
            ActionBar.Tab tab = actionBar.newTab().setText(tabName.get(i));

            tab.setTabListener(new ActionTabListener(fragments.get(i)));
            actionBar.addTab(tab);
            actionBar.setStackedBackgroundDrawable(new ColorDrawable(
                    this.getResources().getColor(R.color.metro_blue)));
        }
        //hide default android icon
        actionBar.setLogo(null);

    }

    public TextView getBtnMyQr() {return (TextView)findViewById(R.id.tvMyQR); }

    public TextView getTvSearch() {return (TextView)findViewById(R.id.tvSearch); }

    public EditText getEtSearch() { return (EditText)findViewById(R.id.etSearch); }

    public TextView getBtnQrScanner() {return (TextView)findViewById(R.id.tvQRScanner); }

    public TextView getTvOptionsMenu() { return (TextView)findViewById(R.id.tvOptionsMenu); }

    public TextView getTvBack() { return (TextView)findViewById(R.id.tvBack); }

    public TextView getTvHome() { return (TextView)findViewById(R.id.tvHome); }

    public TextView getTvHeader() { return (TextView)findViewById(R.id.tvHeader); }

    public LinearLayout getllMyCode() {return (LinearLayout)findViewById(R.id.llMyCode);}

    public LinearLayout getllQrScanner() {return (LinearLayout)findViewById(R.id.llQrScanner);}

    public void animateToLeft(Activity activity) {
        activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public Typeface getTfSegoe() { return TF_SEGOE; }

    public Typeface getTfBootstrap() { return TF_BOOTSTRAP; }

    public void animateToRight(Activity activity) {
        activity.overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
    }

    public void setGroupFont(ViewGroup parent, Typeface typeFace) {
        for(int i = 0 ; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view instanceof TextView || view instanceof EditText || view instanceof Button) {
                ((TextView)view).setTypeface(typeFace);
            } else if (view instanceof LinearLayout || view instanceof RelativeLayout || view instanceof FrameLayout) {
                setGroupFont((ViewGroup)view,typeFace);
            }
        }
    }


    public void setFont(Typeface font, TextView view) {
        view.setTypeface(font);
    }
    public void showProgressDialog(String title, String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void updateProgressDialog(String message) {
        progressDialog.setMessage(message);
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public void showCustomDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Close",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void showToast(String message) { Toast.makeText(this,message,Toast.LENGTH_SHORT).show(); }

    public ArrayList<Exhibitor> getExhibitorList() {
        return myExhibitors;
    }

    public  Bitmap trimDownImgReso(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public ArrayList<String> getCountries() {
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        return countries;
    }

    public JSONObject getVisitorJSON() { return visitorJSON; }

    public void contructVisitorJSON() {
        try {
            String visitor_id = Prefs.getMyStringPrefs(this,"visitor_id");
            String fname =  Prefs.getMyStringPrefs(this, "fname");
            String lname = Prefs.getMyStringPrefs(this,"lname");
            String contact_no = Prefs.getMyStringPrefs(this,"contact_no");
            String email = Prefs.getMyStringPrefs(this,"email");
            String company_name = Prefs.getMyStringPrefs(this,"company_name");

            //visitorJSON.put("visitor_id",visitor_id);
            visitorJSON.put("f",fname);
            visitorJSON.put("l",lname);
            visitorJSON.put("m",contact_no);
            visitorJSON.put("e",email);
            visitorJSON.put("c",company_name);
            Log.d("qr", "my json --> " + visitorJSON.toString());
        } catch (Exception e) {
            Log.d("qr", "error ->  " + e.toString());
        }
    }

    public Exhibitor getExhibitorById(String id) {
        Exhibitor exhibitor = null;
        for (Exhibitor e : getExhibitorList()) {
            if (e.getObjectId().equals(id)) {
                exhibitor = e;
                break;
            }
        }
        return exhibitor;
    }

    public Categories getCategoryById(String id) {
        Categories categories = null;
        for (Categories c : getCategoriesList()) {
            Log.d("mb","cc --> " + c.getObjectId() + "  -->  " + id);
            if (c.getObjectId().equals(id)) {
                categories = c;
                break;
            }
        }
        return categories;
    }

    public Exhibitor getExhibitorByEmail(String email) {
        Exhibitor exhibitor = null;
        for (Exhibitor e : getExhibitorList()) {
            if (e.getEmail() != null) {
                if (e.getEmail().equals(email)) {
                    exhibitor = e;
                }
            }
        }
        return exhibitor;
    }

    public void getMyProductBookMarks() {
        ParseObject visitor = getVisitor().get(0);
        getMyProductFavorites().clear();

        if (visitor.get("myProductBookMark") != null) {
            Log.d("mb","my product book mark not null size");
            for (Object o : visitor.getList("myProductBookMark")) {
                Log.d("mb","o --> " + o.toString());
                try {
                    getMyProductFavorites().add(o.toString().trim());
                } catch (Exception ex) {
                    Log.d("myFave", "error ----> " + ex.toString());
                }
            }
        }
    }
    public static void toggleSoftKeyboard(Context context, boolean show) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!show) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
        }  else {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT); // show
        }
    }

    public void getMyBookMarks() {
        ParseObject visitor = getVisitor().get(0);
        getMyFavorites().clear();

        if (visitor.get("myBookMarked") != null) {
            for (Object o : visitor.getList("myBookMarked")) {
                try {
                    getMyFavorites().add(o.toString().trim());
                } catch (Exception ex) {
                    Log.d("myFave", "error ----> " + ex.toString());
                }
            }
        }
    }

    public void sortArrayList(ArrayList<Exhibitor> data) {
        Collections.sort(data, new Comparator<Exhibitor>() {
            @Override
            public int compare(Exhibitor e1, Exhibitor e2) {
                return e1.getCompanyName().toLowerCase()
                        .compareTo(e2.getCompanyName().toLowerCase());
            }
        });
    }

    public void sortByPackageType(ArrayList<Exhibitor> data) {
        Collections.sort(data, new Comparator<Exhibitor>() {
            @Override
            public int compare(Exhibitor e1, Exhibitor e2) {
                return e1.getPackageType().compareTo(e2.getPackageType());
            }
        });
    }

    public void sortMyVisit(ArrayList<MergedBookMarks> data) {
        Collections.sort(data, new Comparator<MergedBookMarks>() {
            @Override
            public int compare(MergedBookMarks e1, MergedBookMarks e2) {
                return e1.getName().compareTo(e2.getName());}
        });
    }

    public void sortArrayListString(ArrayList<String> data) {
        Collections.sort(data, new Comparator<String>() {
            @Override
            public int compare(String e1, String e2) {
                return e1.compareTo(e2);}
        });
    }

    public void sortCategories(ArrayList<Categories> data) {
        Collections.sort(data, new Comparator<Categories>() {
            @Override
            public int compare(Categories e1, Categories e2) {
                return e1.getName().compareTo(e2.getName());}
        });
    }

    public HashMap<String,ArrayList<Exhibitor>> getCategoriesExhibitor() { return categoriesExhibitor; }

    public ArrayList<Categories> getCategoriesList() { return categoriesArrayList; }

    public ArrayList<News> getNewsList() { return myNews; }

    public ArrayList<Event> getMyEvents() { return myEvents; }

    public String getHashTag() { return hashTag; }

    public void setHashTag(String hashTag) { this.hashTag = hashTag; }

    public static ArrayList<String> getMyFavorites() { return myFavorites; }

    public static ArrayList<ParseObject> getVisitor() { return myVisitor;}

    public void setOfficialFanPage(String officialFanPage) { this.officialFanPage = officialFanPage; }

    public static String getOfficialFanPage() { return officialFanPage; }

    public static ArrayList<MergedBookMarks> getMyMergedBookMarks () { return myMergedBookMarks; }

    public static ArrayList<String> getMyProductFavorites() { return myProductFavorites; }

    public static ArrayList<PhotoVideo> getMyPhotoVidoes() { return myPhotoVidoes; }

    public static ArrayList<EventSchedule> getMyEventSchedule() { return myEventSchedule; }

    public static SimpleDateFormat getDateFormat() { return dateFormat;}

    public static ArrayList<EventMap> getMyEventMaps() { return myEventMaps; }
}
