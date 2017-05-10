package sg.com.comnet.emotionbubbles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by JCB on 10-May-2017.
 */

/*
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private Context mContext;
    private View mFootView;
    private int mTotalItemCount;
    private OnLoadMoreListener mLoadMoreListener;
    private boolean mIsLoading=false;

    public LoadMoreListView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        this.mContext=context;
        mFootView= LayoutInflater.from(context).inflate(R.layout.foot_view,null);
        setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        int lastVisibleIndex=listView.getLastVisiblePosition();
        if (!mIsLoading&&scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex ==mTotalItemCount-1) {
            mIsLoading=true;
            addFooterView(mFootView);
            if (mLoadMoreListener!=null) {
                mLoadMoreListener.onloadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount=totalItemCount;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        mLoadMoreListener=listener;
    }

    public interface OnLoadMoreListener{
        void onloadMore();
    }
    public void setLoadCompleted(){
        mIsLoading=false;
        removeFooterView(mFootView);
    }
}*/


public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private OnScrollListener mOnScrollListener;
    private LayoutInflater mInflater;

    private LinearLayout mFooterView;
    private TextView mTextLoadMore;
    private ProgressBar mProgressBarLoadMore;

    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean mIsLoadingMore = false;
    private boolean mCanLoadMore = true;
    private int mCurrentScrollState;

    public LoadMoreListView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFooterView = (LinearLayout) mInflater.inflate(R.layout.foot_view, this, false);
        mTextLoadMore = (TextView) mFooterView.findViewById(R.id.textView);
        mProgressBarLoadMore = (ProgressBar) mFooterView.findViewById(R.id.pbar);

        addFooterView(mFooterView);
//        mFooterView.setOnClickListener(this);
        super.setOnScrollListener(this);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if (mOnLoadMoreListener != null) {

            if (visibleItemCount == totalItemCount) {
                mProgressBarLoadMore.setVisibility(View.GONE);
//                mTextLoadMore.setVisibility(View.GONE);
                return;
            }

            boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

            if (!mIsLoadingMore && loadMore && mCurrentScrollState != SCROLL_STATE_IDLE) {
                if (!mCanLoadMore) {
                    mTextLoadMore.setVisibility(View.VISIBLE);
                    return;
                }
                mProgressBarLoadMore.setVisibility(View.VISIBLE);
                mTextLoadMore.setVisibility(View.GONE);
                mIsLoadingMore = true;
                onLoadMore();
            }

        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mCurrentScrollState = scrollState;

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }

    }


    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        mCanLoadMore = canLoadMore;
        if (canLoadMore) {
            mTextLoadMore.setVisibility(View.GONE);
        } else {
            mTextLoadMore.setVisibility(View.VISIBLE);
        }
    }

    private void onLoadMore() {
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
        }
    }

    public void onLoadMoreComplete() {
        mIsLoadingMore = false;
        mProgressBarLoadMore.setVisibility(View.GONE);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

//    @Override
//    public void onClick(View v) {
//
//    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}