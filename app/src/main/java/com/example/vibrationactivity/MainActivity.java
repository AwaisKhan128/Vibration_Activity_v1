package com.example.vibrationactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button button, button2, button3;
    boolean isVibrated = false;
    FirebaseDatabase firebaseDatabase;
    Context context;
    int different_count_row = 0;
    int tactile_wait_count = 0;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        //--------------------
        imageView.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);
        context = MainActivity.this;
        firebaseDatabase = FirebaseDatabase.getInstance();
        textView = findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);


        firebaseDatabase
                .getReference("Buttons_Visibility").child("imageView").setValue(0);
        firebaseDatabase
                .getReference("Buttons_Visibility").child("button").setValue(1);
        firebaseDatabase
                .getReference("Buttons_Visibility").child("button2").setValue(1);
        firebaseDatabase
                .getReference("Buttons_Visibility").child("button3").setValue(1);

        //--------------------

        DatabaseReference dRef = firebaseDatabase.getReference("Devices").child("vibration");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isVibrated) {
                    if (snapshot.child("started").exists()) {
                        if (snapshot.child("started").getValue(Boolean.class)) {
                            imageView.setVisibility(View.VISIBLE);
                            button2.setVisibility(View.INVISIBLE);
                            isVibrated = true;
                            new background().execute(0);
                            button2.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                            button3.setVisibility(View.VISIBLE);
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toaster(error.getMessage());
            }
        });

        DatabaseReference dRef_B = firebaseDatabase.getReference("Buttons_Visibility");
        dRef_B.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.child("imageView").exists())
                {
                    if (snapshot.child("imageView").getValue(Integer.class)==0)
                    {
                        imageView.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }

                if (snapshot.child("button").exists())
                {
                    if (snapshot.child("button").getValue(Integer.class)==0)
                    {
                        button.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        button.setVisibility(View.VISIBLE);
                    }
                }
                if (snapshot.child("button2").exists())
                {
                    if (snapshot.child("button2").getValue(Integer.class)==0)
                    {
                        button2.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        button2.setVisibility(View.VISIBLE);
                    }
                }
                if (snapshot.child("button3").exists())
                {
                    if (snapshot.child("button3").getValue(Integer.class)==0)
                    {
                        button3.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        button3.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReference = firebaseDatabase.getReference("Devices").child("vibration").child("vibrated");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.getValue(Integer.class)==1)
                    {
                        Perform_Utilization_on_next();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReference1 = firebaseDatabase.getReference("Devices")
                .child("difference");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.getValue(Integer.class)>2)
                    {
                        firebaseDatabase.getReference("Devices")
                                .child("Least_weight").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists())
                                {
                                    if (snapshot.getValue(Integer.class)>0) {
                                        String s = "";
                                        Toaster("Least Weight is found " + snapshot.getValue(Integer.class));
                                        textView.setVisibility(View.VISIBLE);
                                        s = String.valueOf((int) snapshot.getValue(Integer.class) / 3);
                                        try {
                                            s = s.replaceAll("0", "");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        textView.setText("Least Weight is found " + (s + "ms"));
                                        intruptAll();
                                    }
                                    else
                                    {
                                        textView.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //------------

    }

    public void Toaster(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public void Perform_Utilization_on_next()
    {
        //Write database to started .. For round 1
        firebaseDatabase.getReference("Devices").child("vibration").child("started").setValue(true);
        firebaseDatabase.getReference("Devices").child("vibration").child("end").setValue(false);
        isVibrated = true;
        new background().execute();
        firebaseDatabase.getReference("Devices").child("vibration").child("vibrated").setValue(2);

        firebaseDatabase.getReference("Devices").child("vibration").child("started").setValue(false);
        firebaseDatabase.getReference("Devices").child("vibration").child("end").setValue(true);
//        button2.setVisibility(View.VISIBLE);
//            button.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        // Other end will be vibrate..
        firebaseDatabase
                .getReference("Buttons_Visibility").child("imageView").setValue(0);
        firebaseDatabase
                .getReference("Buttons_Visibility").child("button2").setValue(0);
        firebaseDatabase
                .getReference("Buttons_Visibility").child("button3").setValue(1);
        firebaseDatabase
                .getReference("Buttons_Visibility").child("button").setValue(1);
//        DatabaseReference dRef_B = firebaseDatabase.getReference("Buttons_Visibility");
//        dRef_B.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//                if (snapshot.child("imageView").exists())
//                {
//                    if (snapshot.child("imageView").getValue(Integer.class)==0)
//                    {
//                        imageView.setVisibility(View.INVISIBLE);
//                    }
//                    else
//                    {
//                        imageView.setVisibility(View.VISIBLE);
//                    }
//                }
//
//                if (snapshot.child("button").exists())
//                {
//                    if (snapshot.child("button").getValue(Integer.class)==0)
//                    {
//                        button.setVisibility(View.INVISIBLE);
//                    }
//                    else
//                    {
//                        button.setVisibility(View.VISIBLE);
//                    }
//                }
//                if (snapshot.child("button2").exists())
//                {
//                    if (snapshot.child("button2").getValue(Integer.class)==0)
//                    {
//                        button2.setVisibility(View.INVISIBLE);
//                    }
//                    else
//                    {
//                        button2.setVisibility(View.VISIBLE);
//                    }
//                }
//                if (snapshot.child("button3").exists())
//                {
//                    if (snapshot.child("button3").getValue(Integer.class)==0)
//                    {
//                        button3.setVisibility(View.INVISIBLE);
//                    }
//                    else
//                    {
//                        button3.setVisibility(View.VISIBLE);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        //-------------


    }

    public void Perform_Utilization_on_test()
    {
        //Write database to started .. For each rounds
        firebaseDatabase
                .getReference("Buttons_Visibility").child("imageView").setValue(1);

        new Wait_Thread().execute();
        //--------------
        firebaseDatabase.getReference("Devices").child("vibration").child("started").setValue(true);
        firebaseDatabase.getReference("Devices").child("vibration").child("end").setValue(false);
        isVibrated = true;
        new background().execute();

        firebaseDatabase.getReference("Devices").child("vibration").child("vibrated").setValue(1);

        firebaseDatabase.getReference("Devices").child("vibration").child("started").setValue(false);
        firebaseDatabase.getReference("Devices").child("vibration").child("end").setValue(true);
        firebaseDatabase.getReference("Devices")
                .child("Least_weight")
                .setValue(tactile_wait_count);
        firebaseDatabase.getReference("Devices")
                .child("difference")
                .setValue(different_count_row);
        button2.setVisibility(View.VISIBLE);
//            button.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        // Other end will be vibrate..
        firebaseDatabase
                .getReference("Buttons_Visibility").child("imageView").setValue(0);
        firebaseDatabase
                .getReference("Buttons_Visibility").child("button2").setValue(1);
        firebaseDatabase
                .getReference("Buttons_Visibility").child("button3").setValue(1);
        firebaseDatabase
                .getReference("Buttons_Visibility").child("button").setValue(1);
//        DatabaseReference dRef_B = firebaseDatabase.getReference("Buttons_Visibility");
//        dRef_B.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//                if (snapshot.child("imageView").exists())
//                {
//                    if (snapshot.child("imageView").getValue(Integer.class)==0)
//                    {
//                        imageView.setVisibility(View.INVISIBLE);
//                    }
//                    else
//                    {
//                        imageView.setVisibility(View.VISIBLE);
//                    }
//                }
//
//                if (snapshot.child("button").exists())
//                {
//                    if (snapshot.child("button").getValue(Integer.class)==0)
//                    {
//                        button.setVisibility(View.INVISIBLE);
//                    }
//                    else
//                    {
//                        button.setVisibility(View.VISIBLE);
//                    }
//                }
//                if (snapshot.child("button2").exists())
//                {
//                    if (snapshot.child("button2").getValue(Integer.class)==0)
//                    {
//                        button2.setVisibility(View.INVISIBLE);
//                    }
//                    else
//                    {
//                        button2.setVisibility(View.VISIBLE);
//                    }
//                }
//                if (snapshot.child("button3").exists())
//                {
//                    if (snapshot.child("button3").getValue(Integer.class)==0)
//                    {
//                        button3.setVisibility(View.INVISIBLE);
//                    }
//                    else
//                    {
//                        button3.setVisibility(View.VISIBLE);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }


    public void Perform_Vibration(View view) {
        if (view.getId() == R.id.button2) {
            imageView.setVisibility(View.VISIBLE);
            button2.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            firebaseDatabase
                    .getReference("Buttons_Visibility").child("imageView").setValue(1);
            firebaseDatabase
                    .getReference("Buttons_Visibility").child("button").setValue(0);
            firebaseDatabase
                    .getReference("Buttons_Visibility").child("button2").setValue(0);
            firebaseDatabase
                    .getReference("Buttons_Visibility").child("button3").setValue(0);
            firebaseDatabase.getReference("Devices")
                    .child("Least_weight")
                    .setValue(tactile_wait_count);

//            DatabaseReference dRef_B = firebaseDatabase.getReference("Buttons_Visibility");
//            dRef_B.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot)
//                {
//                    if (snapshot.child("imageView").exists())
//                    {
//                        if (snapshot.child("imageView").getValue(Integer.class)==0)
//                        {
//                            imageView.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            imageView.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                    if (snapshot.child("button").exists())
//                    {
//                        if (snapshot.child("button").getValue(Integer.class)==0)
//                        {
//                            button.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            button.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    if (snapshot.child("button2").exists())
//                    {
//                        if (snapshot.child("button2").getValue(Integer.class)==0)
//                        {
//                            button2.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            button2.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    if (snapshot.child("button3").exists())
//                    {
//                        if (snapshot.child("button3").getValue(Integer.class)==0)
//                        {
//                            button3.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            button3.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
            //-------------------->
            Perform_Utilization_on_test();

        }
        if (view.getId() == R.id.imageView) {

        }
        if (view.getId() == R.id.button)
        {
            different_count_row = 0;
//            firebaseDatabase.getReference("Devices").child("vibration").child("started").setValue(true);
//            firebaseDatabase.getReference("Devices").child("vibration").child("end").setValue(false);
//            isVibrated = true;
//            new background().execute(tactile_wait_count);
//            firebaseDatabase.getReference("Devices").child("vibration").child("started").setValue(false);
//            firebaseDatabase.getReference("Devices").child("vibration").child("end").setValue(true);
//            button2.setVisibility(View.VISIBLE);
////            button.setVisibility(View.VISIBLE);
//            button3.setVisibility(View.VISIBLE);
            tactile_wait_count += 5000;
            imageView.setVisibility(View.VISIBLE);
            button2.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            //-----------
            firebaseDatabase
                    .getReference("Buttons_Visibility").child("imageView").setValue(1);
            firebaseDatabase
                    .getReference("Buttons_Visibility").child("button").setValue(0);
            firebaseDatabase
                    .getReference("Buttons_Visibility").child("button2").setValue(0);
            firebaseDatabase
                    .getReference("Buttons_Visibility").child("button3").setValue(0);
//            Perform_Utilization_on_test();
//            DatabaseReference dRef_B = firebaseDatabase.getReference("Buttons_Visibility");
//            dRef_B.addValueEventListener(new ValueEventListener()
//            {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot)
//                {
//                    if (snapshot.child("imageView").exists())
//                    {
//                        if (snapshot.child("imageView").getValue(Integer.class)==0)
//                        {
//                            imageView.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            imageView.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                    if (snapshot.child("button").exists())
//                    {
//                        if (snapshot.child("button").getValue(Integer.class)==0)
//                        {
//                            button.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            button.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    if (snapshot.child("button2").exists())
//                    {
//                        if (snapshot.child("button2").getValue(Integer.class)==0)
//                        {
//                            button2.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            button2.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    if (snapshot.child("button3").exists())
//                    {
//                        if (snapshot.child("button3").getValue(Integer.class)==0)
//                        {
//                            button3.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            button3.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
            Perform_Utilization_on_test();
        }
        if (view.getId() == R.id.button3)
        {
            different_count_row += 1;
            tactile_wait_count += 5000;

            imageView.setVisibility(View.VISIBLE);
            button2.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            firebaseDatabase
                    .getReference("Buttons_Visibility").child("imageView").setValue(1);
            firebaseDatabase
                    .getReference("Buttons_Visibility").child("button").setValue(0);
            firebaseDatabase
                    .getReference("Buttons_Visibility").child("button2").setValue(0);
            firebaseDatabase
                    .getReference("Buttons_Visibility").child("button3").setValue(0);
//            Perform_Utilization_on_test();
//            DatabaseReference dRef_B = firebaseDatabase.getReference("Buttons_Visibility");
//            dRef_B.addValueEventListener(new ValueEventListener()
//            {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot)
//                {
//                    if (snapshot.child("imageView").exists())
//                    {
//                        if (snapshot.child("imageView").getValue(Integer.class)==0)
//                        {
//                            imageView.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            imageView.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                    if (snapshot.child("button").exists())
//                    {
//                        if (snapshot.child("button").getValue(Integer.class)==0)
//                        {
//                            button.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            button.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    if (snapshot.child("button2").exists())
//                    {
//                        if (snapshot.child("button2").getValue(Integer.class)==0)
//                        {
//                            button2.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            button2.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    if (snapshot.child("button3").exists())
//                    {
//                        if (snapshot.child("button3").getValue(Integer.class)==0)
//                        {
//                            button3.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            button3.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
            Perform_Utilization_on_test();

        }
    }

    public void Vibrates() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect
                    .createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(200);
        }
    }

    class background extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... voids) {
            Vibrates();
            try {
                Thread.sleep(tactile_wait_count);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

//    class wait_count_listener extends AsyncTask<Void, Void, Void>
//    {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            while(different_count_row==2)
//            {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        intruptAll();
//                    }
//                });
//
//            }
//            return null;
//        }
//    }

    private void intruptAll() {
        resetAll();
        Toaster("Found 3 different in a row");
        //Transmit to next Activity....
        //Next Activity will get the time....
    }

    private void resetAll()
    {
        firebaseDatabase.getReference("Devices").child("vibration").child("started").setValue(false);
        firebaseDatabase.getReference("Devices").child("vibration").child("end").setValue(false);
        tactile_wait_count = 0;
        different_count_row = 0;
        isVibrated = false;
        button2.setVisibility(View.VISIBLE);
        button.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        //-------------------

        firebaseDatabase
                .getReference("Buttons_Visibility").child("imageView").setValue(0);
        firebaseDatabase
                .getReference("Buttons_Visibility").child("button").setValue(0);
        firebaseDatabase
                .getReference("Buttons_Visibility").child("button2").setValue(1);
        firebaseDatabase
                .getReference("Buttons_Visibility").child("button3").setValue(0);
    }

    private Handler handler;
    private int progressStatus = 0;
    public void Wait_thread()
    {
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 2) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                }
                //finish();
            }
        }).start();
    }


    class Wait_Thread extends AsyncTask<Void, Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}