package com.zjmy.signin.utils.update.service;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.zjmy.signin.BuildConfig;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.utils.update.bean.FirVersionInfo;
import com.zjmy.signin.utils.update.callback.DownloadProgressCallback;
import com.zjmy.signin.utils.update.callback.DownloadTaskCanceledCallback;
import com.zjmy.signin.utils.update.utils.CommonUtils;
import com.zjmy.signin.utils.update.utils.NetUtils;
import com.zjmy.signin.utils.update.utils.PackageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import okhttp3.OkHttpClient;

/**
 * Create on: 2016-07-30 Author: wangh Summary: TODO
 */
public abstract class BaseUpdateService extends Service implements DownloadProgressCallback {
    private final static String TAG = BaseUpdateService.class.getSimpleName();
    private final static int THREAD_KEEP_ALIVE_TIME = 1;
    private final static int NOTIFICATION_ID = 20169394;
    private final static String DIR_NAME = "update";
    private int mRetryTime = 0;

    private DownloadTask mDownloadTask;
    private String DOWNLOAD_DIR;

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private ConnectivityManager mConnectivityManager;
    private ThreadPoolExecutor mPoolExecutor;

    private com.zjmy.signin.utils.update.bean.PackageInfo mDirCacheLastPackInfo;

    private static int NUMBER_OF_CORES;

    private boolean downloadAllNetStatus;

    @Override
    public final void onCreate() {
        super.onCreate();
        initThreadPool();
        initNotification();
        initDownloadDir();
        initConnectManager();
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) setupFirTask(intent);
        return Service.START_STICKY_COMPATIBILITY;
    }

    @Nullable
    @Override
    public final IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        if (mDownloadTask != null && !mDownloadTask.isCancelled()) {
            mDownloadTask.cancel(true);
        }
        if (mPoolExecutor != null) {
            if (!mPoolExecutor.isShutdown()) {
                mPoolExecutor.shutdownNow();
            }
        }
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(120, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    @Override
    public void progressCallback(final double progress) {
        mBuilder.setProgress(100, (int) progress, false);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void downloadFail(Exception exception) {

    }

    @Override
    public void downloadCompleted(File file) {
        if (getPackageName().equals(getPackageName())) PackageUtils.installAPK(this, file);
        stopForeground(true);
        stopSelf();
    }

    private void initThreadPool() {
        NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        mPoolExecutor =
                new ThreadPoolExecutor(0, NUMBER_OF_CORES, THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                        new SynchronousQueue<Runnable>());
    }

    @SuppressWarnings("unchecked")
    private void initDownloadDir() {
        mPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                initDownloadTask();
            }
        });
    }

    private void initDownloadTask() {
        if (getApplication().getExternalCacheDir() != null && !TextUtils.isEmpty(
                getApplication().getExternalCacheDir().getAbsolutePath())) {
            DOWNLOAD_DIR =
                    getApplication().getExternalCacheDir().getAbsolutePath() + File.separator + DIR_NAME;
            final File file = new File(DOWNLOAD_DIR);
            if (!file.exists()) {
                if (!file.mkdir()) {
          /*todo*/
                } else {
          /*todo*/
                }
            } else {

                mPoolExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        listDownloadDirApkTask(file.getAbsolutePath());
                    }
                });
            }
        } else {
      /*todo*/
            stopSelf();
        }
    }

    /**
     * 列出指定目录所有下载好的apk安装包
     *
     * @param path 安装包的目录
     */
    private void listDownloadDirApkTask(final String path) {
        List<File> files = new ArrayList<>();
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            File[] fileList = dir.listFiles();
            if (fileList.length > 0) {
                for (File file : fileList) {
                    if (file.isFile() && file.getName().endsWith(".apk")) {
                        files.add(file);
                    }
                }
                if (files.size() > 0) {
                    List<com.zjmy.signin.utils.update.bean.PackageInfo> packageInfoList = new ArrayList<>();
                    for (File file : files) {
                        packageInfoList.add(
                                PackageUtils.getUninstallAPKInfo(BaseUpdateService.this, file.getAbsolutePath()));
                    }
                    if (packageInfoList.size() > 0) {
                        List<com.zjmy.signin.utils.update.bean.PackageInfo> samePackageList = new ArrayList<>();
                        for (com.zjmy.signin.utils.update.bean.PackageInfo info : packageInfoList) {
                            if (getPackageName().equals(info.packageName)) {
                                if (PackageUtils.getPackageInfo(BaseUpdateService.this).versionCode
                                        < info.versionCode) {
                                    samePackageList.add(info);
                                }
                                continue;
                            }
                        }
                        if (samePackageList.size() > 0) {
                            int versionCode = 0;
                            com.zjmy.signin.utils.update.bean.PackageInfo packageInfo = null;
                            for (com.zjmy.signin.utils.update.bean.PackageInfo info : samePackageList) {
                                if (info.versionCode > versionCode) {
                                    versionCode = info.versionCode;
                                    packageInfo = info;
                                }
                            }
                            if (packageInfo != null) {
                                mDirCacheLastPackInfo = packageInfo;
                                for (File file : files) {
                                    if (file.getAbsolutePath().equals(packageInfo.path)) {
                                        continue;
                                    }
                                    file.deleteOnExit();
                                }
                            } else {
                                for (File file : files) {
                                    boolean b = file.delete();
                                }
                            }
                        } else {
                            for (File file : files) {
                                boolean b = file.delete();
                            }
                        }
                    }
                }
            } else {
        /*todo*/
            }
        } else {
      /*todo*/
        }
    }

    private void initConnectManager() {
        mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }

    private void initNotification() {
        mBuilder = new NotificationCompat.Builder(this).setSmallIcon(
                getApplication().getApplicationInfo().icon)
                .setOngoing(true)
                .setContentText("正在更新" + getString(getApplicationInfo().labelRes))
                .setContentTitle(getString(getApplicationInfo().labelRes));
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void setupFirTask(final Intent intent) {
        downloadAllNetStatus = intent.getBooleanExtra("download_all_net_status", false);
        if (downloadAllNetStatus) {
            if (mPoolExecutor != null && !mPoolExecutor.isShutdown()) {
                mPoolExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        setupFir(intent);
                    }
                });
            } else {
                stopSelf();
            }
        } else {
            if (NetUtils.getConnectedType(mConnectivityManager) == ConnectivityManager.TYPE_WIFI) {
                if (mPoolExecutor != null && !mPoolExecutor.isShutdown()) {
                    mPoolExecutor.submit(new Runnable() {
                        @Override
                        public void run() {
                            setupFir(intent);
                        }
                    });
                } else {
                    stopSelf();
                }
            } else {
                stopSelf();
            }
        }
    }

    private void setupFir(final Intent intent) {

        String apiToken = intent.getStringExtra("api_token");
        if (TextUtils.isEmpty(apiToken)) {
            stopSelf();
        }
        FIR.checkForUpdateInFIR(apiToken, new VersionCheckCallback() {
            @Override
            public void onSuccess(String s) {
                try {
                    isNewVersion(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                    stopSelf();
                }
            }

            @Override
            public void onFail(Exception e) {
                mRetryTime++;
                if (mRetryTime > BuildConfig.MAX_RETRY_TIME) {
                    stopSelf();
                } else {
                    setupFir(intent);
                }
            }
        });
    }

    private void isNewVersion(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        FirVersionInfo info = CommonUtils.parseJsonToObj(jsonObject);
        if (mDirCacheLastPackInfo != null) {
            if (mDirCacheLastPackInfo.versionCode >= Integer.parseInt(info.versionCode)) {
                if (Math.abs(new File(mDirCacheLastPackInfo.path).length() - info.fileSize) < 500) {
                    Dialog dialog = setInstallTipsDialog(info, mDirCacheLastPackInfo.path);
                    if (dialog == null) {
                        throw new NullPointerException("dialog is empty");
                    } else {
                       /* if (Build.VERSION.SDK_INT >= 19) {
                            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
                        } else {
                            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        }*/
                        dialog.show();
                    }
                } else {
                    File tempFile = new File(mDirCacheLastPackInfo.path);
                    if (tempFile.exists()) tempFile.delete();
                    PackageInfo current = PackageUtils.getPackageInfo(this);
                    if (current.versionCode < Integer.parseInt(info.versionCode)) {
                        showNewVersion(info);
                    } else {
                        //showLeastVersion(info);
                    }
                }
            } else {
                File tempFile = new File(mDirCacheLastPackInfo.path);
                if (tempFile.exists()) tempFile.delete();
                PackageInfo current = PackageUtils.getPackageInfo(this);
                if (current.versionCode < Integer.parseInt(info.versionCode)) {
                    showNewVersion(info);
                } else {
                    //showLeastVersion(info);
                }
            }
        } else {
            PackageInfo current = PackageUtils.getPackageInfo(this);
            if (current.versionCode < Integer.parseInt(info.versionCode)) {
                showNewVersion(info);
            } else {
                //showLeastVersion(info);
            }
        }
    }

    private void showLeastVersion(FirVersionInfo info) {

        //final android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog_AppCompat_Dialog);
        final AlertDialog.Builder builder = MainActivity.getBuilder();
        builder.setPositiveButton(getResources().getString(R.string.close), (DialogInterface dialogInterface, int i) -> {
            stopSelf();
            onCancelDownloadClick(null, dialogInterface);
        })
                .setTitle(info.appName
                        + "(" + info.versionName
                        + "."
                        + info.versionCode
                        + ")  已是新版本")
                .setIcon(getApplicationInfo().icon)
                .setMessage("更新日志(" + info.updateDate + "):\n\n"
                        + info.changeLog
                        + "\n");


        final Dialog dialog = builder.create();
        if (dialog == null) throw new NullPointerException("Dialog Must Not be NULL!");

        // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
       /* if (Build.VERSION.SDK_INT >= 19) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }*/
        dialog.show();

    }

    private void showNewVersion(FirVersionInfo info) {
        final Dialog dialog = setDialogContent(info);
        if (dialog == null) throw new NullPointerException("Dialog Must Not be NULL!");
        /*if (Build.VERSION.SDK_INT >= 19) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }*/
        dialog.show();
    }

    protected abstract Dialog setInstallTipsDialog(final FirVersionInfo info,
                                                   final String installApkPath);

    protected final void onInstallClick(Dialog dialog, DialogInterface dialogInterface, File file) {
        if (dialog != null) {
            dialog.dismiss();
        } else if (dialogInterface != null) {
            dialogInterface.dismiss();
        } else {
            throw new IllegalArgumentException("dialog and dialogInterface Not be NULL both!");
        }
        PackageUtils.installAPK(this, file);
        stopSelf();
    }

    protected final void onCancelInstallClick(Dialog dialog, DialogInterface dialogInterface) {
        if (dialog != null) {
            dialog.dismiss();
        } else if (dialogInterface != null) {
            dialogInterface.dismiss();
        } else {
            throw new IllegalArgumentException("dialog and dialogInterface Not be NULL both!");
        }
        stopSelf();
    }

    protected abstract Dialog setDialogContent(final FirVersionInfo info);

    protected final void onDownloadClick(Dialog dialog, DialogInterface dialogInterface,
                                         final FirVersionInfo info) {
        if (dialog != null) {
            dialog.dismiss();
        } else if (dialogInterface != null) {
            dialogInterface.dismiss();
        } else {
            throw new IllegalArgumentException("dialog and dialogInterface Not be NULL both!");
        }
        if (mDownloadTask == null) {
            mDownloadTask = new DownloadTask(getOkHttpClient(), info.downloadUrl, DOWNLOAD_DIR);
        }
        if (mDownloadTask.getStatus() == AsyncTask.Status.RUNNING) {
            if (mDownloadTask.getNetUrl().equals(info.downloadUrl)) {
                return;
            } else {
                mDownloadTask.cancel(true);
                mDownloadTask.setCanceledCallback(new DownloadTaskCanceledCallback() {
                    @Override
                    public void onCanceledCallback() {
                        mDownloadTask = null;
                        mDownloadTask = new DownloadTask(getOkHttpClient(), info.downloadUrl, DOWNLOAD_DIR);
                        mDownloadTask.setupProgressCallBack(BaseUpdateService.this);
                        startForeground(NOTIFICATION_ID, mBuilder.build());
                        mDownloadTask.execute();
                    }
                });
            }
        } else {
            mDownloadTask = null;
            mDownloadTask = new DownloadTask(getOkHttpClient(), info.downloadUrl, DOWNLOAD_DIR);
            mDownloadTask.setupProgressCallBack(BaseUpdateService.this);
            startForeground(NOTIFICATION_ID, mBuilder.build());
            mDownloadTask.execute();
        }
    }

    protected final void onCancelDownloadClick(Dialog dialog, DialogInterface dialogInterface) {
        if (dialog != null) {
            dialog.dismiss();
            stopSelf();
        } else if (dialogInterface != null) {
            dialogInterface.dismiss();
            stopSelf();
        } else {
            throw new IllegalArgumentException("dialog and dialogInterface Not be NULL both!");
        }
    }
}
