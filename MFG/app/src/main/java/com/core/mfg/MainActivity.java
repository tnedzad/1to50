package com.core.mfg;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.lang3.ArrayUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12, btn13, btn14, btn15, btn16, btn17, btn18, btn19, btn20, btn21, btn22, btn23, btn24, btn25;
    private int[] numbersA = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
    private int[] numbersB = {26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50};
    private int[] randomized, randomizedA;
    private String newValue = null;
    private Button[] buttons;
    private Random random;
    private TextView reset, info, mistakes;
    private Vibrator vibe;
    private ImageButton vibration, share, screenShot;
    private boolean vibrate = true;
    private int count = 0;
    private int hundredths = 0;
    private boolean running;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onConfigurationChanged(getResources().getConfiguration());
        buttons = new Button[]{
                btn1 = findViewById(R.id.btn1), btn2 = findViewById(R.id.btn2), btn3 = findViewById(R.id.btn3),
                btn4 = findViewById(R.id.btn4), btn5 = findViewById(R.id.btn5), btn6 = findViewById(R.id.btn6),
                btn7 = findViewById(R.id.btn7), btn8 = findViewById(R.id.btn8), btn9 = findViewById(R.id.btn9),
                btn10 = findViewById(R.id.btn10), btn11 = findViewById(R.id.btn11), btn12 = findViewById(R.id.btn12),
                btn13 = findViewById(R.id.btn13), btn14 = findViewById(R.id.btn14), btn15 = findViewById(R.id.btn15),
                btn16 = findViewById(R.id.btn16), btn17 = findViewById(R.id.btn17), btn18 = findViewById(R.id.btn18),
                btn19 = findViewById(R.id.btn19), btn20 = findViewById(R.id.btn20), btn21 = findViewById(R.id.btn21),
                btn22 = findViewById(R.id.btn22), btn23 = findViewById(R.id.btn23), btn24 = findViewById(R.id.btn24),
                btn25 = findViewById(R.id.btn25)};
        info = findViewById(R.id.infoView);
        reset = findViewById(R.id.textView);
        mistakes = findViewById(R.id.textView2);
        vibration = findViewById(R.id.vibration);
        screenShot = findViewById(R.id.screenShot);
        share = findViewById(R.id.share);
        setupVibrations(vibration);


        random = new Random();
        vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        runTimer();
        setupButtons();

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setupButtons();

            }
        });

        screenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                getScreenShot(v);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getTheme().applyStyle(R.style.DarkAppTheme, true);


            }
        });
        for (final Button b : buttons) {

            b.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int indexPosition = findSmallestIndexValue(numbersA);
                    int indexPosition2 = findSmallestIndexValue(numbersB);


                    if (randomized.length != 0) {
                        if (b == buttons[indexPosition]) {
                            running = true;
                            b.setAlpha(0.50f);
                            newValue = String.valueOf(randomized[0]);
                            b.setText(newValue);
                            randomized = ArrayUtils.remove(randomized, 0);
                            numbersA[indexPosition] = Integer.parseInt(buttons[indexPosition].getText().toString());

                        } else {

                            count++;
                            mistakes.setText("Mistakes: " + count);
                            mistakes.setTextColor(Color.RED);
                            if (vibrate) {

                                vibe.cancel();
                            } else {

                                vibe.vibrate(50);
                            }
                        }
                    } else {
                        if (b.getText() == String.valueOf(numbersB[indexPosition2])) {
                            running = true;
                            numbersB[indexPosition2] = Integer.parseInt(buttons[indexPosition2].getText().toString());
                            numbersB = ArrayUtils.remove(numbersB, indexPosition2);
                            b.setAlpha(0.0f);
                            if (b.getText().equals("50")) {
                                running = false;
                                info.setText("MY SCORE : ");
                                vibe.vibrate(1000);

                                for (Button button : buttons) {
                                    button.setClickable(false);
                                }

                            }
                        } else {
                            count++;
                            mistakes.setText("Mistakes: " + count);
                            mistakes.setTextColor(Color.RED);
                            if (vibrate) {
                                vibe.cancel();
                            } else {
                                vibe.vibrate(50);

                            }

                        }
                    }


                }

                ;


            });
        }
    }

    @Override  // this method is not finished yet  //
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        theme.applyStyle(R.style.DarkAppTheme, true);

        return theme;
    }

    public void getScreenShot(View view) {

        view = findViewById(R.id.textView);
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        saveScreenShot(bitmap, "ScreenShot" + "(" + count + ")" + ".png");
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/DCIM/Screenshots/");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("image/png");
        Intent i = Intent.createChooser(intent, "Gallery");

        if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
            startActivityForResult(i, 0);

        } else {
            Toast.makeText(this, "No such file!", Toast.LENGTH_SHORT).show();
        }
        view.setDrawingCacheEnabled(false);


    }

    public void saveScreenShot(Bitmap b, String fileName) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Screenshots/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(path, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }

    }

    public void setupVibrations(View v) {
        if (vibrate) {
            v.setBackgroundResource(R.drawable.ic_vibration_white_24dp);
        } else {

            v.setBackgroundResource(R.drawable.default_icon);

        }
        vibrate = !vibrate;
    }

    public void setupButtons() {

        running = false;
        count = 0;
        mistakes.setTextColor(Color.BLACK);
        mistakes.setText("Mistakes: " + count);
        hundredths = 0;
        info.setText("");
//        numbersA = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
//        numbersB = new int[]{26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50};

        randomized = shuffleArray(numbersB);
        for (int i = 0; i < numbersA.length; i++) {
            int indexOfNumbersA = i + random.nextInt(numbersA.length - i);
            int text = numbersA[indexOfNumbersA];
            numbersA[indexOfNumbersA] = numbersA[i];
            numbersA[i] = text;
            //  buttons[i].animate().rotationY(360);
            buttons[i].setText(String.valueOf(text));
            buttons[i].setAlpha(0.9f);
            buttons[i].setClickable(true);
        }

    }

    public int[] shuffleArray(int[] array) {

        Random rand = new Random();
        for (int i = array.length; i > 0; i--) {
            int random = rand.nextInt(i);
            int indexRand = array[random];
            array[random] = array[i - 1];
            array[i - 1] = indexRand;
        }
        return array;
    }

    public static int findSmallestIndexValue(int[] array) {
        int indexWithSmallestValue = 0;
        int min = array[indexWithSmallestValue];

        for (int i = 1; i < array.length; i++) {

            if (array[i] < min) {
                min = array[i];
                indexWithSmallestValue = i;
            }
        }

        return indexWithSmallestValue;
    }

    public void runTimer() {

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = hundredths / 360000;
                int minutes = (hundredths % 360000) / 600;
                int secs = (hundredths % 600) / 10;
                int hund = (hundredths % 8) * 12;

                String time = String.format("%02d:%02d:%02d", minutes, secs, hund);

                reset.setText(time);

                if (running) {
                    hundredths++;
                }
                handler.postDelayed(this, 100);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getSupportActionBar().hide();
    }

}