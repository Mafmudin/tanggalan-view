package udinsi.dev.tagglanview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import udinsi.dev.tanggalanview.TanggalanView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TanggalanView tanggalanView = (TanggalanView) findViewById(R.id.tanggalan_view);

        tanggalanView.setUserCurrentMonthYear(6, 2018);
        tanggalanView.setCallBack(new TanggalanView.DayClickListener() {
            @Override
            public void onDayClick(View view) {
//                tanggalanView.setBackgroundColor(Color.RED);
            }
        });
    }
}
