package vip.mengqin.diandezhun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.util.GlideUtil;
import com.android.baselibrary.view.RoundImageView;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.util.List;

import vip.mengqin.diandezhun.R;
import vip.mengqin.diandezhun.bean.BannerInfo;

/**
 * 默认实现的图片适配器，图片加载需要自己实现
 */
public class MyBannerImageAdapter extends BannerAdapter<BannerInfo, BannerImageHolder> {

    CommCallBack callBack;
    Context context;

    public MyBannerImageAdapter(Context context, List<BannerInfo> mData, CommCallBack callBack) {
        super(mData);
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    public void onBindView(BannerImageHolder holder, BannerInfo data, int position, int size) {
        GlideUtil.displayImage(holder.itemView.getContext(), data.fileUrl, holder.imageView, R.drawable.ico_default_banner);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    String uid = UserManager.getUserInfo(MyApplication.context).id;
//                    String url = data.jumpUrl + "?uid=" + uid + "&pid=" + data.pid;
//                    Intent intent = new Intent(holder.itemView.getContext(), WebViewActivity.class);
//                    intent.putExtra("url", url);
//                    holder.itemView.getContext().startActivity(intent);

//                if (!TextUtils.isEmpty(data.AndroidUrl)) {
//                    RouterUtil.startActivity(context, data.AndroidUrl);
//                } else if (!TextUtils.isEmpty(data.url)) {
//                    WebViewActivity.startActivity(context, "", data.url);
//                }
            }
        });
    }

    @Override
    public BannerImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        RoundImageView imageView = new RoundImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        imageView.currMode = RoundImageView.MODE_ROUND;
//        imageView.currRound = Util.dip2px(parent.getContext(), 8);
        return new BannerImageHolder(imageView);
    }

}
