package com.herman.brandon.fortnitecallouts;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Custom axis for graph
 */
public class SystemAxis implements IAxisValueFormatter {

    protected String[] mSystems = new String[]{
            "PC", "Xbox", "PS4", "Mobile"
    };

    private BarLineChartBase<?> chart;

    public SystemAxis(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mSystems[(int) value];
    }
}
