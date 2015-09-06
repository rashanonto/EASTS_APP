package com.dynamicobjx.visitorapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dynamicobjx.visitorapp.R;
import com.dynamicobjx.visitorapp.activities.ScheduleActivity;
import com.dynamicobjx.visitorapp.models.EventSchedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by rsbulanon on 2/4/15.
 */
public class SchedulesAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ScheduleActivity scheduleActivity;
    private ArrayList<EventSchedule> schedules;
    private SimpleDateFormat headerFormat = new SimpleDateFormat("MMM dd yyyy");
    private SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm a");

    public SchedulesAdapter(Context context, ArrayList<EventSchedule> schedules) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.schedules = schedules;
        this.scheduleActivity = (ScheduleActivity)context;
    }

    @Override
    public long getHeaderId(int i) {
        //return formattedDates.get(i).subSequence(0, 1).charAt(0);
        return getItem(i).getJulianDay();
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
        return schedules.size();
    }

    @Override
    public EventSchedule getItem(int i) {
        return schedules.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.row_schedules, viewGroup, false);
            holder = new ItemViewHolder();
            holder.tvTitle = (TextView)view.findViewById(R.id.tvTitle);
            holder.tvVenue = (TextView)view.findViewById(R.id.tvVenue);
            holder.tvTime = (TextView)view.findViewById(R.id.tvTime);
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
        String startDate = hourFormat.format(getItem(i).getStartDate());
        String endDate = hourFormat.format(getItem(i).getEndDate());
        String time = startDate + " - " + endDate;
        holder.tvTitle.setText(getItem(i).getTitle());
        holder.tvVenue.setText(getItem(i).getRoomNumber());
        holder.tvTime.setText(time);
        holder.tvTitle.setTypeface(scheduleActivity.getTfSegoe(), Typeface.BOLD);
        scheduleActivity.setFont(scheduleActivity.getTfSegoe(), holder.tvVenue);
        scheduleActivity.setFont(scheduleActivity.getTfSegoe(),holder.tvTime);
        return view;
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
        //String header = displayHeader(events.get(i).getEventDate(),formattedDates.get(i));
        //String header = getItem(i).getEventDate().toString().subSequence(0, 1).charAt(0) + "";
        holder.tvHeader.setText(displayHeader(i));
        scheduleActivity.setFont(scheduleActivity.getTfSegoe(),holder.tvHeader);
        return view;
    }

    private class HeaderViewHolder {
        TextView tvHeader;
    }

    private class ItemViewHolder {
        TextView tvTitle;
        TextView tvVenue;
        TextView tvTime;
    }

    private String displayHeader(Date eventDate, String formattedDate) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(eventDate);
        cal2.setTime(new Date());
        Log.d("cal",formattedDate + "    -->  " + new Date());
        if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
            return "Today";
        }
        return formattedDate;
    }

    private String displayHeader(int i) {
        long currDayJulian = Long.parseLong(new SimpleDateFormat("D").format(new Date()));
        Log.d("julian","adapter --> " + i + "   currDayJulian -->  "
                + currDayJulian + "      --> " + getItem(i).getJulianDay());
        if (currDayJulian == getItem(i).getJulianDay()) {
            return "Today";
        }
        return headerFormat.format(getItem(i).getStartDate());
    }
}
