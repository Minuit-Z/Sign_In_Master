package com.zjmy.signin.utils.update.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.zjmy.signin.BuildConfig;

import java.io.File;


/**
 * Create on: 2016-07-28
 * Author: wangh
 * Summary: TODO
 */
public class PackageUtils {

  /**
   * 获取未安装APK信息
   *
   * @param context 上下文
   * @param path 安装包路径
   * @return 返回PackageInfo对象
   */
  public static com.zjmy.signin.utils.update.bean.PackageInfo getUninstallAPKInfo(Context context,
      String path) {
    com.zjmy.signin.utils.update.bean.PackageInfo packageInfo =
        new com.zjmy.signin.utils.update.bean.PackageInfo();
    PackageManager packageManager = context.getPackageManager();
    PackageInfo info = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
    try {
      packageInfo.packageName = info.packageName;
      packageInfo.versionName = info.versionName;
      packageInfo.versionCode = info.versionCode;
      packageInfo.path = path;
    } catch (NullPointerException e) {
      e.printStackTrace();
      packageInfo = null;
    }

    return packageInfo;
  }

  public static String getMetaDataFromApplication(Context context, String metaName) {
    String metaData = null;
    ApplicationInfo applicationInfo = null;
    try {
      applicationInfo = context.getPackageManager()
          .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      metaData = applicationInfo.metaData.getString(metaName);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      metaData = null;
    }
    return metaData;
  }

  public static PackageInfo getPackageInfo(Context context) {
    PackageInfo info = new PackageInfo();
    PackageInfo packageInfo = null;
    try {
      packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      info.packageName = context.getPackageName();
      info.versionCode = packageInfo.versionCode;
      info.versionName = packageInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return info;
  }

  public static void installAPK(Context context, String apkPath) {
    installAPK(context, new File(apkPath));
  }

  public static void installAPK(Context context, File apkPath) {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    if (Build.VERSION.SDK_INT > 23) {
      intent.setDataAndType(FileProvider.getUriForFile(context,
          context.getApplicationContext().getPackageName() + BuildConfig.PROVIDER_AUTH_END_STUFF,
          apkPath), BuildConfig.ARCHIVE_MIME_TYPE_APK);
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    } else {
      intent.setDataAndType(Uri.parse(BuildConfig.FILE_START_STUFF + apkPath.getAbsolutePath()),
          BuildConfig.ARCHIVE_MIME_TYPE_APK);
    }
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
