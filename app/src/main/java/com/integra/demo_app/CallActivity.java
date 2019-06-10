package com.colabus.Webrtc;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.colabus.ChatUserList;
import com.colabus.FacebookFirbase.CustomApplication;
import com.colabus.Fcm.util.NotificationUtils;
import com.colabus.R;
import com.colabus.SingleUserChatNew;
import com.colabus.Webrtc.AppRTCAudioManager.AudioDevice;
import com.colabus.Webrtc.AppRTCAudioManager.AudioManagerEvents;
import com.colabus.Webrtc.AppRTCClient.RoomConnectionParameters;
import com.colabus.Webrtc.AppRTCClient.SignalingParameters;
import com.colabus.Webrtc.utils.SurfaceViewRenderercol;
import com.colabus.appBean;
import com.colabus.checkinternet.CircleTransform;
import com.colabus.checkinternet.ConnectivityReceiver;
import com.colabus.services.ConnectionDetector;
import com.colabus.services.HTTPService;
import com.colabus.services.Utils;

import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.StreamOpen;
import org.jivesoftware.smack.util.Objects;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.json.packet.JsonPacketExtension;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.packet.MUCInitialPresence;
import org.jivesoftware.smackx.muc.packet.MUCOwner;
import org.jivesoftware.smackx.muc.packet.MUCUser;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.json.JSONException;
import org.json.JSONObject;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.DataChannel;
import org.webrtc.EglBase;
import org.webrtc.FileVideoCapturer;
import org.webrtc.IceCandidate;
import org.webrtc.Logging;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.NetworkMonitor;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RendererCommon.ScalingType;
import org.webrtc.RtpReceiver;
import org.webrtc.ScreenCapturerAndroid;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoFileRenderer;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.colabus.Webrtc.CallFragment.cameraSwitchButton;
import static com.colabus.Webrtc.CallFragment.highlightcount;
import static com.colabus.Webrtc.CallFragment.speakerButton;
import static com.colabus.Webrtc.CallFragment.streamTORemote;
import static com.colabus.Webrtc.CallFragment.toggleMuteButton;
import static com.colabus.Webrtc.CallFragment.videoScalingButton;
import static com.colabus.Webrtc.HighlightList.DataListCall;
import static com.colabus.Webrtc.HighlightList.HighlightListContext;
import static com.colabus.Webrtc.HighlightList.addclicked;
import static com.colabus.Webrtc.HighlightList.getTime;
import static com.colabus.Webrtc.HighlightList.highighlight_time;
import static com.colabus.Webrtc.RoomtextChat.RoomtextChatContext;
import static com.colabus.appBean.callAnswered;
import static com.colabus.appBean.callOccured;
import static com.colabus.appBean.callType;
import static com.colabus.appBean.isOnCall;
import static com.colabus.services.HTTPService.connection;
import static org.webrtc.NetworkMonitor.getAutoDetectorForTest;

//import com.colabus.OneOnOneMessage;

/**
 * Activity for peer connection call setup, call waiting
 * and call view.
 */
public class CallActivity extends Activity implements
        CallFragment.OnCallEvents, View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener, SensorEventListener {
    public static final String EXTRA_ROOMID = "org.appspot.apprtc.ROOMID";
    public static final String EXTRA_URLPARAMETERS = "org.appspot.apprtc.URLPARAMETERS";
    public static final String EXTRA_LOOPBACK = "org.appspot.apprtc.LOOPBACK";
    public static final String EXTRA_VIDEO_CALL = "org.appspot.apprtc.VIDEO_CALL";
    public static final String EXTRA_SCREENCAPTURE = "org.appspot.apprtc.SCREENCAPTURE";
    public static final String EXTRA_CAMERA2 = "org.appspot.apprtc.CAMERA2";
    public static final String EXTRA_VIDEO_WIDTH = "org.appspot.apprtc.VIDEO_WIDTH";
    public static final String EXTRA_VIDEO_HEIGHT = "org.appspot.apprtc.VIDEO_HEIGHT";
    public static final String EXTRA_VIDEO_FPS = "org.appspot.apprtc.VIDEO_FPS";
    public static final String EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED =
            "org.appsopt.apprtc.VIDEO_CAPTUREQUALITYSLIDER";
    public static final String EXTRA_VIDEO_BITRATE = "org.appspot.apprtc.VIDEO_BITRATE";
    public static final String EXTRA_VIDEOCODEC = "VIDEOCODEC";
    public static final String EXTRA_HWCODEC_ENABLED = "org.appspot.apprtc.HWCODEC";
    public static final String EXTRA_CAPTURETOTEXTURE_ENABLED = "org.appspot.apprtc.CAPTURETOTEXTURE";
    public static final String EXTRA_FLEXFEC_ENABLED = "org.appspot.apprtc.FLEXFEC";
    public static final String EXTRA_AUDIO_BITRATE = "org.appspot.apprtc.AUDIO_BITRATE";
    public static final String EXTRA_AUDIOCODEC = "org.appspot.apprtc.AUDIOCODEC";
    public static final String EXTRA_NOAUDIOPROCESSING_ENABLED =
            "org.appspot.apprtc.NOAUDIOPROCESSING";
    public static final String EXTRA_AECDUMP_ENABLED = "org.appspot.apprtc.AECDUMP";
    public static final String EXTRA_OPENSLES_ENABLED = "org.appspot.apprtc.OPENSLES";
    public static final String EXTRA_DISABLE_BUILT_IN_AEC = "org.appspot.apprtc.DISABLE_BUILT_IN_AEC";
    public static final String EXTRA_DISABLE_BUILT_IN_AGC = "org.appspot.apprtc.DISABLE_BUILT_IN_AGC";
    public static final String EXTRA_DISABLE_BUILT_IN_NS = "org.appspot.apprtc.DISABLE_BUILT_IN_NS";
    public static final String EXTRA_ENABLE_LEVEL_CONTROL = "org.appspot.apprtc.ENABLE_LEVEL_CONTROL";
    public static final String EXTRA_DISABLE_WEBRTC_AGC_AND_HPF =
            "org.appspot.apprtc.DISABLE_WEBRTC_GAIN_CONTROL";
    public static final String EXTRA_DISPLAY_HUD = "org.appspot.apprtc.DISPLAY_HUD";
    public static final String EXTRA_TRACING = "org.appspot.apprtc.TRACING";
    public static final String EXTRA_CMDLINE = "org.appspot.apprtc.CMDLINE";
    public static final String EXTRA_RUNTIME = "org.appspot.apprtc.RUNTIME";
    public static final String EXTRA_VIDEO_FILE_AS_CAMERA = "org.appspot.apprtc.VIDEO_FILE_AS_CAMERA";
    public static final String EXTRA_SAVE_REMOTE_VIDEO_TO_FILE =
            "org.appspot.apprtc.SAVE_REMOTE_VIDEO_TO_FILE";
    public static final String EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_WIDTH =
            "org.appspot.apprtc.SAVE_REMOTE_VIDEO_TO_FILE_WIDTH";
    public static final String EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT =
            "org.appspot.apprtc.SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT";
    public static final String EXTRA_USE_VALUES_FROM_INTENT =
            "org.appspot.apprtc.USE_VALUES_FROM_INTENT";
    public static final String EXTRA_DATA_CHANNEL_ENABLED = "org.appspot.apprtc.DATA_CHANNEL_ENABLED";
    public static final String EXTRA_ORDERED = "org.appspot.apprtc.ORDERED";
    public static final String EXTRA_MAX_RETRANSMITS_MS = "org.appspot.apprtc.MAX_RETRANSMITS_MS";
    public static final String EXTRA_MAX_RETRANSMITS = "org.appspot.apprtc.MAX_RETRANSMITS";
    public static final String EXTRA_PROTOCOL = "org.appspot.apprtc.PROTOCOL";
    public static final String EXTRA_NEGOTIATED = "org.appspot.apprtc.NEGOTIATED";
    public static final String EXTRA_ID = "org.appspot.apprtc.ID";
    //we added
    public static final String VIDEO_TRACK_ID = "COLV" + appBean.userId;
    public static final String AUDIO_TRACK_ID = "COLA" + appBean.userId;
    public static final String VIDEO_TRACK_TYPE = "video";
    private static final String TAG = CallActivity.class.getSimpleName();
    private static final int CAPTURE_PERMISSION_REQUEST_CODE = 1;
    // List of mandatory application permissions.
    private static final String[] MANDATORY_PERMISSIONS = {"android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.RECORD_AUDIO", "android.permission.INTERNET"};
    private static final String VIDEO_CODEC_VP8 = "VP8";
    private static final String VIDEO_CODEC_VP9 = "VP9";
    private static final String VIDEO_CODEC_H264 = "H264";
    private static final String VIDEO_CODEC_H264_BASELINE = "H264 Baseline";
    private static final String VIDEO_CODEC_H264_HIGH = "H264 High";
    private static final String AUDIO_CODEC_OPUS = "opus";
    private static final String AUDIO_CODEC_ISAC = "ISAC";
    private static final String VIDEO_CODEC_PARAM_START_BITRATE = "x-google-start-bitrate";
    private static final String VIDEO_FLEXFEC_FIELDTRIAL =
            "WebRTC-FlexFEC-03-Advertised/Enabled/WebRTC-FlexFEC-03/Enabled/";
    private static final String VIDEO_VP8_INTEL_HW_ENCODER_FIELDTRIAL = "WebRTC-IntelVP8/Enabled/";
    private static final String VIDEO_H264_HIGH_PROFILE_FIELDTRIAL =
            "WebRTC-H264HighProfile/Enabled/";
    private static final String DISABLE_WEBRTC_AGC_FIELDTRIAL =
            "WebRTC-Audio-MinimizeResamplingOnMobile/Enabled/";
    private static final String AUDIO_CODEC_PARAM_BITRATE = "maxaveragebitrate";
    private static final String AUDIO_ECHO_CANCELLATION_CONSTRAINT = "googEchoCancellation";
    private static final String AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT = "googAutoGainControl";
    private static final String AUDIO_HIGH_PASS_FILTER_CONSTRAINT = "googHighpassFilter";
    private static final String AUDIO_NOISE_SUPPRESSION_CONSTRAINT = "googNoiseSuppression";
    private static final String AUDIO_LEVEL_CONTROL_CONSTRAINT = "levelControl";
    private static final String DTLS_SRTP_KEY_AGREEMENT_CONSTRAINT = "DtlsSrtpKeyAgreement";
    private static final int HD_VIDEO_WIDTH = 256;
    private static final int HD_VIDEO_HEIGHT = 144;
    private static final int BPS_IN_KBPS = 1000;
    // Peer connection statistics callback period in ms.
    private static final int STAT_CALLBACK_PERIOD = 1000;
    private static final int SENSOR_SENSITIVITY = 4;
    private static final int MAX_CONNECTIONS = 3;
    public static CallActivity callActivityContext;
    public static String calltype = "";
    //    boolean callDisconnected = false;
    public static SharedPreferences appPrefs;
    public static String videotojid = "", videofromjid = "", image = "";
    public static String callIdForDb = "";
    public static boolean isBackground = false;
    public static String userName = "";
    public static int OVERLAY_PERMISSION_REQ_CODE_CHATHEAD = 1234;
    public static int OVERLAY_PERMISSION_REQ_CODE_CHATHEAD_MSG = 5678;
    private static Intent mediaProjectionPermissionResultData;
    private static int mediaProjectionPermissionResultCode;
    private final List<VideoRenderer.Callbacks> remoteRenderers =
            new ArrayList<VideoRenderer.Callbacks>();
    private final PCObserver pcObserver = new PCObserver();
    // private final SDPObserver sdpObserver = new SDPObserver();
    private final SDPObserver sdpObserver = new SDPObserver();
    private final PCObserver1 pcObserver1 = new PCObserver1();
    // private final SDPObserver sdpObserver = new SDPObserver();
    private final SDPObserver1 sdpObserver1 = new SDPObserver1();
    VideoRenderer localRenderer;
    // VideoRenderer remoteRenderer;
    boolean whenCallBusyResponse = false;
    PeerConnectionFactory.Options options = null;
    ImageView userimage_caller, disconnectimg_caller, userimage_receiver, callended_receiver, callaccepted_receiver;
    GradientDrawable drawableCaller, drawableReceiver;
    int color = 0, temp = 0;
    boolean enquee = true;
    FrameLayout frameLayout, fragment_container_audio;
    String ResponseResult = "", roomId, callId, confGroupNameSrt;
    Jid confGroupName = null;
    Jid confGroupNameJid = null;
    RelativeLayout caller_layout, callreceiver, callduration;
    TextView connectingtxt_cal, username_receiver, connectingtxt_receiver, calldurationText;
    Timer timer;
    String extensionToSend = "";
    private MediaConstraints audioConstraints;
    private VideoTrack localVideoTrack;
    private AudioTrack localAudioTrack;
    private AudioTrack remoteAudioTrack;
    private AudioTrack remoteAudioTrack1;
    private MediaStream mediaStream;
    private AudioSource audioSource;
    private VideoSource videoSource;
    private int videoFps;
    private int videoWidth;
    private int videoHeight;
    private String preferredVideoCodec, concallReq = "";
    private String localMediaTag = "COLM" + appBean.userId;
    private PeerConnectionFactory factory;
    //    private PeerConnectionClient peerConnectionClient = null;
    private SignalingParameters signalingParameters;
    private AppRTCAudioManager audioManager = null;
    private EglBase rootEglBase;
    private SurfaceViewRenderercol local_video_view;
    private SurfaceViewRenderercol remote_video_view, remote_video_view_one, remote_video_view_two;
    private VideoFileRenderer videoFileRenderer;
    private Toast logToast;
    private boolean commandLineRun;
    private int runTimeMs;
    private boolean activityRunning;
    private RoomConnectionParameters roomConnectionParameters;
    //    private PeerConnectionParameters peerConnectionParameters;
    private boolean iceConnected;
    private boolean isError;
    private boolean callControlFragmentVisible = true;
    private long callStartedTimeMs = 0;
    private boolean micEnabled = true;
    private boolean micEnabledSpeaker = true;
    private boolean isScreencaptureEnabled = false;
    private boolean screencaptureEnabled = false;
    private boolean onCameraSwitchIcon = true;
    private boolean showToRemote = true;
    private boolean highLight = true;
    private boolean callHold = true;
    // True if local view is in the fullscreen renderer.
    private boolean isSwappedFeeds;
    private boolean isInitator;
    private MediaPlayer mediaPlayer, mediaPlayerForBusy;
    // Controls
    private CallFragment callFragment;
    /*private HudFragment hudFragment;
    private CpuMonitor cpuMonitor;*/
    // enableAudio is set to true if audio should be sent.
    private boolean enableAudio;
    private AudioManager audioManagerspeaker = null;
    private boolean isclicked = false;
    private RelativeLayout root_view;
    private int _xDelta, _yDelta, ScreenWidth, ScreenHeight;
    //screen recorder
    private PermissionResultListener mPermissionResultListener;
    private MediaProjection mMediaProjection;
    private MediaProjectionManager mProjectionManager;
    private FloatingActionButton fab;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SharedPreferences prefs;
    //    private String conversationCallId;
    private MediaStream globalStream;
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private PowerManager powerManager;
    //private PeerConnection peerConnection;
    private PowerManager.WakeLock wakeLock;
    private int field = 0x00000020;
    //    private boolean onCallHangUpSUCN = false;
    private boolean micEnabledSpeakerAudio = false;
    private ArrayList<String> userOnCallInfo = new ArrayList<>();
    //    private PeerConnection peerConnection;
//    //    private final ExecutorService executor;
//    private final PCObserver pcObserver = new PCObserver();
//    private final SDPObserver sdpObserver = new SDPObserver();
//    public static VideoTrack remoteVideoTrack;
//    public static VideoTrack remoteVideoTrack1;
//
    private VideoCapturer videoCapturer;
    // private final PCObserver pcObserver = new PCObserver();
    private PeerConnection[] peerConnection = new PeerConnection[MAX_CONNECTIONS];
    // private VideoTrack[] remoteVideoTracks = new VideoTrack[MAX_CONNECTIONS];
    private LinkedList<IceCandidate> queuedRemoteCandidates;
    private VideoTrack remoteVideoTrack;
    private SessionDescription localSdps; // either offer or answer SDP
    private VideoRenderer remoteRenderer;
    private VideoRenderer.Callbacks[] remoteRenders = new VideoRenderer.Callbacks[MAX_CONNECTIONS];

    //MultiUserChat groupMuc;
    public CallActivity() {
        // Executor thread is started once in private ctor and is used for all
        // peer connection API calls to ensure new peer connection factory is
        // created on the same thread as previously destroyed factory.
//        executor = Executors.newSingleThreadExecutor();
    }

    public static void changeActivityState(String getMessageInstance) {
    }

    @TargetApi(19)
    private static int getSystemUiVisibility() {
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        return flags;
    }

    private static String joinString(
            Iterable<? extends CharSequence> s, String delimiter, boolean delimiterAtEnd) {
        Iterator<? extends CharSequence> iter = s.iterator();
        if (!iter.hasNext()) {
            return "";
        }
        StringBuilder buffer = new StringBuilder(iter.next());
        while (iter.hasNext()) {
            buffer.append(delimiter).append(iter.next());
        }
        if (delimiterAtEnd) {
            buffer.append(delimiter);
        }
        return buffer.toString();
    }

    private static String movePayloadTypesToFront(List<String> preferredPayloadTypes, String mLine) {
        // The format of the media description line should be: m=<media> <port> <proto> <fmt> ...
        final List<String> origLineParts = Arrays.asList(mLine.split(" "));
        if (origLineParts.size() <= 3) {
            Log.e(TAG, "Wrong SDP media description format: " + mLine);
            return null;
        }
        final List<String> header = origLineParts.subList(0, 3);
        final List<String> unpreferredPayloadTypes =
                new ArrayList<String>(origLineParts.subList(3, origLineParts.size()));
        unpreferredPayloadTypes.removeAll(preferredPayloadTypes);
        // Reconstruct the line with |preferredPayloadTypes| moved to the beginning of the payload
        // types.
        final List<String> newLineParts = new ArrayList<String>();
        newLineParts.addAll(header);
        newLineParts.addAll(preferredPayloadTypes);
        newLineParts.addAll(unpreferredPayloadTypes);
        return joinString(newLineParts, " ", false /* delimiterAtEnd */);
    }

    /**
     * Returns the line number containing "m=audio|video", or -1 if no such line exists.
     */
    private static int findMediaDescriptionLine(boolean isAudio, String[] sdpLines) {
        final String mediaDescription = isAudio ? "m=audio " : "m=video ";
        for (int i = 0; i < sdpLines.length; ++i) {
            if (sdpLines[i].startsWith(mediaDescription)) {
                return i;
            }
        }
        return -1;
    }

    private static String preferCodec(String sdpDescription, String codec, boolean isAudio) {
        final String[] lines = sdpDescription.split("\r\n");
        final int mLineIndex = findMediaDescriptionLine(isAudio, lines);
        if (mLineIndex == -1) {
            Log.w(TAG, " so can it make changes No mediaDescription line, so can't prefer " + codec);
            return sdpDescription;
        }
        // A list with all the payload types with name |codec|. The payload types are integers in the
        // range 96-127, but they are stored as strings here.
        final List<String> codecPayloadTypes = new ArrayList<String>();
        // a=rtpmap:<payload type> <encoding name>/<clock rate> [/<encoding parameters>]
        final Pattern codecPattern = Pattern.compile("^a=rtpmap:(\\d+) " + codec + "(/\\d+)+[\r]?$");
        for (int i = 0; i < lines.length; ++i) {
            Matcher codecMatcher = codecPattern.matcher(lines[i]);
            if (codecMatcher.matches()) {
                codecPayloadTypes.add(codecMatcher.group(1));
            }
        }
        if (codecPayloadTypes.isEmpty()) {
            Log.w(TAG, " so can it make changes No payload types with name " + codec);
            return sdpDescription;
        }

        final String newMLine = movePayloadTypesToFront(codecPayloadTypes, lines[mLineIndex]);
        if (newMLine == null) {
            return sdpDescription;
        }
        Log.d(TAG, " so can it make changes Change media description from: " + lines[mLineIndex] + " to " + newMLine);
        lines[mLineIndex] = newMLine;
        return joinString(Arrays.asList(lines), "\r\n", true /* delimiterAtEnd */);
    }

    //Method to create app directory which is default directory for storing recorded videos
    public static void createDir() {
        File appDir = new File(Environment.getExternalStorageDirectory() + File.separator + Const.APPDIR);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !appDir.isDirectory()) {
            appDir.mkdirs();
        }
    }

    @Override
    protected void onPause() {
        System.out.println("coming inside onpause-----------");
        super.onPause();
        boolean isConnected = ConnectivityReceiver.isConnected();
        System.out.println();
        if (isConnected) {
            isBackground = true;
            callHoldOnPause();
            if (enquee) {
                if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                    mediaPlayer.pause();
                }
//            mediaPlayer.release();
            }
        } else {
            enquee = false;
        }
        if (!callType.equals("AV")) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onResume() {
        System.out.println("coming inside onresume-----------");
        super.onResume();
        boolean isConnected = ConnectivityReceiver.isConnected();
//        if (isConnected) {
        System.out.println("isBackground--->" + isBackground + "enquee" + enquee);
        if (isBackground) {
            if (calltype.equals("AV")) {
                callResumeOnResume();
            }
            if (enquee) {
                if (mediaPlayer != null) mediaPlayer.start();
            }
            isBackground = false;
        }
        CustomApplication.getInstance().setConnectivityListener(this);
        if (!callType.equals("AV")) {
            mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }

//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.disconnectimg_caller: {
                if (!whenCallBusyResponse)
                    callrequestcancelledMethod();
                else {
                    mediaPlayerForBusy.stop();
                    callOccured = false;
                    enquee = false;
//                    disconnect();
                    showToRemote = false;
                    callHold = false;

                    if (appBean.fromSUCNContext) {
                        appBean.fromSUCNContext = false;
                        Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
                        myIntent.putExtra("username", userName);
                        myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
                        myIntent.putExtra("imgUrl", image);
                        myIntent.putExtra("from", "afterCall");
                        startActivity(myIntent);
                    }
                    callDisconnectPresence();
                    finish();
                }
                break;
            }
            case R.id.callaccepted_receiver: {
                enquee = false;
                mediaPlayer.stop();
                if (concallReq.equals("")) {
                    callcallacceptedMethod();
                } else {
                    //String str[] = concallReq.split(",");
                    callcallacceptedMethodConference(appBean.userId, callId);
//                    for (int i =0;i<str.length;i++) {
//
//
//                    }
                }
                break;
            }
            case R.id.callended_receiver: {
                receiverDeclinedCall();
                break;
            }
            case R.id.remote_video_view: {
                if (isclicked) {
                    // Activate call and HUD fragments and start the call.
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.hide(callFragment);
//                    ft.hide(hudFragment);
                    ft.commit();
                    isclicked = false;

                    ViewGroup.LayoutParams frameParams = frameLayout.getLayoutParams();

                    frameParams.height = 0;
                    frameParams.width = 0;
                    frameLayout.setLayoutParams(frameParams);
                    frameLayout.setVisibility(View.INVISIBLE);

//                    System.out.println(" so call Width and height " + previousValueWidth + " " + previousValueHeight);
//
//                    TranslateAnimation translateAnimation = new TranslateAnimation(local_video_view.getX(), local_video_view.getX() - 50, local_video_view.getY(), local_video_view.getY() - 50);
//                    translateAnimation.setDuration(1000);
//                    translateAnimation.setFillAfter(true);
//                    local_video_view.startAnimation(translateAnimation);

                } else {
                    // Activate call and HUD fragments and start the call.
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.show(callFragment);
//                    ft.show(hudFragment);
                    ft.commit();
                    isclicked = true;

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    ViewGroup.LayoutParams frameParams = frameLayout.getLayoutParams();

                    frameParams.height = Integer.valueOf(displayMetrics.heightPixels);
                    frameParams.width = displayMetrics.heightPixels;
                    frameLayout.setLayoutParams(frameParams);
                    frameLayout.setVisibility(View.VISIBLE);

//                    float callWidht = callFrame.getX();
//                    float callHeight = callFrame.getY();

//                    System.out.println(" so call Width and height " + callWidht + " " + callHeight);

//                    TranslateAnimation translateAnimation = new TranslateAnimation(local_video_view.getX(),callWidht, local_video_view.getY(),callHeight);
//                    translateAnimation.setDuration(1000);
//                    translateAnimation.setFillAfter(true);
//                    local_video_view.startAnimation(translateAnimation);
                }
            }


        }
    }

    private void callcallacceptedMethodConference(String from, String to) {
        if (calltype.equals("AV")) {
            callreceiver.setVisibility(View.GONE);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("calltype", calltype);
                jsonObject.put("type", "callaccepted");
                jsonObject.put("devicetype", "android");
                //jsonObject.put("callid", callIdForDb);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
            try {
                EntityBareJid bareJid = JidCreate.entityBareFrom(to + "@devchat.colabus.com");
                EntityBareJid bareJidfrom = JidCreate.entityBareFrom(from + "@devchat.colabus.com");

                if (connection != null) {
                    try {
                        final Message msg = new Message();
                        msg.setTo(bareJid);
                        msg.setType(Message.Type.normal);
                        msg.addExtension(json);
                        msg.setFrom(bareJidfrom);
                        ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                        Chat chat = chatmanager.chatWith(bareJid);
                        chat.send(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }
            updateDBAboutCallReceiverDetails();
            callduration.setVisibility(View.VISIBLE);
            sendPresencetoUser();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
            callDurationOnThread();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        System.out.println("intent " + intent.getStringExtra("userID") + intent.getStringExtra("USER") + intent.getStringExtra("userImage"));
        GetSpeed();
        /*myIntent1.putExtra("userID", this.userId);
        myIntent1.putExtra("USER", this.userName);
        myIntent1.putExtra("status", this.status);
        myIntent1.putExtra("from", "userslist");
        myIntent1.putExtra("userImage", val);*/
//        super.onNewIntent(intent);
        addCallInitMethod(intent.getStringExtra("userID"));
        createPeerConnection(1);

    }

    private void initialiseViews() {
        root_view = (RelativeLayout) findViewById(R.id.root_view);
        local_video_view = (SurfaceViewRenderercol) root_view.findViewById(R.id.local_video_view);
        remote_video_view = (SurfaceViewRenderercol) findViewById(R.id.remote_video_view);
        remote_video_view_one = (SurfaceViewRenderercol) findViewById(R.id.remote_video_view_one);
        remote_video_view_two = (SurfaceViewRenderercol) findViewById(R.id.remote_video_view_two);
        remote_video_view_one.setVisibility(View.GONE);
        remote_video_view_two.setVisibility(View.GONE);
        local_video_view.setVisibility(View.VISIBLE);
        // Create video renderers.
        rootEglBase = EglBase.create();
        local_video_view.init(rootEglBase.getEglBaseContext(), null);
        local_video_view.setScalingType(ScalingType.SCALE_ASPECT_FIT);
        remote_video_view.init(rootEglBase.getEglBaseContext(), null);
        remote_video_view.setScalingType(ScalingType.SCALE_ASPECT_FILL);
        remote_video_view.setOnClickListener(this);
        local_video_view.setZOrderMediaOverlay(true);
        /*  local_video_view.setEnableHardwareScaler(true *//* enabled *//*);
        remote_video_view.setEnableHardwareScaler(true *//* enabled *//*);*/
        callFragment = new CallFragment();
//        hudFragment = new HudFragment();

        caller_layout = (RelativeLayout) findViewById(R.id.caller_layout);
        callreceiver = (RelativeLayout) findViewById(R.id.callreceiver);

        userimage_caller = (ImageView) findViewById(R.id.userimage_caller);
        disconnectimg_caller = (ImageView) findViewById(R.id.disconnectimg_caller);
        connectingtxt_cal = (TextView) findViewById(R.id.connectingtxt_cal);

        userimage_receiver = (ImageView) findViewById(R.id.userimage_receiver);
        callended_receiver = (ImageView) findViewById(R.id.callended_receiver);
        callaccepted_receiver = (ImageView) findViewById(R.id.callaccepted_receiver);
        username_receiver = (TextView) findViewById(R.id.username_receiver);
        connectingtxt_receiver = (TextView) findViewById(R.id.connectingtxt_receiver);

        frameLayout = (FrameLayout) findViewById(R.id.call_fragment_container);
        fragment_container_audio = (FrameLayout) findViewById(R.id.fragment_container_audio);
        fragment_container_audio.setVisibility(View.GONE);
        disconnectimg_caller.setOnClickListener(this);

        callended_receiver.setOnClickListener(this);

        callaccepted_receiver.setOnClickListener(this);

        callduration = (RelativeLayout) findViewById(R.id.callduration);
        callduration.setVisibility(View.GONE);

        calldurationText = (TextView) findViewById(R.id.calldurationtext);

        // drag and drop
        ViewGroup.LayoutParams params = local_video_view.getLayoutParams();
        getWidthAndHeight();
//        RelativeLayout.LayoutParams layoutParams =
        local_video_view.setLayoutParams(new RelativeLayout.LayoutParams(params.width, params.height));
        local_video_view.setOnTouchListener(new ChoiceListner());

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(field, getLocalClassName());


    }

    private void getWidthAndHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ScreenWidth = displayMetrics.widthPixels;
        ScreenHeight = displayMetrics.heightPixels;

        ScreenWidth = ScreenWidth - 50;
        ScreenHeight = ScreenHeight - 200;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Coming inside---->");

        callActivityContext = this;
        Thread.setDefaultUncaughtExceptionHandler(new UnhandledExceptionHandler(this));
// Set window styles for fullscreen-window size. Needs to be done before
        // adding content.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_KEEP_SCREEN_ON
                | LayoutParams.FLAG_DISMISS_KEYGUARD | LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(getSystemUiVisibility());
        setContentView(R.layout.activity_call);
        final Intent intent = getIntent();


        //if (!appBean.isOnCall) {

        isInitator = intent.getBooleanExtra("isInitator", true);
        videotojid = intent.getStringExtra("videotojid");
        System.out.println("videotojid------->" + videotojid);
        videofromjid = intent.getStringExtra("videofromjid");
        System.out.println("videofromjid------->" + videofromjid);
        image = intent.getStringExtra("image");

        calltype = intent.getStringExtra("callType");
        roomId = intent.getStringExtra("roomId");
        callId = intent.getStringExtra("callId");
        concallReq = intent.getStringExtra("userOnCallInfo");

        System.out.println("concallReq------->" + concallReq);

        if (appBean.lighttpdPath.contains("?")) {
            appBean.lighttpdPath = appBean.lighttpdPath.substring(0, appBean.lighttpdPath.lastIndexOf("?"));
        }
        extensionToSend = appBean.lighttpdPath.substring(appBean.lighttpdPath.lastIndexOf(".") + 1, appBean.lighttpdPath.length());
        try {
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);

            confGroupName = JidCreate.entityBareFrom(roomId + "@conference.devchat.colabus.com/"
                    + "user_" +
                    appBean.userId + "_" + appBean.loggedInName + "_" + extensionToSend);
            confGroupNameSrt = roomId + "@conference.devchat.colabus.com";
            confGroupNameJid = JidCreate.bareFrom(roomId + "@conference.devchat.colabus.com");
//            confGroupNameJid = JidCreate.entityBareFrom(roomId + "@conference.devchat.colabus.com/"
//                    + "user_" +
//                    appBean.userId + "_" + appBean.loggedInName + "_" + extensionToSend);
            //  groupMuc = manager.getMultiUserChat(confGroupName);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

        appBean.roomid = "0";
//            appBean.highLightcallID = callIdForDb;
//            conversationCallId = intent.getStringExtra("conversationId");
//            System.out.println(" so conversationCallId from while getting call " + conversationCallId);
        //   }

        Log.e(TAG, isInitator + videotojid + videofromjid + image + calltype);
        // Create UI controls.
        initialiseViews();
        checkConnection();
//        phoneStateLister();
//        initScreenCapturer();

        System.out.println(" so callType isOnCall " + callType + "   " + appBean.isOnCall);

        if (appBean.isOnCall) {

            if (callType.equals("AV")) {
                if (globalStream.videoTracks.size() == 1) {
                    local_video_view.setVisibility(View.GONE);
                    remote_video_view.setVisibility(View.VISIBLE);

                    remoteVideoTrack = globalStream.videoTracks.getFirst();
                    remoteAudioTrack = globalStream.audioTracks.getFirst();
                    remoteRenderer = new VideoRenderer(remote_video_view);
                    remoteVideoTrack.setEnabled(true);
                    remoteVideoTrack.addRenderer(remoteRenderer);

                    // Activate call and HUD fragments and start the call.

                    ViewGroup.LayoutParams params = local_video_view.getLayoutParams();
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    params.height = Integer.valueOf(displayMetrics.heightPixels / 4);
                    params.width = Integer.valueOf(displayMetrics.widthPixels / 3);
                    System.out.println(" so what is the height params " + params.height + " " + params.width);
                    local_video_view.setPadding(0, 0, 10, 10);
                    local_video_view.setLayoutParams(params);
                    local_video_view.setVisibility(View.VISIBLE);

                    ViewGroup.LayoutParams frameParams = frameLayout.getLayoutParams();

                    frameParams.height = 0;
                    frameParams.width = 0;
                    frameLayout.setLayoutParams(frameParams);
                    frameLayout.setVisibility(View.VISIBLE);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.call_fragment_container, callFragment);
//                                ft.add(R.id.hud_fragment_container, hudFragment);
                    ft.hide(callFragment);
//                                ft.hide(hudFragment);
                    ft.commit();

//                    if (calltype.equals("AV")) {
//                        videoCapturer = createVideoCapturer();
//                        mediaStream.addTrack(createVideoTrack(videoCapturer));
//                    }

                    if (videoCapturer instanceof CameraVideoCapturer) {
                        if (videoFps == 0) {
                            videoFps = 30;
                        }
                        videoCapturer.startCapture(HD_VIDEO_WIDTH, HD_VIDEO_HEIGHT, videoFps);
                        Logging.d(TAG, "Capturing format: recaptureAndRenderView startCapture" + videoWidth + "x" + videoHeight + "@" + videoFps);
                        callResumeForEndUser();
                    }

                    mediaStream.addTrack(createAudioTrack());
                    peerConnection[0].addStream(mediaStream);


                }


            } else {
                // audio call
                System.out.println("comming Audio call =========>" + calltype);
                if (globalStream.audioTracks.size() == 1) {
                    local_video_view.setVisibility(View.GONE);
                    remote_video_view.setVisibility(View.GONE);
                    callended_receiver.setVisibility(View.GONE);
                    caller_layout.setVisibility(View.VISIBLE);
                    caller_layout.setBackgroundColor(Color.parseColor("#234D6E"));
//                                caller_layout.setBackgroundColor(getResources().getColor(R.color.White));
                    disconnectimg_caller.setVisibility(View.GONE);
                    connectingtxt_cal.setVisibility(View.VISIBLE);
                    connectingtxt_cal.setText(getCallerName(videotojid.substring(0, videotojid.indexOf("@"))));
                    Glide.with(CallActivity.this)
                            .load(getCallerImage(videotojid.substring(0, videotojid.indexOf("@"))).toString())
                            .placeholder(R.drawable.user2)
                            .transform(new CircleTransform(CallActivity.this))
                            .into(userimage_caller);
                    remoteAudioTrack = globalStream.audioTracks.getFirst();

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    ViewGroup.LayoutParams frameParams = frameLayout.getLayoutParams();

                    frameParams.height = Integer.valueOf(displayMetrics.heightPixels / 4);
                    frameParams.width = displayMetrics.heightPixels;
                    fragment_container_audio.setLayoutParams(frameParams);
                    fragment_container_audio.setVisibility(View.VISIBLE);
                    fragment_container_audio.setBackgroundColor(Color.parseColor("#234D6E"));
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.fragment_container_audio, callFragment);
                    ft.show(callFragment);
                    ft.commit();
                }

            }
            callFragment.setArguments(intent.getExtras());

        } else {
            if (calltype.equals("AV")) {
                if (isInitator) {
                    callOccured = true;
                    caller_layout.setVisibility(View.VISIBLE);
                    callreceiver.setVisibility(View.GONE);
                    local_video_view.setVisibility(View.VISIBLE);
                    userimage_caller.setVisibility(View.VISIBLE);
                    disconnectimg_caller.setVisibility(View.VISIBLE);
//                connectingtxt_cal.setText(appBean.userId);
//                Glide.with(CallActivity.this)
//                        .load(image) // add your image url
//                        .placeholder(R.drawable.round_user_img)
//                        .transform(new CircleTransform(CallActivity.this)) // applying the image transformer
//                        .into(userimage_caller);

                    drawableCaller = (GradientDrawable) userimage_caller.getBackground();
                    image = getCallerImage(videotojid.substring(0, videotojid.indexOf("@"))).toString();
                    Glide.with(CallActivity.this)
                            .load(image)
                            .placeholder(R.drawable.user2) // add your image url
                            .transform(new CircleTransform(CallActivity.this)) // applying the image transformer
                            .into(userimage_caller);

                    userName = getCallerName(videotojid.substring(0, videotojid.indexOf("@")));
                    connectingtxt_cal.setText(userName);

                    mediaPlayer = MediaPlayer.create(this, R.raw.dialer_tone_new);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    updateDBAboutCallDetails();

                } else {
                    callIdForDb = intent.getStringExtra("callIdForDB");
                    caller_layout.setVisibility(View.GONE);
                    callreceiver.setVisibility(View.VISIBLE);
                    local_video_view.setVisibility(View.VISIBLE);
                    username_receiver.setVisibility(View.VISIBLE);
                    userimage_receiver.setVisibility(View.VISIBLE);
                    username_receiver.setText(appBean.userId);
//                Glide.with(CallActivity.this)
//                        .load(image) // add your image url
//                        .placeholder(R.drawable.round_user_img)
//                        .transform(new CircleTransform(CallActivity.this)) // applying the image transformer
//                        .into(userimage_receiver);
                    connectingtxt_receiver.setText("Colabus Video Call");
                    drawableReceiver = (GradientDrawable) userimage_receiver.getBackground();
                    image = getCallerImage(videotojid.substring(0, videotojid.indexOf("@"))).toString();
                    Glide.with(CallActivity.this)
                            .load(image)
                            .placeholder(R.drawable.user2)
                            .transform(new CircleTransform(CallActivity.this))
                            .into(userimage_receiver);
                    userName = getCallerName(getCallerName(videotojid.substring(0, videotojid.indexOf("@"))).toString());
                    callaccepted_receiver.setImageResource(R.drawable.camgreen);
                    username_receiver.setText(userName);

//                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    mediaPlayer = MediaPlayer.create(this, R.raw.incoming_ringtone);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
            } else {
                System.out.println("comming Audio call =========>" + calltype);
                if (isInitator) {
                    callOccured = true;
                    local_video_view.setVisibility(View.GONE);
                    remote_video_view.setVisibility(View.GONE);
                    callreceiver.setVisibility(View.GONE);
                    caller_layout.setVisibility(View.VISIBLE);
                    userimage_caller.setVisibility(View.VISIBLE);
                    disconnectimg_caller.setVisibility(View.VISIBLE);
                    connectingtxt_cal.setVisibility(View.VISIBLE);
//                connectingtxt_cal.setText(appBean.userId);
                    caller_layout.setBackgroundColor(Color.parseColor("#234D6E"));
//                Glide.with(CallActivity.this)
//                        .load(image) // add your image url
//                        .placeholder(R.drawable.round_user_img)
//                        .transform(new CircleTransform(CallActivity.this)) // applying the image transformer
//                        .into(userimage_caller);

                    drawableCaller = (GradientDrawable) userimage_caller.getBackground();
                    image = getCallerImage(videotojid.substring(0, videotojid.indexOf("@"))).toString();
                    Glide.with(CallActivity.this)
                            .load(image)
                            .placeholder(R.drawable.user2) // add your image url
                            .transform(new CircleTransform(CallActivity.this)) // applying the image transformer
                            .into(userimage_caller);

                    userName = getCallerName(getCallerName(videotojid.substring(0, videotojid.indexOf("@"))).toString());

                    connectingtxt_cal.setText(userName);

                    mediaPlayer = MediaPlayer.create(this, R.raw.dialer_tone_new);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();

                    updateDBAboutCallDetails();
                } else {
                    callIdForDb = intent.getStringExtra("callIdForDB");
                    local_video_view.setVisibility(View.GONE);
                    remote_video_view.setVisibility(View.GONE);
                    caller_layout.setVisibility(View.GONE);
                    callreceiver.setVisibility(View.VISIBLE);
                    callreceiver.setBackgroundColor(Color.parseColor("#234D6E"));
                    username_receiver.setVisibility(View.VISIBLE);
                    userimage_receiver.setVisibility(View.VISIBLE);
//                username_receiver.setText(appBean.userId);
//                Glide.with(CallActivity.this)
//                        .load(image) // add your image url
//                        .placeholder(R.drawable.round_user_img)
//                        .transform(new CircleTransform(CallActivity.this)) // applying the image transformer
//                        .into(userimage_receiver);
//                connectingtxt_receiver.setText(appBean.userId);
                    connectingtxt_receiver.setText("Colabus Audio Call");
                    image = getCallerImage(videotojid.substring(0, videotojid.indexOf("@"))).toString();
                    drawableReceiver = (GradientDrawable) userimage_receiver.getBackground();
                    Glide.with(CallActivity.this)
                            .load(image)
                            .placeholder(R.drawable.user2)
                            .transform(new CircleTransform(CallActivity.this))
                            .into(userimage_receiver);

                    userName = getCallerName(videotojid.substring(0, videotojid.indexOf("@"))).toString();

                    username_receiver.setText(userName);

//                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    mediaPlayer = MediaPlayer.create(this, R.raw.incoming_ringtone);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }


            }
            startThread();
            callFragment.setArguments(intent.getExtras());
//            pcObserver = new PCObserver();
//            pcObserver.connectionId = 0;
//            pcObserver[1] = new PCObserver();
//            pcObserver[1].connectionId = 1;
//            pcObserver[2] = new PCObserver();
//            pcObserver[2].connectionId = 2;

//            sdpObserver[0] = new SDPObserver();
//            sdpObserver[0].connectionId = 0;
//            sdpObserver[1] = new SDPObserver();
//            sdpObserver[1].connectionId = 1;
//            sdpObserver[2] = new SDPObserver();
//            sdpObserver[2].connectionId = 2;

            String fieldTrials = "";
            fieldTrials += VIDEO_VP8_INTEL_HW_ENCODER_FIELDTRIAL;
            // Check preferred video codec.
            preferredVideoCodec = VIDEO_CODEC_VP8;
            if (EXTRA_VIDEOCODEC != null) {
                switch (EXTRA_VIDEOCODEC) {
                    case VIDEO_CODEC_VP8:
                        preferredVideoCodec = VIDEO_CODEC_VP8;
                        break;
                    case VIDEO_CODEC_VP9:
                        preferredVideoCodec = VIDEO_CODEC_VP9;
                        break;
                    case VIDEO_CODEC_H264_BASELINE:
                        preferredVideoCodec = VIDEO_CODEC_H264;
                        break;
                    case VIDEO_CODEC_H264_HIGH:
                        // TODO(magjed): Strip High from SDP when selecting Baseline instead of using field trial.
                        fieldTrials += VIDEO_H264_HIGH_PROFILE_FIELDTRIAL;
                        preferredVideoCodec = VIDEO_CODEC_H264;
                        break;
                    default:
                        preferredVideoCodec = VIDEO_CODEC_VP8;
                }
            }
            Log.d(TAG, "Preferred video codec: " + preferredVideoCodec);
            PeerConnectionFactory.initializeFieldTrials(fieldTrials);
            Log.d(TAG, "Field trials: " + fieldTrials);

            // Create peer connection factory.
            PeerConnectionFactory.initializeAndroidGlobals(
                    this, true);
            if (options != null) {
                Log.d(TAG, "Factory networkIgnoreMask option: " + options.networkIgnoreMask);
            }
//            factory = new PeerConnectionFactory(options);
            factory = new PeerConnectionFactory(options);
            Log.d(TAG, "Peer connection factory created.");

            // Create and audio manager that will take care of audio routing,
            // audio modes, audio device enumeration etc.
            audioManager = AppRTCAudioManager.create(getApplicationContext());
            // Store existing audio settings and change audio mode to
            // MODE_IN_COMMUNICATION for best possible VoIP performance.
            Log.d(TAG, "Starting the audio manager...");
            audioManager.start(new AudioManagerEvents() {
                // This method will be called each time the number of available audio
                // devices has changed.
                @Override
                public void onAudioDeviceChanged(
                        AudioDevice audioDevice, Set<AudioDevice> availableAudioDevices) {
                    onAudioManagerDevicesChanged(audioDevice, availableAudioDevices);
                }
            });


            // Create audio constraints.
            audioConstraints = new MediaConstraints();
            // added for audio performance measurements
            boolean noAudioProcessing = false, enableLevelControl = false;
            if (noAudioProcessing) {
                Log.d(TAG, "Disabling audio processing");
                audioConstraints.mandatory.add(
                        new MediaConstraints.KeyValuePair(AUDIO_ECHO_CANCELLATION_CONSTRAINT, "false"));
                audioConstraints.mandatory.add(
                        new MediaConstraints.KeyValuePair(AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT, "false"));
                audioConstraints.mandatory.add(
                        new MediaConstraints.KeyValuePair(AUDIO_HIGH_PASS_FILTER_CONSTRAINT, "false"));
                audioConstraints.mandatory.add(
                        new MediaConstraints.KeyValuePair(AUDIO_NOISE_SUPPRESSION_CONSTRAINT, "false"));
            }
            if (enableLevelControl) {
                Log.d(TAG, "Enabling level control.");
                audioConstraints.mandatory.add(
                        new MediaConstraints.KeyValuePair(AUDIO_LEVEL_CONTROL_CONSTRAINT, "true"));
            }

            if (factory == null || isError) {
                Log.e(TAG, "Peerconnection factory is not created");
                return;
            }

            Log.d(TAG, "Create peer connection.");

            if (calltype.equals("AV")) {
                factory.setVideoHwAccelerationOptions(rootEglBase.getEglBaseContext(), rootEglBase.getEglBaseContext());
            }
            queuedRemoteCandidates = new LinkedList<IceCandidate>();
            createPeerConnection(0);
            // createPeerConnection(1);
            //createPeerConnection(2);

            //createPeerConnection(2);

            if (calltype.equals("AV")) {

                videoFps = intent.getIntExtra(EXTRA_AUDIO_BITRATE, 0);
           /* DisplayMetrics displayMetrics = getDisplayMetrics();
            videoWidth = displayMetrics.widthPixels;
            videoHeight = displayMetrics.heightPixels;*/
                // If video resolution is not specified, default to HD.
                if (videoWidth == 0 || videoHeight == 0) {
                    videoWidth = HD_VIDEO_WIDTH;
                    videoHeight = HD_VIDEO_HEIGHT;
                }
                // If fps is not specified, default to 30.
                if (videoFps == 0) {
                    videoFps = 30;
                }
                Logging.d(TAG, "Capturing format: " + videoWidth + "x" + videoHeight + "@" + videoFps);
            }
        }
        appBean.isOnCall = true;
        if (!concallReq.equals("")) {
            System.out.println("concallReq------->" + concallReq + concallReq.length());
            List<String> con = new ArrayList<String>();
            String string = concallReq.replace("[", "");
            String string1 = string.replace("]", "");
            String str[] = string1.split(",");
            if (str.length > 0) {
                System.out.println("concallReq------->" + concallReq);
                createPeerConnection(1);
            }
        }
    }

    private void createPeerConnection(int connectionId) {


        //queuedRemoteCandidates = new LinkedList<IceCandidate>();
        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
//        iceServers.add(new PeerConnection.IceServer("stun:stun.l.devchat.colabus.com:3478"));
//        iceServers.add(new PeerConnection.IceServer("stun:stun.l.devchat.colabus.com:3479"));
//            iceServers.add(new PeerConnection.IceServer("turn:104.238.119.35:3478?transport=udp", "username1", "password1"));
//            iceServers.add(new PeerConnection.IceServer("turn:104.238.119.35:3478?transport=tcp", "username1", "password1"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));
        iceServers.add(new PeerConnection.IceServer("turn:numb.viagenie.ca", "ajmeerkhan.s@stridus.com", "Ajmeerkhan21!"));

        PeerConnection.RTCConfiguration rtcConfig =
                new PeerConnection.RTCConfiguration(iceServers);
        // TCP candidates are only useful when connecting to a server that supports
        // ICE-TCP.
        rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        // Use ECDSA encryption.
        rtcConfig.keyType = PeerConnection.KeyType.ECDSA;
        if (connectionId == 0) {
            peerConnection[0] = factory.createPeerConnection(rtcConfig, getDefaultPeerConnectionConstrain(), pcObserver);
            mediaStream = factory.createLocalMediaStream(localMediaTag);
            if (calltype.equals("AV")) {
                Log.d(" so instance ", " in the beginning  ");
                videoCapturer = createVideoCapturer();
                mediaStream.addTrack(createVideoTrack(videoCapturer));
            }

            mediaStream.addTrack(createAudioTrack());
            peerConnection[0].addStream(mediaStream);
        } else {
            peerConnection[1] = factory.createPeerConnection(rtcConfig, getDefaultPeerConnectionConstrain(), pcObserver1);
            mediaStream = factory.createLocalMediaStream(localMediaTag);
            if (calltype.equals("AV")) {
                Log.d(" so instance ", " in the beginning  ");
                videoCapturer = createVideoCapturer();
                mediaStream.addTrack(createVideoTrack(videoCapturer));
            }

            mediaStream.addTrack(createAudioTrack());
            peerConnection[1].addStream(mediaStream);
        }

    }

    /* private void initScreenCapturer() {
         prefs = PreferenceManager.getDefaultSharedPreferences(this);
         //Arbitrary "Write to external storage" permission since this permission is most important for the app
         requestPermissionStorage();

         //Acquiring media projection service to start screen mirroring
         mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

         //Respond to app shortcut
         if (getIntent().getAction() != null && getIntent().getAction().equals(getString(R.string.app_shortcut_action))) {
             startActivityForResult(mProjectionManager.createScreenCaptureIntent(), Const.SCREEN_RECORD_REQUEST_CODE);
             return;
         }

         requestPermissionAudio();
     }
 */
    /* Marshmallow style permission request.
     * We also present the user with a dialog to notify why storage permission is required */
    public boolean requestPermissionStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.storage_permission_request_title))
                    .setMessage(getString(R.string.storage_permission_request_summary))
                    .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(CallActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Const.EXTDIR_REQUEST_CODE);
                        }
                    })
                    .setCancelable(false);

            alert.create().show();
            return false;
        }
        return true;
    }

    //Permission on api below 23 are granted by default
    @TargetApi(23)
    public void requestSystemWindowsPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, Const.SYSTEM_WINDOWS_CODE);
        }
    }

    //Pass the system windows permission result to settings fragment
    @TargetApi(23)
    private void setSystemWindowsPermissionResult() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                mPermissionResultListener.onPermissionResult(Const.SYSTEM_WINDOWS_CODE,
                        new String[]{"System Windows Permission"},
                        new int[]{PackageManager.PERMISSION_GRANTED});
            } else {
                mPermissionResultListener.onPermissionResult(Const.SYSTEM_WINDOWS_CODE,
                        new String[]{"System Windows Permission"},
                        new int[]{PackageManager.PERMISSION_DENIED});
            }
        } else {
            mPermissionResultListener.onPermissionResult(Const.SYSTEM_WINDOWS_CODE,
                    new String[]{"System Windows Permission"},
                    new int[]{PackageManager.PERMISSION_GRANTED});
        }
    }

    // Marshmallow style permission request for audio recording
    public void requestPermissionAudio() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    Const.AUDIO_REQUEST_CODE);
        }
    }

    // Overriding onRequestPermissionsResult method to receive results of marshmallow style permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Const.EXTDIR_REQUEST_CODE:
                if ((grantResults.length > 0) &&
                        (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    Log.d(Const.TAG, "write storage Permission Denied");
                    /* Disable floating action Button in case write storage permission is denied.
                     * There is no use in recording screen when the video is unable to be saved */
                    fab.setEnabled(false);
                } else {
                    /* Since we have write storage permission now, lets create the app directory
                     * in external storage*/
                    Log.d(Const.TAG, "write storage Permission granted");
                    createDir();
                }
                return;
            case Const.AUDIO_REQUEST_CODE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d(Const.TAG, "Record audio permission granted.");

                } else {
                    Log.d(Const.TAG, "Record audio permission denied");
                }
                return;
            default:
                Log.d(Const.TAG, "Unknown permission request with request code: " + requestCode);
        }

        // Let's also pass the result data to SettingsPreferenceFragment using the callback interface
        if (mPermissionResultListener != null) {
            mPermissionResultListener.onPermissionResult(requestCode, permissions, grantResults);
        }
    }

    //Overriding onActivityResult to capture screen mirroring permission request result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE_CHATHEAD) {
            if (!Utils.canDrawOverlays(CallActivity.this)) {
                needPermissionDialog(requestCode);
            } else {
                Intent intent = new Intent(getApplicationContext(), HighlightList.class);
                intent.putExtra("from", "callactivity");
                intent.putExtra("callid", callIdForDb);
//                        intent.putExtra("convId", appBean.conversationId);
                intent.putExtra("hTime", appBean.calldurationTime);
                intent.putExtra("senderId", videotojid.substring(0, videotojid.indexOf("@")));
                intent.putExtra("userName", userName);
                intent.putExtra("confGroupNameJid", confGroupNameJid.toString());

                startActivity(intent);
                startService(new Intent(getApplicationContext(), FloatingWidgetService.class));
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out);
                highlightcount.setVisibility(View.GONE);
            }

        } else if (requestCode == OVERLAY_PERMISSION_REQ_CODE_CHATHEAD_MSG) {
            if (!Utils.canDrawOverlays(CallActivity.this)) {
                needPermissionDialog(requestCode);
            } else {
                callRunning();
                Intent myIntent = new Intent(getApplicationContext(), RoomtextChat.class);
                myIntent.putExtra("username", userName);
                myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
                myIntent.putExtra("imgUrl", image);
                myIntent.putExtra("callId", callIdForDb);
                myIntent.putExtra("confGroupNameJid", confGroupNameJid.toString());
                myIntent.putExtra("from", "RoomtextChat");
//                Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
////            appBean.conversationId = conversationCallId;
////            System.out.println("so appBean.conversationId = conversationCallId " + appBean.conversationId + " " + conversationCallId);
////            myIntent.putExtra("convId", conversationCallId);
//                myIntent.putExtra("username", userName);
//                myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
//                myIntent.putExtra("imgUrl", image);
//                myIntent.putExtra("from", "callactivity");
//            myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(myIntent);
                startService(new Intent(getApplicationContext(), FloatingWidgetService.class));
            }

        }
    }


    private void phoneStateLister() {
        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private String getCallerName(String videofromjid) {
        System.out.println(" so this is videofrom " + videofromjid + " " + videotojid + " " + appBean.contactsinfo.length());
        for (int i = 0; i < appBean.contactsinfo.length(); i++) {
            try {
                String uId = appBean.contactsinfo.getJSONObject(i).getString("userId");

                if (videofromjid.equals(uId)) {
                    videofromjid = appBean.contactsinfo.getJSONObject(i).getString("userName");
                    System.out.println(" so this is name user" + videofromjid);
                    break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return videofromjid;
    }

    private String getCallerImage(String videofromjid) {
        System.out.println(" so this is videofrom " + videofromjid + " " + videotojid + " " + appBean.contactsinfo.length());
        for (int i = 0; i < appBean.contactsinfo.length(); i++) {
            try {
                String uId = appBean.contactsinfo.getJSONObject(i).getString("userId");

                if (videofromjid.equals(uId)) {
                    videofromjid = appBean.contactsinfo.getJSONObject(i).getString("imageUrl");
                    System.out.println(" so this is name image" + videofromjid);
                    break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return videofromjid;
    }

    private AudioTrack createAudioTrack() {
        audioSource = factory.createAudioSource(audioConstraints);
        localAudioTrack = factory.createAudioTrack(AUDIO_TRACK_ID, audioSource);
        localAudioTrack.setEnabled(true);
        return localAudioTrack;
    }

    private VideoTrack createVideoTrack(VideoCapturer capturer) {
        videoSource = factory.createVideoSource(capturer);
        capturer.changeCaptureFormat(videoWidth, videoHeight, videoFps);
        capturer.startCapture(videoWidth, videoHeight, videoFps);
        Logging.d(TAG, "Capturing format: startCapture" + videoWidth + "x" + videoHeight + "@" + videoFps);
        localVideoTrack = factory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
        localVideoTrack.setEnabled(true);
        localVideoTrack.addRenderer(new VideoRenderer(local_video_view));
        return localVideoTrack;
    }

    private MediaConstraints getDefaultPeerConnectionConstrain() {
        MediaConstraints temp = new MediaConstraints();
        temp.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
        return temp;
    }

    private MediaConstraints getDefaultMediaConstraints() {
        MediaConstraints temp = new MediaConstraints();
        return temp;
    }

    private MediaConstraints createAConstraints() {
        MediaConstraints temp = new MediaConstraints();
        temp.mandatory.add(new MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"));
        return temp;
    }

/*    @TargetApi(21)
    private void startScreenCapture() {
        MediaProjectionManager mediaProjectionManager =
                (MediaProjectionManager) getApplication().getSystemService(
                        Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(), CAPTURE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CAPTURE_PERMISSION_REQUEST_CODE)
            return;
        mediaProjectionPermissionResultCode = resultCode;
        mediaProjectionPermissionResultData = data;
    }*/

    private MediaConstraints createAVConstraints() {
        MediaConstraints temp = new MediaConstraints();
        temp.mandatory.add(new MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"));
        temp.mandatory.add(new MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"));
        return temp;
    }

    public void listenForHighlightMethod(Message message, String callid) {
        System.out.println(" so we have listner for videoCall 7 videohighlight inside CallActivity" + callActivityContext + callOccured + callIdForDb);

        if (callIdForDb.toString().trim().equals(callid)) {
            System.out.println(" so we have listner for videoCall 7 videohighlight inside CallActivity inside if " + callIdForDb);
            CheckCasesHighlight CheckCasesHighlight = new CheckCasesHighlight(message);
            CheckCasesHighlight.reflectActiivityStatusHighlight();
        }
    }

    public void listenForComposeMessageMethod(Message message, String callId) {
//        StanzaFilter filter = new AndFilter(new StanzaTypeFilter(Message.class),
//                new ToMatchesFilter(jid, true));
//        StanzaListener listenForCompose = new StanzaListener() {
//            @Override
//            public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
//                Message message = (Message) packet;

//            }
//        };
//        connection.addSyncStanzaListener(listenForCompose, filter);

        System.out.println(" know values " + callId + " " + callIdForDb);
        System.out.println(" know values " + callId.toString().trim() + " " + callIdForDb.toString().trim());
        System.out.println(" know values check equals ?? " + callIdForDb.toString().trim().equals(callId.toString().trim()));

        // if (callIdForDb.toString().trim().equals(callId.toString().trim())) {
        System.out.println(" So callIDForDB " + callIdForDb);
        CheckCases checkCases = new CheckCases(message);
        checkCases.reflectActiivityStatus();
        // }
        System.out.println("running in listenForComposeMessageMethod");

    }

    private void receiverDeclinedCall() {

        JSONObject jsonObject = new JSONObject();

        //<message xmlns="jabber:client" type="normal" to=“1079@devchat.colabus.com" from=“7@devchat.colabus.com/9dzmchkq84">
        // <json xmlns="urn:xmpp:json:0">{"calltype":"AV","type":"calldeclined"}</json></message>
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "calldeclined");
            jsonObject.put("callid", callIdForDb);

            // use JsonPacket to send Json

            JsonPacketExtension jsonPacketExtension = new JsonPacketExtension(jsonObject.toString());
            try {
                EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
                EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);

                if (connection != null) {
                    final Message msg = new Message();
                    msg.setTo(bareJid);
                    msg.setType(Message.Type.normal);
                    msg.addExtension(jsonPacketExtension);
                    msg.setFrom(bareJidfrom);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    try {
                        chat.send(msg);
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        callOccured = false;
        enquee = false;
//        disconnect();
//        appBean.isOnCall = false;

        appBean.isOnCall = false;
        showToRemote = false;
        callHold = false;
        if (appBean.fromSUCNContext) {
            appBean.fromSUCNContext = false;
            Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
            myIntent.putExtra("username", userName);
            myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
            myIntent.putExtra("imgUrl", image);
            myIntent.putExtra("from", "afterCall");
            startActivity(myIntent);
        }
        callDisconnectPresence();
        finish();
    }

    @TargetApi(17)
    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager =
                (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        return displayMetrics;
    }

    private boolean useCamera2() {
        return Camera2Enumerator.isSupported(this) && getIntent().getBooleanExtra(EXTRA_CAMERA2, true);
    }

    private boolean captureToTexture() {
        return getIntent().getBooleanExtra(EXTRA_CAPTURETOTEXTURE_ENABLED, false);
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        Logging.d(TAG, "Looking for front facing cameras.");
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating front facing camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
        Logging.d(TAG, "Looking for other cameras.");
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating other camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    @TargetApi(21)
    private VideoCapturer createScreenCapturer() {
        if (mediaProjectionPermissionResultCode != Activity.RESULT_OK) {
            reportError("User didn't give permission to capture the screen.");
            return null;
        }
        return new ScreenCapturerAndroid(
                mediaProjectionPermissionResultData, new MediaProjection.Callback() {
            @Override
            public void onStop() {
                reportError("User revoked permission to capture the screen.");
            }
        });
    }

    @Override
    protected void onDestroy() {
        Thread.setDefaultUncaughtExceptionHandler(null);
        disconnect();
        if (logToast != null) {
            logToast.cancel();
        }
        if (rootEglBase != null) {
            rootEglBase.release();
        }
        super.onDestroy();
    }

    // CallFragment.OnCallEvents interface implementation.
    @Override
    public void onCallHangUp() {
//        disconnect();
        showToRemote = false;
        callHold = false;

        if (isOnCall) {
            if (NotificationUtils.isMyServiceRunningForCall(CallActivity.this))
                stopService(new Intent(getApplicationContext(), FloatingWidgetService.class));
            if (SingleUserChatNew.video != null) {
                SingleUserChatNew.video.setVisibility(View.VISIBLE);
            }
            if (SingleUserChatNew.audioButton != null) {
                SingleUserChatNew.audioButton.setVisibility(View.VISIBLE);
            }
            isOnCall = false;
        }

        if (appBean.fromSUCNContext) {
            appBean.fromSUCNContext = false;
            Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
            myIntent.putExtra("username", userName);
            myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
            System.out.println("afterCall--> in CallAct " + image);
            myIntent.putExtra("imgUrl", image);
            myIntent.putExtra("from", "afterCall");
            startActivity(myIntent);
        }
        calldisconnectMethod();
        callDisconnectPresence();
        finish();
       /* if (isServiceRunning(RecorderService.class)){
            Intent recordStopIntent = new Intent(this, RecorderService.class);
            recordStopIntent.setAction(Const.SCREEN_RECORDING_STOP);
            startService(recordStopIntent);
        }*/
    }

    private void callDisconnectPresence() {
        try {

            if (isInitator) {
                PresenceCustom joinPresence = new PresenceCustom(confGroupName, Presence.Type.unavailable);
                connection.sendStanza(joinPresence);
            } else {
                PresenceCustomUser joinPresence = new PresenceCustomUser(confGroupName, Presence.Type.unavailable);
                connection.sendStanza(joinPresence);
            }
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCallconfernce() {
        Intent intent = new Intent(getApplicationContext(), ChatUserList.class);
        appBean.classfrom = "CallActivity";
        startActivity(intent);

        //createPeerConnection(1);
//        userOnCallInfo.add(videotojid.substring(0, videotojid.indexOf("@")));
        /*userOnCallInfo.add(videofromjid.substring(0, videofromjid.indexOf("@")));*/
//        GetSpeed();
//        addCallInitMethod();

    }

    private void addCallInitMethod(String userId) {
//<message type="normal" to="950@devchat.colabus.com"><json xmlns="urn:xmpp:json:0">
//{"calltype":"AV","type":"callconfrequest”,”devicetype”:”ios”,” callId”:”16”,"NameforChatRoom":"userMeet_1113_2019012125211",
// "userOnCallInfo":"[16,1113]"}</json></message>
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "callconfrequest");
            jsonObject.put("devicetype", "android");
            jsonObject.put("callid", callId);
            jsonObject.put("NameforChatRoom", roomId);
            jsonObject.put("userOnCallInfo", userOnCallInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            EntityBareJid bareJid = JidCreate.entityBareFrom(userId + "@devchat.colabus.com");
            EntityBareJid entityBareJid = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message();
                    msg.setTo(bareJid);
                    msg.setFrom(entityBareJid);
                    msg.setType(Message.Type.normal);
                    msg.addExtension(json);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    chat.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        sendPresenceOwner();
    }

    // CallFragment.OnCallEvents interface implementation.
    @Override
    public void onCallchat() {
        if (peerConnection != null) {
            if (!Utils.canDrawOverlays(CallActivity.this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                needPermissionDialog(OVERLAY_PERMISSION_REQ_CODE_CHATHEAD_MSG);
            } else {
                callRunning();
                Intent myIntent = new Intent(getApplicationContext(), RoomtextChat.class);
                myIntent.putExtra("username", userName);
                myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
                myIntent.putExtra("imgUrl", image);
                myIntent.putExtra("callId", callId);
                myIntent.putExtra("confGroupNameJid", confGroupNameJid.toString());
                myIntent.putExtra("from", "RoomtextChat");
                startActivity(myIntent);
//                Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
//                myIntent.putExtra("username", userName);
//                myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
//                myIntent.putExtra("imgUrl", image);
//                myIntent.putExtra("roomId",roomId);
//                myIntent.putExtra("from", "callactivity");
//                startActivity(myIntent);
                startService(new Intent(getApplicationContext(), FloatingWidgetService.class));
            }
        }
    }

    private void callRunning() {
        appBean.isOnCall = true;
        if (audioSource != null) {
            audioSource.dispose();
            audioSource = null;
        }
//        Log.d(TAG, "Stopping capture.");
        if (videoCapturer != null) {
            try {
                videoCapturer.stopCapture();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            videoCapturer.dispose();
//            videoCapturer = null;
        }
//        if (videoSource != null) {
//            videoSource.dispose();
//            videoSource = null;
//        }
        /*if (null != callActivityContext) {
            callActivityContext = null;
        }*/
//        finish();
    }

    @Override
    public boolean onCameraSwitchIcon() {
        if (callHold) {
            if (peerConnection != null) {
                onCameraSwitchIcon = !onCameraSwitchIcon;
                onCameraSwitch(onCameraSwitchIcon);
            }
        }
        return onCameraSwitchIcon;
    }

    @Override
    public boolean onSpeakerSwitchAudio() {

        if (callHold) {
            if (peerConnection != null) {
                setSpeakerEnabled(micEnabledSpeakerAudio);
                micEnabledSpeakerAudio = !micEnabledSpeakerAudio;
            }
        }
        return micEnabledSpeakerAudio;
    }

    public void onCameraSwitch(final boolean onCameraSwitchIcon) {
        if (callHold) {
            if (peerConnection != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switchCameraInternal();
                    }
                });
            }
        }
    }

//    public void onVideoScreenRecorder(final boolean isScreencaptureEnabled) {
//        if (peerConnection != null) {
//            if (!isScreencaptureEnabled) {
//                if (mMediaProjection == null && !isServiceRunning(RecorderService.class)) {
//                    //Request Screen recording permission
//                    startActivityForResult(mProjectionManager.createScreenCaptureIntent(), Const.SCREEN_RECORD_REQUEST_CODE);
//                } else if (isServiceRunning(RecorderService.class)) {
//                    //stop recording if the service is already active and recording
//                    Toast.makeText(CallActivity.this, "Screen already recording", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                if (isServiceRunning(RecorderService.class)) {
//                    stopScreenSharing();
//                }
//            }
//        }
//    }

    // Set stop intent and start the recording service
    private void stopScreenSharing() {
        Intent stopIntent = new Intent(this, RecorderService.class);
        stopIntent.setAction(Const.SCREEN_RECORDING_STOP);
        startService(stopIntent);
    }

    //Method to check if the service is running
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onSpeakerSwitch() {

        if (callHold) {

            if (peerConnection != null) {
                micEnabledSpeaker = !micEnabledSpeaker;
                setSpeakerEnabled(micEnabledSpeaker);
            }

        }
        return micEnabledSpeaker;
    }

    public boolean streamTORemote() {

        if (callHold) {

            if (peerConnection != null) {
                showToRemote = !showToRemote;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!showToRemote) {
                            stopCapturerAndRemoveLocalView();
                            videoScalingButton.setEnabled(false);
                        } else {
                            recaptureAndRenderView();
                            videoScalingButton.setEnabled(true);
                        }
                    }
                });
            }
        }
        return showToRemote;
    }

    public boolean highLight() {
        if (peerConnection != null) {
            highLight = !highLight;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    if (isInitator) {
                    if (!Utils.canDrawOverlays(CallActivity.this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        needPermissionDialog(OVERLAY_PERMISSION_REQ_CODE_CHATHEAD);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), HighlightList.class);
                        intent.putExtra("from", "callactivity");
                        intent.putExtra("callid", callIdForDb);
//                        intent.putExtra("convId", appBean.conversationId);
                        intent.putExtra("hTime", appBean.calldurationTime);
                        intent.putExtra("senderId", videotojid.substring(0, videotojid.indexOf("@")));
                        intent.putExtra("userName", userName);
                        intent.putExtra("confGroupNameJid", confGroupNameJid.toString());
                        startActivity(intent);
                        startService(new Intent(getApplicationContext(), FloatingWidgetService.class));
                        overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out);
                        highlightcount.setVisibility(View.GONE);
                    }
                }
            });
        }
        return highLight;
    }

    //Permission is not available then display toast
    private void needPermissionDialog(final int requestCode) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CallActivity.this);
        builder.setMessage("Enable 'Display Overlay Permission' to allow Text Chat!");
        builder.setPositiveButton("Enable",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        requestPermission(requestCode);
                    }
                });
        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void requestPermission(int requestCode) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, requestCode);
    }

    private void recaptureAndRenderView() {

//        local_video_view.setVisibility(View.VISIBLE);
        if (videoCapturer instanceof CameraVideoCapturer) {
            videoCapturer.startCapture(videoWidth, videoHeight, videoFps);
            Logging.d(TAG, "Capturing format: recaptureAndRenderView startCapture" + videoWidth + "x" + videoHeight + "@" + videoFps);
            Toast.makeText(CallActivity.this, "You have Resumed video ", Toast.LENGTH_LONG).show();
            notifyPlayForEndUser();
        }
    }

    private void stopCapturerAndRemoveLocalView() {

        if (videoCapturer instanceof CameraVideoCapturer) {
            try {
                videoCapturer.stopCapture();
//                local_video_view.setVisibility(View.GONE);
                Toast.makeText(CallActivity.this, "You have Paused video ", Toast.LENGTH_LONG).show();
                notifyPauseForEndUser();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void callHoldOnPause() {

        System.out.println(" calling first second ");
        if (callHold && showToRemote) {
            if (videoCapturer instanceof CameraVideoCapturer) {
                try {
                    videoCapturer.stopCapture();
                    notifyPauseForEndUser();
//                local_video_view.setVisibility(View.GONE);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void callHold() {

        System.out.println(" so what is callHold " + callHold);
        if (videoCapturer instanceof CameraVideoCapturer) {
            try {
                videoCapturer.stopCapture();
//                local_video_view.setVisibility(View.GONE);
                callHoldForEndUser();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (localAudioTrack != null) {
            localAudioTrack.setEnabled(false);
        }

    }


    private void callResumeOnResume() {
        if (callHold && showToRemote) {
            if (videoCapturer instanceof CameraVideoCapturer) {
                videoCapturer.startCapture(videoWidth, videoHeight, videoFps);
                Logging.d(TAG, "Capturing format: recaptureAndRenderView startCapture" + videoWidth + "x" + videoHeight + "@" + videoFps);

            }
        }
        if (callHold && showToRemote) notifyPlayForEndUser();

    }

    private void callResume() {
        System.out.println("coming inside callResume-----------");
//        local_video_view.setVisibility(View.VISIBLE);
        if (videoCapturer instanceof CameraVideoCapturer) {
            videoCapturer.startCapture(videoWidth, videoHeight, videoFps);
            Logging.d(TAG, "Capturing format: recaptureAndRenderView startCapture" + videoWidth + "x" + videoHeight + "@" + videoFps);
            callResumeForEndUser();
        }

        if (localAudioTrack != null) {
            localAudioTrack.setEnabled(true);
        }

    }


    private void callHoldForEndUser() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "streamstatus");
            jsonObject.put("status", "off");
            jsonObject.put("callid", callIdForDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    // Message message = groupMuc.createMessage();
                    final Message msg = new Message(confGroupNameJid, Message.Type.groupchat);
                    msg.setTo(confGroupNameJid);
                    msg.setType(Message.Type.groupchat);
                    msg.addExtension(json);
                    // msg.setTo(bareJid);
                    // msg.setFrom(bareJidfrom);
                    // groupMuc.sendMessage(msg);
                    connection.sendStanza(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

    }

    private void callResumeForEndUser() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "streamstatus");
            jsonObject.put("status", "on");
            jsonObject.put("callid", callIdForDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message(confGroupNameJid, Message.Type.groupchat);
                    msg.setTo(confGroupNameJid);
                    msg.setType(Message.Type.groupchat);
                    msg.addExtension(json);
                    //msg.setFrom(bareJidfrom);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    //groupMuc.sendMessage(msg);
                    connection.sendStanza(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

    }


    private void notifyPauseForEndUser() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "videostatus");
            jsonObject.put("status", "off");
            jsonObject.put("callid", callIdForDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message(confGroupNameJid, Message.Type.groupchat);
                    msg.setTo(confGroupNameJid);
                    msg.setType(Message.Type.groupchat);
                    msg.addExtension(json);
                    // msg.setFrom(bareJidfrom);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    //groupMuc.sendMessage(msg);
                    connection.sendStanza(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

    }

    private void notifyPlayForEndUser() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "videostatus");
            jsonObject.put("status", "on");
            jsonObject.put("callid", callIdForDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            // EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message(confGroupNameJid, Message.Type.groupchat);
                    msg.setTo(confGroupNameJid);
                    msg.setType(Message.Type.groupchat);
                    msg.addExtension(json);
                    //msg.setFrom(bareJidfrom);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    // groupMuc.sendMessage(msg);
                    connection.sendStanza(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

    }

    private void AudioMute() {

        System.out.println(" so this is callnotresponded " + confGroupNameJid + " " + videotojid);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "audiostatus");
            jsonObject.put("status", "off");
            jsonObject.put("callid", callIdForDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            //EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    //MessageCustom msg = new MessageCustom(confGroupNameJid,Message.Type.groupchat);
                    final Message msg = new Message(confGroupNameJid, Message.Type.groupchat);
                    msg.setTo(confGroupNameJid);
                    msg.setType(Message.Type.groupchat);
                    // msg.setFrom(bareJidfrom);
                    msg.addExtension(json);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    // groupMuc.sendMessage(msg);
                    connection.sendStanza(msg);
//                    Message message = new Message(confGroupName,Message.Type.groupchat);
//                    message.addExtension(json);
//                    connection.sendStanza(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

    }

    private void AudioUnMute() {
//<message to='userMeet_16_20191616495@conference.devchat.colabus.com' type='groupchat'
//roomType='conference' xmlns='jabber:client'><json xmlns='urn:xmpp:json:0'>
//{"calltype":"AV","type":"audiostatus","status":"off","callid":"8769"}</json></message>
        System.out.println(" so this is callnotresponded " + confGroupNameJid + " " + videotojid);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "audiostatus");
            jsonObject.put("status", "on");
            jsonObject.put("callid", callIdForDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            //EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);

            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message(confGroupNameJid, Message.Type.groupchat);
                    msg.setTo(confGroupNameJid);
                    msg.setType(Message.Type.groupchat);
                    msg.addExtension(json);

                    //msg.setFrom(bareJidfrom);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    //groupMuc.sendMessage(msg);
                    connection.sendStanza(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

    }

    // <message type="normal" to="1113@devchat.colabus.com"><json xmlns="urn:xmpp:json:0">
    // {"calltype":"AV","type":"sendspeed”,” callId”:”16”,"callspeed":"1.5"}</json></message>


    private void SendSpeed() {
        System.out.println(" RequestSpeed-----------");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "sendspeed");
            jsonObject.put("callId", callIdForDb);
            jsonObject.put("callspeed", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            EntityBareJid bareJid = JidCreate.entityBareFrom("951@devchat.colabus.com");
            EntityBareJid entityBareJid = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message();
                    msg.setTo(bareJid);
                    msg.setFrom(entityBareJid);
                    msg.setType(Message.Type.normal);
                    msg.addExtension(json);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    chat.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }

    //<message type="normal" to="1113@devchat.colabus.com"><json xmlns="urn:xmpp:json:0">
    // {"calltype":"AV","type":"getspeed”,” callId”:”16”}</json></message>

    private void GetSpeed() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "getspeed");
            jsonObject.put("callId", callId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid +
                    "@devchat.colabus.com");
            EntityBareJid entityBareJid = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message();
                    msg.setTo(bareJid);
                    msg.setFrom(entityBareJid);
                    msg.setType(Message.Type.normal);
                    msg.addExtension(json);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    chat.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

    }

    private void switchCameraInternal() {
        if (videoCapturer instanceof CameraVideoCapturer) {
            if (isError || videoCapturer == null) {
                Log.e(TAG, "Failed to switch camera. Video: " + ". Error : " + isError);
                return; // No video is sent or only one camera is available or error happened.
            }
            Log.d(TAG, "Switch camera");
            CameraVideoCapturer cameraVideoCapturer = (CameraVideoCapturer) videoCapturer;
            cameraVideoCapturer.switchCamera(null);
        } else {
            Log.d(TAG, "Will not switch camera, video caputurer is not a camera");
        }
    }

//    @Override
//    public void onVideoScalingSwitch(ScalingType scalingType) {
//        remote_video_view.setScalingType(scalingType);
//    }


    @Override
    public void onCaptureFormatChange(int width, int height, int framerate) {

        if (peerConnection != null) {
            changeCaptureFormat(width, height, framerate);
        }
    }

    public void changeCaptureFormat(final int width, final int height, final int framerate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                changeCaptureFormatInternal(width, height, framerate);
            }
        });
    }

    @Override
    public boolean onToggleMic() {
        if (callHold) {
            if (peerConnection != null) {
                micEnabled = !micEnabled;
                setAudioEnabled(micEnabled);
            }
        }
        return micEnabled;
    }

    @Override
    public boolean onVideoScreenRecordericon() {
        if (peerConnection != null) {
            isScreencaptureEnabled = !isScreencaptureEnabled;
//            onVideoScreenRecorder(isScreencaptureEnabled);
        }
        return isScreencaptureEnabled;
    }

    public void chatIcon() {

        Toast.makeText(CallActivity.this, " Implementing the functionality", Toast.LENGTH_SHORT).show();

    }

    public void setAudioEnabled(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                enableAudio = enable;
                if (localAudioTrack != null) {
                    localAudioTrack.setEnabled(enableAudio);
                }
                if (enableAudio) {
                    AudioUnMute();
                    Toast.makeText(CallActivity.this, "You have UnMuted audio ", Toast.LENGTH_LONG).show();
                } else {
                    AudioMute();
                    Toast.makeText(CallActivity.this, "You have Muted audio ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setSpeakerEnabled(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                enableAudio = enable;
                audioManager.setSpeakerphoneOn(enableAudio);
            }
        });
    }

    private void changeCaptureFormatInternal(int width, int height, int framerate) {
        if (isError || videoCapturer == null) {
            Log.e(TAG,
                    "Failed to change capture format. Video: " + ". Error : " + isError);
            return;
        }
        Log.d(TAG, "changeCaptureFormat: " + width + "x" + height + "@" + framerate);
        videoSource.adaptOutputFormat(width, height, framerate);
    }

    // This method is called when the audio manager reports audio device change,
    // e.g. from wired headset to speakerphone.
    private void onAudioManagerDevicesChanged(
            final AudioDevice device, final Set<AudioDevice> availableDevices) {
        Log.d(TAG, "onAudioManagerDevicesChanged: " + availableDevices + ", "
                + "selected: " + device);
        // TODO(henrika): add callback handler.
    }

    private void disconnectWithErrorMessage(final String errorMessage) {
        if (commandLineRun || !activityRunning) {
            Log.e(TAG, "Critical error: " + errorMessage);
//            disconnect();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getText(R.string.channel_error_title))
                    .setMessage(errorMessage)
                    .setCancelable(false)
                    .setNeutralButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
//                                    disconnect();
                                }
                            })
                    .create()
                    .show();
        }
    }

    // Log |msg| and Toast about it.
    private void logAndToast(String msg) {
        Log.d(TAG, msg);
        if (logToast != null) {
            logToast.cancel();
        }
        logToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        logToast.show();
    }

    private void reportError(final String description) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isError) {
                    isError = true;
                    disconnectWithErrorMessage(description);
                }
            }
        });
    }

    private VideoCapturer createVideoCapturer() {
        VideoCapturer videoCapturer;
        String videoFileAsCamera = getIntent().getStringExtra(MediaStore.ACTION_VIDEO_CAPTURE);
        if (videoFileAsCamera != null) {
            try {
                videoCapturer = new FileVideoCapturer(videoFileAsCamera);
                Log.d(" so instance ", " video Capturer " + videoCapturer);
            } catch (IOException e) {
                reportError("Failed to open video file for emulated camera");
                return null;
            }
        } else if (screencaptureEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(" so instance ", " video Capturer first else if ");
            return createScreenCapturer();
        } else if (useCamera2()) {
            Log.d(" so instance ", " video Capturer second else if ");
            if (!captureToTexture()) {
                Log.d(" so instance ", " video Capturer second else if inside if  ");
                reportError(getString(R.string.camera2_texture_only_error));
//                return null;
            }

            Logging.d(TAG, "Creating capturer using camera2 API.");
            videoCapturer = createCameraCapturer(new Camera2Enumerator(this));
        } else {
            Log.d(" so instance ", " video Capturer else  ");
            Logging.d(TAG, "Creating capturer using camera1 API.");
            videoCapturer = createCameraCapturer(new Camera1Enumerator(captureToTexture()));
        }
        if (videoCapturer == null) {
            Log.d(" so instance ", " video Capturer last if  ");
            reportError("Failed to open camera");
            return null;
        }
        return videoCapturer;
    }

    private void setSwappedFeeds(boolean isSwappedFeeds) {
        Logging.d(TAG, "setSwappedFeeds: " + isSwappedFeeds);
        this.isSwappedFeeds = isSwappedFeeds;
//        localProxyRenderer.setTarget(isSwappedFeeds ? remote_video_view : local_video_view);
//        remoteProxyRenderer.setTarget(isSwappedFeeds ? local_video_view : remote_video_view);
        remote_video_view.setMirror(isSwappedFeeds);
        local_video_view.setMirror(!isSwappedFeeds);
    }

    private void candidateMethod(IceCandidate iceCandidate) {
        //Stanza
        //<message type="normal" to="1568@devchat.colabus.com" from="1079@devchat.colabus.com/6sdbbm6g7m"><json xmlns="urn:xmpp:json:0">
// {"calltype":"AV","callid":"9175","type":"candidate",
//"ice":{"id":"audio","candidate":"candidate:2401801666 1 udp 2122260223 192.168.0.43 49945 typ host generation 0 ufrag 2ip8 network-id 1 network-cost 10",
//                                        "label":0}}</json></message>
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("id", iceCandidate.sdpMid);
            jsonObject1.put("candidate", iceCandidate.sdp);
            jsonObject1.put("label", iceCandidate.sdpMLineIndex);
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "candidate");
            jsonObject.put("callid", callIdForDb);
            jsonObject.put("ice", jsonObject1);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
//            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message();
                    msg.setTo(bareJid);
                    msg.setFrom(bareJidfrom);
                    msg.setType(Message.Type.normal);
                    msg.addExtension(json);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    chat.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }

    public void addRemoteIceCandidate(final IceCandidate candidate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (peerConnection != null && !isError) {
                    if (queuedRemoteCandidates != null) {
                        queuedRemoteCandidates.add(candidate);
                    } else {
                        peerConnection[0].addIceCandidate(candidate);
                        if (!concallReq.equals("")) {
                            peerConnection[1].addIceCandidate(candidate);
                        }
                    }
                }
            }
        });
    }


    public boolean callHoldPlay() {

        if (showToRemote) {

            if (peerConnection != null) {
                callHold = !callHold;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!callHold) {
                            callHold();
                            cameraSwitchButton.setEnabled(false);
                            toggleMuteButton.setEnabled(false);
                            speakerButton.setEnabled(false);
                            streamTORemote.setEnabled(false);
                        } else {
                            callResume();
                            cameraSwitchButton.setEnabled(true);
                            toggleMuteButton.setEnabled(true);
                            speakerButton.setEnabled(true);
                            streamTORemote.setEnabled(true);
                        }
                    }
                });
            }
        }
        return callHold;
    }

    private void drainCandidates() {
        if (queuedRemoteCandidates != null) {
            Log.d(TAG, "Add " + queuedRemoteCandidates.size() + " remote candidates");
            for (IceCandidate candidate : queuedRemoteCandidates) {
                peerConnection[0].addIceCandidate(candidate);
            }
            queuedRemoteCandidates = null;
        }
    }

    private void remoteCallInitMethod() {


        //<message type="normal" to="1113@devchat.colabus.com"><json xmlns="urn:xmpp:json:0">

        // {"calltype":"AV","type":"callrequest”,”devicetype”:”ios”,” callId”:”16”,"NameforChatRoom":"userMeet_1113_2019012125211"}</json></message>
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "callrequest");
            jsonObject.put("devicetype", "android");
            jsonObject.put("callid", callIdForDb);
            jsonObject.put("NameforChatRoom", roomId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid entityBareJid = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message();
                    msg.setTo(bareJid);
                    msg.setFrom(entityBareJid);
                    msg.setType(Message.Type.normal);
                    msg.addExtension(json);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    chat.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        sendPresenceOwner();
    }

    private void sendPresenceOwner() {
        try {
            PresenceCustom joinPresence = new PresenceCustom(JidCreate.from(roomId + "@conference.devchat.colabus.com/" + "user_" +
                    appBean.userId + "_" + appBean.loggedInName + "_" + extensionToSend), Presence.Type.available);
            connection.sendStanza(joinPresence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }
//    private void remoteCallInitMethod() {
//
//        Presence dndPresence = new Presence(Presence.Type.available, "", 0, Presence.Mode.dnd);
//        try {
//            connection.sendStanza(dndPresence);
//        } catch (SmackException.NotConnectedException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("calltype", calltype);
//            jsonObject.put("type", "callrequest");
//            jsonObject.put("devicetype", "android");
//            jsonObject.put("callid", callIdForDb);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
//        try {
//            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
//            EntityBareJid entityBareJid = JidCreate.entityBareFrom(videofromjid);
//            if (connection != null) {
//                try {
//                    final Message msg = new Message();
//                    msg.setTo(bareJid);
//                    msg.setFrom(entityBareJid);
//                    msg.setType(Message.Type.normal);
//                    msg.addExtension(json);
//                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
//                    Chat chat = chatmanager.chatWith(bareJid);
//                    chat.send(msg);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (XmppStringprepException e) {
//            e.printStackTrace();
//        }
//
//
//    }

    private void callrequestcancelledMethod() {
//        <message xmlns="jabber:client" type="normal" to="7@devchat.colabus.com" from="1079@devchat.colabus.com/9dzmchkq84">
// <json xmlns="urn:xmpp:json:0">{"calltype":"AV","type":"callrequestcancelled”}</json></message>
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "callrequestcancelled");
            //  jsonObject.put("callId", callIdForDb);
            jsonObject.put("callid", callIdForDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message();
                    msg.setTo(bareJid);
                    msg.setType(Message.Type.normal);
                    msg.addExtension(json);
                    msg.setTo(bareJid);
                    msg.setFrom(bareJidfrom);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    chat.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        new CallResponseUpdate("callrequestcancelled").execute();
        callOccured = false;
        enquee = false;
//        disconnect();
        appBean.isOnCall = false;
        showToRemote = false;
        callHold = false;
        if (appBean.fromSUCNContext) {
            appBean.fromSUCNContext = false;
            Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
            myIntent.putExtra("username", userName);
            myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
            myIntent.putExtra("imgUrl", image);
            myIntent.putExtra("from", "afterCall");
            startActivity(myIntent);
            callDisconnectPresence();
            finish();
        }

    }

    private void calldisconnectMethod() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "calldisconnect");
            jsonObject.put("callid", callIdForDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
//            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message(confGroupNameJid, Message.Type.groupchat);
                    msg.setTo(confGroupNameJid);
                    msg.setType(Message.Type.groupchat);
                    msg.addExtension(json);

                    //msg.setFrom(bareJidfrom);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    //groupMuc.sendMessage(msg);
                    connection.sendStanza(msg);
//                    final Message msg = new Message();
//                    msg.setTo(bareJid);
//                    msg.setFrom(bareJidfrom);
//                    msg.setType(Message.Type.normal);
//                    msg.addExtension(json);
//                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
//                    Chat chat = chatmanager.chatWith(bareJid);
//                    chat.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        // for multiple devices login
//        ownSignal("calloff");
        new CallResponseUpdate("calldisconnect").execute();
        callOccured = false;
        enquee = false;
        new CallDisconnection().execute();
//        disconnect();


    }

    private void callcallacceptedMethod() {
        callAnswered = true;

        if (calltype.equals("AV")) {
            callreceiver.setVisibility(View.GONE);
//            DisplayMetrics displayMetrics = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//            ViewGroup.LayoutParams frameParams = frameLayout.getLayoutParams();
//
//            frameParams.height = Integer.valueOf(displayMetrics.heightPixels / 4);
//            frameParams.width = displayMetrics.heightPixels;
//            frameLayout.setLayoutParams(frameParams);
//            frameLayout.setVisibility(View.VISIBLE);
//            frameLayout.setBackgroundColor(getResources().getColor(R.color.LightGrey));
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
////            ft.add(R.id.fragment_container_audio, callFragment);
//            ft.show(callFragment);
//            ft.commit();
        } else {
            local_video_view.setVisibility(View.GONE);
            remote_video_view.setVisibility(View.GONE);
            callreceiver.setVisibility(View.GONE);
            caller_layout.setVisibility(View.VISIBLE);
//            caller_layout.setBackgroundColor(getResources().getColor(R.color.LightGrey));
//            caller_layout.setBackgroundColor(getResources().getColor(R.color.White));
            caller_layout.setBackgroundColor(getResources().getColor(R.color.LightGrey));
            disconnectimg_caller.setVisibility(View.GONE);
            connectingtxt_cal.setVisibility(View.VISIBLE);
            connectingtxt_cal.setText(getCallerName(videotojid.substring(0, videotojid.indexOf("@"))));
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.show(callFragment);
//            ft.commit();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            ViewGroup.LayoutParams frameParams = frameLayout.getLayoutParams();

            frameParams.height = Integer.valueOf(displayMetrics.heightPixels / 4);
            frameParams.width = displayMetrics.heightPixels;
            fragment_container_audio.setLayoutParams(frameParams);
            fragment_container_audio.setVisibility(View.VISIBLE);
            fragment_container_audio.setBackgroundColor(getResources().getColor(R.color.LightGrey));
            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.add(R.id.fragment_container_audio, callFragment);
            ft.show(callFragment);
            ft.commit();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "callaccepted");
            jsonObject.put("devicetype", "android");
            jsonObject.put("callid", callIdForDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);

            if (connection != null) {
                try {
                    final Message msg = new Message();
                    msg.setTo(bareJid);
                    msg.setType(Message.Type.normal);
                    msg.addExtension(json);
                    msg.setFrom(bareJidfrom);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    chat.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        updateDBAboutCallReceiverDetails();
        callduration.setVisibility(View.VISIBLE);
        sendPresencetoUser();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
        callDurationOnThread();
//            }
//        });

// for multiple devices login
//        ownSignal("callon");
    }

    private void sendPresencetoUser() {
        try {
            PresenceCustomUser joinPresence = new PresenceCustomUser(JidCreate.from(roomId + "@conference.devchat.colabus.com/" + "user_" +
                    appBean.userId + "_" + appBean.loggedInName + "_" + extensionToSend), Presence.Type.available);
            connection.sendStanza(joinPresence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }

    public void callDurationOnThread() {
        //set Time Using Thread

        callStartedTimeMs = System.currentTimeMillis();

        timer = new Timer();
        timer.schedule(new CallDuration(), 0, 1000);
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {


            }
        });*/
//        runOnThreadForCallDuration();

    }

    // Disconnect from remote resources, dispose of local resources, and exit.
    private void disconnect() {

        Presence dndPresence = new Presence(Presence.Type.available, "", 0, Presence.Mode.available);
        try {
            connection.sendStanza(dndPresence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (getAutoDetectorForTest() != null) {
            try {
                NetworkMonitor.setAutoDetectConnectivityState(false);
//                ws.disconnect();
                Log.d(TAG, "Disconnecting WebSocket done.");
//            NetworkMonitorAutoDetect networkMonitorAutoDetect = new NetworkMonitorAutoDetect(Ob);
            } catch (IllegalArgumentException exception) {
                exception.printStackTrace();
            }
        }
        System.out.println("getAutoDetectorForTest -->" + getAutoDetectorForTest());
//        peerConnection.dispose();
//        videoSource.dispose();
//        factory.dispose();
//        remoteRenderer.dispose();
        if (peerConnection[0] != null) {
            peerConnection[0].close();
            peerConnection[0].dispose();
        }
        if (peerConnection[1] != null) {
            peerConnection[1].close();
            peerConnection[1].dispose();
        }
        if (peerConnection[2] != null) {
            peerConnection[2].close();
            peerConnection[2].dispose();
        }
        Log.d(TAG, "Closing peer connection factory.");
//        if (factory != null) {
//            factory.dispose();
//            factory = null;
//        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }

        if (local_video_view != null) {
            local_video_view.release();
            local_video_view = null;
        }
        if (videoFileRenderer != null) {
            videoFileRenderer.release();
            videoFileRenderer = null;
        }
        if (remote_video_view != null) {
            remote_video_view.release();
            remote_video_view = null;
        }
        if (audioManager != null) {
            audioManager.stop();
            audioManager = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (audioSource != null) {
            audioSource.dispose();
            audioSource = null;
        }
//        Log.d(TAG, "Stopping capture.");
        if (videoCapturer != null) {
            try {
                videoCapturer.stopCapture();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            videoCapturer.dispose();
            videoCapturer = null;
        }
        if (videoSource != null) {
            videoSource.dispose();
            videoSource = null;
        }

//        if (rootEglBase != null) {
//            rootEglBase.release();
////            rotEglBase = null;
//        }
        if (null != callActivityContext) {
            callActivityContext = null;
        }
        /*if (null != singleUserChatNewContext) {

            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;

            System.out.println(" cn.getShortClassName() " + cn.getShortClassName());
            if (cn.getShortClassName().equals(".SingleUserChatNew"))
                singleUserChatNewContext.finish();

            singleUserChatNewContext = null;
        }*/
//        if (factory != null) {
//            factory.dispose();
//            factory = null;
//        }
        options = null;
        Log.d(TAG, "Closing peer connection done.");
        if (null != timer) {
            timer.cancel();
        }

        if (null != globalStream) {
            globalStream = null;
        }

//        if (isOnCall) {
////            letsCloseResources();
//            //check service is running
//            System.out.println(" what is the FloatingWidgetService status in " + NotificationUtils.isMyServiceRunning(CallActivity.this));
//            if (NotificationUtils.isMyServiceRunningForCall(CallActivity.this))
//                stopService(new Intent(getApplicationContext(), FloatingWidgetService.class));
//            if (SingleUserChatNew.video != null) {
//                SingleUserChatNew.video.setVisibility(View.VISIBLE);
//            }
//            if (SingleUserChatNew.audioButton != null) {
//                SingleUserChatNew.audioButton.setVisibility(View.VISIBLE);
//            }
//            isOnCall = false;
//        }

        // for multiple devices login callOccured = false;
        showToRemote = false;
        callHold = false;
        callOccured = false;
        callAnswered = false;
        appBean.callerId = "";
        isBackground = false;
        appBean.highLightCount = 0;
        if (null != DataListCall) {
            DataListCall = null;
            addclicked = false;
        }

        if (null != HighlightListContext) {
            HighlightListContext.finish();
            HighlightListContext = null;
        }

//           stopScreenSharing();
/*        if (factory != null) {
            factory.stopAecDump();
            factory.dispose();
            factory = null;
        }
        options = null;
        PeerConnectionFactory.stopInternalTracingCapture();
        PeerConnectionFactory.shutdownInternalTracer();*/
       /* if (iceConnected && !isError) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }*/
//       if(callActivityContext!=null){
        System.out.println(" calling first ");

//        finish();
//        Intent intent = new Intent(CallActivity.this, PlaceholderFragment.class);
//        startActivity(intent);

       /* ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;

        System.out.println(" cn.getShortClassName() " + cn.getShortClassName());
        if (cn.getShortClassName().equals(".SingleUserChatNew")) {
            if (appBean.fromSUCNContext) {
                Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
                System.out.println("so appBean.conversationId = conversationCallId " + appBean.conversationId + " " + conversationCallId);
//            myIntent.putExtra("convId", conversationCallId);
                myIntent.putExtra("username", userName);
                myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
                myIntent.putExtra("imgUrl", image);
                myIntent.putExtra("from", "afterCall");
//                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(myIntent);
            }
        }else if(isInitator){
        }*/
//       }
    }

//    private void runOnThreadForCallDuration() {
//
//    }

    private void updateDBAboutCallReceiverDetails() {
        new ReceiverId().execute();
    }

    private void updateDBAboutCallDetails() {

        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int date = now.get(Calendar.DATE);

        //userMeet_"+id+"_"+d.getFullYear()+d.getMonth()+d.getDate()+d.getHours()+d.getMinutes()+d.getSeconds()

        String NameforChatRoom = "userMeet_" + appBean.userId + "_" + year + month + date + hour + minute + second;

        System.out.println(" print the value for NameforChatRoom " + NameforChatRoom);
        new GetCallId(NameforChatRoom).execute();

        //createRoom(NameforChatRoom);

        //Date Format with dd-M-yyyy hh:mm:ss : 13-4-2015 10:59:26
    }

    private void ownSignal(String callOnOff) {

//        @"type" : @"ownsignal", @"status" : @"callon", @"userid" : _callWithUserJId
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "ownsignal");
            jsonObject.put("devicetype", "android");
            jsonObject.put("status", callOnOff);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            EntityBareJid bareJid = JidCreate.entityBareFrom(videofromjid);
            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);

            if (connection != null) {
                try {
                    final Message msg = new Message();
                    msg.setTo(bareJid);
                    msg.setType(Message.Type.normal);
                    msg.addExtension(json);
                    msg.setFrom(bareJidfrom);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    chat.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }

    private void offeranswerMethod(SessionDescription sessionDescription) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("type", sessionDescription.type.toString().toLowerCase());
            jsonObject1.put("sdp", sessionDescription.description);
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", sessionDescription.type.toString().toLowerCase());
            jsonObject.put("sdp", jsonObject1);
            jsonObject.put("callid", callIdForDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
//            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message();
                    msg.setTo(bareJid);
                    msg.setFrom(bareJidfrom);
                    msg.setType(Message.Type.normal);
                    msg.addExtension(json);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    chat.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        disconnectPeerconnection();
    }

    private void callNotResponded() {

        System.out.println(" so this is callnotresponded " + videofromjid + " " + videotojid);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("calltype", calltype);
            jsonObject.put("type", "callnotresponded");
            jsonObject.put("callid", callIdForDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPacketExtension json = new JsonPacketExtension(jsonObject.toString());
        try {
            EntityBareJid bareJid = JidCreate.entityBareFrom(videotojid);
            EntityBareJid bareJidfrom = JidCreate.entityBareFrom(videofromjid);
            if (connection != null) {
                try {
                    final Message msg = new Message();
                    msg.setTo(bareJid);
                    msg.setType(Message.Type.normal);
                    msg.addExtension(json);
                    msg.setTo(bareJid);
                    msg.setFrom(bareJidfrom);
                    ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                    Chat chat = chatmanager.chatWith(bareJid);
                    chat.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        new CallResponseUpdate("callnotresponded").execute();
        callOccured = false;
//        disconnect();
//        appBean.isOnCall = false;

        appBean.isOnCall = false;

        if (appBean.fromSUCNContext) {
            appBean.fromSUCNContext = false;
            Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
            myIntent.putExtra("username", userName);
            myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
            myIntent.putExtra("imgUrl", image);
            myIntent.putExtra("from", "afterCall");
            startActivity(myIntent);
        }
        callDisconnectPresence();
        finish();
    }

    private void disconnectPeerconnection() {
        if (peerConnection != null) {
            peerConnection[0].close();
            peerConnection = null;
        }
        if (local_video_view != null) {
            local_video_view.release();
            local_video_view = null;
        }
        if (videoFileRenderer != null) {
            videoFileRenderer.release();
            videoFileRenderer = null;
        }
        if (remote_video_view != null) {
            remote_video_view.release();
            remote_video_view = null;
        }
        if (audioManager != null) {
//            audioManager.stop();
            audioManager = null;
        }
    }

    private void startThread() {
        new Thread(new Runnable() {
            public void run() {
                while (enquee) {
                    System.out.println(" so will cause threat ?? main ");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateColor();
                }
            }
        }).start();
    }

    private void updateColor() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(" so will cause threat ?? UC " + color);
                temp++;

                switch (color) {

                    case 0:
                        System.out.println(" so will cause threat ?? color UC 0");
                        if (isInitator) {
                            drawableCaller.setStroke(10, Color.parseColor("#FF0000"));
                            userimage_caller.setBackground(drawableCaller);
                        } else {
                            drawableReceiver.setStroke(10, Color.parseColor("#FF0000"));
                            userimage_receiver.setBackground(drawableReceiver);
                        }
                        color++;
                        break;

                    case 1:

                        if (isInitator) {
                            drawableCaller.setStroke(10, Color.parseColor("#006400"));
                            userimage_caller.setBackground(drawableCaller);
                        } else {
                            drawableReceiver.setStroke(10, Color.parseColor("#006400"));
                            userimage_receiver.setBackground(drawableReceiver);
                        }

                        color++;
                        break;
                    case 2:

                        if (isInitator) {
                            drawableCaller.setStroke(10, Color.parseColor("#FFFF00"));
                            userimage_caller.setBackground(drawableCaller);

                        } else {
                            drawableReceiver.setStroke(10, Color.parseColor("#FFFF00"));
                            userimage_receiver.setBackground(drawableReceiver);
                        }

                        color++;
                        break;
                    case 3:

                        if (isInitator) {
                            drawableCaller.setStroke(10, Color.parseColor("#FFC0CB"));
                            userimage_caller.setBackground(drawableCaller);
                        } else {
                            drawableReceiver.setStroke(5, Color.parseColor("#FFC0CB"));
                            userimage_receiver.setBackground(drawableReceiver);
                        }

                        color++;
                        break;
                    case 4:

                        if (isInitator) {
                            drawableCaller.setStroke(10, Color.parseColor("#FFA500"));
                            userimage_caller.setBackground(drawableCaller);
                        } else {
                            drawableReceiver.setStroke(10, Color.parseColor("#FFA500"));
                            userimage_receiver.setBackground(drawableReceiver);
                        }

                        color = 0;
                        break;

                }

                if (temp == 30) {
                    enquee = false;
//                    if (!isInitator) {
                    callNotResponded();
//                    }
                }
            }
        });
    }

    private void letsCloseResources() {
        Presence dndPresence = new Presence(Presence.Type.available, "", 0, Presence.Mode.available);
        try {
            connection.sendStanza(dndPresence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (peerConnection != null) {
            peerConnection[0].close();
            peerConnection = null;
        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }

        if (local_video_view != null) {
            local_video_view.release();
            local_video_view = null;
        }
        if (videoFileRenderer != null) {
            videoFileRenderer.release();
            videoFileRenderer = null;
        }
        if (remote_video_view != null) {
            remote_video_view.release();
            remote_video_view = null;
        }
        if (audioManager != null) {
            audioManager.stop();
            audioManager = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (audioSource != null) {
            audioSource.dispose();
            audioSource = null;
        }
//        Log.d(TAG, "Stopping capture.");
        if (videoCapturer != null) {
            try {
                videoCapturer.stopCapture();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            videoCapturer.dispose();
            videoCapturer = null;
        }
        if (videoSource != null) {
            videoSource.dispose();
            videoSource = null;
        }
        if (null != callActivityContext) {
            callActivityContext = null;
        }
        // for multiple devices login callOccured = false;
        callOccured = false;
        callAnswered = false;
        appBean.callerId = "";
        isBackground = false;
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        ConnectionDetector.server_showDialog(isConnected, CallActivity.this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        ConnectionDetector.server_showDialog(isConnected, CallActivity.this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {

            if (!callType.equals("AV")) {
                if (sensorEvent.values[0] >= -SENSOR_SENSITIVITY && sensorEvent.values[0] <= SENSOR_SENSITIVITY) {
                    //near

                    try {
                        // Yeah, this is hidden field.
                        field = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
                    } catch (Throwable ignored) {
                    }

                    if (!wakeLock.isHeld()) {
                        wakeLock.acquire();
                    }

                } else {
                    //far
                    try {
                        // Yeah, this is hidden field.
                        field = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
                    } catch (Throwable ignored) {
                    }

                    if (wakeLock.isHeld()) {
                        wakeLock.release();
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void showToast() {
        Toast.makeText(CallActivity.this,
                "Currently Video/Audio Calling Feature is availble between same Device Type (e.g Web  to Web or Mobile to Mobile)",
                Toast.LENGTH_LONG).show();
    }

    private void setlayout(SurfaceViewRenderercol remote_video_view, int on) {
        remote_video_view.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams params1 = remote_video_view.getLayoutParams();
        DisplayMetrics displayMetrics1 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics1);
        switch (on) {
            case 0:
                params1.height = Integer.valueOf(displayMetrics1.heightPixels);
                params1.width = Integer.valueOf(displayMetrics1.widthPixels);
                //params1.height = Integer.valueOf(displayMetrics1.heightPixels / 2);
                //params1.width = displayMetrics1.widthPixels;
                System.out.println(" so what is the height params " + params1.height + " " + params1.width);
                remote_video_view.setPadding(0, 0, 10, 10);
                remote_video_view.setLayoutParams(params1);
                //end for half view
                remote_video_view.setVisibility(View.VISIBLE);
                break;
            case 1:
                params1.height = Integer.valueOf(displayMetrics1.heightPixels / 2);
                params1.width = Integer.valueOf(displayMetrics1.widthPixels / 2);
                System.out.println(" so what is the height params " + params1.height + " " + params1.width);
                remote_video_view.setPadding(0, 0, 10, 10);
                remote_video_view.setLayoutParams(params1);
                //end for half view
                remote_video_view.setVisibility(View.VISIBLE);
                break;
            case 2:
                params1.height = Integer.valueOf(displayMetrics1.heightPixels / 3);
                params1.width = Integer.valueOf(displayMetrics1.widthPixels / 2);
                System.out.println(" so what is the height params " + params1.height + " " + params1.width);
                remote_video_view.setPadding(0, 0, 10, 10);
                remote_video_view.setLayoutParams(params1);
                //end for half view
                remote_video_view.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void createRoom(String chatRoom) {
        try {
            EntityBareJid roomId = JidCreate.entityBareFrom(chatRoom);
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
            MultiUserChat muc = null;
            Resourcepart nickName = null;
            muc = manager.getMultiUserChat(roomId);
            nickName = Resourcepart.from(chatRoom);
            muc.create(nickName);
            Form form = muc.getConfigurationForm();
            Form answerForm = form.createAnswerForm();
            for (FormField field : form.getFields()) {
                if (!FormField.Type.hidden.name().equals(field.getType()) && field.getVariable() != null) {
                    answerForm.setDefaultAnswer(field.getVariable());
                }
            }
            answerForm.setAnswer(FormField.FORM_TYPE, "http://jabber.org/protocol/muc#roomconfig 2");
            //answerForm.setAnswer("muc#roomconfig_roomname",name);
            //answerForm.setAnswer("muc#roomconfig_roomdesc", desc);
            answerForm.setAnswer("muc#roomconfig_changesubject", true);
            List maxusers = new ArrayList();
            maxusers.add("100");
            answerForm.setAnswer("muc#roomconfig_maxusers", maxusers);

            List cast_values = new ArrayList();
            cast_values.add("moderator");
            cast_values.add("participant");
            cast_values.add("visitor");
            answerForm.setAnswer("muc#roomconfig_presencebroadcast", cast_values);
            answerForm.setAnswer("muc#roomconfig_publicroom", true);
            answerForm.setAnswer("muc#roomconfig_persistentroom", true);
            answerForm.setAnswer("x-muc#roomconfig_canchangenick", true);
            answerForm.setAnswer("x-muc#roomconfig_registration", true);

            muc.sendConfigurationForm(answerForm);
            muc.join(nickName);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XMPPException e) {

            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {

            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {

            e.printStackTrace();
        } catch (SmackException e) {

            e.printStackTrace();
        }

    }

    class CallDuration extends TimerTask {

        @Override
        public void run() {


/*
            final long delta = System.currentTimeMillis() - callStartedTimeMs;

            final String temp = delta+"";*/

            final long millis = System.currentTimeMillis() - callStartedTimeMs;
            int seconds = (int) (millis / 1000);
            final int minutes = seconds / 60;
            final int secondsFinal = seconds % 60;
            final int hours = minutes / 60;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    appBean.calldurationTime = String.format("%02d:%02d:%02d", hours, minutes, secondsFinal);
                    calldurationText.setText(String.format("%02d:%02d:%02d", hours, minutes, secondsFinal));

                    if (highighlight_time != null) {

                        if (getTime) {
                            highighlight_time.setText(appBean.calldurationTime);
                        }
                    }
                }
            });
            System.out.print(" running still timerTask for call duration " + String.format("%d:%02d", minutes, secondsFinal));
        }
    }

    public class CheckCases {

        Message constructorMessage;

        public CheckCases(Message message) {
            this.constructorMessage = message;
        }

        public void reflectActiivityStatus() {

            System.out.println("running in reflectActiivityStatus");

            System.out.println("msg_xml---->" + this.constructorMessage.toString() + this.constructorMessage.getType().toString());
            if (this.constructorMessage.toString().contains("normal") || this.constructorMessage.toString().contains("groupchat")) {
                JsonPacketExtension packetExtension = this.constructorMessage.getExtension("json", "urn:xmpp:json:0");
                try {
                    JSONObject object = new JSONObject(packetExtension.getJson().toString());
                    System.out.println("msg_xml object videocall---->" + object.getString("type") + " " + callOccured);
                    switch (object.getString("type")) {
                        case "callaccepted": {

                            callDurationOnThread();
                            CallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    enquee = false;
                                    caller_layout.setVisibility(View.GONE);
                                    callduration.setVisibility(View.VISIBLE);

                                    if (peerConnection != null) {
                                        mediaPlayer.stop();
                                        switch (calltype) {
                                            case "AV":
                                                if (concallReq.equals("")) {
                                                    peerConnection[0].createOffer(sdpObserver, createAVConstraints());
                                                } else {
                                                    sendPresenceOwner();
                                                    //peerConnection[1].createOffer(sdpObserver1, createAVConstraints());
                                                    // peerConnection[1].createOffer(sdpObserver[1], createAVConstraints());
                                                    // peerConnection[2].createOffer(sdpObserver[2], createAVConstraints());

                                                }

                                                //peerConnection[1].createOffer(sdpObserver[1], createAVConstraints());
                                                //  peerConnection[2].createOffer(sdpObserver[2], createAVConstraints());
                                                break;
                                            case "A":
                                                if (concallReq.equals("")) {
                                                    peerConnection[0].createOffer(sdpObserver, createAVConstraints());
                                                } else {
                                                    //peerConnection[1].createOffer(sdpObserver1, createAVConstraints());
                                                    // peerConnection[1].createOffer(sdpObserver[1], createAVConstraints());
                                                    // peerConnection[2].createOffer(sdpObserver[2], createAVConstraints());
                                                    sendPresenceOwner();
                                                }
                                                // peerConnection[0].createOffer(sdpObserver[0], createAConstraints());
                                                //peerConnection[1].createOffer(sdpObserver[1], createAConstraints());
                                                //peerConnection[2].createOffer(sdpObserver[2], createAConstraints());
                                                break;
                                        }
                                    } else {
//                                            logAndToast("peerConnection is not created");
//                                            finish();
                                    }

                                }
                            });

                            break;
                        }
//                        case "ownsignal": {
//
//                            final String status = object.getString("status");
//                            CallActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    if (status.equals("callon")) {
//                                        callOccured = true;
//                                    } else if (status.equals("calloff")) {
//                                        callOccured = false;
//                                    }
//
//                                }
//                            });
//                            break;
//                        }
                        case "offer": {
                            System.out.println(callAnswered + " Candidate Received offer ");
                            //JSONObject jsonObject1 =
                            JSONObject object1 = object.getJSONObject("sdp");
                            if (callAnswered) {

                                if (peerConnection != null) {
                                    SessionDescription temp = new SessionDescription(SessionDescription.Type.OFFER,
                                            object1.getString("sdp").toString());
                                    if (concallReq.equals("")) {
                                        peerConnection[0].setRemoteDescription(sdpObserver, temp);
                                    } else {
                                        // peerConnection[1].setRemoteDescription(sdpObserver1, temp);
                                        // peerConnection[1].setRemoteDescription(sdpObserver[1], temp);
                                        // peerConnection[2].setRemoteDescription(sdpObserver[2], temp);
                                    }
                                    //peerConnection[1].setRemoteDescription(sdpObserver[1], temp);
                                    // peerConnection[2].setRemoteDescription(sdpObserver[2], temp);

                                }

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        enquee = false;
//                                        callOccured = false;
//                                        disconnect();
//                                        appBean.isOnCall = false;

                                        appBean.isOnCall = false;
                                        showToRemote = false;
                                        callHold = false;
                                        if (appBean.fromSUCNContext) {
                                            appBean.fromSUCNContext = false;
                                            Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
                                            myIntent.putExtra("username", userName);
                                            myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
                                            myIntent.putExtra("imgUrl", image);
                                            myIntent.putExtra("from", "afterCall");
                                            startActivity(myIntent);
                                        }
                                        callDisconnectPresence();
                                        finish();
                                    }
                                });
                            }
                            break;
                        }
                        case "answer": {
                            JSONObject object1 = object.getJSONObject("sdp");
                            if (peerConnection != null) {
                                SessionDescription temp = new SessionDescription(SessionDescription.Type.ANSWER, object1.getString("sdp"));
                                if (concallReq.equals("")) {
                                    peerConnection[0].setRemoteDescription(sdpObserver, temp);
                                } else {
                                    // peerConnection[1].setRemoteDescription(sdpObserver1, temp);
                                    // peerConnection[1].setRemoteDescription(sdpObserver[1], temp);
                                    //peerConnection[2].setRemoteDescription(sdpObserver[2], temp);
                                }
                                // peerConnection[1].setRemoteDescription(sdpObserver[1], temp);
                                //peerConnection[2].setRemoteDescription(sdpObserver[2], temp);
                            }
                            break;
                        }
                        case "candidate": {
                            System.out.println(callAnswered + " Candidate Received candidate " + object);
                            JSONObject object1 = object.getJSONObject("ice");
                            IceCandidate tempCandidate = new IceCandidate(object1.getString("id"),
                                    object1.getInt("label"), object1.getString("candidate"));
                            addRemoteIceCandidate(tempCandidate);
                            break;
                        }
                        case "calldeclined":
                            CallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new CallResponseUpdate("calldeclined").execute();
                                    enquee = false;
                                    callOccured = false;
//                                    disconnect();
                                    Toast.makeText(getApplicationContext(), "Call declined", Toast.LENGTH_SHORT).show();
                                    showToRemote = false;
                                    callHold = false;
                                    if (appBean.fromSUCNContext) {
                                        appBean.fromSUCNContext = false;
                                        Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
                                        myIntent.putExtra("username", userName);
                                        myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
                                        myIntent.putExtra("imgUrl", image);
                                        myIntent.putExtra("from", "afterCall");
                                        startActivity(myIntent);
                                    }
                                    appBean.isOnCall = false;
                                    callDisconnectPresence();
                                    finish();
                                }
                            });

                            break;
                        case "calldisconnect":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    enquee = false;
                                    callOccured = false;
                                    callAnswered = false;
//                                    appBean.isOnCall = false;
//                                    Intent intent = new Intent(CallActivity.this, PlaceholderFragment.class);
//                                    startActivity(intent);

                                    new CallDisconnection().execute();
                                    Toast.makeText(getApplicationContext(), "Call Ended", Toast.LENGTH_SHORT).show();

                                    showToRemote = false;
                                    callHold = false;
                                    if (isOnCall) {
                                        if (NotificationUtils.isMyServiceRunningForCall(CallActivity.this))
                                            stopService(new Intent(getApplicationContext(), FloatingWidgetService.class));
                                        if (SingleUserChatNew.video != null) {
                                            SingleUserChatNew.video.setVisibility(View.VISIBLE);
                                        }
                                        if (SingleUserChatNew.audioButton != null) {
                                            SingleUserChatNew.audioButton.setVisibility(View.VISIBLE);
                                        }
                                        if (RoomtextChatContext != null) {
                                            RoomtextChatContext = null;
                                            RoomtextChatContext.finish();
                                        }
                                        isOnCall = false;
                                    }
                                    if (appBean.fromSUCNContext) {
                                        appBean.fromSUCNContext = false;
                                        appBean.callactivitycontextIntent = true;
                                        Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
                                        System.out.print("callactivity values " + userName + "image " + image);
                                        myIntent.putExtra("username", userName);
                                        myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
                                        myIntent.putExtra("imgUrl", image);
                                        myIntent.putExtra("from", "afterCall");
                                        startActivity(myIntent);
                                    }
                                    callDisconnectPresence();
                                    finish();
//                                    enquee = false;
//                                    callOccured = false;
//                                    callAnswered = false;

//                                    if (isOnCall) {
//                                        letsCloseResources();
//                                        stopService(new Intent(getApplicationContext(), FloatingWidgetService.class));
//                                        if (SingleUserChatNew.video != null) {
//                                             SingleUserChatNew.video.setVisibility(View.VISIBLE);
//                                        }
//                                        if (SingleUserChatNew.audioButton != null) {
//                                            SingleUserChatNew.audioButton.setVisibility(View.VISIBLE);
//                                        }
//                                    }
//                                    disconnect();
//                                    appBean.isOnCall = false;

                                }
                            });
                            break;
                        case "callbusy":
//                                CallActivity.this.finish();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new CallResponseUpdate("callbusy").execute();
                                    mediaPlayer.stop();
                                    mediaPlayerForBusy = MediaPlayer.create(getApplicationContext(), R.raw.busy_tone);
                                    mediaPlayerForBusy.start();
                                    whenCallBusyResponse = true;
                                    mediaPlayerForBusy.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            callOccured = false;
                                            System.out.println(" so this is running in a listener ");
                                            enquee = false;
                                            callDisconnectPresence();
                                            CallActivity.this.finish();
                                        }
                                    });
                                    callOccured = false;
                                }
                            });
                            break;
//                        case "callcrossdevice":
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    showToast();
//                                    //new CallResponseUpdate("callbusy").execute();
//                                    mediaPlayer.stop();
//                                    mediaPlayerForBusy = MediaPlayer.create(getApplicationContext(), R.raw.busy_tone);
//                                    mediaPlayerForBusy.start();
//                                    whenCallBusyResponse = true;
//                                    mediaPlayerForBusy.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                                        @Override
//                                        public void onCompletion(MediaPlayer mp) {
//                                            callOccured = false;
//                                            //System.out.println(" so this is running in a listener ");
//                                            enquee = false;
//                                            CallActivity.this.finish();
//
//                                        }
//                                    });
//                                    callOccured = false;
//                                }
//                            });
//                            break;

                        case "callrequestcancelled": {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new CallResponseUpdateRequestCancelled("callrequestcancelled").execute();
                                    enquee = false;
                                    callOccured = false;
//                                    disconnect();
//                                    appBean.isOnCall = false;
                                    Toast.makeText(getApplicationContext(), "Call requestcancelled", Toast.LENGTH_SHORT).show();


                                    showToRemote = false;
                                    callHold = false;
                                    if (appBean.fromSUCNContext) {
                                        appBean.fromSUCNContext = false;
                                        Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
                                        myIntent.putExtra("username", userName);
                                        myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
                                        myIntent.putExtra("imgUrl", image);
                                        myIntent.putExtra("from", "afterCall");
                                        startActivity(myIntent);
                                    }
                                    appBean.isOnCall = false;
                                    callDisconnectPresence();
                                    finish();
                                }
                            });

                            break;
                        }
                        case "callnotresponded": {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new CallResponseUpdate("callnotresponded").execute();
                                    enquee = false;
                                    callOccured = false;
//                                    disconnect();
//                                    appBean.isOnCall = false;
                                    Toast.makeText(getApplicationContext(), "Call notresponded", Toast.LENGTH_SHORT).show();

                                    showToRemote = false;
                                    callHold = false;
                                    if (appBean.fromSUCNContext) {
                                        appBean.fromSUCNContext = false;
                                        Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
                                        myIntent.putExtra("username", userName);
                                        myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
                                        myIntent.putExtra("imgUrl", image);
                                        myIntent.putExtra("from", "afterCall");
                                        startActivity(myIntent);
                                    }
                                    appBean.isOnCall = false;
                                    callDisconnectPresence();
                                    finish();
                                }
                            });

                            break;
                        }
                        case "videostatus": {
                            //System.out.println("msg_xml object videocall---->" + object.getString("status"));
//usermeet_951_2019214174432@conference.devchat.colabus.com/user_1568_Ezhilarasan_jpg
                            final String status = object.getString("status");
                            CallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String splitmes[] = constructorMessage.getFrom().toString().split("@");
                                    String splitmes1[] = splitmes[1].toString().split("_");
                                    System.out.println("msg_xml object videocall---->" +
                                            splitmes1[1] + "");
                                    if (!splitmes1[1].equals(appBean.userId)) {
                                        if (status.equals("off")) {
                                            Toast.makeText(CallActivity.this, "Video has been Paused by " + getCallerName(videotojid.substring(0, videotojid.indexOf("@"))), Toast.LENGTH_LONG).show();
                                        } else if (status.equals("on")) {
                                            Toast.makeText(CallActivity.this, "Video has been Resumed by " + getCallerName(videotojid.substring(0, videotojid.indexOf("@"))), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                            break;

                        }
                        case "streamstatus": {
                            //  System.out.println("msg_xml object videocall---->" + object.getString("status"));
                            final String status = object.getString("status");
                            CallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String splitmes[] = constructorMessage.getFrom().toString().split("@");
                                    String splitmes1[] = splitmes[1].toString().split("_");
                                    System.out.println("msg_xml object videocall---->" +
                                            splitmes1[1] + "");
                                    if (!splitmes1[1].equals(appBean.userId)) {
                                        if (status.equals("off")) {
                                            Toast.makeText(CallActivity.this, "Call on Hold " + getCallerName(videotojid.substring(0, videotojid.indexOf("@"))), Toast.LENGTH_LONG).show();
                                        } else if (status.equals("on")) {
                                            Toast.makeText(CallActivity.this, "Call Resumed " + getCallerName(videotojid.substring(0, videotojid.indexOf("@"))), Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }
                            });
                            break;
                        }
                        case "audiostatus": {

                            final String status = object.getString("status");
                            CallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String splitmes[] = constructorMessage.getFrom().toString().split("@");
                                    String splitmes1[] = splitmes[1].toString().split("_");
                                    System.out.println("msg_xml object videocall---->" +
                                            splitmes1[1] + "");
                                    if (!splitmes1[1].equals(appBean.userId)) {
                                        if (status.equals("off")) {
                                            Toast.makeText(CallActivity.this, "Audio muted by " +
                                                    getCallerName(videotojid.substring(0, videotojid.indexOf("@"))), Toast.LENGTH_LONG).show();
                                        } else if (status.equals("on")) {
                                            Toast.makeText(CallActivity.this, "Audio unmuted by " +
                                                    getCallerName(videotojid.substring(0, videotojid.indexOf("@"))), Toast.LENGTH_LONG).show();
                                        }
                                    }


                                }
                            });
                            break;

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Implementation detail: observe ICE & stream changes and react accordingly.
    private class PCObserver implements PeerConnection.Observer {

        @Override
        public void onIceCandidate(final IceCandidate candidate) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    candidateMethod(candidate);
                }
            });
        }

        @Override
        public void onIceCandidatesRemoved(final IceCandidate[] candidates) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    events.onIceCandidatesRemoved(candidates);
                }
            });
        }

        @Override
        public void onSignalingChange(PeerConnection.SignalingState newState) {
            Log.d(TAG, "SignalingState: " + newState);
        }

        @Override
        public void onIceConnectionChange(final PeerConnection.IceConnectionState newState) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "IceConnectionState: " + newState);
                    if (newState == PeerConnection.IceConnectionState.CONNECTED) {
//                        events.onIceConnected();
                    } else if (newState == PeerConnection.IceConnectionState.DISCONNECTED) {
//                        events.onIceDisconnected();
                        reportError("ICE connection DISCONNECTED.");
                      /*  if (){
                            logAndToast("Poor Connection!");
                        }
                        else {*/
                        logAndToast("Connection disconnected/poor connection");
//                        if (isOnCall) {
//                            letsCloseResources();
//                            stopService(new Intent(getApplicationContext(), FloatingWidgetService.class));
//                            if (SingleUserChatNew.video != null) {
//                                SingleUserChatNew.video.setVisibility(View.VISIBLE);
//                            }
//                            if (SingleUserChatNew.audioButton != null) {
//                                SingleUserChatNew.audioButton.setVisibility(View.VISIBLE);
//                            }
//                        }
//                        }
//                        disconnect();

                    } else if (newState == PeerConnection.IceConnectionState.FAILED) {
                        reportError("ICE connection failed.");
                        logAndToast("Connection failed");
                    } else if (newState == PeerConnection.IceConnectionState.CLOSED) {
//                        disconnect();
//                        appBean.isOnCall = false;

                        showToRemote = false;
                        callHold = false;
                        if (appBean.fromSUCNContext) {
                            appBean.fromSUCNContext = false;
                            Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
                            myIntent.putExtra("username", userName);
                            myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
                            myIntent.putExtra("imgUrl", image);
                            myIntent.putExtra("from", "afterCall");
                            startActivity(myIntent);
                        }
                        appBean.isOnCall = false;
                        callDisconnectPresence();
                        finish();
                    }
                }
            });
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState newState) {
            Log.d(TAG, "IceGatheringState: " + newState);
        }

        @Override
        public void onIceConnectionReceivingChange(boolean receiving) {
            Log.d(TAG, "IceConnectionReceiving changed to " + receiving);
        }

        @Override
        public void onAddStream(final MediaStream stream) {
            Log.d(TAG, "onAddStream changed to --->" + stream.videoTracks.size());

//            executor.execute(new Runnable() {
            globalStream = stream;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (peerConnection == null || isError) {
                        return;
                    }
//                    if (stream.audioTracks.size() > 1 || stream.videoTracks.size() > 1) {
//                        reportError("Weird-looking stream: " + stream);
//                        return;
//                    }
                    switch (calltype) {
                        case "AV":
                            Log.d(TAG, "onAddStream changed to strea --->" + stream.videoTracks.size());
                            if (stream.videoTracks.size() == 1) {

                                ViewGroup.LayoutParams params1 = remote_video_view.getLayoutParams();
                                DisplayMetrics displayMetrics1 = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics1);
                                params1.height = Integer.valueOf(displayMetrics1.heightPixels / 2);
                                params1.width = Integer.valueOf(displayMetrics1.widthPixels);
                                System.out.println(" so what is the height params " + params1.height + " " + params1.width);
                                remote_video_view.setPadding(0, 0, 10, 10);
                                remote_video_view.setLayoutParams(params1);
                                remote_video_view.setVisibility(View.VISIBLE);
                                remoteVideoTrack = stream.videoTracks.get(0);
                                remoteAudioTrack = stream.audioTracks.get(0);
                                remoteRenderer = new VideoRenderer(remote_video_view);
                                remoteVideoTrack.setEnabled(true);
                                remoteVideoTrack.addRenderer(remoteRenderer);
                                ViewGroup.LayoutParams params2 = remote_video_view_one.getLayoutParams();
                                DisplayMetrics displayMetrics2 = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics2);
                                params2.height = Integer.valueOf(displayMetrics2.heightPixels / 3);
                                params2.width = Integer.valueOf(displayMetrics2.widthPixels / 3);
                                System.out.println(" so what is the height params " + params2.height + " " + params2.width);
                                remote_video_view_one.setPadding(0, 0, 10, 10);
                                remote_video_view_one.setLayoutParams(params2);
                                remote_video_view_one.setVisibility(View.VISIBLE);
                                remoteVideoTrack = stream.videoTracks.get(0);
                                remoteAudioTrack = stream.audioTracks.get(0);
                                remoteRenderer = new VideoRenderer(remote_video_view_one);
                                remoteVideoTrack.setEnabled(true);
                                remoteVideoTrack.addRenderer(remoteRenderer);
                                Log.d(TAG, "onAddStream changed to  stream.videoTracks.size() --->" + stream.videoTracks.size());
                                local_video_view.setVisibility(View.GONE);
                                //new code for half view


                                // remoteVideoTrack = stream.videoTracks.getFirst();
                                Log.d(TAG, "onAddStream changed to  remote_video_view --->" + stream.videoTracks.size());

                                ViewGroup.LayoutParams params = local_video_view.getLayoutParams();
                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                params.height = Integer.valueOf(displayMetrics.heightPixels / 4);
                                params.width = Integer.valueOf(displayMetrics.widthPixels / 3);
                                System.out.println(" so what is the height params " + params.height + " " + params.width);
                                local_video_view.setPadding(0, 0, 10, 10);
                                local_video_view.setLayoutParams(params);
                                local_video_view.setVisibility(View.VISIBLE);
                                // Activate call and HUD fragments and start the call.
                                ViewGroup.LayoutParams frameParams = frameLayout.getLayoutParams();
                                frameParams.height = 0;
                                frameParams.width = 0;
                                frameLayout.setLayoutParams(frameParams);
                                frameLayout.setVisibility(View.VISIBLE);
                                if (callFragment.isAdded()) {
                                    return;
                                } else {
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.add(R.id.call_fragment_container, callFragment);
//                                ft.add(R.id.hud_fragment_container, hudFragment);
                                    ft.hide(callFragment);
//                                ft.hide(hudFragment);
                                    ft.commit();
                                }
                            }
                            break;
                        case "A":
                            if (stream.audioTracks.size() == 1) {
                                local_video_view.setVisibility(View.GONE);
                                remote_video_view.setVisibility(View.GONE);
                                callended_receiver.setVisibility(View.GONE);
                                caller_layout.setVisibility(View.VISIBLE);
                                caller_layout.setBackgroundColor(Color.parseColor("#234D6E"));
//                                caller_layout.setBackgroundColor(getResources().getColor(R.color.White));
                                disconnectimg_caller.setVisibility(View.GONE);
                                connectingtxt_cal.setVisibility(View.VISIBLE);
                                connectingtxt_cal.setText(getCallerName(videotojid.substring(0, videotojid.indexOf("@"))));
                                Glide.with(CallActivity.this)
                                        .load(getCallerImage(videotojid.substring(0, videotojid.indexOf("@"))).toString())
                                        .placeholder(R.drawable.user2)
                                        .transform(new CircleTransform(CallActivity.this))
                                        .into(userimage_caller);
                                remoteAudioTrack = stream.audioTracks.getFirst();
                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                ViewGroup.LayoutParams frameParams = frameLayout.getLayoutParams();
                                frameParams.height = Integer.valueOf(displayMetrics.heightPixels / 4);
                                frameParams.width = displayMetrics.heightPixels;
                                fragment_container_audio.setLayoutParams(frameParams);
                                fragment_container_audio.setVisibility(View.VISIBLE);
                                fragment_container_audio.setBackgroundColor(Color.parseColor("#234D6E"));
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.add(R.id.fragment_container_audio, callFragment);
                                ft.show(callFragment);
                                ft.commit();
                            }
                            break;


                    }

                }
            });
        }

        @Override
        public void onRemoveStream(final MediaStream stream) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //remoteVideoTrack = null;
                    remoteVideoTrack = null;
                }
            });
        }

        @Override
        public void onDataChannel(final DataChannel dc) {
            Log.d(TAG, "New Data channel " + dc.label());

        }

        @Override
        public void onRenegotiationNeeded() {
            // No need to do anything; AppRTC follows a pre-agreed-upon
            // signaling/negotiation protocol.
        }

        @Override
        public void onAddTrack(final RtpReceiver receiver, final MediaStream[] mediaStreams) {
        }
    }

    private class PCObserver1 implements PeerConnection.Observer {


        @Override
        public void onIceCandidate(final IceCandidate candidate) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    candidateMethod(candidate);
                }
            });
        }

        @Override
        public void onIceCandidatesRemoved(final IceCandidate[] candidates) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    events.onIceCandidatesRemoved(candidates);
                }
            });
        }

        @Override
        public void onSignalingChange(PeerConnection.SignalingState newState) {
            Log.d(TAG, "SignalingState: " + newState);
        }

        @Override
        public void onIceConnectionChange(final PeerConnection.IceConnectionState newState) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "IceConnectionState: " + newState);
                    if (newState == PeerConnection.IceConnectionState.CONNECTED) {
//                        events.onIceConnected();
                    } else if (newState == PeerConnection.IceConnectionState.DISCONNECTED) {
//                        events.onIceDisconnected();
                        reportError("ICE connection DISCONNECTED.");
                      /*  if (){
                            logAndToast("Poor Connection!");
                        }
                        else {*/
                        logAndToast("Connection disconnected/poor connection");
//                        if (isOnCall) {
//                            letsCloseResources();
//                            stopService(new Intent(getApplicationContext(), FloatingWidgetService.class));
//                            if (SingleUserChatNew.video != null) {
//                                SingleUserChatNew.video.setVisibility(View.VISIBLE);
//                            }
//                            if (SingleUserChatNew.audioButton != null) {
//                                SingleUserChatNew.audioButton.setVisibility(View.VISIBLE);
//                            }
//                        }
//                        }
//                        disconnect();

                    } else if (newState == PeerConnection.IceConnectionState.FAILED) {
                        reportError("ICE connection failed.");
                        logAndToast("Connection failed");
                    } else if (newState == PeerConnection.IceConnectionState.CLOSED) {
//                        disconnect();
//                        appBean.isOnCall = false;

                        showToRemote = false;
                        callHold = false;
                        if (appBean.fromSUCNContext) {
                            appBean.fromSUCNContext = false;
                            Intent myIntent = new Intent(getApplicationContext(), SingleUserChatNew.class);
                            myIntent.putExtra("username", userName);
                            myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
                            myIntent.putExtra("imgUrl", image);
                            myIntent.putExtra("from", "afterCall");
                            startActivity(myIntent);
                        }
                        appBean.isOnCall = false;
                        callDisconnectPresence();
                        finish();
                    }
                }
            });
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState newState) {
            Log.d(TAG, "IceGatheringState: " + newState);
        }

        @Override
        public void onIceConnectionReceivingChange(boolean receiving) {
            Log.d(TAG, "IceConnectionReceiving changed to " + receiving);
        }

        @Override
        public void onAddStream(final MediaStream stream) {
            Log.d(TAG, "onAddStream changed to --->" + stream.videoTracks.size());

//            executor.execute(new Runnable() {
            globalStream = stream;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (peerConnection == null || isError) {
                        return;
                    }
//                    if (stream.audioTracks.size() > 1 || stream.videoTracks.size() > 1) {
//                        reportError("Weird-looking stream: " + stream);
//                        return;
//                    }
                    switch (calltype) {
                        case "AV":
                            Log.d(TAG, "onAddStream changed to strea --->" + stream.videoTracks.size());
                            if (stream.videoTracks.size() == 1) {
                                ViewGroup.LayoutParams params2 = remote_video_view_one.getLayoutParams();
                                DisplayMetrics displayMetrics2 = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics2);
                                params2.height = Integer.valueOf(displayMetrics2.heightPixels / 3);
                                params2.width = Integer.valueOf(displayMetrics2.widthPixels / 3);
                                System.out.println(" so what is the height params " + params2.height + " " + params2.width);
                                remote_video_view_one.setPadding(0, 0, 10, 10);
                                remote_video_view_one.setLayoutParams(params2);
                                remote_video_view_one.setVisibility(View.VISIBLE);
                                remoteVideoTrack = stream.videoTracks.get(0);
                                remoteAudioTrack = stream.audioTracks.get(0);
                                remoteRenderer = new VideoRenderer(remote_video_view_one);
                                remoteVideoTrack.setEnabled(true);
                                remoteVideoTrack.addRenderer(remoteRenderer);
                                Log.d(TAG, "onAddStream changed to  stream.videoTracks.size() --->" + stream.videoTracks.size());
                                local_video_view.setVisibility(View.GONE);
                                //new code for half view


                                // remoteVideoTrack = stream.videoTracks.getFirst();
                                Log.d(TAG, "onAddStream changed to  remote_video_view --->" + stream.videoTracks.size());

                                ViewGroup.LayoutParams params = local_video_view.getLayoutParams();
                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                params.height = Integer.valueOf(displayMetrics.heightPixels / 4);
                                params.width = Integer.valueOf(displayMetrics.widthPixels / 3);
                                System.out.println(" so what is the height params " + params.height + " " + params.width);
                                local_video_view.setPadding(0, 0, 10, 10);
                                local_video_view.setLayoutParams(params);
                                local_video_view.setVisibility(View.VISIBLE);
                                // Activate call and HUD fragments and start the call.
                                ViewGroup.LayoutParams frameParams = frameLayout.getLayoutParams();
                                frameParams.height = 0;
                                frameParams.width = 0;
                                frameLayout.setLayoutParams(frameParams);
                                frameLayout.setVisibility(View.VISIBLE);
                                if (callFragment.isAdded()) {
                                    return;
                                } else {
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.add(R.id.call_fragment_container, callFragment);
//                                ft.add(R.id.hud_fragment_container, hudFragment);
                                    ft.hide(callFragment);
//                                ft.hide(hudFragment);
                                    ft.commit();
                                }
                            }
                            break;
                        case "A":
                            if (stream.audioTracks.size() == 1) {
                                local_video_view.setVisibility(View.GONE);
                                remote_video_view.setVisibility(View.GONE);
                                callended_receiver.setVisibility(View.GONE);
                                caller_layout.setVisibility(View.VISIBLE);
                                caller_layout.setBackgroundColor(Color.parseColor("#234D6E"));
//                                caller_layout.setBackgroundColor(getResources().getColor(R.color.White));
                                disconnectimg_caller.setVisibility(View.GONE);
                                connectingtxt_cal.setVisibility(View.VISIBLE);
                                connectingtxt_cal.setText(getCallerName(videotojid.substring(0, videotojid.indexOf("@"))));
                                Glide.with(CallActivity.this)
                                        .load(getCallerImage(videotojid.substring(0, videotojid.indexOf("@"))).toString())
                                        .placeholder(R.drawable.user2)
                                        .transform(new CircleTransform(CallActivity.this))
                                        .into(userimage_caller);
                                remoteAudioTrack = stream.audioTracks.getFirst();
                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                ViewGroup.LayoutParams frameParams = frameLayout.getLayoutParams();
                                frameParams.height = Integer.valueOf(displayMetrics.heightPixels / 4);
                                frameParams.width = displayMetrics.heightPixels;
                                fragment_container_audio.setLayoutParams(frameParams);
                                fragment_container_audio.setVisibility(View.VISIBLE);
                                fragment_container_audio.setBackgroundColor(Color.parseColor("#234D6E"));
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.add(R.id.fragment_container_audio, callFragment);
                                ft.show(callFragment);
                                ft.commit();
                            }
                            break;


                    }

                }
            });
        }

        @Override
        public void onRemoveStream(final MediaStream stream) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //remoteVideoTrack = null;
                    remoteVideoTrack = null;
                }
            });
        }

        @Override
        public void onDataChannel(final DataChannel dc) {
            Log.d(TAG, "New Data channel " + dc.label());

        }

        @Override
        public void onRenegotiationNeeded() {
            // No need to do anything; AppRTC follows a pre-agreed-upon
            // signaling/negotiation protocol.
        }

        @Override
        public void onAddTrack(final RtpReceiver receiver, final MediaStream[] mediaStreams) {
        }
    }

    private class SDPObserver implements SdpObserver {

        @Override
        public void onCreateSuccess(final SessionDescription origSdp) {
//            if (localSdp != null) {
//                reportError("Multiple SDP create.");
//                return;
//            }
            String sdpDescription = origSdp.description;
//            if (preferIsac) {
//                sdpDescription = preferCodec(sdpDescription, AUDIO_CODEC_ISAC, true);
//            }
            if (calltype.equals("AV")) {
                sdpDescription = preferCodec(sdpDescription, preferredVideoCodec, false);
            }
            final SessionDescription sdp = new SessionDescription(origSdp.type, sdpDescription);
//            localSdp = sdp;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (peerConnection[0] != null && !isError) {
                        Log.d(TAG, "Set local SDP from " + sdp.type);
                        peerConnection[0].setLocalDescription(sdpObserver, sdp);
                        offeranswerMethod(sdp);
                    }
                }
            });
        }

        @Override
        public void onSetSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (peerConnection[0] == null || isError) {
                        return;
                    }
                    if (isInitator) {
                        // For offering peer connection we first create offer and set
                        // local SDP, then after receiving answer set remote SDP.
                        if (peerConnection[0].getRemoteDescription() == null) {
                            // We've just set our local SDP so time to send it.
                            Log.d(TAG, "Local SDP set succesfully");
//                            events.onLocalDescription(localSdp);
                        } else {
                            // We've just set remote description, so drain remote
                            // and send local ICE candidates.
                            Log.d(TAG, "Remote SDP set succesfully");
                            drainCandidates();
                        }
                    } else {
                        // For answering peer connection we set remote SDP and then
                        // create answer and set local SDP.
                        if (peerConnection[0].getLocalDescription() != null) {
                            // We've just set our local SDP so time to send it, drain
                            // remote and send local ICE candidates.
                            Log.d(TAG, "Local SDP set succesfully");
//                            events.onLocalDescription(localSdp);
                            drainCandidates();
                        } else {
                            // We've just set remote SDP - do nothing for now -
                            // answer will be created soon.
                            Log.d(TAG, "Remote SDP set succesfully");
                            peerConnection[0].createAnswer(sdpObserver, new MediaConstraints());
                        }
                    }
                }
            });
        }

        @Override
        public void onCreateFailure(final String error) {
            reportError("createSDP error: " + error);
        }

        @Override
        public void onSetFailure(final String error) {
            reportError("setSDP error: " + error);
        }
    }

    private class SDPObserver1 implements SdpObserver {

        @Override
        public void onCreateSuccess(final SessionDescription origSdp) {
//            if (localSdp != null) {
//                reportError("Multiple SDP create.");
//                return;
//            }
            String sdpDescription = origSdp.description;
//            if (preferIsac) {
//                sdpDescription = preferCodec(sdpDescription, AUDIO_CODEC_ISAC, true);
//            }
            if (calltype.equals("AV")) {
                sdpDescription = preferCodec(sdpDescription, preferredVideoCodec, false);
            }
            final SessionDescription sdp = new SessionDescription(origSdp.type, sdpDescription);
//            localSdp = sdp;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (peerConnection[1] != null && !isError) {
                        Log.d(TAG, "Set local SDP from " + sdp.type);
                        peerConnection[1].setLocalDescription(sdpObserver1, sdp);
                        offeranswerMethod(sdp);
                    }
                }
            });
        }

        @Override
        public void onSetSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (peerConnection[1] == null || isError) {
                        return;
                    }
                    if (isInitator) {
                        // For offering peer connection we first create offer and set
                        // local SDP, then after receiving answer set remote SDP.
                        if (peerConnection[1].getRemoteDescription() == null) {
                            // We've just set our local SDP so time to send it.
                            Log.d(TAG, "Local SDP set succesfully");
//                            events.onLocalDescription(localSdp);
                        } else {
                            // We've just set remote description, so drain remote
                            // and send local ICE candidates.
                            Log.d(TAG, "Remote SDP set succesfully");
                            drainCandidates();
                        }
                    } else {
                        // For answering peer connection we set remote SDP and then
                        // create answer and set local SDP.
                        if (peerConnection[1].getLocalDescription() != null) {
                            // We've just set our local SDP so time to send it, drain
                            // remote and send local ICE candidates.
                            Log.d(TAG, "Local SDP set succesfully");
//                            events.onLocalDescription(localSdp);
                            drainCandidates();
                        } else {
                            // We've just set remote SDP - do nothing for now -
                            // answer will be created soon.
                            Log.d(TAG, "Remote SDP set succesfully");
                            peerConnection[1].createAnswer(sdpObserver1, new MediaConstraints());
                        }
                    }
                }
            });
        }

        @Override
        public void onCreateFailure(final String error) {
            reportError("createSDP error: " + error);
        }

        @Override
        public void onSetFailure(final String error) {
            reportError("setSDP error: " + error);
        }
    }

    private class ChoiceListner implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();

            System.out.println(" action is action down " + X + " " + Y);

            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams layoutParamsLocal = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    _xDelta = X - layoutParamsLocal.leftMargin;
                    _yDelta = Y - layoutParamsLocal.topMargin;
                    System.out.println(" action is action down " + _xDelta + " " + _yDelta);
                    break;
                case MotionEvent.ACTION_UP:
                    System.out.println(" action is action up " + _xDelta + " " + _yDelta);
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    System.out.println(" action is action pointer down " + _xDelta + " " + _yDelta);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    System.out.println(" action is action pointer up " + _xDelta + " " + _yDelta);
                    break;
                case MotionEvent.ACTION_MOVE:

                    /*if((X<200 && Y<400) || (X>ScreenWidth && Y>ScreenHeight) || (X<200 && Y>ScreenHeight) || (X>ScreenWidth && Y<400)){
                        return false;
                    }*/
                    if (X > ScreenWidth || Y > ScreenHeight || X < 50 || Y < 200) {
                        return false;
                    } else {
                        RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        localLayoutParams.leftMargin = X - _xDelta;
                        localLayoutParams.topMargin = Y - _yDelta;
                        localLayoutParams.rightMargin = -150;
                        localLayoutParams.bottomMargin = -150;
                        v.setLayoutParams(localLayoutParams);
                        return true;
                    }

            }
            root_view.invalidate();
            return true;
        }
    }

    private class GetCallId extends AsyncTask<Void, Integer, Void> {

        String chatRoom = "";

        public GetCallId(String chatroom) {
            this.chatRoom = chatroom;
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            http://localhost:8080/colabusApp/ChatAuth?act=insertCallHistory&userId=16&id=280&companyId=3&NameforChatRoom="teset"

//>https://newtest.colabus.com/ChatAuth?act=insertCallHistory&userId=216&id=1079&companyId=2&NameforChatRoom=userMeet_216_20183214386
//https://newtest.colabus.com/ChatAuth?act=insertCallHistory&userId=951&id=601@devchat.colabus.com&companyId=2&NameforChatRoom=userMeet_951_20183212198
            List<BasicNameValuePair> projParams = new ArrayList<BasicNameValuePair>();
            projParams.add(new BasicNameValuePair("act", "insertCallHistory"));
            projParams.add(new BasicNameValuePair("userId", appBean.userId));
            projParams.add(new BasicNameValuePair("id", videotojid.substring(0, videotojid.indexOf("@"))));
            projParams.add(new BasicNameValuePair("companyId", appBean.userCompanyId));
            projParams.add(new BasicNameValuePair("NameforChatRoom", this.chatRoom));
            projParams.add(new BasicNameValuePair("callType", calltype));

            appPrefs = PreferenceManager.getDefaultSharedPreferences(CallActivity.this);

            try {
                appBean.chatUrl = "https://"
                        + appPrefs.getString("appURL", "").toString()
                        + "/ChatAuth";
                ResponseResult = (String) HTTPService.executeHttpPost(
                        appBean.chatUrl, projParams, getApplicationContext());
            } catch (Exception e) {
//                progressDialog.dismiss();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callIdForDb = ResponseResult.toString().trim();
//            callIdForDb.replace("\n", "");
            System.out.println("After trimm empty or new line" + callIdForDb);
            remoteCallInitMethod();

        }
    }


//        try {
//
//            MultiUserChat muc = new MultiUserChat(connection, chatRoom);
//            // Create a chat room
//
//            muc.create(groupName);
//            // RoomName room name
//            // To obtain the chat room configuration form
//            Form form = muc.getConfigurationForm();
//            // Create a new form to submit the original form according to the.
//            Form submitForm = form.createAnswerForm();
//            // To submit the form to add a default reply
//
//            List<FormField> fields = form.getFields();
//            for (int i = 0; i < fields.size(); i++) {
//                FormField field = fields.get(i);
//                if (!FormField.TYPE_HIDDEN.equals(field.getType())
//                        && field.getVariable() != null) {
//                    // Set default values for an answer
//                    submitForm.setDefaultAnswer(field.getVariable());
//                }
//            }
//            // Set the chat room of the new owner
//            List<String> owners = new ArrayList<String>();
//            owners.add(connection.getUser());// The user JID
//            submitForm.setAnswer("muc#roomconfig_roomowners", owners);
//            // Set the chat room is a long chat room, soon to be preserved
//            submitForm.setAnswer("muc#roomconfig_persistentroom", false);
//            // Only members of the open room
//            submitForm.setAnswer("muc#roomconfig_membersonly", false);
//            // Allows the possessor to invite others
//            submitForm.setAnswer("muc#roomconfig_allowinvites", true);
//            // Enter the password if needed
//            //submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
//            // Set to enter the password
//            //submitForm.setAnswer("muc#roomconfig_roomsecret", "password");
//            // Can be found in possession of real JID role
//            // submitForm.setAnswer("muc#roomconfig_whois", "anyone");
//            // Login room dialogue
//            submitForm.setAnswer("muc#roomconfig_enablelogging", true);
//            // Only allow registered nickname log
//            submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
//            // Allows the user to modify the nickname
//            submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
//            // Allows the user to register the room
//            submitForm.setAnswer("x-muc#roomconfig_registration", false);
//            // Send the completed form (the default) to the server to configure the chat room
//            submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
//            // Send the completed form (the default) to the server to configure the chat room
//            muc.sendConfigurationForm(submitForm);
//        } catch (XMPPException e) {
//            e.printStackTrace();
//        } catch (SmackException.NoResponseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (SmackException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    private class ReceiverId extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            //http://localhost:8080/colabusApp/ChatAuth?act=insertReciverCallHistory&calId=289

            List<BasicNameValuePair> projParams = new ArrayList<BasicNameValuePair>();
            projParams.add(new BasicNameValuePair("act", "insertReciverCallHistory"));
            projParams.add(new BasicNameValuePair("calId", callIdForDb));
            projParams.add(new BasicNameValuePair("userId", appBean.userId));

            appPrefs = PreferenceManager
                    .getDefaultSharedPreferences(CallActivity.this);

            try {
                appBean.chatUrl = "https://"
                        + appPrefs.getString("appURL", "").toString()
                        + "/ChatAuth";
                ResponseResult = (String) HTTPService.executeHttpPost(
                        appBean.chatUrl, projParams, getApplicationContext());
            } catch (Exception e) {
//                progressDialog.dismiss();
                e.printStackTrace();
            }
            return null;
        }
    }

    private class CallDisconnection extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            //http://localhost:8080/colabusApp/ChatAuth?act=updateCallEndTime&callId=callId&toUserId=id

            List<BasicNameValuePair> projParams = new ArrayList<BasicNameValuePair>();
            projParams.add(new BasicNameValuePair("act", "updateCallEndTime"));
            projParams.add(new BasicNameValuePair("callId", callIdForDb));
            projParams.add(new BasicNameValuePair("toUserId", videotojid.substring(0, videotojid.indexOf("@"))));

            appPrefs = PreferenceManager
                    .getDefaultSharedPreferences(CallActivity.this);

            try {
                appBean.chatUrl = "https://"
                        + appPrefs.getString("appURL", "").toString()
                        + "/ChatAuth";
                ResponseResult = (String) HTTPService.executeHttpPost(appBean.chatUrl, projParams, getApplicationContext());
            } catch (Exception e) {
//                progressDialog.dismiss();
                e.printStackTrace();
            }
            return null;
        }
    }

    private class CallResponseUpdate extends AsyncTask<Void, Integer, Void> {

        String responseType = "";

        public CallResponseUpdate(String responseType) {
            this.responseType = responseType;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //http://localhost:8080/colabusApp/ChatAuth?act=updateCallResponse&callId=callId&toUserId=id&resType=resType

            List<BasicNameValuePair> projParams = new ArrayList<BasicNameValuePair>();
            projParams.add(new BasicNameValuePair("act", "updateCallResponse"));
            projParams.add(new BasicNameValuePair("callId", callIdForDb));
            projParams.add(new BasicNameValuePair("toUserId", videotojid.substring(0, videotojid.indexOf("@"))));
            projParams.add(new BasicNameValuePair("resType", this.responseType));
            projParams.add(new BasicNameValuePair("deviceType", "android"));
            appPrefs = PreferenceManager.getDefaultSharedPreferences(CallActivity.this);

            try {
                appBean.chatUrl = "https://" + appPrefs.getString("appURL", "").toString() + "/ChatAuth";
                ResponseResult = (String) HTTPService.executeHttpPost(appBean.chatUrl, projParams, getApplicationContext());
            } catch (Exception e) {
//                progressDialog.dismiss();
                e.printStackTrace();
            }
            return null;
        }
    }

    private class CallResponseUpdateRequestCancelled extends AsyncTask<Void, Integer, Void> {

        String responseType = "";

        public CallResponseUpdateRequestCancelled(String responseType) {
            this.responseType = responseType;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //http://localhost:8080/colabusApp/ChatAuth?act=updateCallResponse&callId=callId&toUserId=id&resType=resType

            List<BasicNameValuePair> projParams = new ArrayList<BasicNameValuePair>();
            projParams.add(new BasicNameValuePair("act", "updateCallResponse"));
            projParams.add(new BasicNameValuePair("callId", callIdForDb));
            projParams.add(new BasicNameValuePair("toUserId", videofromjid.substring(0, videofromjid.indexOf("@"))));
            projParams.add(new BasicNameValuePair("resType", this.responseType));
            projParams.add(new BasicNameValuePair("deviceType", "android"));
            appPrefs = PreferenceManager.getDefaultSharedPreferences(CallActivity.this);

            try {
                appBean.chatUrl = "https://" + appPrefs.getString("appURL", "").toString() + "/ChatAuth";
                ResponseResult = (String) HTTPService.executeHttpPost(appBean.chatUrl, projParams, getApplicationContext());
            } catch (Exception e) {
//                progressDialog.dismiss();
                e.printStackTrace();
            }
            return null;
        }
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        String LOG_TAG = "PhoneCallListener";
        private boolean isPhoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");
                if (isPhoneCalling) {
                    Log.i(LOG_TAG, "restart app");
                    // restart app
                  /*  Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);*/

                    isPhoneCalling = false;
                }

            }
        }
    }

    private class KnowConversationId extends AsyncTask<Void, Integer, Void> {

        String userId = "", userName = "", ResponseResult = "";

        public KnowConversationId(String userId, String userName) {
            this.userId = userId;
            this.userName = userName;
        }


        @Override
        protected Void doInBackground(Void... params) {

            List<BasicNameValuePair> projParams = new ArrayList<BasicNameValuePair>();
            projParams.add(new BasicNameValuePair("act", "checkConvId"));
            projParams.add(new BasicNameValuePair("fromUser", appBean.userId));
            projParams.add(new BasicNameValuePair("toUser", this.userId));

            appPrefs = PreferenceManager
                    .getDefaultSharedPreferences(CallActivity.this);
            try {
                appBean.chatUrl = "https://"
                        + appPrefs.getString("appURL", "").toString()
                        + "/ChatAuth";

                ResponseResult = (String) HTTPService.executeHttpPost(
                        appBean.chatUrl, projParams, getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            appBean.conversationId = ResponseResult;
            Intent intent = new Intent(getApplicationContext(), HighlightList.class);
            intent.putExtra("from", "callactivity");
            intent.putExtra("callid", appBean.highLightcallID);
            intent.putExtra("convId", ResponseResult);
            intent.putExtra("hTime", appBean.calldurationTime);
            intent.putExtra("senderId", videotojid.substring(0, videotojid.indexOf("@")));
            intent.putExtra("userName", userName);
            intent.putExtra("confGroupNameJid", confGroupNameJid.toString());
            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out);
           /* Intent myIntent = new Intent(getApplicationContext(), OneOnOneMessage.class);
            myIntent.putExtra("convId", appBean.conversationId);
            myIntent.putExtra("username", userName);
            myIntent.putExtra("idOfUser", videotojid.substring(0, videotojid.indexOf("@")));
            myIntent.putExtra("imgUrl", image);
            myIntent.putExtra("from", "callactivity");
            startActivity(myIntent);*/
        }
    }

    private class CheckCasesHighlight {

        Message constructorMessage;

        public CheckCasesHighlight(Message message) {
            this.constructorMessage = message;
        }

        public void reflectActiivityStatusHighlight() {

            System.out.println("running in reflectActiivityStatusHighlight CheckCasesHighlight");
            JsonPacketExtension packetExtension = this.constructorMessage.getExtension("json", "urn:xmpp:json:0");
            try {
                final JSONObject object = new JSONObject(packetExtension.getJson().toString());
                System.out.println("CheckCasesHighlight ---->" + object.getString("type") + " " + callOccured);

                ActivityManager am = (ActivityManager) CallActivity.this.getSystemService(Context.ACTIVITY_SERVICE);
                ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//                ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//                ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                System.out.println("coming HighlightList ---->" + cn.getShortClassName());
//                if (cn.getShortClassName().equals(".HighlightList")) {
//                    //update adapter
//                    // flag
//                    System.out.println("coming HighlightList ---->" + cn.getShortClassName());
//
//                } else {
                // no updating adapter

                switch (object.getString("status").trim()) {
                    case "new": {
                        CallActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // <message type="normal" to="7@devchat.colabus.com"><json xmlns="urn:xmpp:json:0">
// {"calltype":"AV","type":" videohighlight”,” callId”:”16”, ”status”:”new / edit / delete”,”hId”:”10”}</json></message>
                                Toast.makeText(CallActivity.this, "New Highlight", Toast.LENGTH_SHORT).show();
                                appBean.highLightCount = appBean.highLightCount + 1;
                                try {
//                                    if (object.getString("callid").trim().contains("\n")){
//                                        String[] strings = object.getString("callId").trim().split("\n");
//                                        callIdForDb =strings[0] ;
//                                    }else {
                                    callIdForDb = object.getString("callid").trim();
//                                    }
                                    appBean.highLightID = object.getString("hId").trim();
                                    appBean.highLightStatus = object.getString("status").trim();
                                    highlightcount.setVisibility(View.VISIBLE);
                                    highlightcount.setText(Integer.toString(appBean.highLightCount));
                                    addclicked = false;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        break;
                    }
                    case "edit": {
                        CallActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CallActivity.this, "Highlight updated!", Toast.LENGTH_SHORT).show();
                                try {
                                    callIdForDb = object.getString("callid").trim();
                                    appBean.highLightID = object.getString("hId").trim();
                                    appBean.highLightStatus = object.getString("status").trim();
//                                    highlight.setBackground(getResources().getDrawable(R.mipmap.logo36));
                                    addclicked = false;
                                    if (appBean.highLightCount >= 1) {
                                        highlightcount.setVisibility(View.VISIBLE);
                                        highlightcount.setText(Integer.toString(appBean.highLightCount));
                                    } else {
                                        highlightcount.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        break;
                    }
                    case "delete": {
                        CallActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CallActivity.this, "Highlight Deleted!", Toast.LENGTH_SHORT).show();
                                try {
                                    callIdForDb = object.getString("callid").trim();
                                    appBean.highLightID = object.getString("hId").trim();
                                    appBean.highLightStatus = object.getString("status").trim();
//                                    highlight.setBackground(getResources().getDrawable(R.mipmap.logo36));
                                    if (appBean.highLightCount > 1) {
                                        appBean.highLightCount = appBean.highLightCount - 1;
                                        highlightcount.setVisibility(View.VISIBLE);
                                        highlightcount.setText(Integer.toString(appBean.highLightCount));
                                    } else {
                                        highlightcount.setVisibility(View.GONE);
                                    }
                                    addclicked = false;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        break;
                    }
//                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public final class PresenceCustom extends Stanza {

        public static final String ELEMENT = "presence";

        private org.jivesoftware.smack.packet.Presence.Type type = org.jivesoftware.smack.packet.Presence.Type.available;
        private String status = null;

        /**
         * The priority of the presence. The magic value {@link Integer#MIN_VALUE} is used to indicate that the original
         * presence stanza did not had an explicit priority set. In which case the priority defaults to 0.
         *
         * @see <a href="https://tools.ietf.org/html/rfc6121#section-4.7.2.3">RFC 6121 § 4.7.2.3.</a>
         */
        private int priority = Integer.MIN_VALUE;

        private org.jivesoftware.smack.packet.Presence.Mode mode = null;

        /**
         * Creates a new presence update. Status, priority, and mode are left un-set.
         *
         * @param type the type.
         */
        public PresenceCustom(org.jivesoftware.smack.packet.Presence.Type type) {
            // Ensure that the stanza ID is set by calling super().
            super();
            setType(type);
        }

        public PresenceCustom(Jid to, org.jivesoftware.smack.packet.Presence.Type type) {
            // Ensure that the stanza ID is set by calling super().
            setType(type);
            setTo(to);
        }

        /**
         * Creates a new presence with the given type and using the given XMPP address as recipient.
         *
         * @param to the recipient.
         * @since 4.2
         */
        public PresenceCustom(Jid to) {
            setTo(to);
        }


        /**
         * Returns true if the presence type is {@link org.jivesoftware.smack.packet.Presence.Type#available available} and the presence
         * mode is {@link org.jivesoftware.smack.packet.Presence.Mode#away away}, {@link org.jivesoftware.smack.packet.Presence.Mode#xa extended away}, or
         * {@link org.jivesoftware.smack.packet.Presence.Mode#dnd do not disturb}. False will be returned when the type or mode
         * is any other value, including when the presence type is unavailable (offline).
         * This is a convenience method equivalent to
         * <tt>type == Type.available &amp;&amp; (mode == Mode.away || mode == Mode.xa || mode == Mode.dnd)</tt>.
         *
         * @return true if the presence type is available and the presence mode is away, xa, or dnd.
         */
        public boolean isAway() {
            return type == org.jivesoftware.smack.packet.Presence.Type.available && (mode == org.jivesoftware.smack.packet.Presence.Mode.away || mode == org.jivesoftware.smack.packet.Presence.Mode.xa || mode == org.jivesoftware.smack.packet.Presence.Mode.dnd);
        }

        /**
         * Returns the type of this presence packet.
         *
         * @return the type of the presence packet.
         */
        public org.jivesoftware.smack.packet.Presence.Type getType() {
            return type;
        }

        /**
         * Sets the type of the presence packet.
         *
         * @param type the type of the presence packet.
         */
        public void setType(org.jivesoftware.smack.packet.Presence.Type type) {
            this.type = Objects.requireNonNull(type, "Type cannot be null");
        }

        /**
         * Returns the status message of the presence update, or <tt>null</tt> if there
         * is not a status. The status is free-form text describing a user's presence
         * (i.e., "gone to lunch").
         *
         * @return the status message.
         */
        public String getStatus() {
            return status;
        }

        /**
         * Sets the status message of the presence update. The status is free-form text
         * describing a user's presence (i.e., "gone to lunch").
         *
         * @param status the status message.
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * Returns the priority of the presence, or Integer.MIN_VALUE if no priority has been set.
         *
         * @return the priority.
         * @see <a href="https://tools.ietf.org/html/rfc6121#section-4.7.2.3">RFC 6121 § 4.7.2.3. Priority Element</a>
         */
        public int getPriority() {
            if (priority == Integer.MIN_VALUE) {
                return 0;
            }
            return priority;
        }

        /**
         * Sets the priority of the presence. The valid range is -128 through 127.
         *
         * @param priority the priority of the presence.
         * @throws IllegalArgumentException if the priority is outside the valid range.
         * @see <a href="https://tools.ietf.org/html/rfc6121#section-4.7.2.3">RFC 6121 § 4.7.2.3. Priority Element</a>
         */
        public void setPriority(int priority) {
            if (priority < -128 || priority > 127) {
                throw new IllegalArgumentException("Priority value " + priority +
                        " is not valid. Valid range is -128 through 127.");
            }
            this.priority = priority;
        }

        /**
         * Returns the mode of the presence update.
         *
         * @return the mode.
         */
        public org.jivesoftware.smack.packet.Presence.Mode getMode() {
            if (mode == null) {
                return org.jivesoftware.smack.packet.Presence.Mode.available;
            }
            return mode;
        }

        /**
         * Sets the mode of the presence update. A null presence mode value is interpreted
         * to be the same thing as {@link org.jivesoftware.smack.packet.Presence.Mode#available}.
         *
         * @param mode the mode.
         */
        public void setMode(org.jivesoftware.smack.packet.Presence.Mode mode) {
            this.mode = mode;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Presence Stanza [");
            logCommonAttributes(sb);
            sb.append("type=").append(type).append(',');
            if (mode != null) {
                sb.append("mode=").append(mode).append(',');
            }
            if (!StringUtils.isNullOrEmpty(status)) {
                sb.append("status=").append(status).append(',');
            }
            if (priority != Integer.MIN_VALUE) {
                sb.append("prio=").append(priority).append(',');
            }
            sb.append(']');
            return sb.toString();
        }

        @Override
        public XmlStringBuilder toXML(String enclosingNamespace) {
            XmlStringBuilder buf = new XmlStringBuilder(enclosingNamespace);
            buf.halfOpenElement(ELEMENT);
            buf.optAttribute("to", getTo());
            buf.optAttribute("type", getType());
            // addCommonAttributes(buf, enclosingNamespace);
            //buf.append(getExtensions(), enclosingNamespace);
            buf.attribute("xmlns", StreamOpen.CLIENT_NAMESPACE);
            buf.append(">");
            buf.halfOpenElement(MUCInitialPresence.ELEMENT);
            buf.attribute("xmlns", MUCOwner.NAMESPACE);
            buf.append(">");
            buf.closeElement(MUCInitialPresence.ELEMENT);
            // Add the error sub-packet, if there is one.
            appendErrorIfExists(buf, enclosingNamespace);
            buf.closeElement(ELEMENT);
            return buf;
        }

    }

    public final class PresenceCustomUser extends Stanza {

        public static final String ELEMENT = "presence";

        private org.jivesoftware.smack.packet.Presence.Type type = org.jivesoftware.smack.packet.Presence.Type.available;
        private String status = null;

        /**
         * The priority of the presence. The magic value {@link Integer#MIN_VALUE} is used to indicate that the original
         * presence stanza did not had an explicit priority set. In which case the priority defaults to 0.
         *
         * @see <a href="https://tools.ietf.org/html/rfc6121#section-4.7.2.3">RFC 6121 § 4.7.2.3.</a>
         */
        private int priority = Integer.MIN_VALUE;

        private org.jivesoftware.smack.packet.Presence.Mode mode = null;

        /**
         * Creates a new presence update. Status, priority, and mode are left un-set.
         *
         * @param type the type.
         */
        public PresenceCustomUser(org.jivesoftware.smack.packet.Presence.Type type) {
            // Ensure that the stanza ID is set by calling super().
            super();
            setType(type);
        }

        public PresenceCustomUser(Jid to, org.jivesoftware.smack.packet.Presence.Type type) {
            // Ensure that the stanza ID is set by calling super().
            setType(type);
            setTo(to);
        }

        /**
         * Creates a new presence with the given type and using the given XMPP address as recipient.
         *
         * @param to the recipient.
         * @since 4.2
         */
        public PresenceCustomUser(Jid to) {
            setTo(to);
        }


        /**
         * Returns true if the presence type is {@link org.jivesoftware.smack.packet.Presence.Type#available available} and the presence
         * mode is {@link org.jivesoftware.smack.packet.Presence.Mode#away away}, {@link org.jivesoftware.smack.packet.Presence.Mode#xa extended away}, or
         * {@link org.jivesoftware.smack.packet.Presence.Mode#dnd do not disturb}. False will be returned when the type or mode
         * is any other value, including when the presence type is unavailable (offline).
         * This is a convenience method equivalent to
         * <tt>type == Type.available &amp;&amp; (mode == Mode.away || mode == Mode.xa || mode == Mode.dnd)</tt>.
         *
         * @return true if the presence type is available and the presence mode is away, xa, or dnd.
         */
        public boolean isAway() {
            return type == org.jivesoftware.smack.packet.Presence.Type.available && (mode == org.jivesoftware.smack.packet.Presence.Mode.away || mode == org.jivesoftware.smack.packet.Presence.Mode.xa || mode == org.jivesoftware.smack.packet.Presence.Mode.dnd);
        }

        /**
         * Returns the type of this presence packet.
         *
         * @return the type of the presence packet.
         */
        public org.jivesoftware.smack.packet.Presence.Type getType() {
            return type;
        }

        /**
         * Sets the type of the presence packet.
         *
         * @param type the type of the presence packet.
         */
        public void setType(org.jivesoftware.smack.packet.Presence.Type type) {
            this.type = Objects.requireNonNull(type, "Type cannot be null");
        }

        /**
         * Returns the status message of the presence update, or <tt>null</tt> if there
         * is not a status. The status is free-form text describing a user's presence
         * (i.e., "gone to lunch").
         *
         * @return the status message.
         */
        public String getStatus() {
            return status;
        }

        /**
         * Sets the status message of the presence update. The status is free-form text
         * describing a user's presence (i.e., "gone to lunch").
         *
         * @param status the status message.
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * Returns the priority of the presence, or Integer.MIN_VALUE if no priority has been set.
         *
         * @return the priority.
         * @see <a href="https://tools.ietf.org/html/rfc6121#section-4.7.2.3">RFC 6121 § 4.7.2.3. Priority Element</a>
         */
        public int getPriority() {
            if (priority == Integer.MIN_VALUE) {
                return 0;
            }
            return priority;
        }

        /**
         * Sets the priority of the presence. The valid range is -128 through 127.
         *
         * @param priority the priority of the presence.
         * @throws IllegalArgumentException if the priority is outside the valid range.
         * @see <a href="https://tools.ietf.org/html/rfc6121#section-4.7.2.3">RFC 6121 § 4.7.2.3. Priority Element</a>
         */
        public void setPriority(int priority) {
            if (priority < -128 || priority > 127) {
                throw new IllegalArgumentException("Priority value " + priority +
                        " is not valid. Valid range is -128 through 127.");
            }
            this.priority = priority;
        }

        /**
         * Returns the mode of the presence update.
         *
         * @return the mode.
         */
        public org.jivesoftware.smack.packet.Presence.Mode getMode() {
            if (mode == null) {
                return org.jivesoftware.smack.packet.Presence.Mode.available;
            }
            return mode;
        }

        /**
         * Sets the mode of the presence update. A null presence mode value is interpreted
         * to be the same thing as {@link org.jivesoftware.smack.packet.Presence.Mode#available}.
         *
         * @param mode the mode.
         */
        public void setMode(org.jivesoftware.smack.packet.Presence.Mode mode) {
            this.mode = mode;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Presence Stanza [");
            logCommonAttributes(sb);
            sb.append("type=").append(type).append(',');
            if (mode != null) {
                sb.append("mode=").append(mode).append(',');
            }
            if (!StringUtils.isNullOrEmpty(status)) {
                sb.append("status=").append(status).append(',');
            }
            if (priority != Integer.MIN_VALUE) {
                sb.append("prio=").append(priority).append(',');
            }
            sb.append(']');
            return sb.toString();
        }

        @Override
        public XmlStringBuilder toXML(String enclosingNamespace) {
            XmlStringBuilder buf = new XmlStringBuilder(enclosingNamespace);
            buf.halfOpenElement(ELEMENT);
            buf.optAttribute("to", getTo());
            buf.optAttribute("type", getType());
            //addCommonAttributes(buf, enclosingNamespace);
            //buf.append(getExtensions(), enclosingNamespace);
            buf.attribute("xmlns", StreamOpen.CLIENT_NAMESPACE);
            buf.append(">");
            buf.halfOpenElement(MUCInitialPresence.ELEMENT);
            buf.attribute("xmlns", MUCUser.NAMESPACE);
            buf.append(">");
            buf.closeElement(MUCInitialPresence.ELEMENT);
            // Add the error sub-packet, if there is one.
            appendErrorIfExists(buf, enclosingNamespace);
            buf.closeElement(ELEMENT);
            return buf;
        }

    }


}

