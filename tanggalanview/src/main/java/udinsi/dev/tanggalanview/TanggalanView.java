package udinsi.dev.tanggalanview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebHistoryItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class TanggalanView extends LinearLayout {

    private String[] MOUNT_NAMES = getResources().getStringArray(R.array.mount_names);

    TextView currentDateTextView;
    TextView currentMonthTextView;
    Button selectedDayButton;

    Button[] daysButton;
    LinearLayout[] weeksLinearLayout;

    LinearLayout weekOneLinearLayout;
    LinearLayout weekTwoLinearLayout;
    LinearLayout weekThreeLinearLayout;
    LinearLayout weekFourLinearLayout;
    LinearLayout weekFiveLinearLayout;
    LinearLayout weekSixLinearLayout;

    int currentDateDay, currentDateMonth, currentDateYear;
    int chosenDateDay, chosenDateMonth, chosenDateYear;
    int pickedDateDay, pickedDateMonth, pickedDateYear;

    int userMonth, userYear;

    DayClickListener mListener;
    LinearLayout.LayoutParams buttonParam;
    LinearLayout.LayoutParams userButtonParam;

    Calendar calendar;
    Drawable userDrawable;

    public TanggalanView(Context context) {
        super(context);
        init(context);
    }

    public TanggalanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TanggalanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TanggalanView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        View view = LayoutInflater.from(context).inflate(R.layout.tanggalan_view,this, true);
        calendar = Calendar.getInstance();

        weekOneLinearLayout = view.findViewById(R.id.calendar_week_1);
        weekTwoLinearLayout = view.findViewById(R.id.calendar_week_2);
        weekThreeLinearLayout = view.findViewById(R.id.calendar_week_3);
        weekFourLinearLayout = view.findViewById(R.id.calendar_week_4);
        weekFiveLinearLayout = view.findViewById(R.id.calendar_week_5);
        weekSixLinearLayout = view.findViewById(R.id.calendar_week_6);

        currentDateTextView = view.findViewById(R.id.current_date);
        currentMonthTextView = view.findViewById(R.id.current_month);

        currentDateDay = chosenDateDay = calendar.get(Calendar.DAY_OF_MONTH);

        if(userMonth != 0 && userYear != 0){
            currentDateMonth = chosenDateMonth = userMonth;
            currentDateYear = chosenDateYear =userYear;
        }else {
            currentDateMonth = chosenDateMonth = calendar.get(Calendar.MONTH);
            currentDateYear = chosenDateYear = calendar.get(Calendar.YEAR);
        }
        currentDateTextView.setText(""+ currentDateDay);
        currentMonthTextView.setText(MOUNT_NAMES[currentDateMonth]);

        initDaysWeeks();

        if (userButtonParam != null){
            buttonParam = userButtonParam;
        }else {
            buttonParam = getDaysLayoutParams();
        }

        addDaysInCalendar(buttonParam, context, metrics);

        initCalendarWithDate(chosenDateYear, chosenDateMonth, chosenDateDay);
    }

    private LayoutParams getDaysLayoutParams(){
        LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParam.weight = 1;
        return buttonParam;
    }

    private void initDaysWeeks(){
        weeksLinearLayout = new LinearLayout[6];
        daysButton = new Button[6*7];

        weeksLinearLayout[0] = weekOneLinearLayout;
        weeksLinearLayout[1] = weekTwoLinearLayout;
        weeksLinearLayout[2] = weekThreeLinearLayout;
        weeksLinearLayout[3] = weekFourLinearLayout;
        weeksLinearLayout[4] = weekFiveLinearLayout;
        weeksLinearLayout[5] = weekSixLinearLayout;
    }

    public interface DayClickListener{
        void onDayClick(View view);
    }

    public void onDayClick(View view){
        mListener.onDayClick(view);

        if(selectedDayButton != null){
            if (currentDateYear == chosenDateYear
                    && currentDateMonth == chosenDateMonth
                    && currentDateDay == chosenDateDay){
                selectedDayButton.setBackgroundColor(Color.RED);
                selectedDayButton.setTextColor(Color.WHITE);
            }else {
                selectedDayButton.setBackgroundColor(Color.TRANSPARENT);
                if (selectedDayButton.getCurrentTextColor() != Color.RED){
                    selectedDayButton.setTextColor(Color.GREEN);
                }
            }
        }

        selectedDayButton = (Button) view;

        if(selectedDayButton.getTag() != null){
            int[] dateArray = (int[]) selectedDayButton.getTag();
            pickedDateDay = dateArray[0];
            pickedDateMonth = dateArray[1];
            pickedDateYear = dateArray[2];
        }

        if(pickedDateYear == currentDateYear
                && pickedDateMonth == currentDateMonth
                && pickedDateDay == currentDateDay){
            selectedDayButton.setBackgroundColor(Color.RED);
            selectedDayButton.setTextColor(Color.WHITE);
        }else {
            selectedDayButton.setBackgroundColor(Color.GRAY);
            if (selectedDayButton.getCurrentTextColor() != Color.RED){
                selectedDayButton.setTextColor(Color.WHITE);
            }
        }
    }

    private void addDaysInCalendar(ViewGroup.LayoutParams layoutParam, Context context,
                                   DisplayMetrics metrics){
        int counter = 0;

        for (int weekNumber = 0; weekNumber< 6; ++weekNumber){
            for (int day = 0; day< 7; ++day){
                final Button dayButton = new Button(context);
                dayButton.setTextColor(Color.GRAY);
                dayButton.setBackgroundColor(Color.TRANSPARENT);
                dayButton.setLayoutParams(layoutParam);
                dayButton.setTextSize((int) metrics.density * 8);
                dayButton.setSingleLine();

                daysButton[counter] = dayButton;
                weeksLinearLayout[weekNumber].addView(dayButton);

                ++counter;
            }
        }
    }

    private void initCalendarWithDate(int year, int month, int day) {
        if (calendar == null)
            calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        int daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        chosenDateYear = year;
        chosenDateMonth = month;
        chosenDateDay = day;

        calendar.set(year, month, 1);
        int firstDayOfCurrentMonth = calendar.get(Calendar.DAY_OF_WEEK);

        calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        int dayNumber = 1;
        int daysLeftInFirstWeek = 0;
        int indexOfDayAfterLastDayOfMonth = 0;

        if (firstDayOfCurrentMonth != 1) {
            daysLeftInFirstWeek = firstDayOfCurrentMonth;
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth;
            for (int i = firstDayOfCurrentMonth; i < firstDayOfCurrentMonth + daysInCurrentMonth; ++i) {
                if (currentDateMonth == chosenDateMonth
                        && currentDateYear == chosenDateYear
                        && dayNumber == currentDateDay) {
                    daysButton[i].setBackgroundColor(Color.RED);
                    daysButton[i].setTextColor(Color.WHITE);
                } else {
                    daysButton[i].setTextColor(Color.BLACK);
                    daysButton[i].setBackgroundColor(Color.TRANSPARENT);
                }

                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = chosenDateMonth;
                dateArr[2] = chosenDateYear;
                daysButton[i].setTag(dateArr);
                daysButton[i].setText(String.valueOf(dayNumber));

                daysButton[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDayClick(v);
                    }
                });
                ++dayNumber;
            }
        } else {
            daysLeftInFirstWeek = 8;
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth;
            for (int i = 8; i < 8 + daysInCurrentMonth; ++i) {
                if (currentDateMonth == chosenDateMonth
                        && currentDateYear == chosenDateYear
                        && dayNumber == currentDateDay) {
                    daysButton[i].setBackgroundColor(Color.RED);
                    daysButton[i].setTextColor(Color.WHITE);
                } else {
                    daysButton[i].setTextColor(Color.BLACK);
                    daysButton[i].setBackgroundColor(Color.TRANSPARENT);
                }

                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = chosenDateMonth;
                dateArr[2] = chosenDateYear;
                daysButton[i].setTag(dateArr);
                daysButton[i].setText(String.valueOf(dayNumber));

                daysButton[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDayClick(v);
                    }
                });
                ++dayNumber;
            }
        }

        if (month > 0)
            calendar.set(year, month - 1, 1);
        else
            calendar.set(year - 1, 11, 1);
        int daysInPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = daysLeftInFirstWeek - 1; i >= 0; --i) {
            int[] dateArr = new int[3];

            if (chosenDateMonth > 0) {
                if (currentDateMonth == chosenDateMonth - 1
                        && currentDateYear == chosenDateYear
                        && daysInPreviousMonth == currentDateDay) {
                } else {
                    daysButton[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = daysInPreviousMonth;
                dateArr[1] = chosenDateMonth - 1;
                dateArr[2] = chosenDateYear;
            } else {
                if (currentDateMonth == 11
                        && currentDateYear == chosenDateYear - 1
                        && daysInPreviousMonth == currentDateDay) {
                } else {
                    daysButton[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = daysInPreviousMonth;
                dateArr[1] = 11;
                dateArr[2] = chosenDateYear - 1;
            }

            daysButton[i].setTag(dateArr);
            daysButton[i].setText(String.valueOf(daysInPreviousMonth--));
            daysButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDayClick(v);
                }
            });
        }

        int nextMonthDaysCounter = 1;
        for (int i = indexOfDayAfterLastDayOfMonth; i < daysButton.length; ++i) {
            int[] dateArr = new int[3];

            if (chosenDateMonth < 11) {
                if (currentDateMonth == chosenDateMonth + 1
                        && currentDateYear == chosenDateYear
                        && nextMonthDaysCounter == currentDateDay) {
                    daysButton[i].setBackgroundColor(Color.RED);
                } else {
                    daysButton[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = nextMonthDaysCounter;
                dateArr[1] = chosenDateMonth + 1;
                dateArr[2] = chosenDateYear;
            } else {
                if (currentDateMonth == 0
                        && currentDateYear == chosenDateYear + 1
                        && nextMonthDaysCounter == currentDateDay) {
                    daysButton[i].setBackgroundColor(Color.RED);
                } else {
                    daysButton[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = nextMonthDaysCounter;
                dateArr[1] = 0;
                dateArr[2] = chosenDateYear + 1;
            }

            daysButton[i].setTag(dateArr);
            daysButton[i].setTextColor(Color.GRAY);
            daysButton[i].setText(String.valueOf(nextMonthDaysCounter++));
            daysButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDayClick(v);
                }
            });
        }

        calendar.set(chosenDateYear, chosenDateMonth, chosenDateDay);
    }

    public void setUserDaysLayoutParams(LinearLayout.LayoutParams userButtonParams) {
        this.userButtonParam = userButtonParams;
    }

    public void setUserCurrentMonthYear(int userMonth, int userYear) {
        this.userMonth = userMonth;
        this.userYear = userYear;
    }

    public void setDayBackground(Drawable userDrawable) {
        this.userDrawable = userDrawable;
    }

    public void setCallBack(DayClickListener mListener) {
        this.mListener = mListener;
    }
}
