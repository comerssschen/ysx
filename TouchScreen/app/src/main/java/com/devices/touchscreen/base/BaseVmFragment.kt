package com.devices.touchscreen.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider


abstract class BaseVmFragment<VM : BaseViewModel> : BaseFragment() {

    protected lateinit var mViewModel: VM
    private var lazyLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        observe()
        initData()
    }

    override fun onResume() {
        super.onResume()
        if (!lazyLoaded) {
            lazyLoadData()
            lazyLoaded = true
        }
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(viewModelClass())
    }

    abstract fun viewModelClass(): Class<VM>

    open fun observe() {
        mViewModel.showLoadding.observe(viewLifecycleOwner) {
            if (it) {
                showProgressDialog()
            } else {
                dismissProgressDialog()
            }
        }
        mViewModel.loginStatusInvalid.observe(viewLifecycleOwner) {
            if (it) {
//                ActivityHelper.startActivity(LoginActivity::class.java)
//                ActivityHelper.finishAll(LoginActivity::class.java)
            }
        }
    }

    open fun initData() {}

    open fun lazyLoadData() {}
}