package com.devices.touchscreen.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils

open class BaseActivity(val res: Int) : AppCompatActivity() {
    private lateinit var dialog: MyProgressFragment
    private var lastClickTime: Long = 0
    private val spaceTime = 100

    private val isAllowClick: Boolean
        get() {
            val currentTime = System.currentTimeMillis()
            val isAllowClick: Boolean = currentTime - lastClickTime > spaceTime
            lastClickTime = currentTime
            return isAllowClick
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(res)
        BarUtils.setStatusBarVisibility(this, false)
        BarUtils.setNavBarVisibility(this, false)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initView()
    }

    open fun initView() {}

    fun showProgressDialog() {
        if (!this::dialog.isInitialized) {
            dialog = MyProgressFragment.newInstance()
        }
        if (!dialog.isAdded) {
            dialog.show(supportFragmentManager, false)
        }
    }

    fun dismissProgressDialog() {
        if (this::dialog.isInitialized && dialog.isVisible) {
            dialog.dismissAllowingStateLoss()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (!isAllowClick) {
                return true
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismissProgressDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgressDialog()
    }

}