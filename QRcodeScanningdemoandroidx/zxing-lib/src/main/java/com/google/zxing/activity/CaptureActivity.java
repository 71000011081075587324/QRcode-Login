package com.google.zxing.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.R;
import com.google.zxing.Result;
import com.google.zxing.camera.CameraManager;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.decoding.CaptureActivityHandler;
import com.google.zxing.decoding.InactivityTimer;
import com.google.zxing.decoding.RGBLuminanceSource;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.util.BitmapUtil;
import com.google.zxing.util.Constant;
import com.google.zxing.utils.OkHttpCallback;
import com.google.zxing.utils.OkHttpUtils;
import com.google.zxing.utils.SharedPreferencesUtils;
import com.google.zxing.view.ViewfinderView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import com.google.zxing.vo.QrLoginVo;
import com.google.zxing.vo.ServerResponse;
import com.google.zxing.vo.UserVo;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends AppCompatActivity implements Callback {

    private static final int REQUEST_CODE_SCAN_GALLERY = 100;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private ImageButton back;
    private ImageButton btnFlash;
    private Button btnAlbum; // 相册
    private boolean isFlashOn = false;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ProgressDialog mProgress;
    private Bitmap scanBitmap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_content);
        back = (ImageButton) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnFlash = (ImageButton) findViewById(R.id.btn_flash);
        btnFlash.setOnClickListener(flashListener);

        btnAlbum = (Button) findViewById(R.id.btn_album);
        btnAlbum.setOnClickListener(albumOnClick);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

    }

    private View.OnClickListener albumOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //打开手机中的相册
            Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
            innerIntent.setType("image/*");
            startActivityForResult(innerIntent, REQUEST_CODE_SCAN_GALLERY);
        }
    };

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    handleAlbumPic(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //发送登录请求
    private void qrLogin(int userid,String username,String randchar,String token){
        //请求接口前缀
        String urlPrefix = "http://3a955v7566.wicp.vip/user";
//        Intent intent = getIntent();
//        String token = intent.getStringExtra("token");


        //请求接口
        OkHttpUtils.getWithToken(urlPrefix + "/qrLogin?userid=" + userid + "&username=" + username + "&randchar=" + randchar, token , new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);

                //使用Gson解析数据
                Gson gson = new Gson();
                Type userType = new TypeToken<ServerResponse<QrLoginVo>>(){}.getType();
                ServerResponse<QrLoginVo> serverResponse = gson.fromJson(msg, userType);

                int statusl = serverResponse.getStatus();

                if(statusl != 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(CaptureActivity.this);
                            dialog.setTitle("扫码登录失败");
                            dialog.setMessage("扫码登录失败，请重新尝试");
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            dialog.setNegativeButton(null,null);
                            dialog.show();
                        }
                    });
                }

            }
        } );

    }


    /**
     * 处理从图库中选择的图片
     * @param data
     */
    private void handleAlbumPic(Intent data) {
        //获取选中图片的路径
        final Uri uri = data.getData();

        mProgress = new ProgressDialog(CaptureActivity.this);
        mProgress.setMessage("正在扫描...");
        mProgress.setCancelable(false);
        mProgress.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Result result = scanningImage(uri);
                mProgress.dismiss();
                if (result != null) {
//                    Intent resultIntent = new Intent();
//                    Bundle bundle = getIntent().getExtras();
//                    if (bundle == null) {
//                        bundle = new Bundle();
//                    }
//                    bundle.putString(Constant.INTENT_EXTRA_KEY_QR_SCAN, result.getText());
//
//                    resultIntent.putExtras(bundle);
//                    CaptureActivity.this.setResult(RESULT_OK, resultIntent);

                    String resultString = result.getText();

                    //判断是否是网页登录二维码
                    String mobile = "QRcodeImg";
                    if(resultString.endsWith(mobile)){
                        SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(CaptureActivity.this);
                        boolean isLogin = sharedPreferencesUtils.readBoolean("isLogin");
//                        ServerResponse<UserVo> serverResponse = sharedPreferencesUtils.readObject("user", new TypeToken<ServerResponse<UserVo>>(){}.getType());
                        if(isLogin){
//                            int userid = serverResponse.getData().getUesrid();
//                            String username = serverResponse.getData().getUsername();
                            String username = sharedPreferencesUtils.readString("user");
                            String token = sharedPreferencesUtils.readString("token");
                            qrLogin(0,username,token,resultString);
                        }
//
//                        CaptureActivity.this.finish();
                    }

                    //判断扫描二维码是否是网址，并将扫描出的信息显示出来
                String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                            + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

                    Pattern pat = Pattern.compile(regex.trim());//比对
                    Matcher mat = pat.matcher(resultString.trim());
                    if(mat.matches()){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(result.getText()));
                        startActivity(intent);
                    }else{
                        //给Capture传内容，不过暂未处理，后续可优化
                        Intent resultIntent = new Intent();
                        Bundle bundle = getIntent().getExtras();
                        if (bundle == null) {
                            bundle = new Bundle();
                        }
                        bundle.putString(Constant.INTENT_EXTRA_KEY_QR_SCAN, result.getText());

                        resultIntent.putExtras(bundle);
                        CaptureActivity.this.setResult(RESULT_OK, resultIntent);
                        finish();

                    }

//                    finish();
                } else {
                    Toast.makeText(CaptureActivity.this, R.string.note_identify_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    /**
     * 扫描二维码图片的方法
     * @param uri
     * @return
     */
    public Result scanningImage(Uri uri) {
        if (uri == null) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        scanBitmap = BitmapUtil.decodeUri(this, uri, 500, 500);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText(); //二维码代表的图片
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(CaptureActivity.this, R.string.note_scan_failed, Toast.LENGTH_SHORT).show();
        } else {
//            Intent resultIntent = new Intent();
//            Bundle bundle = getIntent().getExtras();
//            if (bundle == null) {
//                bundle = new Bundle();
//            }
//            bundle.putString(Constant.INTENT_EXTRA_KEY_QR_SCAN, resultString);
//            resultIntent.putExtras(bundle);
//            this.setResult(RESULT_OK, resultIntent);
//

            //判断是否是网页登录二维码
            String mobile = "QRcodeImg";
            if(resultString.endsWith(mobile)){
                SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(CaptureActivity.this);
                boolean isLogin = sharedPreferencesUtils.readBoolean("isLogin");
//                ServerResponse<UserVo> serverResponse = sharedPreferencesUtils.readObject("user", new TypeToken<ServerResponse<UserVo>>(){}.getType());
                if(isLogin){
//                    int userid = serverResponse.getData().getUesrid();
//                    String username = serverResponse.getData().getUsername();
//                    qrLogin(userid,username,resultString);

                    String username = sharedPreferencesUtils.readString("user");
                    String token = sharedPreferencesUtils.readString("token");
                    qrLogin(0,username,resultString,token);

//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(this,"登录成功").show();
//                        }
//                    });
                }
//                CaptureActivity.this.finish();

            }


            //判断扫描二维码是否是网址，并将扫描出的信息显示出来
            String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                    + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

            Pattern pat = Pattern.compile(regex.trim());//比对
            Matcher mat = pat.matcher(resultString.trim());
            if (mat.matches()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(resultString));
                startActivity(intent);

            }else{

                CaptureActivity.this.finish();
            }
//            CaptureActivity.this.finish();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     *  闪光灯开关按钮
     */
    private View.OnClickListener flashListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                boolean isSuccess = CameraManager.get().setFlashLight(!isFlashOn);
                if (!isSuccess) {
                    Toast.makeText(CaptureActivity.this, R.string.note_no_flashlight, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isFlashOn) {
                    // 关闭闪光灯
                    btnFlash.setImageResource(R.drawable.flash_off);
                    isFlashOn = false;
                } else {
                    // 开启闪光灯
                    btnFlash.setImageResource(R.drawable.flash_on);
                    isFlashOn = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}