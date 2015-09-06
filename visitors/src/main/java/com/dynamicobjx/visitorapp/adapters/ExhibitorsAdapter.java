package com.dynamicobjx.visitorapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.ExhibitorsActivity;
import com.dynamicobjx.visitorapp.models.Exhibitor;
import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.FlipHorizontalAnimation;
import com.easyandroidanimations.library.RotationAnimation;
import com.parse.ParseObject;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by graciosa on 2/4/15.
 */
public class ExhibitorsAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private LayoutInflater inflater;
    private Context context;
    private ExhibitorsActivity exhibitorsActivity;
    private ArrayList<Exhibitor> exhibitors;

    public ExhibitorsAdapter(Context context, ArrayList<Exhibitor> exhibitors) {
        this.context = context;
        this.exhibitors = exhibitors;
        this.inflater = LayoutInflater.from(context);
        this.exhibitorsActivity = (ExhibitorsActivity)context;
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
        String header = getItem(i).getCompanyName().toUpperCase().subSequence(0, 1).charAt(0) + "";
        holder.tvHeader.setText(header);
        exhibitorsActivity.setFont(exhibitorsActivity.getTfSegoe(),holder.tvHeader);
        return view;
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
    public long getHeaderId(int i) {
        return getItem(i).getCompanyName().toUpperCase().subSequence(0,1).charAt(0);
    }

    @Override
    public int getCount() {
        return exhibitors.size();
    }

    @Override
    public Exhibitor getItem(int i) {
        return exhibitors.get(i);
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
            holder.tvBoothNo = (TextView)view.findViewById(R.id.tvBoothNo);
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
        if (getItem(i).isFavorite()) {
            holder.tvFavorite.setText(context.getResources().getString(R.string.fa_star_full));
        } else {
            holder.tvFavorite.setText(context.getResources().getString(R.string.fa_star_empty));
        }

        holder.tvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FlipHorizontalAnimation(view)
                        .setDuration(500)
                        .setInterpolator(new DecelerateInterpolator())
                        .setPivot(RotationAnimation.PIVOT_CENTER)
                        .setListener(new AnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                boolean isFavorite = getItem(i).isFavorite();
                                Log.d("book","before isFavorite --> " + isFavorite);
                                isFavorite = !isFavorite;
                                String id = getItem(i).getObjectId();
                                getItem(i).setFavorite(isFavorite);
                                Log.d("book","after isFavorite --> " + isFavorite);

                                ParseObject visitor = exhibitorsActivity.getVisitor().get(0);
                                if (isFavorite) {
                                    exhibitorsActivity.getMyFavorites().add(id);
                                    visitor.addUnique("myBookMarked", id);
                                    visitor.saveEventually();
                                } else {
                                    exhibitorsActivity.getMyFavorites().remove(id);
                                    visitor.remove("myBookMarked");
                                    visitor.addAll("myBookMarked",exhibitorsActivity.getMyFavorites());
                                    visitor.saveEventually();
                                }
                                notifyDataSetChanged();
                            }
                        }).animate();
            }
        });
        holder.tvRow.setText(getItem(i).getCompanyName());
        holder.tvBoothNo.setText(getItem(i).getBoothNo());
        exhibitorsActivity.setFont(exhibitorsActivity.getTfSegoe(),holder.tvRow);
        exhibitorsActivity.setFont(exhibitorsActivity.getTfSegoe(),holder.tvBoothNo);
        exhibitorsActivity.setFont(exhibitorsActivity.getTfBootstrap(),holder.tvFavorite);
        return view;
    }

    private class ItemViewHolder {
        TextView tvFavorite;
        TextView tvRow;
        TextView tvBoothNo;
    }

    private class HeaderViewHolder {
        TextView tvHeader;
    }

}
