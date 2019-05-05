package us.xingkong.retrofitrealmpicload.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import us.xingkong.retrofitrealmpicload.R;

import static us.xingkong.retrofitrealmpicload.bean.AppInfo.appInfo;

/**
 * Created by Arcobaleno on 2017/7/9.
 */

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.ViewHolder> {

    private int itemCount = 0;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView appIcon;
        TextView appCount;
        TextView appType;
        TextView appName;
        TextView appBundleId;
        TextView appReleaseVersion;

        ViewHolder(View itemView) {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.appImage);
            appCount = (TextView) itemView.findViewById(R.id.appCount);
            appType = (TextView) itemView.findViewById(R.id.appType);
            appName = (TextView) itemView.findViewById(R.id.appName);
            appBundleId = (TextView) itemView.findViewById(R.id.appBundleId);
            appReleaseVersion = (TextView) itemView.findViewById(R.id.appReleaseVersion);
//            Log.i("adapterViewAlready---", "true");
        }
    }

    public AppInfoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appinfo_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            new GlideLoadPic(context, holder.appIcon, appInfo.getItems().get(position).getIcon_url(), 0).LoadPic();
//            Log.i("IconUrl---", appInfo.getItems().get(position).getIcon_url() + "");
            holder.appCount.setText(context.getText(R.string.AppCount)+":"+String.valueOf((position + 1)));
            holder.appType.setText(context.getText(R.string.AppType)+":"+String.valueOf(appInfo.getItems().get(position).getType()));
            holder.appName.setText(context.getText(R.string.AppName)+":"+String.valueOf(appInfo.getItems().get(position).getName()));
            holder.appBundleId.setText(context.getText(R.string.AppBundleId)+":"+String.valueOf(appInfo.getItems().get(position).getBundle_id()));
            holder.appReleaseVersion.setText(context.getText(R.string.AppReleaseVersion)+":"+String.valueOf(appInfo.getItems().get(position).getMaster_release().getVersion()));
        } catch (NullPointerException e) {
            new RetrofitLoadAppInfo(context).call();
        }
//        Log.i("adapterAlready---", "true");
    }

    @Override
    public int getItemCount() {
        if (appInfo != null) {
            itemCount = appInfo.getApps_count();
//            Log.i("itemCountThread---", "" + itemCount);
        }

//        Log.i("itemCount---", "" + itemCount);
//        System.out.println("GIC");

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                boolean isShow = false;
//                while (!isShow) {
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (appInfo != null) {
//                         itemCount = appInfo.getApps_count();
//                        Log.i("itemCountThread---", "" + itemCount);
//                        isShow = true;
//                    }
//                }
//            }
//        }).start();
        return itemCount;
    }

}
