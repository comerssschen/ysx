package com.devices.touchscreen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.devices.touchscreen.R


/**
 *
 *
 * @author admin
 * @date 4/26/21 20:24
 */
class MyProgressFragment : DialogFragment() {
    companion object {
        fun newInstance() =
            MyProgressFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress_dialog, container, false)
    }

    fun show(fragmentManager: FragmentManager, isCancelable: Boolean = false) {
        this.isCancelable = isCancelable
        try {
            show(fragmentManager, "progressDialogFragment")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}