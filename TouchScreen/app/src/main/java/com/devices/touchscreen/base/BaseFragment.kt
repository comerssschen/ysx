package com.devices.touchscreen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


open class BaseFragment : Fragment() {

    private lateinit var progressDialogFragment: MyProgressFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes(), container, false)
    }

    open fun initView() {}
    open fun layoutRes() = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun showProgressDialog() {
        if (!this::progressDialogFragment.isInitialized) {
            progressDialogFragment = MyProgressFragment.newInstance()
        }
        if (!progressDialogFragment.isAdded) {
            progressDialogFragment.show(childFragmentManager, false)
        }
    }

    fun dismissProgressDialog() {
        if (this::progressDialogFragment.isInitialized && progressDialogFragment.isVisible) {
            progressDialogFragment.dismissAllowingStateLoss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgressDialog()
    }
}
