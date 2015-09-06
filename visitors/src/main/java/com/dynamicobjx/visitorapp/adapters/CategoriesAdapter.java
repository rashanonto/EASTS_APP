package com.dynamicobjx.visitorapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.CategoriesActivity;
import com.dynamicobjx.visitorapp.activities.ExhibitorsActivity;
import com.dynamicobjx.visitorapp.models.Exhibitor;
import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.FlipHorizontalAnimation;
import com.easyandroidanimations.library.RotationAnimation;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by rsbulanon on 2/10/15.
 */
public class CategoriesAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private LayoutInflater inflater;
    private Context context;
    private CategoriesActivity categoriesActivity;
    private ArrayList<Exhibitor> exhibitors;

    public CategoriesAdapter(Context context, ArrayList<Exhibitor> exhibitors) {
        this.context = context;
        this.exhibitors = exhibitors;
        this.inflater = LayoutInflater.from(context);
        this.categoriesActivity = (CategoriesActivity)context;
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
        String header = getItem(i).getCompanyName().subSequence(0, 1).charAt(0) + "";
        holder.tvHeader.setText(header);
        categoriesActivity.setFont(categoriesActivity.getTfSegoe(),holder.tvHeader);
        return view;
    }

    @Override
    public long getHeaderId(int i) {
        return getItem(i).getCompanyName().subSequence(0,1).charAt(0);
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
            view.setTag(holder);
        } else {
            holder = (ItemViewHolder)view.getTag();
        }
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
                        .setDegrees(360)
                        .setPivot(RotationAnimation.PIVOT_CENTER)
                        .setListener(new AnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                boolean isFavorite = getItem(i).isFavorite();
                                getItem(i).setFavorite(!isFavorite);
                                notifyDataSetChanged();
                            }
                        }).animate();
            }
        });
        holder.tvRow.setText(getItem(i).getCompanyName());
        categoriesActivity.setFont(categoriesActivity.getTfSegoe(),holder.tvRow);
        categoriesActivity.setFont(categoriesActivity.getTfBootstrap(),holder.tvFavorite);
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
