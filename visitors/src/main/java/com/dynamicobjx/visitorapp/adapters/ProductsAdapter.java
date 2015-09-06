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
import com.dynamicobjx.visitorapp.activities.ProductsActivity;
import com.dynamicobjx.visitorapp.models.Categories;
import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.FlipHorizontalAnimation;
import com.easyandroidanimations.library.RotationAnimation;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class ProductsAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private LayoutInflater inflater;
    private Context context;
    private ProductsActivity productsActivity;
    private ArrayList<Categories> categoryList;

    public ProductsAdapter(Context context, ArrayList<Categories> categoryList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.productsActivity = (ProductsActivity)context;
        this.categoryList = categoryList;
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
        String header = String.valueOf(getItem(i).getName().subSequence(0, 1).charAt(0));
        holder.tvHeader.setText(header);
        productsActivity.setFont(productsActivity.getTfSegoe(),holder.tvHeader);
        return view;
    }

    @Override
    public long getHeaderId(int i) {
        return getItem(i).getName().subSequence(0, 1).charAt(0);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Categories getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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


        if (getItem(i).isFavorite()) {
            holder.tvFavorite.setText(context.getResources().getString(R.string.fa_star_full));
        } else {
            holder.tvFavorite.setText(context.getResources().getString(R.string.fa_star_empty));
        }

        if (getItemViewType(i) == 0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.lv_dark_gray));
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.lv_light_gray));
        }

        view.getBackground().setAlpha(150);
        holder.tvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new FlipHorizontalAnimation(view)
                        .setDuration(500)
                        .setInterpolator(new DecelerateInterpolator())
                        .setPivot(RotationAnimation.PIVOT_CENTER)
                        .setListener(new AnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                ParseObject visitor = productsActivity.getVisitor().get(0);
                                final ArrayList<String> toUpdate = new ArrayList<>();
                                final String id = getItem(i).getObjectId();
                                boolean isFavorite = getItem(i).isFavorite();

                                isFavorite = !isFavorite;

                                Log.d("prod","current " + productsActivity.getMyProductFavorites().toString());
                                for (String s : productsActivity.getMyProductFavorites()) {
                                    if (!s.equals(id)) {
                                        toUpdate.add(s);
                                    }
                                }
                                Log.d("prod","new --> " + toUpdate.toString());

                                //remove to favorites
                                if (!isFavorite) {
                                    getItem(i).setFavorite(false);
                                    Log.d("prod","remove to favorites ---> " + id);
                                    visitor.remove("myProductBookMark");
                                    visitor.addAll("myProductBookMark",toUpdate);
                                    visitor.saveEventually(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e==null) {
                                                Log.d("prod","saved successfully!");
                                            }
                                        }
                                    });
                                } else { //add to favorites
                                    getItem(i).setFavorite(true);
                                    Log.d("prod","add to favoritess --> " + id);
                                    visitor.addUnique("myProductBookMark",id);
                                    visitor.saveEventually();
                                }
                                productsActivity.getMyProductBookMarks();
                                ProductsAdapter.this.notifyDataSetChanged();
                            }
                        }).animate();
            }
        });
        String child = getItem(i).getName();
        holder.tvRow.setText(child);
        holder.tvBoothNo.setVisibility(View.GONE);
        productsActivity.setFont(productsActivity.getTfBootstrap(),holder.tvFavorite);
        productsActivity.setFont(productsActivity.getTfSegoe(),holder.tvRow);
        productsActivity.setFont(productsActivity.getTfSegoe(),holder.tvBoothNo);
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    private class ItemViewHolder {
        TextView tvRow;
        TextView tvBoothNo;
        TextView tvFavorite;
    }

    private class HeaderViewHolder {
        TextView tvHeader;
        TextView tvHeaderCount;
    }

}
