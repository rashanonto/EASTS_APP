package com.dynamicobjx.visitorapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.MyVisitsActivity;
import com.dynamicobjx.visitorapp.activities.MyVisitsCategoriesActivity;
import com.dynamicobjx.visitorapp.activities.MyVisitsListingActivity;
import com.dynamicobjx.visitorapp.models.Categories;
import com.dynamicobjx.visitorapp.models.Exhibitor;
import com.dynamicobjx.visitorapp.models.MergedBookMarks;
import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.FlipHorizontalAnimation;
import com.easyandroidanimations.library.RotationAnimation;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by rsbulanon on 2/6/15.
 */
public class ProductsFavoritesAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private LayoutInflater inflater;
    private Context context;
    private MyVisitsCategoriesActivity myVisitsActivity;
    private ArrayList<Categories> categories;

    public ProductsFavoritesAdapter(Context context, ArrayList<Categories> categories) {
        this.context = context;
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
        this.myVisitsActivity = (MyVisitsCategoriesActivity)context;
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        HeaderViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.stickylistview_header, viewGroup, false);
            holder = new HeaderViewHolder();
            holder.tvHeader = (TextView)view.findViewById(R.id.tvHeader);
            view.setTag(holder);
        } else {
            holder = (HeaderViewHolder)view.getTag();
        }
        String header = getItem(i).getName().subSequence(0, 1).charAt(0) + "";
        holder.tvHeader.setText(header);
        myVisitsActivity.setFont(myVisitsActivity.getTfSegoe(),holder.tvHeader);
        return view;
    }

    @Override
    public long getHeaderId(int i) {
        return getItem(i).getName().subSequence(0,1).charAt(0);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Categories getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ItemViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.row_exhibitors, viewGroup, false);
            holder = new ItemViewHolder();
            holder.tvFavorite = (TextView)view.findViewById(R.id.tvFavorite);
            holder.tvRow = (TextView)view.findViewById(R.id.tvRow);
            view.setTag(holder);
        } else {
            holder = (ItemViewHolder)view.getTag();
        }

        if (getItemViewType(i) == 0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.lv_dark_gray));
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.lv_light_gray));
        }

        view.getBackground().setAlpha(150);

        holder.tvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FlipHorizontalAnimation(view)
                        .setDuration(500)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDegrees(360)
                        .setPivot(RotationAnimation.PIVOT_CENTER)
                        .setListener(new AnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                ArrayList<String> toUpdate = new ArrayList<String>();
                                getItem(i).setFavorite(false);

                                for (String s : myVisitsActivity.getMyProductFavorites()) {
                                    if (!s.equals(getItem(i).getObjectId())) {
                                        toUpdate.add(s);
                                    }
                                }

                                categories.remove(i);
                                ProductsFavoritesAdapter.this.notifyDataSetChanged();
                                Log.d("myFave","to update -->  " + toUpdate.toString());
                                ParseObject visitor = myVisitsActivity.getVisitor().get(0);
                                visitor.remove("myProductBookMark");
                                visitor.addAll("myProductBookMark", toUpdate);
                                visitor.saveEventually(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Log.d("myFave","successfully saved!");
                                        } else {
                                            Log.d("myFave","error while saving --> " + e.toString());
                                        }
                                    }
                                });
                                myVisitsActivity.getMyProductBookMarks();
                                if (myVisitsActivity.getMyProductFavorites().size() == 0) {
                                    myVisitsActivity.getNoDataFound().setVisibility(View.VISIBLE);
                                    myVisitsActivity.getShlvMyVisits().setVisibility(View.GONE);
                                } else {
                                    myVisitsActivity.getNoDataFound().setVisibility(View.GONE);
                                    myVisitsActivity.getShlvMyVisits().setVisibility(View.VISIBLE);
                                }
                                ProductsFavoritesAdapter.this.notifyDataSetChanged();
                            }
                        }).animate();
            }
        });
        holder.tvRow.setText(getItem(i).getName());
        myVisitsActivity.setFont(myVisitsActivity.getTfSegoe(),holder.tvRow);
        myVisitsActivity.setFont(myVisitsActivity.getTfBootstrap(),holder.tvFavorite);
        return view;
    }

    private class ItemViewHolder {
        TextView tvFavorite;
        TextView tvRow;
    }

    private class HeaderViewHolder {
        TextView tvHeader;
    }
}
