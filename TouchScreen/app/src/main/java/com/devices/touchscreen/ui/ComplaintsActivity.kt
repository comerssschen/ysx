package com.devices.touchscreen.ui

import android.os.CountDownTimer
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ViewUtils
import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseVmActivity
import com.devices.touchscreen.common.ActivityHelper
import com.devices.touchscreen.common.GlideEngine
import com.devices.touchscreen.common.showToast
import com.devices.touchscreen.common.toStr
import com.devices.touchscreen.view.CommonDialog
import com.devices.touchscreen.viewmodel.ComplaintsViewModel
import kotlinx.android.synthetic.main.activity_complaints.*

class ComplaintsActivity : BaseVmActivity<ComplaintsViewModel>(R.layout.activity_complaints) {
    override fun viewModelClass() = ComplaintsViewModel::class.java

    var carType = 0
    var mTimer: CountDownTimer? = null
    override fun initView() {
        super.initView()
        mTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                ActivityHelper.finish(ComplaintsActivity::class.java)
            }
        }.start()
        groupComplants.isVisible = intent.getIntExtra("Type", 0) == 1
        group2.isVisible = intent.getIntExtra("Type", 0) == 2

        etOpinion.addTextChangedListener {
            mTimer?.cancel()
            mTimer?.start()
            tvCount.text = "${it.toString().length}/50"
        }
        intent.getStringExtra("BannerUrl")?.let { GlideEngine.createGlideEngine().loadImage(this, it, ivBanner) }
        ivHome.setOnClickListener {
            if (carType > 0) {
                CommonDialog(this, "您的操作将被清空\n继续返回首页？") {
                    ActivityHelper.finish(ComplaintsActivity::class.java)
                }.show()
            } else {
                ActivityHelper.finish(ComplaintsActivity::class.java)
            }
        }
        btSubmit.setOnClickListener {
            when {
                etName.toStr().isEmpty() -> showToast("请输入姓名")
                etPhone.toStr().isEmpty() -> showToast("请输入手机号")
                !RegexUtils.isMobileSimple(etPhone.toStr()) -> showToast("手机号格式不正确")
                etOpinion.toStr().isEmpty() -> showToast("请输入您的宝贵意见")
                else -> mViewModel.addPublicComplain(etName.toStr(), etPhone.toStr(), etOpinion.toStr())
            }

        }

        tvBigCarDriver.setOnClickListener {
            carType = 1
            notifyBg()
        }
        tvLittlecarDriver.setOnClickListener {
            carType = 2
            notifyBg()
        }
        tvBigCar.setOnClickListener {
            carType = 3
            notifyBg()
        }
        tvCarDriver.setOnClickListener {
            carType = 4
            notifyBg()
        }
        btNext.setOnClickListener {
            if (carType == 0) {
                showToast("请选择类型")
            } else {
                startEvaluation()
            }
        }

        tvVerySatisfaction.setOnClickListener { pointNext(1) }
        ivVerySatisfaction.setOnClickListener { pointNext(1) }
        tvSatisfaction.setOnClickListener { pointNext(2) }
        ivSatisfaction.setOnClickListener { pointNext(2) }
        tvNoSatisfaction.setOnClickListener { pointNext(3) }
        ivNoSatisfaction.setOnClickListener { pointNext(3) }
        btRight.setOnClickListener { pointNext(4) }
        btLeft.setOnClickListener { pointNext(5) }
        tvCommitDirectly.setOnClickListener {
            group3.isVisible = false
            group4.isVisible = true
        }


        ivStart1.setOnClickListener {
            satisfaction = 1
            ivStart1.setImageResource(R.drawable.start_s)
            ivStart2.setImageResource(R.drawable.start_n)
            ivStart3.setImageResource(R.drawable.start_n)
            ivStart4.setImageResource(R.drawable.start_n)
            ivStart5.setImageResource(R.drawable.start_n)
        }
        ivStart2.setOnClickListener {
            satisfaction = 2
            ivStart1.setImageResource(R.drawable.start_s)
            ivStart2.setImageResource(R.drawable.start_s)
            ivStart3.setImageResource(R.drawable.start_n)
            ivStart4.setImageResource(R.drawable.start_n)
            ivStart5.setImageResource(R.drawable.start_n)
        }
        ivStart3.setOnClickListener {
            satisfaction = 3
            ivStart1.setImageResource(R.drawable.start_s)
            ivStart2.setImageResource(R.drawable.start_s)
            ivStart3.setImageResource(R.drawable.start_s)
            ivStart4.setImageResource(R.drawable.start_n)
            ivStart5.setImageResource(R.drawable.start_n)
        }
        ivStart4.setOnClickListener {
            satisfaction = 4
            ivStart1.setImageResource(R.drawable.start_s)
            ivStart2.setImageResource(R.drawable.start_s)
            ivStart3.setImageResource(R.drawable.start_s)
            ivStart4.setImageResource(R.drawable.start_s)
            ivStart5.setImageResource(R.drawable.start_n)
        }
        ivStart5.setOnClickListener {
            satisfaction = 5
            ivStart1.setImageResource(R.drawable.start_s)
            ivStart2.setImageResource(R.drawable.start_s)
            ivStart3.setImageResource(R.drawable.start_s)
            ivStart4.setImageResource(R.drawable.start_s)
            ivStart5.setImageResource(R.drawable.start_s)
        }

        etEvaluation.addTextChangedListener {
            mTimer?.cancel()
            mTimer?.start()
            tvCountEvaluation.text = "${it.toString().length}/50"
        }
        btLeftEvaluation.setOnClickListener {
            mCureentPosition = 6
            notityImage()
            group3.isVisible = true
            group4.isVisible = false
        }
        btRightEvaluation.setOnClickListener {
            if (satisfaction == 0) {
                showToast("您需要评星级才能提交")
                return@setOnClickListener
            }
            mViewModel.addPublicEvaluation(satisfaction, mListStatus, etEvaluation.toStr())
        }
    }

    var satisfaction = 0
    private fun pointNext(status: Int) {
        when (status) {
            4 -> {
                if (mCureentPosition < 6) {
                    mCureentPosition++
                    notityImage()
                } else {
                    group3.isVisible = false
                    group4.isVisible = true
                }
            }
            5 -> {
                if (mCureentPosition > 0) {
                    mCureentPosition--
                    notityImage()
                } else {
                    group3.isVisible = false
                    group2.isVisible = true
                }
            }
            else -> {
                mListStatus[mCureentPosition] = status
                notityImage()
                if (mCureentPosition < 6) {
                    mCureentPosition++
                    ViewUtils.runOnUiThreadDelayed({
                        notityImage()
                    }, 600)
                } else {
                    group3.isVisible = false
                    group4.isVisible = true
                }
            }
        }
    }

    private fun notityImage() {
        tvCurrentNum.text = "${mCureentPosition + 1}/8"
        tvTips.text = mListTips[mCureentPosition]
        ivVerySatisfaction.setImageResource(R.drawable.very_satisfaction)
        ivSatisfaction.setImageResource(R.drawable.satisfaction)
        ivNoSatisfaction.setImageResource(R.drawable.no_satisfaction)
        when (mListStatus[mCureentPosition]) {
            1 -> {
                ivVerySatisfaction.setImageResource(R.drawable.very_satisfaction_select)
            }
            2 -> {
                ivSatisfaction.setImageResource(R.drawable.satisfaction_select)
            }
            3 -> {
                ivNoSatisfaction.setImageResource(R.drawable.no_satisfaction_select)
            }
        }

    }

    var mCureentPosition = 0
    var mListStatus = arrayListOf(0, 0, 0, 0, 0, 0, 0)
    var mListTips = arrayListOf("停车服务", "卫生间清洁卫生", "餐饮价格", "商品价格", "汽车维修价格和质量", "加油站服务质量", "服务人员服务态度")
    private fun startEvaluation() {
        mCureentPosition = 0
        group2.isVisible = false
        group3.isVisible = true
        notityImage()
    }

    private fun notifyBg() {
        tvBigCarDriver.setBackgroundResource(R.drawable.normal_bg)
        tvLittlecarDriver.setBackgroundResource(R.drawable.normal_bg)
        tvBigCar.setBackgroundResource(R.drawable.normal_bg)
        tvCarDriver.setBackgroundResource(R.drawable.normal_bg)
        when (carType) {
            1 -> tvBigCarDriver.setBackgroundResource(R.drawable.select_bg)
            2 -> tvLittlecarDriver.setBackgroundResource(R.drawable.select_bg)
            3 -> tvBigCar.setBackgroundResource(R.drawable.select_bg)
            4 -> tvCarDriver.setBackgroundResource(R.drawable.select_bg)
        }
        startEvaluation()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            publicComplainResult.observe(this@ComplaintsActivity) {
                ActivityHelper.startActivity(ComplaintsActivity::class.java, mapOf("type" to "投诉建议"))
                ActivityHelper.finish(ComplaintsSucessActivity::class.java)
            }
            publicEvaluationResult.observe(this@ComplaintsActivity) {
                ActivityHelper.startActivity(ComplaintsActivity::class.java, mapOf("type" to "评价"))
                ActivityHelper.finish(ComplaintsSucessActivity::class.java)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer?.cancel()
        mTimer = null
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN || ev.action == MotionEvent.ACTION_MOVE) {
            mTimer?.cancel()
            mTimer?.start()
        }
        return super.dispatchTouchEvent(ev)
    }

}