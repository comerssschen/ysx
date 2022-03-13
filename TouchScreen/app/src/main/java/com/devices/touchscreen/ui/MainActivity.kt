package com.devices.touchscreen.ui

import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.view.View
import com.blankj.utilcode.util.ClickUtils
import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseActivity
import com.devices.touchscreen.common.ActivityHelper
import com.lake.banner.*
import com.lake.banner.loader.ViewItemBean
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : BaseActivity(R.layout.activity_main) {

    val list: MutableList<ViewItemBean> = ArrayList()
    override fun initView() {
        super.initView()
        banner.setViews(list)
            .setBannerAnimation(Transformer.Default) //换场方式
            .setBannerStyle(BannerStyle.CIRCLE_INDICATOR_TITLE) //指示器模式
            .setCache(true) //可以不用设置，默认为true
            .setCachePath(getExternalFilesDir(Environment.DIRECTORY_MOVIES)!!.absolutePath.toString() + File.separator + "hbanner")
            .setVideoGravity(VideoGravityType.FULL_SCREEN) //视频布局方式
            .setImageGravity(ImageGravityType.FIT_XY) //图片布局方式
            .setPageBackgroundColor(Color.TRANSPARENT) //设置背景
            .setShowTitle(false) //是否显示标题
            .setViewPagerIsScroll(false) //是否支持手滑
            .start()

        list.add(ViewItemBean(BannerConfig.VIDEO, null, Uri.parse("http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4"), BannerConfig.MAXTIME))
        list.add(ViewItemBean(BannerConfig.IMAGE, null, Uri.parse("https://model-player.oss-cn-beijing.aliyuncs.com/bg_banner_blue.png"), BannerConfig.TIME))
        list.add(ViewItemBean(BannerConfig.IMAGE, null, Uri.parse("https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg"), BannerConfig.TIME))
        list.add(ViewItemBean(BannerConfig.IMAGE, null, Uri.parse("https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"), BannerConfig.TIME))
        list.add(ViewItemBean(BannerConfig.VIDEO, null, Uri.parse("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319125415785691.mp4"), BannerConfig.MAXTIME))
        list.add(ViewItemBean(BannerConfig.VIDEO, null, Uri.parse("http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4"), BannerConfig.MAXTIME))
        list.add(ViewItemBean(BannerConfig.IMAGE, null, Uri.parse("https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"), BannerConfig.TIME))
        list.add(ViewItemBean(BannerConfig.VIDEO, null, Uri.parse("http://vfx.mtime.cn/Video/2019/03/14/mp4/190314102306987969.mp4"), BannerConfig.MAXTIME))
        banner.update(list)

        btJump.setOnClickListener { ActivityHelper.startActivity(ConfigActivity::class.java) }

        clParent.setOnClickListener(object : ClickUtils.OnMultiClickListener(5) {
            override fun onTriggerClick(v: View?) {
                ActivityHelper.startActivity(ConfigActivity::class.java)
            }

            override fun onBeforeTriggerClick(v: View?, count: Int) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        banner.onResume()
    }

    override fun onPause() {
        banner.onPause()
        super.onPause()
    }

    override fun onStop() {
        banner.onStop()
        super.onStop()
    }


}