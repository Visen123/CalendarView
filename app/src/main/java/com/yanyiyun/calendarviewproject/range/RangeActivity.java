package com.yanyiyun.calendarviewproject.range;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yanyiyun.calendarview.Calendar;
import com.yanyiyun.calendarview.CalendarView;
import com.yanyiyun.calendarviewproject.R;
import com.yanyiyun.calendarviewproject.base.activity.BaseActivity;

import java.util.List;

public class RangeActivity extends BaseActivity implements
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnCalendarRangeSelectListener,
        View.OnClickListener {

    TextView mTextLeftDate;
    TextView mTextLeftWeek;

    TextView mTextRightDate;
    TextView mTextRightWeek;

    TextView mTextMinRange;
    TextView mTextMaxRange;

    CalendarView mCalendarView;

    private int mCalendarHeight;

    public static void show(Context context) {
        context.startActivity(new Intent(context, RangeActivity.class));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_range;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mTextLeftDate = (TextView) findViewById(R.id.tv_left_date);
        mTextLeftWeek = (TextView) findViewById(R.id.tv_left_week);
        mTextRightDate = (TextView) findViewById(R.id.tv_right_date);
        mTextRightWeek = (TextView) findViewById(R.id.tv_right_week);

        mTextMinRange = (TextView) findViewById(R.id.tv_min_range);
        mTextMaxRange = (TextView) findViewById(R.id.tv_max_range);

        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setOnCalendarRangeSelectListener(this);

        //设置日期拦截事件，当前有效
        mCalendarView.setOnCalendarInterceptListener(this);

        findViewById(R.id.iv_clear).setOnClickListener(this);
        findViewById(R.id.iv_reduce).setOnClickListener(this);
        findViewById(R.id.iv_increase).setOnClickListener(this);
        findViewById(R.id.tv_commit).setOnClickListener(this);

        mCalendarHeight = dipToPx(this, 46);

        mCalendarView.setRange(mCalendarView.getCurYear(), mCalendarView.getCurMonth(), mCalendarView.getCurDay(),
                mCalendarView.getCurYear() + 2, 12, 31);
    }

    @Override
    protected void initData() {
        mTextMinRange.setText(String.format("min range = %s", mCalendarView.getMinSelectRange()));
        mTextMaxRange.setText(String.format("max range = %s", mCalendarView.getMaxSelectRange()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear:
                mCalendarView.clearSelectRange();
                mTextLeftWeek.setText("开始日期");
                mTextRightWeek.setText("结束日期");
                mTextLeftDate.setText("");
                mTextRightDate.setText("");
                break;
            case R.id.iv_reduce:

                mCalendarHeight -= dipToPx(this, 8);
                if (mCalendarHeight <= dipToPx(this, 46)) {
                    mCalendarHeight = dipToPx(this, 46);
                }
                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                break;
            case R.id.iv_increase:
                mCalendarHeight += dipToPx(this, 8);
                if (mCalendarHeight >= dipToPx(this, 90)) {
                    mCalendarHeight = dipToPx(this, 90);
                }
                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                break;
            case R.id.tv_commit:
                List<Calendar> calendars = mCalendarView.getSelectCalendarRange();
                if (calendars == null || calendars.size() == 0) {
                    return;
                }
                for (Calendar c : calendars) {
                    Log.e("SelectCalendarRange", c.toString()
                            + " -- " + c.getScheme()
                            + "  --  " + c.getLunar());
                }
                Toast.makeText(this, String.format("选择了%s个日期: %s —— %s", calendars.size(),
                        calendars.get(0).toString(), calendars.get(calendars.size() - 1).toString()),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 屏蔽某些不可点击的日期，可根据自己的业务自行修改
     * 如 calendar > 2018年1月1日 && calendar <= 2020年12月31日
     *
     * @param calendar calendar
     * @return 是否屏蔽某些不可点击的日期，MonthView和WeekView有类似的API可调用
     */
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        //Log.e("onCalendarIntercept", calendar.toString());
        int day = calendar.getDay();
        return day == 1 || day == 3 || day == 6 || day == 11 ||
                day == 12 || day == 15 || day == 20 || day == 26;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        Toast.makeText(this,
                calendar.toString() + (isClick ? "拦截不可点击" : "拦截滚动到无效日期"),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCalendarSelectOutOfRange(Calendar calendar) {
        // TODO: 2018/9/13 超出范围提示
    }

    @Override
    public void onSelectOutOfRange(Calendar calendar, boolean isOutOfMinRange) {
        Toast.makeText(this,
                calendar.toString() + (isOutOfMinRange ? "小于最小选择范围" : "超过最大选择范围"),
                Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarRangeSelect(Calendar calendar, boolean isEnd) {
        if (!isEnd) {
            mTextLeftDate.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
            mTextLeftWeek.setText(WEEK[calendar.getWeek()]);
            mTextRightWeek.setText("结束日期");
            mTextRightDate.setText("");
        } else {
            mTextRightDate.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
            mTextRightWeek.setText(WEEK[calendar.getWeek()]);
        }
    }

    private static final String[] WEEK = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
}
