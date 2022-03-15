package com.devices.touchscreen.ui

import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ClickUtils
import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseVmActivity
import com.devices.touchscreen.common.ActivityHelper
import com.devices.touchscreen.common.BusHelper
import com.devices.touchscreen.viewmodel.MainViewModel
import com.lake.banner.*
import com.lake.banner.loader.ViewItemBean
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : BaseVmActivity<MainViewModel>(R.layout.activity_main) {
    override fun viewModelClass() = MainViewModel::class.java

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
        clParent.setOnClickListener(object : ClickUtils.OnMultiClickListener(10) {
            override fun onTriggerClick(v: View?) {
                ActivityHelper.startActivity(ConfigActivity::class.java)
            }

            override fun onBeforeTriggerClick(v: View?, count: Int) {}

        })

        tvToQCode.setOnClickListener {
            tvComplaints.animate().x((-tvComplaints.width).toFloat()).setDuration(1000).start()
            tvEvaluation.animate().x(window.decorView.width.toFloat()).setDuration(1000).start()
            ivQrCode.animate().alpha(1f).setDuration(600).start()
            tvTips.animate().alpha(1f).setDuration(600).start()
            ivBack.isVisible = true
            tvToQCode.isVisible = false
        }

        ivBack.setOnClickListener {
            tvComplaints.animate().x(locationLeft[0].toFloat()).setDuration(1000).start()
            tvEvaluation.animate().x(locationRight[0].toFloat()).setDuration(1000).start()
            ivQrCode.animate().alpha(0f).setDuration(600).start()
            tvTips.animate().alpha(0f).setDuration(600).start()
            ivBack.isVisible = false
            tvToQCode.isVisible = true
        }

    }

    val locationLeft = IntArray(2)
    val locationRight = IntArray(2)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        tvComplaints.getLocationOnScreen(locationLeft)
        tvEvaluation.getLocationOnScreen(locationRight)
    }

    override fun initData() {
        super.initData()
        mViewModel.getInfo()
    }

    override fun observe() {
        super.observe()
        BusHelper.observe<Boolean>("BannerUpdate", this) {
            initData()
        }
        mViewModel.run {
            restInfoResult.observe(this@MainActivity) {
                tvName.text = "${it.restName}（${it.directionAliasName}）\n 欢迎您！"
            }
            publicPagePhoto.observe(this@MainActivity) { bean ->
                list.clear()
                bean.publicFrontPageList?.forEach { url ->
                    if (url.endsWith(".mp4") || url.endsWith(".MP4")) {
                        list.add(ViewItemBean(BannerConfig.VIDEO, null, Uri.parse(url), BannerConfig.MAXTIME))
                    } else {
                        list.add(ViewItemBean(BannerConfig.IMAGE, null, Uri.parse(url), BannerConfig.TIME))
                    }
                }
                banner.update(list)

                tvComplaints.setOnClickListener { ActivityHelper.startActivity(ComplaintsActivity::class.java, mapOf("BannerUrl" to bean.inPage, "Type" to 1)) }
                tvEvaluation.setOnClickListener { ActivityHelper.startActivity(ComplaintsActivity::class.java, mapOf("BannerUrl" to bean.inPage, "Type" to 2)) }
            }
        }
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