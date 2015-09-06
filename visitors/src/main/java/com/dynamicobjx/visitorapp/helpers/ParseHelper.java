package com.dynamicobjx.visitorapp.helpers;

import android.util.Log;

import com.dynamicobjx.visitorapp.listeners.OnInsertaParseObjectListener;
import com.dynamicobjx.visitorapp.listeners.OnLoadDataListener;
import com.dynamicobjx.visitorapp.listeners.OnLoginListener;
import com.dynamicobjx.visitorapp.listeners.OnParseQueryListener;
import com.dynamicobjx.visitorapp.models.Categories;
import com.dynamicobjx.visitorapp.models.Event;
import com.dynamicobjx.visitorapp.models.EventMap;
import com.dynamicobjx.visitorapp.models.EventSchedule;
import com.dynamicobjx.visitorapp.models.Exhibitor;
import com.dynamicobjx.visitorapp.models.News;
import com.dynamicobjx.visitorapp.models.PhotoVideo;
import com.google.gson.Gson;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by rsbulanon on 2/3/15.
 */

public class ParseHelper {

    private final static String APP_ID = "tgrxqUH3aPiQMOV7BdRdCQFpFM21iaU2AFw10nCR";
    private final static String CLIENT_KEY = "yfKEiiAc7nKEMRSc77VCRzGFssQJpEGubyIG6AYy";
    private OnParseQueryListener parseQueryListener;
    private OnLoginListener onLoginListener;
    private OnLoadDataListener onLoadDataListener;
    private OnInsertaParseObjectListener onInsertaParseObjectListener;

    public void setOnParseQueryListener(OnParseQueryListener parseQueryListener) {
        this.parseQueryListener = parseQueryListener;
    }

    public void setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    public void setOnLoadDataListener(OnLoadDataListener onLoadDataListener) {
        this.onLoadDataListener = onLoadDataListener;
    }

    public void setOnInsertaParseObjectListener(OnInsertaParseObjectListener onInsertaParseObjectListener) {
        this.onInsertaParseObjectListener = onInsertaParseObjectListener;
    }

    public void getNews(final ArrayList<News> news, boolean fromCache) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("News");
        query.orderByDescending("publish_date");
        if (fromCache) {
            query.fromLocalDatastore();
        }
        if (onLoadDataListener != null) {
            onLoadDataListener.onPreLoad();
        }
        query.setLimit(Integer.MAX_VALUE);
        query.findInBackground().continueWithTask(new Continuation<List<ParseObject>, Task<Void>>() {
            @Override
            public Task<Void> then(final Task<List<ParseObject>> results) throws Exception {
                if (results.isCompleted()) {
                    ParseObject.unpinAllInBackground("news",new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground("news",results.getResult());
                                if (results.getResult() == null) {
                                    Log.d("parse","news walang nahugot");
                                } else {
                                    Log.d("parse","news nahugot --> " + results.getResult().size());
                                }
                                for (ParseObject obj : results.getResult()) {
                                    String headline = obj.getString("headline");
                                    String body = obj.getString("body");
                                    Date publishDate = obj.getDate("publish_date");
                                    news.add(new News(headline, body, publishDate));
                                }
                            }
                        }
                    });
                } else {
                    Log.d("parse", "failed in getting news --> " + results.getError().toString());
                }
                return null;
            }
        }).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> voidTask) throws Exception {
                if (onLoadDataListener != null) {
                    onLoadDataListener.onLoadFinished(1);
                }
                return null;
            }
        });
    }

    public void getMedia(final ArrayList<PhotoVideo> photoVideo, boolean isFromCache) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Media");

        if (isFromCache) {
            query.fromLocalDatastore();
        }
        query.setLimit(Integer.MAX_VALUE);
        photoVideo.clear();
        query.findInBackground().continueWithTask(new Continuation<List<ParseObject>, Task<Void>>() {
            @Override
            public Task<Void> then(final Task<List<ParseObject>> results) throws Exception {
                if (results.isCompleted()) {
                    ParseObject.unpinAllInBackground("media", new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground("media", results.getResult());

                                if (results.getResult() == null) {
                                    Log.d("parse","media walang nahugot");
                                } else {
                                    Log.d("parse","media nahugot --> " + results.getResult().size());
                                }
                                for (ParseObject obj : results.getResult()) {
                                    String objectId = obj.getObjectId();
                                    String fileName = obj.getString("filename");
                                    String mediaType = obj.getString("media_type");
                                    ParseFile asset = obj.getParseFile("asset");
                                    photoVideo.add(new PhotoVideo(objectId, fileName, mediaType,asset));
                                }
                            } else {
                                Log.d("parse", "error in getting event --> " + e.toString());
                            }
                        }
                    });
                } else {
                    Log.d("parse", "failed in getting event --> " + results.getError().toString());
                }
                return null;
            }
        }).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> voidTask) throws Exception {
                if (onLoadDataListener != null) {
                    onLoadDataListener.onLoadFinished(1);
                }
                return null;
            }
        });
    }

    public void getVisitor(String visitorId, boolean isFromCache, final ArrayList<ParseObject> visitor) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Visitor");
        Log.d("parse","get visitor by id -->>  " + visitorId);
        query.setLimit(Integer.MAX_VALUE);
        query.whereEqualTo("objectId",visitorId);
        if (isFromCache) {
            query.fromLocalDatastore();
        }
        visitor.clear();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, ParseException e) {
                visitor.clear();
                if (e == null) {
                    Log.d("parse","on visitor found --> " + parseObject.getString("fname"));
                    if (onLoadDataListener != null) {
                        ParseObject.unpinAllInBackground("visitor", new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    visitor.add(parseObject);
                                    parseObject.pinInBackground("visitor");
                                    onLoadDataListener.onLoadFinished(1);
                                }
                            }
                        });
                    }
                } else {
                    Log.d("parse","failed in getting visitors --> " + e.toString());
                }
            }
        });
    }

    public void getEvent(final ArrayList<EventSchedule> schedules, final ArrayList<Event>  events,
                         final ArrayList<EventMap> maps, final boolean isFromCache) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        if (isFromCache) {
            query.fromLocalDatastore();
        }
        query.setLimit(Integer.MAX_VALUE);
        query.findInBackground().continueWithTask(new Continuation<List<ParseObject>, Task<Void>>() {
            @Override
            public Task<Void> then(final Task<List<ParseObject>> results) throws Exception {
                if (results.isCompleted()) {
                    ParseObject.unpinAllInBackground("event", new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground("event", results.getResult());
                                ArrayList<Task<List<ParseObject>>> scheduelTask = new ArrayList<>();
                                ArrayList<Task<List<ParseObject>>> mapTask = new ArrayList<>();
                                events.clear();
                                schedules.clear();
                                maps.clear();
                                if (results.getResult() == null) {
                                    Log.d("parse","event walang nahugot");
                                } else {
                                    Log.d("parse","event nahugot --> " + results.getResult().size());
                                }

                                for (ParseObject obj : results.getResult()) {
                                    String objectId = obj.getObjectId();
                                    String title = obj.getString("title");
                                    Date eventDate = obj.getDate("eventDate");
                                    String venue = obj.getString("venue");
                                    String description = obj.getString("description");
                                    String hashtag = obj.getString("hashTag");
                                    String fbPage = obj.getString("fb_Page");
                                    String aboutUs = obj.getString("aboutUs");
                                    Event event = new Event(objectId, title, eventDate, venue,
                                            description,hashtag,fbPage,aboutUs);
                                    events.add(event);
                                    scheduelTask.add(getSchedule(obj,schedules,isFromCache));
                                    mapTask.add(getMap(obj,maps,isFromCache));
/*                                    schedules.add(new Schedules(objectId,event,dateFormat.format(eventDate),
                                            Long.parseLong(new SimpleDateFormat("D").format(eventDate))));*/
                                }
                            } else {
                                Log.d("parse", "error in getting event --> " + e.toString());
                            }
                        }
                    });
                } else {
                    Log.d("parse", "failed in getting event --> " + results.getError().toString());
                }
                return null;
            }
        }).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> voidTask) throws Exception {
                if (onLoadDataListener != null) {
                    onLoadDataListener.onLoadFinished(1);
                }
                return null;
            }
        });
    }

    public Task<List<ParseObject>> getMap(ParseObject event, final ArrayList<EventMap> maps, boolean isFromCache) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FloorMap");
        final Task<List<ParseObject>>.TaskCompletionSource tcs = Task.create();
        query.whereEqualTo("event_id",event);
        if (isFromCache) {
            query.fromLocalDatastore();
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> results, ParseException e) {
                final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
                if (e == null) {
                    ParseObject.unpinAllInBackground("maps",new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if ( e == null) {
                                ParseObject.pinAllInBackground("maps",results);
                                for (ParseObject obj : results) {
                                    maps.add(new EventMap(obj.getParseFile("asset"),obj.getString("filename")));
                                }
                                tcs.setResult(results);
                            }
                        }
                    });
                } else {
                    tcs.setError(e);
                }
            }
        });
        return tcs.getTask();
    }

    public Task<List<ParseObject>> getSchedule(ParseObject event, final ArrayList<EventSchedule> schedule, boolean isFromCache) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
        final Task<List<ParseObject>>.TaskCompletionSource tcs = Task.create();
        if (isFromCache) {
            query.fromLocalDatastore();
        }
        query.orderByDescending("startDate");
        query.whereEqualTo("event_id",event);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> results, ParseException e) {
                final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
                if (e == null) {
                    ParseObject.unpinAllInBackground("schedule",new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground("schedule", results);
                                for (ParseObject obj : results) {
                                    String objectId = obj.getObjectId();
                                    String title = obj.getString("title");
                                    String description = obj.getString("description");
                                    Date startDate = obj.getDate("startDate");
                                    Date endDate = obj.getDate("endDate");
                                    String roomNumber = obj.getString("room_number");
                                    schedule.add(new EventSchedule(objectId, title, description, startDate,
                                            endDate,roomNumber,Long.parseLong(new SimpleDateFormat("D").format(startDate))));
                                }
                                tcs.setResult(results);
                            }
                        }
                    });
                } else {
                    tcs.setError(e);
                }
            }
        });
        return tcs.getTask();
    }

    public void getCategories(final ArrayList<Categories> categories, boolean isFromCache) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
        query.orderByAscending("name");
        //query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        if (onLoadDataListener != null) {
            onLoadDataListener.onPreLoad();
        }
        if (isFromCache) {
            query.fromLocalDatastore();
        }
        query.setLimit(Integer.MAX_VALUE);
        query.findInBackground().continueWithTask(new Continuation<List<ParseObject>, Task<Void>>() {
            @Override
            public Task<Void> then(final Task<List<ParseObject>> results) throws Exception {
                if (results.isCompleted()) {
                    ParseObject.unpinAllInBackground("categories", new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground("categories", results.getResult());
                                categories.clear();
                                if (results.getResult() == null) {
                                    Log.d("parse","category walang nahugot");
                                } else {
                                    Log.d("parse","category nahugot --> " + results.getResult().size());
                                }
                                for (ParseObject obj : results.getResult()) {
                                    categories.add(new Categories(obj.getObjectId(),obj.getString("name"),false));
                                }
                            } else {
                                Log.d("parse", "error in getting categories --> " + e.toString());
                            }
                        }
                    });
                } else {
                    Log.d("parse", "failed in getting category --> " + results.getError().toString());
                }
                return null;
            }
        }).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> voidTask) throws Exception {
                if (onLoadDataListener != null) {
                    onLoadDataListener.onLoadFinished(1);
                }
                return null;
            }
        });
    }

    public void getExhibitors(final ArrayList<Exhibitor> exhibitors, boolean isFromCache) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Exhibitor");
        query.setLimit(Integer.MAX_VALUE);

        if (onLoadDataListener != null) {
            onLoadDataListener.onPreLoad();
        }

        if (isFromCache) {
            query.fromLocalDatastore();
        }

        query.findInBackground().continueWithTask(new Continuation<List<ParseObject>, Task<Void>>() {
            public Task<Void> then(final Task<List<ParseObject>> results) throws Exception {
                if (results.isFaulted()) {
                    Log.d("tcs", "isfaulted -->  " + results.getError());
                } else {
                    Log.d("tcs", "ok");
                    if (results.getResult() == null) {
                        Log.d("parse","exhibitors walang nahugot");
                    } else {
                        Log.d("parse","exhibitors nahugot --> " + results.getResult().size());
                    }
                    ParseObject.unpinAllInBackground("exhibitors", new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground("exhibitors", results.getResult());
                                exhibitors.clear();
                                for (ParseObject obj : results.getResult()) {
                                    String objectId = obj.getObjectId();
                                    String companyName = obj.getString("company_name");
                                    String desc = obj.getString("description");
                                    String contactNo = obj.getString("contact_no");
                                    String fax = obj.getString("fax");
                                    String email = obj.getString("email");
                                    String website = obj.getString("website");
                                    String boothNo = obj.getString("booth_number");
                                    String contactPerson = obj.getString("contactPerson");
                                    String schedule = obj.getString("schedule");
                                    String address = obj.getString("address");
                                    String logoUrl = "Not available";
                                    if (obj.getParseFile("logo") != null) {
                                        logoUrl = obj.getParseFile("logo").getUrl();
                                    }


                                    companyName = companyName == null ? "Not available" : companyName;
                                    Log.d("comp",companyName);
                                    desc = desc == null ? "Not available" : desc;
                                    contactNo = contactNo == null ? "Not available" : contactNo;
                                    fax = fax == null ? "Not available" : fax;
                                    email = email == null ? "Not available" : email;
                                    website = website == null ? "Not available" : website;
                                    boothNo = boothNo == null ? "Not available" : boothNo;
                                    contactPerson = contactPerson == null ? "Not available" : contactPerson;
                                    schedule = schedule == null ? "Not available" : schedule;
                                    address = address == null ? "Not available" : address;
                                    logoUrl = logoUrl == null ? "Not available" : logoUrl;

                                    String packageType = obj.getString("package");
                                    Log.d("booth", "booth no --> " + boothNo);
                                    ArrayList<String> categories = new ArrayList<>();
                                    ArrayList<String> categoryId = new ArrayList<>();
                                    if (obj.get("category") != null) {
                                        for (Object o : obj.getList("category")) {
                                            try {
                                                HashMap<Object, Object> map = (HashMap<Object, Object>) o;
                                                Gson gson = new Gson();
                                                JSONObject json = new JSONObject(gson.toJson(map));
                                                categories.add(json.getString("name"));
                                                categoryId.add(json.getString("objectId"));
                                            } catch (Exception ex) {
                                                Log.d("json", "errrr ----> " + ex.toString());
                                            }
                                        }
                                    }
                                    exhibitors.add(new Exhibitor(objectId, companyName, desc, contactNo,fax,
                                            email, website, false, categories, categoryId, boothNo,packageType,
                                            contactPerson,schedule,address, logoUrl ));
                                }
                            } else {
                                Log.d("parse", "failed in caching exhibitors --> " + e.toString());
                            }
                        }
                    });
                }
                return null;
            }
        }).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> voidTask) throws Exception {
                Log.d("tcs", "FINISHED!");
                if (onLoadDataListener != null) {
                    onLoadDataListener.onLoadFinished(1);
                }
                return null;
            }
        });
    }

    /* authenticate user credentials */
    public void authenticateUser(final String username, final String password) {
        Log.d("parse","authenticating user...");
        ParseUser user = new ParseUser();
        if (onLoginListener != null) {
            onLoginListener.onPreLoginQuery();
        }
        user.logInInBackground(username,password).continueWithTask(new Continuation<ParseUser, Task<ParseUser>>() {
            @Override
            public Task<ParseUser> then(Task<ParseUser> parseUserTask) throws Exception {
                if (onLoginListener != null) {
                    onLoginListener.OnLoginFinished(parseUserTask);
                }
                return parseUserTask;
            }
        });
    }

    /* query by class */
    public void getQuery(final String className) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(className);
        // query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        //query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        if (parseQueryListener != null) {
            parseQueryListener.onPreQuery(className);
        }
        query.findInBackground().continueWith(new Continuation<List<ParseObject>, Object>() {
            @Override
            public Object then(Task<List<ParseObject>> listTask) throws Exception {
                if (parseQueryListener != null) {
                    parseQueryListener.onQueryFinished(className,listTask);
                }
                return null;
            }
        });
    }

    /* insert a single row on parse table. has a ParseObject parameter. */
    public void insertSingle(ParseObject parseObject) {
        if (parseObject != null) {
            onInsertaParseObjectListener.onPreInsert();
            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (onInsertaParseObjectListener != null) {
                        onInsertaParseObjectListener.onInserted(e);
                        onInsertaParseObjectListener.onInsertFinish();
                    }
                }
            });
        }
    }


}
