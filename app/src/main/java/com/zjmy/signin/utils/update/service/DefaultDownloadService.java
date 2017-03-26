package com.zjmy.signin.utils.update.service;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.zjmy.signin.R;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.utils.update.bean.FirVersionInfo;
import com.zjmy.signin.utils.update.utils.CommonUtils;

import java.io.File;


/**
 * Create on: 2016-07-31
 * Author: wangh
 * Summary: TODO
 */
public class DefaultDownloadService extends BaseUpdateService {
    @Override
    protected Dialog setInstallTipsDialog(FirVersionInfo info, final String installApkPath) {
        final AlertDialog.Builder builder = MainActivity.getBuilder();
        builder.setNegativeButton(getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onCancelInstallClick(null, dialogInterface);
            }
        }).setPositiveButton(getResources().getString(R.string.install), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onInstallClick(null, dialogInterface, new File(installApkPath));
            }
        }).setTitle(info.appName).setIcon(getApplicationInfo().icon).setMessage(getResources().getString(R.string.is_immediately_install));

        return builder.create();
    }

    @Override
    protected Dialog setDialogContent(final FirVersionInfo info) {
        final AlertDialog.Builder builder = MainActivity.getBuilder();
        builder.setNegativeButton(getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onCancelDownloadClick(null, dialogInterface);
            }
        })
                .setPositiveButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDownloadClick(null, dialogInterface, info);
                    }
                })
                .setTitle(info.appName + getResources().getString(R.string.find_new_version))
                .setIcon(getApplicationInfo().icon)
                .setMessage("更新日期:"
                        + info.updateDate
                        + "\n"
                        + "更新日志:\n"
                        + info.changeLog
                        + "\n"
                        + "版本: v"
                        + info.versionName
                        + "."
                        + info.versionCode
                        + "\n安装包大小:"
                        + CommonUtils.bytes2kb(info.fileSize));
        return builder.create();
    }
}
