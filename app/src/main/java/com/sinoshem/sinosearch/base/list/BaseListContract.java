package com.sinoshem.sinosearch.base.list;

import com.sinoshem.corelibrary.mvp.MVPPresenter;
import com.sinoshem.corelibrary.mvp.MvpView;

import java.util.List;

/**
 * author: jackydu
 * date: on 17/1/4 16:12
 * description:
 */

public interface BaseListContract {

    interface IListMvpView<C extends List> extends MvpView {
        void showEmptyPage(String emptyInfo);

        void showErrorPage(String errorInfo);

        void showContent(C data, boolean refresh);
    }

    abstract class IRefreshPresenter<C extends List> extends MVPPresenter<IListMvpView<C>> implements IRefreshLoadMore {

    }

}
