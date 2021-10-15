package com.mcgrady.pay.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Created by mcgrady on 2021/10/13.
 */
//object PayClient : MutableLiveData<PayResult>() {
class PayClient private constructor() : MutableLiveData<PayResult>() {

    companion object {
        //通过@JvmStatic注解，使得在Java中调用instance直接是像调用静态函数一样，
        //类似KLazilyDCLSingleton.getInstance(),如果不加注解，在Java中必须这样调用: KLazilyDCLSingleton.Companion.getInstance().
        @JvmStatic
        //使用lazy属性代理，并指定LazyThreadSafetyMode为SYNCHRONIZED模式保证线程安全
        val instance: PayClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { PayClient() }
    }

    private fun readResolve(): Any {//防止单例对象在反序列化时重新生成对象
        return instance
    }

    fun <T> send(owner: LifecycleOwner, payCall: BasePayCall<T>, observer: BasePayObserver) {
        super.observe(owner, observer)

        payCall.send()
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in PayResult>) {
    }

    override fun observeForever(observer: Observer<in PayResult>) {
    }
}