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
import com.dynamicobjx.visitorapp.activities.ExhibitorsActivity;
import com.dynamicobjx.visitorapp.models.Exhibitor;
import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.FlipHorizontalAnimation;
import com.easyandroidanimations.library.RotationAnimation;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by rsbulanon on 2/24/15.
 */
public class SortedByPackageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Exhibitor> exhibitors;
    private LayoutInflater inflater;
    private ExhibitorsActivity exhibitorsActivity;

    public SortedByPackageAdapter(Context context, ArrayList<Exhibitor> exhibitors) {
        this.context = context;
        this.exhibitors = exhibitors;
        Log.d("sorted","constructor --> " + exhibitors.size());
        this.inflater = LayoutInflater.from(context);
        this.exhibitorsActivity = (ExhibitorsActivity)context;
    }

    @Override
    public int getCount() {
        Log.d("sorted","size --> " + exhibitors.size());
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
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
                                Log.d("book", "before isFavorite --> " + isFavorite);
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
}
