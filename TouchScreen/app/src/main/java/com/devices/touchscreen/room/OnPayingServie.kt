package com.devices.touchscreen.room

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.blankj.utilcode.util.NetworkUtils
import com.devices.touchscreen.net.RetrofitClient
import com.devices.touchscreen.room.complaints.RoomComplaintsHelper
import com.devices.touchscreen.room.evaluation.RoomHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

/**
 * 作者：create by comersss on 2020/9/10 14:30
 * 邮箱：904359289@qq.com
 *
 */
class OnPayingServie : Service() {
    override fun onCreate() {
        super.onCreate()
        thread {
            run {
                while (true) {
                    Log.i("test", "我run了")
                    if (NetworkUtils.isConnected()) {
                        RoomComplaintsHelper.queryAllReadHistory().forEach {
                            GlobalScope.launch {
                                try {
                                    RetrofitClient.apiService.addPublicComplain(it).apiData()
                                    RoomComplaintsHelper.deleteReadHistory(it)
                                } catch (e: Exception) {
                                    Log.i("test", "OnPayingServie e:$e")
                                }
                            }
                        }

                        RoomHelper.queryAllReadHistory().forEach {
                            GlobalScope.launch {
                                try {
                                    RetrofitClient.apiService.addPublicEvaluation(it).apiData()
                                    RoomHelper.deleteReadHistory(it)
                                } catch (e: Exception) {
                                    Log.i("test", "OnPayingServie e:$e")
                                }
                            }

                        }
                        Thread.sleep(1000 * 10)
                    }
                    Thread.sleep(1000 * 60)
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder()
    }

    class LocalBinder : Binder() {
        val service: OnPayingServie
            get() = OnPayingServie()
    }
}