package com.sinoshem.sinosearch.base.list;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.sinoshem.corelibrary.fragments.BaseFragment;
import com.sinoshem.corelibrary.api.ApiConstants;
import com.sinoshem.corelibrary.weidgets.LoadFrameLayout;
import com.sinoshem.sinosearch.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * @author jackydu
 * @date 2018/8/21
 */
public abstract class BaseListFragment<E, P extends BaseListContract.IRefreshPresenter<List<E>>> extends BaseFragment implements BaseListContract.IListMvpView<List<E>> {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ptr)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.load_frameLayout)
    LoadFrameLayout loadFrameLayout;
    private P mPresent;
    private BaseQuickAdapter<E,BaseViewHolder> mAdapter;

    @Override
    public int provideLayoutId() {
        return R.layout.layout_base_list;
    }

    protected abstract BaseQuickAdapter<E,BaseViewHolder> provideAdapter();
    protected abstract P providePresent();

    @Override
    protected void initOnCreateView() {
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = provideAdapter();
        recycler.setAdapter(mAdapter);
        recycler.addItemDecoration(getDefaultItemDecoration());
        mPresent = providePresent();
        mPresent.attachView(this);
        if (mAdapter.isLoadMoreEnable()) mAdapter.setOnLoadMoreListener(mPresent,recycler);
        refreshLayout.setOnRefreshListener(mPresent);
    }

    public RecyclerView.ItemDecoration getDefaultItemDecoration() {
        return new HorizontalDividerItemDecoration.Builder(recycler.getContext())
                .colorResId(R.color.default_divider_color)
                .sizeResId(R.dimen.default_divider_height)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresent.detachView();
    }
    @Override public void showEmptyPage(String emptyInfo) {
        if (mAdapter == null) {
            return;
        }
        if (TextUtils.isEmpty(emptyInfo)) {
            emptyInfo = ApiConstants.EMPTY_INFO;
        }
        loadFrameLayout.setEmptyText(emptyInfo);
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override public void showErrorPage(String errorInfo) {
        if (mAdapter == null) {
            return;
        }
        loadFrameLayout.setErrorText(errorInfo);
        mAdapter.notifyDataSetChanged();
    }

    @Override public void showContent(List<E> data, boolean refresh) {
        if (refresh) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }

    public void smoothScrollToTop() {
        if (recycler != null) {
            recycler.smoothScrollToPosition(0);
        }
    }

    @Override
    public void showLoading(boolean show) {
//        if (show) loadFrameLayout.bind(LoadFrameLayout.LOADING);
    }
}
