package wowo.kjt.library;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class VpImagesAdapter extends PagerAdapter {

    private ArrayList<ImageView> mViews;
    private onLongClickListener mListener = null;

    public VpImagesAdapter(ArrayList<ImageView> mListViews) {
        mViews = mListViews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));//删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {    //这个方法用来实例化页卡
        container.addView(mViews.get(position));//添加页卡
        mViews.get(position).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null)
                    mListener.onLongClick();
                return false;
            }
        });
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mViews.size();//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;//官方提示这样写
    }

    public void setOnLongClickListener(onLongClickListener listener) {
        mListener = listener;
    }

    public interface onLongClickListener {
        void onLongClick();
    }
}
