package com.sinoshem.corelibrary.mvp;

/**
 * @author jackydu
 * @date 2018/8/23
 */
public interface IMultistate {
    void showContent();
    void showLoading();
    void showError(String error);
    void showEmpty();
}
