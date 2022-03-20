package com.devices.touchscreen.ui

import android.os.CountDownTimer
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ViewUtils
import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseVmActivity
import com.devices.touchscreen.bean.DropDownBean
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
        mTimer = object : CountDownTimer(300000, 1000) {
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

        tvVerySatisfaction.setOnClickListener { pointNext(evaluationGradeListValue[0]) }
        ivVerySatisfaction.setOnClickListener { pointNext(evaluationGradeListValue[0]) }
        tvSatisfaction.setOnClickListener { pointNext(evaluationGradeListValue[1]) }
        ivSatisfaction.setOnClickListener { pointNext(evaluationGradeListValue[1]) }
        tvNoSatisfaction.setOnClickListener { pointNext(evaluationGradeListValue[2]) }
        ivNoSatisfaction.setOnClickListener { pointNext(evaluationGradeListValue[2]) }
        btRight.setOnClickListener { pointNext(100) }
        btLeft.setOnClickListener { pointNext(500) }
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
            mViewModel.addPublicEvaluation(satisfaction, evaluateType, etEvaluation.toStr(), carType)
        }
    }

    override fun initData() {
        super.initData()
        if (intent.getIntExtra("Type", 0) == 2) {
//            mViewModel.evaluator()
            mViewModel.evaluateType()
            mViewModel.evaluationGrade()
        }
    }

    var satisfaction = 0
    private fun pointNext(status: Int) {
        when (status) {
            100 -> {
                if (mCureentPosition < 6) {
                    mCureentPosition++
                    notityImage()
                } else {
                    group3.isVisible = false
                    group4.isVisible = true
                }
            }
            500 -> {
                if (mCureentPosition > 0) {
                    mCureentPosition--
                    notityImage()
                } else {
                    group3.isVisible = false
                    group2.isVisible = true
                }
            }
            else -> {
                evaluateType[mCureentPosition].count = status
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
        tvTips.text = evaluateType[mCureentPosition].label
        ivVerySatisfaction.setImageResource(R.drawable.very_satisfaction)
        ivSatisfaction.setImageResource(R.drawable.satisfaction)
        ivNoSatisfaction.setImageResource(R.drawable.no_satisfaction)
        when (evaluateType[mCureentPosition].count) {
            evaluationGradeListValue[0] -> {
                ivVerySatisfaction.setImageResource(R.drawable.very_satisfaction_select)
            }
            evaluationGradeListValue[1] -> {
                ivSatisfaction.setImageResource(R.drawable.satisfaction_select)
            }
            evaluationGradeListValue[2] -> {
                ivNoSatisfaction.setImageResource(R.drawable.no_satisfaction_select)
            }
        }

    }

    var mCureentPosition = 0
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

    val evaluationGradeListValue = arrayListOf(1, 3, 5)
    var evaluateType = arrayListOf(
        DropDownBean("停车服务", "parkingEvaluate", 0),
        DropDownBean("卫生间清洁卫生", "toiletPriceEvaluate", 0),
        DropDownBean("餐饮价格", "cateringPriceEvaluate", 0),
        DropDownBean("商品价格", "goodsPriceEvaluate", 0),
        DropDownBean("汽车维修价格和质量", "garageEvaluate", 0),
        DropDownBean("加油站服务质量", "oilEvaluate", 0),
        DropDownBean("服务人员服务态度", "serviceEvaluate", 0)
    )

    override fun observe() {
        super.observe()
        mViewModel.run {
            evaluateTypeResult.observe(this@ComplaintsActivity) {
                evaluateType = it
            }
            evaluatorResult.observe(this@ComplaintsActivity) {

            }
            evaluationGradeResult.observe(this@ComplaintsActivity) {
                it.forEachIndexed { index, dropDownBean ->
                    when (index) {
                        0 -> {
                            tvVerySatisfaction.text = dropDownBean.label
                        }
                        1 -> {
                            tvSatisfaction.text = dropDownBean.label
                        }
                        2 -> {
                            tvNoSatisfaction.text = dropDownBean.label
                        }
                    }
                }
                it.forEach { dropDownBean ->
                    evaluationGradeListValue.add(dropDownBean.value.toInt())
                }
            }
            publicComplainResult.observe(this@ComplaintsActivity) {
                ActivityHelper.startActivity(ComplaintsSucessActivity::class.java, mapOf("type" to "投诉建议"))
                ActivityHelper.finish(ComplaintsActivity::class.java)
            }
            publicEvaluationResult.observe(this@ComplaintsActivity) {
                ActivityHelper.startActivity(ComplaintsSucessActivity::class.java, mapOf("type" to "评价"))
                ActivityHelper.finish(ComplaintsActivity::class.java)
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