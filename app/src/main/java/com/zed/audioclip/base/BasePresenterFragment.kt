package com.zed.audioclip.base

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jaeger.library.StatusBarUtil
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import com.zed.audioclip.control.activity.UIActivityConstraint
import com.zed.common.util.UiUtil

/**
 * @author zd
 * @fileName BaseFragment
 * @date on 2017/12/1 0001 11:46
 * @org 湖北博娱天成科技有限公司
 * @describe TODO
 * @email 1053834336@qq.com
 */
abstract class BasePresenterFragment<in T : BasePresenter<*, *>> : RxFragment(), UIActivityConstraint {
    private var presenter: T? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UiUtil.inflate(context, setLayoutId(), container, false)
    }

    fun setPresenter(presenter: T) {
        this.presenter = presenter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun clickTitleLeft() {
    }

    override fun clickTitleRight() {
    }

    override fun clickTitle() {
    }

    override fun <T> getHttpLifeRecycle(): LifecycleTransformer<T> {
        return this.bindUntilEvent(FragmentEvent.STOP)
    }


    fun setStatusBarColor(color: Int) {
        StatusBarUtil.setColorNoTranslucent(
                activity, ContextCompat.getColor(context!!, color))
    }

    //沉浸式通知栏
    fun setStatusBarImmersive(viewNeedOffset: View?) {
        StatusBarUtil.setTransparentForImageView(activity, viewNeedOffset)
    }

    fun setStatusBarImmersiveInCoordinatorLayout() {
        StatusBarUtil.setTranslucentForCoordinatorLayout(activity, 0)
    }

    override fun onDestroyView() {
        presenter?.onDestory()
        presenter = null
        super.onDestroyView()
    }
}