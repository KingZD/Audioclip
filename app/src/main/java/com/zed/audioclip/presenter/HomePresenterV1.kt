package com.zed.audioclip.presenter

import android.Manifest
import android.support.v7.widget.LinearLayoutManager
import com.bytc.qudong.control.fragment.UIHomeConstraint
import com.google.gson.Gson
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zed.audioclip.adapter.HomeAdapter
import com.zed.audioclip.base.BasePresenter
import com.zed.audioclip.net.bean.SongBean
import com.zed.audioclip.net.bean.HomeBean
import com.zed.audioclip.util.MusicUtil
import com.zed.common.util.LogUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author zd
 * @package com.bytc.qudong.presenter
 * @fileName HomePresenter
 * @date on 2017/12/1 0001 16:10
 * @org 湖北博娱天成科技有限公司
 * @describe TODO
 * @email 1053834336@qq.com
 */
class HomePresenterV1(view: UIHomeConstraint) : BasePresenter<UIHomeConstraint, HomeBean>(view) {
    var adapter: HomeAdapter? = null
    var songs: MutableList<SongBean>? = null

    override fun init() {
        songs = arrayListOf()
        adapter = HomeAdapter(mActivity, songs)
        getView()?.getRlView()?.layoutManager = LinearLayoutManager(mActivity)
        getView()?.getRlView()?.adapter = adapter
        RxPermissions(mActivity!!)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe({
                    if (it) {
                        scanMusic()
                    }
                })
    }

    /**
     * 扫描音乐
     */
    private fun scanMusic() {
        Observable
                .create(ObservableOnSubscribe<List<SongBean>> {
                    it.onNext(MusicUtil.getMusicData(mActivity))
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    LogUtils.i("Music", "Music size is ".plus(it.size))
                    songs?.addAll(it)
                    adapter?.notifyDataSetChanged()
                })
    }
}