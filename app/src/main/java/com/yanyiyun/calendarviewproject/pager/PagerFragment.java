package com.yanyiyun.calendarviewproject.pager;

import android.support.v7.widget.LinearLayoutManager;

import com.yanyiyun.calendarviewproject.Article;
import com.yanyiyun.calendarviewproject.ArticleAdapter;
import com.yanyiyun.calendarviewproject.R;
import com.yanyiyun.calendarviewproject.base.fragment.BaseFragment;
import com.yanyiyun.calendarviewproject.group.GroupItemDecoration;
import com.yanyiyun.calendarviewproject.group.GroupRecyclerView;

public class PagerFragment extends BaseFragment {

    private GroupRecyclerView mRecyclerView;


    public static PagerFragment newInstance() {
        return new PagerFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pager;
    }

    @Override
    protected void initView() {
        mRecyclerView = (GroupRecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.setAdapter(new ArticleAdapter(mContext));
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    public boolean isScrollTop() {
        return mRecyclerView != null && mRecyclerView.computeVerticalScrollOffset() == 0;
    }
}
