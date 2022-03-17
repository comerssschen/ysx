package com.devices.touchscreen.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.devices.touchscreen.R
import com.devices.touchscreen.common.showToast
import com.devices.touchscreen.common.toStr
import kotlinx.android.synthetic.main.dialog_input.*

class InputDialog(mContext: Context, confirm: () -> Unit) : Dialog(mContext) {
    init {
        setContentView(R.layout.dialog_input)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = window?.attributes
        val display = (mContext as Activity).windowManager.defaultDisplay
        lp?.width = (display.width * 0.7).toInt() // 宽度
        window?.attributes = lp
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        tvConfirm.setOnClickListener {
            if (etInput.toStr() == "123456") {
                confirm.invoke()
                dismiss()
            } else {
                showToast("密码错误")
            }

        }
        tvCancle.setOnClickListener {
            dismiss()
        }
    }

    override fun show() {
        super.show()
        if (!isShowing) {
            show()
        }
    }

}