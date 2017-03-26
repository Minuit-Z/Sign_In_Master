package com.zjmy.signin.utils.app;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zjmy.signin.BuildConfig;
import com.zjmy.signin.utils.update.service.BaseUpdateService;
import com.zjmy.signin.utils.update.service.DefaultDownloadService;
import com.zjmy.signin.utils.update.utils.PackageUtils;


/**
 * @Description:软件更新管理器 基于fir.im 配置详情请参见 http://bughd.com/doc/android
 * @authors: utopia
 * @Create time: 17-3-4 下午4:21
 * @Update time: 17-3-4 下午4:21
*/
public class UpdateManager {

  /**
   * 使用此方法<b>请务必在manifests文件中添加如下声明:</b><br><b>"< meta-data android:name="BUG_HD_API_TOKEN"
   * android:value="{apiToken}">"</b>
   *
   * @param context 上下文
   */
  public static void checkUpdate(Context context) {
    checkUpdate(context, false, DefaultDownloadService.class);
  }

  public static void checkUpdate(Context context, boolean downloadAllNewStatus) {
    checkUpdate(context, downloadAllNewStatus, DefaultDownloadService.class);
  }

  public static void checkUpdate(Context context, boolean downloadAllNewStatus,
      Class<? extends BaseUpdateService> serviceClazz) {
    String apiToken = PackageUtils.getMetaDataFromApplication(context, BuildConfig.API_TOKEN);

    if (TextUtils.isEmpty(apiToken)) {
      throw new NullPointerException(
          "请务必在manifests文件中添加如下声明:\n< meta-data android:name=\"BUG_HD_API_TOKEN\"\n"
              + "   * android:value=\"{你的apiToken}\">\"");
    }
    checkUpdate(context, apiToken, downloadAllNewStatus, serviceClazz);
  }

  /**
   * @param context 上下文
   * @param apiToken 查看apiToken <a href="http://fir.im/apps/apitoken">Api Token</a>
   */
  public static void checkUpdate(Context context, String apiToken, boolean downloadAllNewStatus) {
    checkUpdate(context, apiToken, downloadAllNewStatus, DefaultDownloadService.class);
  }

  public static void checkUpdate(Context context, String apiToken, boolean downloadAllNewStatus,
      Class<? extends BaseUpdateService> serviceClazz) {
    Intent intent = new Intent(context, serviceClazz);
    intent.putExtra("api_token", apiToken);
    intent.putExtra("download_all_net_status", downloadAllNewStatus);
    context.startService(intent);
  }
}
