package com.devices.touchscreen.common

import android.app.Activity
import android.app.Application
import android.content.Intent


object ActivityHelper {
    private val activities = mutableListOf<Activity>()

    @JvmOverloads
    fun startActivity(
        clazz: Class<out Activity>,
        params: Map<String, Any?> = emptyMap(),
    ) {
        val currentActivity = activities[activities.lastIndex]
        val intent = Intent(currentActivity, clazz)
        params.forEach { map ->
            map.value?.let {
                intent.putExtras(map.key to it)
            }
        }
        currentActivity.startActivity(intent)
    }

    fun init(context: Application) {
        activities.clear()
        context.registerActivityLifecycleCallbacks(ActLifeCallbackAdapter(
            onActivityCreated = { activity, _ ->
                activities.add(activity)
            },
            onActivityDestroyed = { activity ->
                activities.remove(activity)
            }
        ))
    }

    fun finish(vararg clazz: Class<out Activity>) {
        activities.forEach { activiy ->
            if (clazz.contains(activiy::class.java)) {
                activiy.finish()
            }
        }
    }

    fun finishAll(vararg clazz: Class<out Activity>) {
        activities.forEach { activiy ->
            if (!clazz.contains(activiy::class.java)) {
                activiy.finish()
            }
        }
    }
}