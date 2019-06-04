package com.dagf.presentlogolib.eq;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dagf.presentlogolib.R;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.gson.Gson;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;

import static com.dagf.presentlogolib.utils.DBHelper.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class EqualizerFragment extends Fragment {

    ImageView backBtn;
    TextView fragTitle;
    private TextPaint tp;
    SwitchCompat equalizerSwitch;

    LineSet dataset;
    LineChartView chart;
    Paint paint;
    float[] points;
    public static RelativeLayout.LayoutParams lps;

    int y = 0;
    private static EqualizerModel equalizerModel;

    ImageView spinnerDropDownIcon;

    short numberOfFrequencyBands;
    LinearLayout mLinearLayout;

    SeekBar[] seekBarFinal = new SeekBar[5];

    AnalogController bassController, reverbController;

    Spinner presetSpinner;

    FrameLayout equalizerBlocker;

    ShowcaseView showCase;

    Context ctx;

    onCheckChangedListener mCallback;
    
    private static BassBoost bassBoost;
    private static PresetReverb presetReverb;
   // private static MediaPlayer mMediaPlayer;

    public static void setupAudioSession(View.OnClickListener cs, int mediaPlayer){
        clickListener = cs;

        audiosessionid = mediaPlayer;
        try {
            bassBoost = new BassBoost(0, audiosessionid);
            bassBoost.setEnabled(false);
            BassBoost.Settings bassBoostSettingTemp = bassBoost.getProperties();
            BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());
            bassBoostSetting.strength = (1000 / 19);
            bassBoost.setProperties(bassBoostSetting);
      //      mMediaPlayer.setAuxEffectSendLevel(1.0f);

            presetReverb = new PresetReverb(0, audiosessionid);
            presetReverb.setPreset(PresetReverb.PRESET_NONE);
            presetReverb.setEnabled(false);
    //        mMediaPlayer.setAuxEffectSendLevel(1.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface onCheckChangedListener {
        void onCheckChanged(boolean isChecked);
    }

    public EqualizerFragment() {
        // Required empty public constructor
    }

    private static View.OnClickListener clickListener;


    void simk(boolean isChecked){

        EqualizerFragment eqFrag = EqualizerFragment.this;
        if (isChecked) {
            try {
                //   equalizerModel.isEqualizerEnabled = true;
                int pos = presetPos;
                if (pos != 0) {
                    mEqualizer.usePreset((short) (pos - 1));
                } else {
                    for (short i = 0; i < 5; i++) {
                        mEqualizer.setBandLevel(i, (short) seekbarpos[i]);
                    }
                }
                if (bassStrength != -1 && reverbPreset != -1) {
                    bassBoost.setEnabled(true);
                    bassBoost.setStrength(bassStrength);
                    presetReverb.setEnabled(true);
                    presetReverb.setPreset(reverbPreset);
                }
                //   mMediaPlayer.setAuxEffectSendLevel(1.0f);
                if (eqFrag != null)
                    eqFrag.setBlockerVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                //   equalizerModel.isEqualizerEnabled = false;
                mEqualizer.usePreset((short) 0);
                bassBoost.setStrength((short) (((float) 1000 / 19) * (1)));
                presetReverb.setPreset((short) 0);
                if (eqFrag != null)
                    eqFrag.setBlockerVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        equalizerModel.isEqualizerEnabled = isChecked;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
        mCallback = new onCheckChangedListener() {
            @Override
            public void onCheckChanged(boolean isChecked) {


               simk(isChecked);
                
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_equalizer, container, false);
    }

    public static SharedPreferences preferences;

    public static short reverbPreset = -1;

    public static final String keyeqenabled = "sdasdasdf";

    public static int[] seekbarpos = new int[5];
    public static float ratio;

    public static int themeColor = Color.parseColor("#B24242");

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        gson = new Gson();
        refWatcher = LeakCanary.install(getActivity().getApplication());

        mEqualizer = new Equalizer(0,audiosessionid);

        getSavedData();

        SetupEq();





     //   equalizerModel.isEqualizerEnabled = preferences.getInt(keyeqenabled, 0) == 1;

        backBtn = (ImageView) view.findViewById(R.id.equalizer_back_btn);
        backBtn.setOnClickListener(clickListener);

        fragTitle = (TextView) view.findViewById(R.id.equalizer_fragment_title);
       /* if (SplashActivity.tf4 != null)
            fragTitle.setTypeface(SplashActivity.tf4);*/

        equalizerSwitch = (SwitchCompat) view.findViewById(R.id.equalizer_switch);
        equalizerSwitch.setChecked(equalizerModel.isEqualizerEnabled);
            simk(equalizerSwitch.isChecked());

        equalizerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCallback.onCheckChanged(isChecked);
            }
        });

        spinnerDropDownIcon = (ImageView) view.findViewById(R.id.spinner_dropdown_icon);
        spinnerDropDownIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presetSpinner.performClick();
            }
        });

        presetSpinner = (Spinner) view.findViewById(R.id.equalizer_preset_spinner);

        equalizerBlocker = (FrameLayout) view.findViewById(R.id.equalizerBlocker);

        Log.e(TAG, "onViewCreated: "+bassStrength);

        if(bassStrength != -1)
        bassBoost.setStrength(bassStrength);
        if(reverbPreset != -1)
        presetReverb.setPreset(reverbPreset);

        if (equalizerModel.isEqualizerEnabled) {
            equalizerBlocker.setVisibility(View.GONE);
        } else {
            equalizerBlocker.setVisibility(View.VISIBLE);
        }

        chart = (LineChartView) view.findViewById(R.id.lineChart);
        paint = new Paint();
        dataset = new LineSet();

        bassController = (AnalogController) view.findViewById(R.id.controllerBass);
        reverbController = (AnalogController) view.findViewById(R.id.controller3D);

        bassController.setLabel("BASS");
        reverbController.setLabel("3D");

        bassController.circlePaint2.setColor(themeColor);
        bassController.linePaint.setColor(themeColor);
        bassController.invalidate();
        reverbController.circlePaint2.setColor(themeColor);
        bassController.linePaint.setColor(themeColor);
        reverbController.invalidate();

        Log.e(TAG, "onViewCreated: "+isEqualizerReloaded);

        if (isEqualizerReloaded) {
            int x = 0;
            if (bassBoost != null) {
                try {
                    x = ((bassBoost.getRoundedStrength() * 19) / 1000);
                } catch (Exception e) {
                    Log.e(TAG, "onViewCreated: "+e.getMessage());
                    e.printStackTrace();
                }
            }

            if (presetReverb != null) {
                try {
                    y = (presetReverb.getPreset() * 19) / 6;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (x == 0) {
                bassController.setProgress(1);
            } else {
                bassController.setProgress(x);
            }

            Log.e(TAG, "onViewCreated: x = "+x);

            if (y == 0) {
                reverbController.setProgress(1);
            } else {
                reverbController.setProgress(y);
            }
        } else {
            int x = ((bassStrength * 19) / 1000);
            y = (reverbPreset * 19) / 6;
            if (x == 0) {
                bassController.setProgress(1);
            } else {
                bassController.setProgress(x);
            }

            Log.e(TAG, "onViewCreated: "+x);

            if (y == 0) {
                reverbController.setProgress(1);
            } else {
                reverbController.setProgress(y);
            }
        }

        bassController.setOnProgressChangedListener(new AnalogController.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                bassStrength = (short) (((float) 1000 / 19) * (progress));
                try {
                    bassBoost.setStrength(bassStrength);
                equalizerModel.setBassStrength(bassStrength);
                    Log.e(TAG, "onProgressChanged: "+bassStrength);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        reverbController.setOnProgressChangedListener(new AnalogController.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                reverbPreset = (short) ((progress * 6) / 19);
                equalizerModel.setReverbPreset(reverbPreset);
                try {
                    presetReverb.setPreset(reverbPreset);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                y = progress;
            }
        });

        mLinearLayout = (LinearLayout) view.findViewById(R.id.equalizerContainer);

        TextView equalizerHeading = new TextView(getContext());
        equalizerHeading.setText("Equalizer");
        equalizerHeading.setTextSize(20);
        equalizerHeading.setGravity(Gravity.CENTER_HORIZONTAL);

        numberOfFrequencyBands = 5;

        points = new float[numberOfFrequencyBands];

        final short lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];
        final short upperEqualizerBandLevel = mEqualizer.getBandLevelRange()[1];

        for (short i = 0; i < numberOfFrequencyBands; i++) {
            final short equalizerBandIndex = i;
            final TextView frequencyHeaderTextView = new TextView(getContext());
            frequencyHeaderTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            frequencyHeaderTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            frequencyHeaderTextView.setTextColor(Color.parseColor("#FFFFFF"));
            frequencyHeaderTextView.setText((mEqualizer.getCenterFreq(equalizerBandIndex) / 1000) + "Hz");

            LinearLayout seekBarRowLayout = new LinearLayout(getContext());
            seekBarRowLayout.setOrientation(LinearLayout.VERTICAL);

            TextView lowerEqualizerBandLevelTextView = new TextView(getContext());
            lowerEqualizerBandLevelTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            lowerEqualizerBandLevelTextView.setTextColor(Color.parseColor("#FFFFFF"));
            lowerEqualizerBandLevelTextView.setText((lowerEqualizerBandLevel / 100) + "dB");

            TextView upperEqualizerBandLevelTextView = new TextView(getContext());
            lowerEqualizerBandLevelTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            upperEqualizerBandLevelTextView.setTextColor(Color.parseColor("#FFFFFF"));
            upperEqualizerBandLevelTextView.setText((upperEqualizerBandLevel / 100) + "dB");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.weight = 1;

            SeekBar seekBar = new SeekBar(getContext());
            TextView textView = new TextView(getContext());
            switch (i) {
                case 0:
                    seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
                    textView = (TextView) view.findViewById(R.id.textView1);
                    break;
                case 1:
                    seekBar = (SeekBar) view.findViewById(R.id.seekBar2);
                    textView = (TextView) view.findViewById(R.id.textView2);
                    break;
                case 2:
                    seekBar = (SeekBar) view.findViewById(R.id.seekBar3);
                    textView = (TextView) view.findViewById(R.id.textView3);
                    break;
                case 3:
                    seekBar = (SeekBar) view.findViewById(R.id.seekBar4);
                    textView = (TextView) view.findViewById(R.id.textView4);
                    break;
                case 4:
                    seekBar = (SeekBar) view.findViewById(R.id.seekBar5);
                    textView = (TextView) view.findViewById(R.id.textView5);
                    break;
            }
            seekBarFinal[i] = seekBar;
            seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN));
            seekBar.getThumb().setColorFilter(new PorterDuffColorFilter(themeColor, PorterDuff.Mode.SRC_IN));
            seekBar.setId(i);
//            seekBar.setLayoutParams(layoutParams);
            seekBar.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);

            textView.setText(frequencyHeaderTextView.getText());
            textView.setTextColor(Color.WHITE);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            if (isEqualizerReloaded) {
                points[i] = seekbarpos[i] - lowerEqualizerBandLevel;
                dataset.addPoint(frequencyHeaderTextView.getText().toString(), points[i]);
                seekBar.setProgress(seekbarpos[i] - lowerEqualizerBandLevel);
            } else {
                points[i] = mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel;
                dataset.addPoint(frequencyHeaderTextView.getText().toString(), points[i]);
                seekBar.setProgress(mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
                seekbarpos[i] = mEqualizer.getBandLevel(equalizerBandIndex);
                isEqualizerReloaded = true;
            }
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mEqualizer.setBandLevel(equalizerBandIndex, (short) (progress + lowerEqualizerBandLevel));
                    points[seekBar.getId()] = mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel;
                    seekbarpos[seekBar.getId()] = (progress + lowerEqualizerBandLevel);
                    equalizerModel.getSeekbarpos()[seekBar.getId()] = (progress + lowerEqualizerBandLevel);
                    dataset.updateValues(points);
                    chart.notifyDataUpdate();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    presetSpinner.setSelection(0);
                    presetPos = 0;
                    equalizerModel.setPresetPos(0);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }

        equalizeSound();

        paint.setColor(Color.parseColor("#555555"));
        paint.setStrokeWidth((float) (1.10 * ratio));

        dataset.setColor(themeColor);
        dataset.setSmooth(true);
        dataset.setThickness(5);

        chart.setXAxis(false);
        chart.setYAxis(false);

        chart.setYLabels(AxisRenderer.LabelPosition.NONE);
        chart.setXLabels(AxisRenderer.LabelPosition.NONE);
       // chart.setGrid(ChartView..NONE, 7, 10, paint);

        chart.setAxisBorderValues(-300, 3300);

        chart.addData(dataset);
        chart.show();

        Button mEndButton = new Button(getContext());
        mEndButton.setBackgroundColor(themeColor);
        mEndButton.setTextColor(Color.WHITE);


        tp = new TextPaint();
        tp.setColor(themeColor);
        tp.setTextSize(65 * ratio);
        tp.setFakeBoldText(true);

        showCase = new ShowcaseView.Builder(getActivity())
                .blockAllTouches()
                .singleShot(4)
                .setStyle(R.style.CustomShowcaseTheme)
                .useDecorViewAsParent()
                .replaceEndButton(mEndButton)
                .setContentTitlePaint(tp)
                .setTarget(new ViewTarget(R.id.showcase_view_equalizer, getActivity()))
                .setContentTitle("Presets")
                .setContentText("Use one of the available presets")
                .build();
        showCase.setButtonText("Next");
        showCase.setButtonPosition(lps);
        showCase.overrideButtonClick(new View.OnClickListener() {
            int count1 = 0;

            @Override
            public void onClick(View v) {
                count1++;
                switch (count1) {
                    case 1:
                        showCase.setTarget(new ViewTarget(R.id.equalizerContainer, getActivity()));
                        showCase.setContentTitle("Equalizer Controls");
                        showCase.setContentText("Use the seekbars to control the Individual frequencies");
                        showCase.setButtonPosition(lps);
                        showCase.setButtonText("Next");
                        break;
                    case 2:
                        showCase.setTarget(new ViewTarget(R.id.controllerBass, getActivity()));
                        showCase.setContentTitle("Bass and Reverb");
                        showCase.setContentText("Use these controls to control Bass and Reverb");
                        showCase.setButtonPosition(lps);
                        showCase.setButtonText("Done");
                        break;
                    case 3:
                        showCase.hide();
                        break;
                }
            }

        });

    }
 /** ==================================================================================================== **/
    private boolean isEqualizerReloaded;
    public static int screen_width;
    public static int screen_height;
    public static float ratio2;
    public static short bassStrength = -1;
    private void SetupEq() {


        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
       // lps.setMargins(margin, margin, margin, navBarHeightSizeinDp + ((Number) (getResources().getDisplayMetrics().density * 5)).intValue());



        if (equalizerModel == null) {
            equalizerModel = new EqualizerModel();
            equalizerModel.isEqualizerEnabled = true;
            Log.e(TAG, "SetupEq: GET FROM NEW");
            isEqualizerReloaded = false;
        } else {
            equalizerModel.isEqualizerEnabled = equalizerModel.isEqualizerEnabled();
            isEqualizerReloaded = true;
            seekbarpos = equalizerModel.getSeekbarpos();
            presetPos = equalizerModel.getPresetPos();
            reverbPreset = equalizerModel.getReverbPreset();
            bassStrength = equalizerModel.getBassStrength();
            Log.e("MAIN", "SetupEq: get from saved "+bassStrength);
        }

        DisplayMetrics metrics = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screen_width = metrics.widthPixels;
        screen_height = metrics.heightPixels;

        ratio = (float) screen_height / (float) 1920;
        ratio2 = (float) screen_width / (float) 1080;
        ratio = Math.min(ratio, ratio2);

    }

    public static int audiosessionid;
    Equalizer mEqualizer;
    private int presetPos;
    public void equalizeSound() {



      //  equalizer.setEnabled(true);

        //equalizer.getNumberOfBands(); //it tells you the number of equalizer in device.

       // equalizer.getNumberOfPresets();

        ArrayList<String> equalizerPresetNames = new ArrayList<>();
        ArrayAdapter<String> equalizerPresetSpinnerAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item,
                equalizerPresetNames);
        equalizerPresetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equalizerPresetNames.add("Custom");

        for (short i = 0; i < mEqualizer.getNumberOfPresets(); i++) {
            equalizerPresetNames.add(mEqualizer.getPresetName(i));
        }

        presetSpinner.setAdapter(equalizerPresetSpinnerAdapter);
        presetSpinner.setDropDownWidth((screen_width * 3) / 4);
        if (isEqualizerReloaded && presetPos != 0) {
//            correctPosition = false;
            presetSpinner.setSelection(presetPos);
        }

        presetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position != 0) {
                        mEqualizer.usePreset((short) (position - 1));
                        presetPos = position;
                        short numberOfFreqBands = 5;

                        final short lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];

                        for (short i = 0; i < numberOfFreqBands; i++) {
                            seekBarFinal[i].setProgress(mEqualizer.getBandLevel(i) - lowerEqualizerBandLevel);
                            points[i] = mEqualizer.getBandLevel(i) - lowerEqualizerBandLevel;
                            seekbarpos[i] = mEqualizer.getBandLevel(i);
                            equalizerModel.getSeekbarpos()[i] = mEqualizer.getBandLevel(i);
                        }
                        dataset.updateValues(points);
                        chart.notifyDataUpdate();
                    }
                } catch (Exception e) {
                    Toast.makeText(ctx, "Error while updating Equalizer", Toast.LENGTH_SHORT).show();
                }
                equalizerModel.setPresetPos(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RefWatcher refWatcher = getRefWatcher();
        refWatcher.watch(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = getRefWatcher();
        refWatcher.watch(this);
        new SaveEqualizer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public boolean isShowcaseVisible() {
        return (showCase != null && showCase.isShowing());
    }

    public void hideShowcase() {
        showCase.hide();
    }

    public void setBlockerVisibility(int visibility) {
        equalizerBlocker.setVisibility(visibility);
    }


    public static class SaveEqualizer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (!isSaveEqualizerRunning) {
         //       Log.e("MAIN", "doInBackground: GUARDANDO...");
                isSaveEqualizerRunning = true;
                try {
                    String json2 = gson.toJson(equalizerModel);
                    preferences.edit().putString("equalizer", json2).commit();
                } catch (Exception e) {

                }
                isSaveEqualizerRunning = false;
            }
            return null;
        }
    }

    private static Gson gson;
    public static boolean isSaveEqualizerRunning;

    private RefWatcher refWatcher;

    public RefWatcher getRefWatcher() {
        
        return refWatcher;
    }


    @Override
    public void onPause() {
        super.onPause();
        new SaveEqualizer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    /** ===================== GET SAVED DATA ==================================== **/


    private void getSavedData() {
        try {
            Gson gson = new Gson();
            Log.d("TIME", "start");
            String json9 = preferences.getString("equalizer", "");
            equalizerModel = gson.fromJson(json9, EqualizerModel.class);
            Log.d("TIME", "equalizer");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
