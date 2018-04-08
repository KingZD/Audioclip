package com.zed.audioclip.adapter

import android.content.Context
import android.view.ViewGroup
import com.zed.audioclip.R
import com.zed.audioclip.adapter.holder.HomeHolder
import com.zed.audioclip.net.bean.SongBean
import com.zed.common.util.UiUtil

/**
 * Created by zed on 2018/4/2.
 */
class HomeAdapter : BaseAdapter<HomeHolder, SongBean> {

    constructor(mContext: Context?, data: List<SongBean>?) : super(mContext, data)

    override fun onBindViewHolder(holder: HomeHolder?, position: Int) {
        holder?.init(mData?.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeHolder {
        return HomeHolder(UiUtil.inflate(mContext, R.layout.item_home_v1, parent, false))
    }
}