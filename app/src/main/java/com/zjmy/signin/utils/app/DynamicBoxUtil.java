package com.zjmy.signin.utils.app;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zjmy.signin.R;

import mehdi.sakout.dynamicbox.DynamicBox;


/**
 * @Description:数据加载
 * @authors: utopia
 * @Create time: 17-1-6 下午2:23
 * @Update time: 17-1-6 下午2:23
 */
public class DynamicBoxUtil {
    public static final String emptyView = "empty";
    public static DynamicBox box;

    public static DynamicBox newInstance(Activity context, View view, String... param) {
        box = new DynamicBox(context, view);
        box.setLoadingMessage(context.getResources().getString(R.string.load_ing));
        box.setOtherExceptionTitle(context.getResources().getString(R.string.load_error));
        box.setOtherExceptionMessage(context.getResources().getString(R.string.msg_read_detail_fail));
        View emptyCollectionView = context.getLayoutInflater().inflate(R.layout.empty_listview_layout, null, false);
        TextView content = (TextView) emptyCollectionView.findViewById(R.id.title);
        if (param != null && param.length > 0) {
            content.setText(param[0]);
        }
        box.addCustomView(emptyCollectionView, emptyView);
        return box;
    }

}
