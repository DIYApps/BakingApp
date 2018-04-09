package com.example.ndp.bakingapp.ui.stepdetails;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.data.models.BakingSteps;
import com.example.ndp.bakingapp.utils.ValidationUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.EventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BakingStepsDetailFragment extends Fragment implements View.OnClickListener {


    private static final String RESUME_WINDOW_KEY = "resume_window_key";
    private static final String RESUME_POSITION_KEY = "resume_position_key";
    private ArrayList<BakingSteps> steps;
    private int position;
    private String STEPS_POSITION_KEY = "steps_position_key";
    private String STEPS_LIST_KEY = "steps_list_key";

    @BindView(R.id.exoplayerBankingStep)
    SimpleExoPlayerView simpleExoPlayerView;

    TextView textViewStepDescription;

    ImageButton imageButtonNextStep;

    ImageButton imageButtonPreviousStep;

    @BindView(R.id.imageViewVideoOverlay)
    ImageView imageViewVideoOverlay;

    @BindView(R.id.videoBufferIndicator)
    ProgressBar videoBufferIndicator;

    private SimpleExoPlayer exoPlayer;
    private boolean shouldAutoPlay;
    private DefaultTrackSelector trackSelector;
    private static final String LOG_TAG = "StepsDetailF";
    private int resumeWindow;
    private long resumePosition;
    private boolean isLandscape;
    private Bitmap mDefaultPlayerBitmap;


    public BakingStepsDetailFragment() {
        // Required empty public constructor
    }

    public void setSteps(ArrayList<BakingSteps> steps) {
        this.steps = steps;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_TAG , "onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_baking_steps_detail, container,
                false);
        ButterKnife.bind(this, view);
        textViewStepDescription = view.findViewById(R.id.textViewStepDescription);

        //set isLandscape if textViewStepDescription is not visible
        isLandscape = textViewStepDescription == null;

        //set the next and previous button
        int visibility = View.VISIBLE;
        if(getContext().getResources().getBoolean(R.bool.is_tablet)){
            visibility = View.INVISIBLE;
        }
        imageButtonNextStep = view.findViewById(R.id.imageButtonNextStep);
        if(imageButtonNextStep != null){
            imageButtonNextStep.setOnClickListener(this);
            imageButtonNextStep.setVisibility(visibility);
        }
        imageButtonPreviousStep = view.findViewById(R.id.imageButtonPreviousStep);
        if(imageButtonPreviousStep != null){
            imageButtonPreviousStep.setOnClickListener(this);
            imageButtonPreviousStep.setVisibility(visibility);
        }

        simpleExoPlayerView.setControllerHideOnTouch(true);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        mDefaultPlayerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cooking);
        // Load the question mark as the background image until the user answers the question.
        simpleExoPlayerView.setDefaultArtwork(mDefaultPlayerBitmap);

        //make the indicator in visible
        videoBufferIndicator.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG , "onActivityCreated");
        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(STEPS_LIST_KEY);
            position = savedInstanceState.getInt(STEPS_POSITION_KEY);
            resumeWindow = savedInstanceState.getInt(RESUME_WINDOW_KEY ,
                    C.INDEX_UNSET);
            resumePosition = savedInstanceState.getLong(RESUME_POSITION_KEY ,
                    C.TIME_UNSET);
        }
        if (isLandscape) {
            //hide the actionbar if the layout is in landscape.
            ActionBar actionBar =  ((AppCompatActivity)getContext()).getSupportActionBar();
            if(null != actionBar){
                actionBar.hide();
            }
        }
        setData();
    }

    private void setData(){
        if (!ValidationUtils.isListEmptyOrNull(steps)) {
            BakingSteps bakingStep = steps.get(position);
            if (!isLandscape) {
                textViewStepDescription.setText(bakingStep.getDescription());
            }
        } else {
            //Log the details
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(LOG_TAG , "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        if (steps != null) {
            outState.putParcelableArrayList(STEPS_LIST_KEY, steps);
            outState.putInt(STEPS_POSITION_KEY, position);

            if(resumeWindow != C.INDEX_UNSET){
                outState.putInt(RESUME_WINDOW_KEY , resumeWindow);
                Log.d(LOG_TAG , "resumeWindow::"+resumeWindow);
            }
            if(resumePosition != C.TIME_UNSET){
                Log.d(LOG_TAG , "resumePosition::"+resumePosition);
                outState.putLong(RESUME_POSITION_KEY , resumePosition);
            }
        }
    }

    private void initExoPlayer() {
        if (exoPlayer == null) {
            // 1. Create a default TrackSelector
            Handler mainHandler = new Handler();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // 2. Create the player
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            // attach the player to the view.
            simpleExoPlayerView.setPlayer(exoPlayer);
            // add listener to player
            exoPlayer.addListener(mPlayerListener);
        }
    }

    private void setVideoSourceAndPlay(){

        if (exoPlayer != null) {

            if(ValidationUtils.isListEmptyOrNull(steps)) {
                return;
            }

            String videoUrlString = steps.get(position).getVideoURL();
            if (ValidationUtils.isStringEmptyOrNull(videoUrlString)) {
                Toast.makeText(getContext(), R.string.empty_video_url_message,
                        Toast.LENGTH_SHORT).show();

                //stop the current video.
                exoPlayer.stop();

                //set the thumbnail if available.
                String photoUrlString = steps.get(position).getThumbnailURL();
                if(!ValidationUtils.isStringEmptyOrNull(photoUrlString)) {
                    //set the thumbnail image
                    Picasso.with(getActivity())
                            .load(photoUrlString)
                            .error(R.drawable.cooking)
                            .into(imageViewVideoOverlay);
                }

                simpleExoPlayerView.setVisibility(View.INVISIBLE);
                imageViewVideoOverlay.setVisibility(View.VISIBLE);
                return;
            }

            if(simpleExoPlayerView.getVisibility() == View.INVISIBLE){
                simpleExoPlayerView.setVisibility(View.VISIBLE);
                imageViewVideoOverlay.setVisibility(View.INVISIBLE);
            }

            Log.d(LOG_TAG , "setVideoSourceAndPlay()::"+ videoUrlString);
            String userAgent = getContext().getPackageName();
            DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);

            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(videoUrlString),
                    dataSourceFactory, new DefaultExtractorsFactory(),
                    null, null);

            Log.d(LOG_TAG , "setVideoSourceAndPlay()::"+videoSource.toString());
            // Prepare the player with the source.
            boolean isPlayerResumed =  resumeWindow != C.INDEX_UNSET;
            if(isPlayerResumed){
                exoPlayer.seekTo(resumePosition);
            }

            exoPlayer.prepare(videoSource , !isPlayerResumed , false);
            exoPlayer.setPlayWhenReady(true);
            Log.d(LOG_TAG , "setVideoSourceAndPlay()::exited.");
        }
    }

    private void stopAndReleasePlayer(){
        if (exoPlayer != null) {
            shouldAutoPlay = exoPlayer.getPlayWhenReady();
            exoPlayer.release();
            exoPlayer = null;
            trackSelector = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG , "onResume");
        initExoPlayer();
        setVideoSourceAndPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG , "onPause");
        updateResumePosition();
        stopAndReleasePlayer();
    }
    private void updateResumePosition() {
        if(exoPlayer !=  null) {
            resumeWindow = exoPlayer.getCurrentWindowIndex();
            resumePosition = Math.max(0, exoPlayer.getContentPosition());
        }
    }

    @Override
    public void onClick(View view) {
        if(ValidationUtils.isListEmptyOrNull(steps)){
            return;
        }
        if(view == imageButtonNextStep){
            if(position == steps.size()-1){
                position = 0;
            }else {
                position++;
            }
            setData();
            setVideoSourceAndPlay();
        }
        if(view == imageButtonPreviousStep){
            if(position == 0){
                position = steps.size()-1;
            }else {
                position--;
            }
            setData();
            setVideoSourceAndPlay();
        }
    }

    //callback
    private Player.EventListener mPlayerListener = new Player.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            Log.d(LOG_TAG , "onPlayerStateChanged()playbackState::"+playbackState);
            if(playbackState == Player.STATE_BUFFERING){
                //show the loading Indicator
                videoBufferIndicator.setVisibility(View.VISIBLE);
            }
            if(playbackState == Player.STATE_READY){
                //hide the loading indicator
                videoBufferIndicator.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity() {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }
    };
}
