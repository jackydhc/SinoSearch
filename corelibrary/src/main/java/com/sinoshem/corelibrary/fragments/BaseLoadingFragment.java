package com.sinoshem.corelibrary.fragments;

import com.sinoshem.corelibrary.mvp.IShowLoading;

/**
 * @author jackydu
 * @date 2018/8/23
 */
public class BaseLoadingFragment extends  LazyFragment implements IShowLoading{

    @Override
    protected void loadData() {

    }

    @Override
    public int provideLayoutId() {
        return 0;
    }

    @Override
    protected void initOnCreateView() {

    }

    @Override
    public void showLoading(boolean show) {

    }
}
