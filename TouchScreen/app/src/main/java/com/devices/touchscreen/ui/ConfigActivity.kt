package com.devices.touchscreen.ui

import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseVmActivity
import com.devices.touchscreen.bean.DropDownBean
import com.devices.touchscreen.common.ActivityHelper
import com.devices.touchscreen.common.BusHelper
import com.devices.touchscreen.common.GlideEngine
import com.devices.touchscreen.common.showToast
import com.devices.touchscreen.view.BaseDropDialog
import com.devices.touchscreen.viewmodel.ConfigViewModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import kotlinx.android.synthetic.main.activity_config.*
import java.io.File
import java.util.stream.Collectors

class ConfigActivity : BaseVmActivity<ConfigViewModel>(R.layout.activity_config) {
    override fun viewModelClass() = ConfigViewModel::class.java

    private var mPosition = 0
    private var uploadType = 0//1新增 2 编辑 3 bannner
    private var imageList = ArrayList<String>()
    override fun initView() {
        super.initView()
        ivBanner.setOnClickListener {
            uploadType = 3
            selectFile()
        }
        ivPhoto.setOnClickListener {
            uploadType = 1
            selectFile()
        }
        initRecycler()
        btSubmit.setOnClickListener {
            if (restId.isEmpty() || restDirection.isEmpty()) {
                showToast("请选择服务区")
                return@setOnClickListener
            }
            mViewModel.submitConfig(restId, restDirection, imageList.toTypedArray(), bannerUrl)
        }
    }

    private fun selectFile() {

        val model = PictureSelector.create(this)
            .openGallery(
                if (uploadType == 3) {
                    SelectMimeType.ofImage()
                } else {
                    SelectMimeType.ofAll()
                }
            )

        model.setImageEngine(GlideEngine.createGlideEngine())
            .setSelectionMode(SelectModeConfig.SINGLE)
            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onCancel() {}
                override fun onResult(result: ArrayList<LocalMedia>) {
                    if (result.size != 0) {
                        mViewModel.uploadImage(File(result[0].realPath))
                    }
                }

            })
    }

    override fun initData() {
        super.initData()
        mViewModel.getDropdown()
        tvRestDirection.setOnClickListener {
            if (restId.isEmpty()) {
                showToast("请选择服务区")
            } else {
                mViewModel.getRestDirectionDropDown(restId)
            }
        }
    }

    var mAdapter: BaseQuickAdapter<String, BaseViewHolder>? = null
    private fun initRecycler() {
        mAdapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_photo) {
            override fun convert(holder: BaseViewHolder, item: String) {
                Glide.with(context).load(item).transform(RoundedCorners(ConvertUtils.dp2px(5f)))
                    .into(holder.getView<View>(R.id.ivImage) as ImageView)
            }
        }
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            uploadType = 2
            mPosition = position
            selectFile()

        }
        mAdapter?.addChildClickViewIds(R.id.ivDeletePhoto)
        mAdapter?.setOnItemChildClickListener { _, _, position ->
            imageList.removeAt(position)
            mAdapter?.setList(imageList)
            if (imageList.size >= 5) {
                ivPhoto.visibility = View.INVISIBLE
            } else {
                ivPhoto.visibility = View.VISIBLE
            }
        }
        val gridLayoutManager = GridLayoutManager(this, 3)
        recyclerviewHome.layoutManager = gridLayoutManager
        recyclerviewHome.adapter = mAdapter
    }

    var keyList = emptyList<String>()
    var restId = ""
    var restDirection = ""
    var bannerUrl = ""
    override fun observe() {
        super.observe()
        mViewModel.run {
            submitConfigResult.observe(this@ConfigActivity) {
                BusHelper.post("BannerUpdate", true)
                ActivityHelper.finish(ConfigActivity::class.java)
            }
            DropDownResult.observe(this@ConfigActivity) { list ->
                keyList = list.stream().map(DropDownBean::label).collect(Collectors.toList())
                tvName.setOnClickListener {
                    BaseDropDialog(this@ConfigActivity, "选择服务区", keyList, 0) {
                        tvName.text = keyList[it]
                        restId = list[it].value
                    }.show()
                }
            }

            DirectionDropDownResult.observe(this@ConfigActivity) { list ->
                val childList = list.stream().map(DropDownBean::label).collect(Collectors.toList())
                BaseDropDialog(this@ConfigActivity, "选择服务区方向", childList, 0) {
                    tvRestDirection.text = childList[it]
                    restDirection = list[it].value
                }.show()
            }
            uploadImageResult.observe(this@ConfigActivity) {
                if (uploadType == 3) {
                    bannerUrl = it
                    Glide.with(this@ConfigActivity).load(it).transform(RoundedCorners(ConvertUtils.dp2px(5f))).into(ivBanner)
                } else {
                    if (uploadType == 1) {
                        imageList.add(it)
                        if (imageList.size >= 5) {
                            ivPhoto.visibility = View.INVISIBLE
                        } else {
                            ivPhoto.visibility = View.VISIBLE
                        }
                    } else {
                        imageList[mPosition] = it
                    }
                    mAdapter?.setList(imageList)
                }
            }
        }
    }
}