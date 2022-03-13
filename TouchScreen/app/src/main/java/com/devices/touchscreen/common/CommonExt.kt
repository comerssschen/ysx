package com.devices.touchscreen.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.Gravity
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ToastUtils
import com.devices.touchscreen.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.devices.touchscreen.base.MyApplication
import java.math.BigDecimal


fun Context.showAlertDialog(message: String, block: () -> Unit, cancle: () -> Unit) {
    AlertDialog.Builder(this)
        .setPositiveButton(R.string.confirm) { _, _ ->
            block.invoke()
        }
        .setCancelable(false)
        .setNegativeButton(R.string.cancle) { _, _ ->
            cancle.invoke()
        }
        .setMessage(message).show()
}

fun Context.showSelecttDialog(
    title: String,
    message: Array<String>,
    block: (position: Int) -> Unit
) {
    AlertDialog.Builder(this)
        .setItems(message) { _, which ->
            block.invoke(which)
        }.setTitle(title)
        .setNegativeButton(R.string.cancle, null)
        .show()
}

fun Context.copyTextIntoClipboard(text: CharSequence?, label: String? = "") {
    if (text.isNullOrEmpty()) return
    val cbs = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        ?: return
    cbs.setPrimaryClip(ClipData.newPlainText(label, text))
    showToast("copy success")
}

fun ViewPager.onPageChangeListener(onPageSelect: (Int) -> Unit) {
    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            onPageSelect(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}

    })
}

fun showToast(message: CharSequence) {
    ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).show(message)
}

fun showToast(@StringRes message: Int) {
    ToastUtils.make().setGravity(Gravity.CENTER, 0, 0)
        .show(MyApplication.instance.getString(message))
}

fun isMobileNo(mobiles: String) =
    mobiles.isNotEmpty() && mobiles.startsWith("0") && mobiles.length == 10

fun dp2px(dpValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}


@Suppress("DEPRECATION")
fun String.renderHtml(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
} else Html.fromHtml(this)

fun Long.toDoubleStr() =
    if (this < 10) {
        "0$this"
    } else {
        this
    }

fun TextView.toStr() = this.text.toString().trim()
fun EditText.toStr() = this.text.toString().trim()

fun String.toWhole() = when {
    this.endsWith(".00") -> {
        this.replace(".00", "")
    }
    this.endsWith(".0") -> {
        this.replace(".0", "")
    }
    this.endsWith(".") -> {
        this.replace(".", "")
    }
    else -> {
        this
    }
}


fun multiply(d1: String?, d2: String?): String {
    if (d1 == null || d2 == null) {
        return ""
    }
    val bd1 = BigDecimal(d1)
    val bd2 = BigDecimal(d2)
    return bd1.multiply(bd2).stripTrailingZeros().toPlainString()
}

fun add(d1: String?, d2: String?): String {
    if (d1 == null || d2 == null) {
        return ""
    }
    val bd1 = BigDecimal(d1)
    val bd2 = BigDecimal(d2)
    return bd1.add(bd2).stripTrailingZeros().toPlainString()
}

fun String?.toListString() =
    this?.let { Gson().fromJson<List<String>>(it, object : TypeToken<List<String>>() {}.type) }

fun List<String>?.listToStr(): String {
    var str = ""
    this?.forEach {
        str += "$it "
    }
    return str
}


fun Context.stringValue(@StringRes strRes: Int) = resources.getString(strRes)

fun Context.drawableValue(@DrawableRes drawRes: Int) =
    ContextCompat.getDrawable(this, drawRes) ?: ColorDrawable(Color.TRANSPARENT)

fun Context.colorValue(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.dimenValue(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun <T> MutableList<T>?.concat(data: Collection<T>): MutableList<T> {
    val currentList = this?.toList() ?: emptyList()
    val newList = currentList + data
    return newList.toMutableList()
}

fun <T> Collection<T>.toArrayList() = ArrayList<T>(this)

fun Context.openInExplorer(link: String?) {
    try {
        startActivity(Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(link)
        })
    } catch (e: Exception) {
    }
}

