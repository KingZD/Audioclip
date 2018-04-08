package com.zed.audioclip.adapter.holder

import android.view.View
import com.zed.audioclip.base.IBaseHolderConstruction
import com.zed.audioclip.net.bean.SongBean
import com.zed.audioclip.util.MusicUtil
import kotlinx.android.synthetic.main.item_home_v1.view.*

/**
 * Created by zed on 2018/4/2.
 */
class HomeHolder : BaseHolder, IBaseHolderConstruction<SongBean> {

    constructor(itemView: View) : super(itemView)

    override fun init(bean: SongBean?) {
        itemView.ivMusicImg.setImageBitmap(MusicUtil.createAlbumArt(bean?.path))
        itemView.tvMusicName.text = bean?.song
        itemView.tvMusicAuthor.text = "演唱者:".plus(bean?.singer)
        itemView.tvMusicSize.text = "时长:".plus(bean?.duration?.div(1000))
        itemView.tvMusicType.text = "类型:".plus(bean?.prefix)
    }
}