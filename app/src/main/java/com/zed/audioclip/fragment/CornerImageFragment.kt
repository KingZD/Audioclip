package com.zed.audioclip.fragment

import com.bytc.qudong.control.fragment.UIHomeConstraint
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.zed.audioclip.R
import com.zed.audioclip.base.BaseFragment
import com.zed.audioclip.presenter.HomePresenter
import com.zed.view.XRecyclerView
import kotlinx.android.synthetic.main.fragment_corner_image.*

/**
 * @author zd
 * @package com.zed.audioclip.activity.fragment
 * @fileName CornerImageFragment
 * @date on 2017/12/15 0015 14:55
 * @org 湖北博娱天成科技有限公司
 * @describe TODO
 * @email 1053834336@qq.com
 */
class CornerImageFragment : BaseFragment(), UIHomeConstraint {
    override fun getRlView(): XRecyclerView? {
        return rv
    }

    override fun getSmartPull(): SmartRefreshLayout? {
        return smartPull
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_corner_image
    }

    override fun initView() {
        setPresenter(HomePresenter(this))
    }

}