package com.zed.audioclip.activity

import android.view.View
import com.zed.audioclip.R
import com.zed.audioclip.base.BaseActivity
import com.zed.audioclip.fragment.HomeFragment
import com.zed.audioclip.fragment.HomeFragmentV1
import com.zed.audioclip.fragment.SecondFragment
import com.zed.audioclip.fragment.ThirdFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author zd
 * @package com.zed.audioclip.activity
 * @fileName MainActivity
 * @date on 2017/11/28 15:57
 * @org 湖北博娱天成科技有限公司
 * @describe TODO
 * @email 1053834336@qq.com
 */
class MainActivity : BaseActivity() {

    //"同一模块下可见"
    private var homeFragment: HomeFragmentV1? = null
    private var secondFragment: SecondFragment? = null
    private var thirdFragment: ThirdFragment? = null
    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        selectHomeTab(R.id.rbOne)
    }

    fun showFragment(view: View) {
        selectHomeTab(view.id)
    }

    fun selectHomeTab(checkedId: Int) {
        val oCid = rgTab.tag ?: -1

        if (checkedId == R.id.rbOne && oCid != R.id.rbOne) {
            homeFragment = homeFragment ?: HomeFragmentV1()
            repalce(homeFragment)
        } else if (checkedId == R.id.rbTwo && oCid != R.id.rbTwo) {
            secondFragment = secondFragment ?: SecondFragment()
            repalce(secondFragment)
        } else if (checkedId == R.id.rbThree && oCid != R.id.rbThree) {
            thirdFragment = thirdFragment ?: ThirdFragment()
            repalce(thirdFragment)
        } else if (checkedId == R.id.rbFour && oCid != R.id.rbFour) {
            homeFragment = homeFragment ?: HomeFragmentV1()
            repalce(homeFragment)
        } else if (checkedId == R.id.rbFive && oCid != R.id.rbFive) {
            homeFragment = homeFragment ?: HomeFragmentV1()
            repalce(homeFragment)
        }
        //默认设置选中的viewtag
        rgTab.tag = checkedId
        rgTab.check(checkedId)
    }
}
