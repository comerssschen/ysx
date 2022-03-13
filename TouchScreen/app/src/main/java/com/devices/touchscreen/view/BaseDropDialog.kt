package com.devices.touchscreen.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import com.devices.touchscreen.R
import kotlinx.android.synthetic.main.base_pick_view.*


class BaseDropDialog<T> : Dialog {
    private var mIndex = 0

    constructor(mContext: Context, title: String, store_data: List<T>, checkId: Int, select: (position: Int) -> Unit) : super(mContext) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.base_pick_view)
        setCancelable(false)
        setCanceledOnTouchOutside(true)
        //解决dialog圆角无效的bug
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.BOTTOM)
        val lp = window?.attributes
        val display = (mContext as Activity).windowManager.defaultDisplay
        lp?.width = display.width
        window?.attributes = lp

        tv_title.text = title
        mIndex = checkId
        wheelView.currentItem = checkId
        wheelView.setCyclic(false)
        wheelView.adapter = ArrayWheelAdapter(store_data)
        wheelView.setOnItemSelectedListener { index ->
            mIndex = index
        }
        tv_ok.setOnClickListener {
            select.invoke(mIndex)
            dismiss()
        }
    }
}