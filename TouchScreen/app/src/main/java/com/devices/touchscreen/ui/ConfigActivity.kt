package com.devices.touchscreen.ui

import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseVmActivity
import com.devices.touchscreen.bean.DropDownBean
import com.devices.touchscreen.common.GlideEngine
import com.devices.touchscreen.common.showToast
import com.devices.touchscreen.view.BaseDropDialog
import com.devices.touchscreen.viewmodel.ConfigViewModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.activity_config.*
import java.io.File
import java.util.stream.Collectors

class ConfigActivity : BaseVmActivity<ConfigViewModel>(R.layout.activity_config) {
    override fun viewModelClass() = ConfigViewModel::class.java

    private var mPosition = 0
    private var isAdd = true
    private var isBanner = false
    private var imageList = ArrayList<String>()
    override fun initView() {
        super.initView()
        ivBanner.setOnClickListener {
            isBanner = true
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//相册和拍照
                .selectionMode(PictureConfig.SINGLE)
                .imageEngine(GlideEngine.createGlideEngine())
                .isCompress(true) //压缩
                .cutOutQuality(50) //压缩后图片质量
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onCancel() {}
                    override fun onResult(result: MutableList<LocalMedia>) {
                        if (result.size != 0) {
                            mViewModel.uploadImage(File(result[0].compressPath))
                        }
                    }
                })
        }
        ivPhoto.setOnClickListener {
            isBanner = false
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//相册和拍照
                .selectionMode(PictureConfig.SINGLE)
                .imageEngine(GlideEngine.createGlideEngine())
                .isCompress(true) //压缩
                .cutOutQuality(50) //压缩后图片质量
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onCancel() {}
                    override fun onResult(result: MutableList<LocalMedia>) {
                        if (result.size != 0) {
                            isAdd = true
                            mViewModel.uploadImage(File(result[0].compressPath))
                        }
                    }
                })

        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getDropdown()
        tvRestDirection.setOnClickListener {
            if (restId.isEmpty()) {
                showToast("请选择服务区")
            } else {
                BaseDropDialog(this@ConfigActivity, "选择服务区方向", keyList, 0) {
                    tvRestDirection.text = keyList[it]
                }.show()
            }
        }
    }

    var mAdapter: BaseQuickAdapter<String, BaseViewHolder>? = null
    private fun initRecycler(canEdit: Boolean) {
        mAdapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_photo) {
            override fun convert(holder: BaseViewHolder, item: String) {
                Glide.with(context).load(item).transform(RoundedCorners(ConvertUtils.dp2px(5f)))
                    .into(holder.getView<View>(R.id.ivImage) as ImageView)
                (holder.getView<View>(R.id.ivDeletePhoto) as ImageView).isVisible = canEdit
            }
        }
        if (canEdit) {
            mAdapter?.setOnItemClickListener { adapter, view, position ->
                isAdd = false
                isBanner = false
                mPosition = position
                PictureSelector.create(this@ConfigActivity)
                    .openGallery(PictureMimeType.ofImage())//相册和拍照
                    .selectionMode(PictureConfig.SINGLE)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .isCompress(true) //压缩
                    .cutOutQuality(50) //压缩后图片质量
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onCancel() {}
                        override fun onResult(result: MutableList<LocalMedia>) {
                            if (result.size != 0) {
                                mViewModel.uploadImage(File(result[0].compressPath))
                            }
                        }
                    })

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
        }
        val gridLayoutManager = GridLayoutManager(this, 3)
        recyclerviewHome.layoutManager = gridLayoutManager
        recyclerviewHome.adapter = mAdapter
    }

    var keyList = emptyList<String>()
    var restId = ""
    var restDirection = ""
    override fun observe() {
        super.observe()
        mViewModel.run {
            DropDownResult.observe(this@ConfigActivity) { list ->
                keyList = list.stream().map(DropDownBean::label).collect(Collectors.toList())
                tvName.setOnClickListener {
                    BaseDropDialog(this@ConfigActivity, "选择服务区", keyList, 0) {
                        tvName.text = keyList[it]
                        restId = list[it].value
                    }.show()
                }
            }

            uploadImageResult.observe(this@ConfigActivity) {
                if (isBanner) {
                    Glide.with(this@ConfigActivity).load(it).transform(RoundedCorners(ConvertUtils.dp2px(5f))).into(ivBanner)
                } else {
                    if (isAdd) {
                        imageList.add(it)
                        if (imageList.size >= 3) {
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