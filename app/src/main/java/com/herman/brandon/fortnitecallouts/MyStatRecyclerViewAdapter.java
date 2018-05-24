package com.herman.brandon.fortnitecallouts;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.herman.brandon.fortnitecallouts.Helpers.StatsContent;
import com.herman.brandon.fortnitecallouts.Helpers.StatsContent.StatItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link StatItem}
 */
public class MyStatRecyclerViewAdapter extends RecyclerView.Adapter<MyStatRecyclerViewAdapter.ViewHolder> {

    private final List<StatsContent.StatItem> mValues;
    private final StatsFragment.OnStatsFragmentInteractionListener mListener;

    public MyStatRecyclerViewAdapter(List<StatsContent.StatItem> items, StatsFragment.OnStatsFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_stat_graph, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.titleView.setText(holder.mItem.title);

        // Create graph
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, holder.mItem.pc)); // PC
        entries.add(new BarEntry(1f, holder.mItem.xbox)); // Xbox
        entries.add(new BarEntry(2f, holder.mItem.ps4)); // PS4
        entries.add(new BarEntry(3f, holder.mItem.mobile)); // Mobile
        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        BarData data = new BarData(dataset);
        data.setBarWidth(0.9f); // set custom bar width
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(12f);

        XAxis xAxis = holder.barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(false);
        xAxis.setGridColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new SystemAxis(holder.barChart));
        xAxis.setAxisMaximum(3.5f);
        xAxis.setAxisMinimum(-.5f);

        YAxis yAxis = holder.barChart.getAxisLeft();
        yAxis.setGridColor(Color.WHITE);
        yAxis.setDrawAxisLine(false);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0f);

        holder.barChart.getAxisRight().setEnabled(false);
        holder.barChart.getLegend().setEnabled(false);
        holder.barChart.getDescription().setEnabled(false);
        holder.barChart.setData(data);
        holder.barChart.setFitBars(true);
        holder.barChart.setClickable(false);
        holder.barChart.setDoubleTapToZoomEnabled(false);
        holder.barChart.animateY(2000, Easing.EasingOption.EaseInQuad);
        holder.barChart.setBackgroundColor(Color.DKGRAY);
        holder.barChart.setDrawBorders(false);
        holder.barChart.setBorderColor(Color.WHITE);
        holder.barChart.invalidate();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleView;
        public final HorizontalBarChart barChart;
        public StatsContent.StatItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleView = view.findViewById(R.id.stat_card_title);
            barChart = view.findViewById(R.id.barChart);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
