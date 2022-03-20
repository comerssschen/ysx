package com.devices.touchscreen.ui

import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.FileUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseVmActivity
import com.devices.touchscreen.bean.DropDownBean
import com.devices.touchscreen.common.*
import com.devices.touchscreen.view.BaseDropDialog
import com.devices.touchscreen.viewmodel.ConfigViewModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import kotlinx.android.synthetic.main.activity_config.*
import java.io.File
import java.util.stream.Collectors
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener

import com.devices.touchscreen.common.GlideEngine
import com.luck.picture.lib.config.PictureMimeType


class ConfigActivity : BaseVmActivity<ConfigViewModel>(R.layout.activity_config) {
    override fun viewModelClass() = ConfigViewModel::class.java

    private var mPosition = 0
    private var uploadType = 0//1新增 2 编辑 3 bannner
    private var imageList = ArrayList<String>()
    override fun initView() {
        super.initView()
        tvName.text = intent.getStringExtra("Name")
        tvRestDirection.text = intent.getStringExtra("RestDirectionName")
        ivBanner.setOnClickListener {
            if (bannerUrl.isEmpty()) {
                uploadType = 3
                selectFile()
            } else {
                PictureSelector.create(this)
                    .openPreview()
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setExternalPreviewEventListener(object : OnExternalPreviewEventListener {
                        override fun onPreviewDelete(position: Int) {}
                        override fun onLongPressDownload(media: LocalMedia): Boolean {
                            return false
                        }
                    }).startActivityPreview(0, true, arrayListOf(LocalMedia.generateLocalMedia(bannerUrl, PictureMimeType.ofPNG())))
            }
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
            if (bannerUrl.isEmpty() || imageList.size < 1) {
                showToast("请上传图片")
                return@setOnClickListener
            }
            mViewModel.submitConfig(restId, restDirection, imageList.toTypedArray(), bannerUrl)
        }
        ivDeletBanner.setOnClickListener {
            bannerUrl = ""
            ivBanner.setImageResource(R.drawable.take_photo)
            ivDeletBanner.isVisible = bannerUrl.isNotEmpty()
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
            .isWithSelectVideoImage(true)
            .setSelectionMode(SelectModeConfig.SINGLE)
            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onCancel() {}
                override fun onResult(result: ArrayList<LocalMedia>) {
                    if (result.size != 0) {
                        if (fireIsAvailble(result[0].realPath)) {
                            mViewModel.uploadImage(File(result[0].realPath))
                        } else {
                            showToast("图片或视频不符合要求")
                        }

                    }
                }

            })
    }

    private fun fireIsAvailble(realPath: String): Boolean {
        if (realPath.endsWith(".jpeg") || realPath.endsWith(".jpg") || realPath.endsWith(".png")
            || realPath.endsWith(".JPEG") || realPath.endsWith(".JPG") || realPath.endsWith(".PNG")
        ) {
//首页图片尺寸1080X608,内页图片 尺寸1080X400，不超过2M。
            if (FileUtils.getFileLength(realPath) > 2 * 1024 * 1024) {
                return false
            }
            BitmapFactory.decodeFile(realPath).apply {
                Log.i("test", "width= $width,,hight = $height")
                if (width != 1080) return false
                if (uploadType == 3) {
                    if (height != 400) return false
                } else {
                    if (height != 608) return false
                }
                return true
            }
        } else if (realPath.endsWith("mp4") || realPath.endsWith("MP4")) {
// 视频比例16：9，大小不超过100M。格式MP4
            if (FileUtils.getFileLength(realPath) > 100 * 1024 * 1024) {
                return false
            }
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(realPath)
            val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt()
            val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()
            Log.i("test", "width= $width,,height = $height")
            return true
        }
        return false
    }

    override fun initData() {
        super.initData()
        mViewModel.getInfo()
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
//            uploadType = 2
//            mPosition = position
//            selectFile()
            val imageLocalMedia = ArrayList<LocalMedia>()
            imageList.forEach { url ->
                if (url.endsWith("mp4") || url.endsWith("MP4")) {
                    imageLocalMedia.add(LocalMedia.generateLocalMedia(url, PictureMimeType.ofPNG()))
                } else {
                    imageLocalMedia.add(LocalMedia.generateLocalMedia(url, PictureMimeType.ofPNG()))
                }
            }
            PictureSelector.create(this)
                .openPreview()
                .setImageEngine(GlideEngine.createGlideEngine())
                .setExternalPreviewEventListener(object : OnExternalPreviewEventListener {
                    override fun onPreviewDelete(position: Int) {
                        imageList.removeAt(position)
                        mAdapter?.setList(imageList)
                        if (imageList.size >= 5) {
                            ivPhoto.visibility = View.GONE
                        } else {
                            ivPhoto.visibility = View.VISIBLE
                        }
                    }

                    override fun onLongPressDownload(media: LocalMedia): Boolean {
                        return true
                    }
                }).startActivityPreview(position, true, imageLocalMedia)

        }
        mAdapter?.addChildClickViewIds(R.id.ivDeletePhoto)
        mAdapter?.setOnItemChildClickListener { _, _, position ->
            imageList.removeAt(position)
            mAdapter?.setList(imageList)
            if (imageList.size >= 5) {
                ivPhoto.visibility = View.GONE
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
            publicPagePhoto.observe(this@ConfigActivity) { bean ->
                bannerUrl = bean.inPage ?: ""
                ivDeletBanner.isVisible = bannerUrl.isNotEmpty()
                Glide.with(this@ConfigActivity).load(bannerUrl).transform(RoundedCorners(ConvertUtils.dp2px(5f))).into(ivBanner)
                imageList = bean.publicFrontPageList ?: ArrayList()
                if (imageList.size >= 5) {
                    ivPhoto.visibility = View.GONE
                } else {
                    ivPhoto.visibility = View.VISIBLE
                }
                mAdapter?.setNewInstance(imageList)
                restId = bean.restId ?: ""
                restDirection = bean.restDirection ?: ""
            }
            submitConfigResult.observe(this@ConfigActivity) {
                BusHelper.post("BannerUpdate", true)
                ActivityHelper.finish(ConfigActivity::class.java)
            }
            DropDownResult.observe(this@ConfigActivity) { list ->
                keyList = list.stream().map(DropDownBean::label).collect(Collectors.toList())
                tvName.setOnClickListener {
                    BaseDropDialog(this@ConfigActivity, "选择服务区", keyList, 0) {
                        tvName.text = keyList[it]
                        if (restId != list[it].value) {
                            restId = list[it].value
                            tvRestDirection.text = ""
                            restDirection = ""
                        }

                    }.show()
                }
            }

            DirectionDropDownResult.observe(this@ConfigActivity) { list ->
                val childList = list.stream().map(DropDownBean::label).collect(Collectors.toList())
                BaseDropDialog(this@ConfigActivity, "选择服务区方向", childList, 0) {
                    tvRestDirection.text = childList[it]
                    restDirection = list[it].value
                    mViewModel.getInfo(restId, restDirection)
                }.show()
            }
            uploadImageResult.observe(this@ConfigActivity) {
                if (uploadType == 3) {
                    bannerUrl = it
                    ivDeletBanner.isVisible = bannerUrl.isNotEmpty()
                    Glide.with(this@ConfigActivity).load(it).transform(RoundedCorners(ConvertUtils.dp2px(5f))).into(ivBanner)
                } else {
                    if (uploadType == 1) {
                        imageList.add(it)
                        if (imageList.size >= 5) {
                            ivPhoto.visibility = View.GONE
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