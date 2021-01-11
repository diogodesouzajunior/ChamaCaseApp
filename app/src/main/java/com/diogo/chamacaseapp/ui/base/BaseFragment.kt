package com.diogo.chamacaseapp.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.diogo.chamacaseapp.ChamaCaseApplication
import com.diogo.chamacaseapp.injection.component.ConfigPersistentComponent
import com.diogo.chamacaseapp.injection.module.PresenterModule
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong


open class BaseFragment: Fragment(){
    lateinit var baseActivity: BaseActivity

    companion object {
        @JvmStatic private val KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID"
        @JvmStatic private val NEXT_ID = AtomicLong(0)
        @JvmStatic private val componentsMap = HashMap<Long, ConfigPersistentComponent>()
    }

    private var fragmentId: Long = 0
    lateinit var component: ConfigPersistentComponent

    @Suppress("UsePropertyAccessSyntax")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        fragmentId = savedInstanceState?.getLong(KEY_FRAGMENT_ID) ?: NEXT_ID.getAndIncrement()

        if (componentsMap[fragmentId] != null)
            Timber.i("Reusing ConfigPersistentComponent id = $fragmentId")


        component = componentsMap.getOrPut(fragmentId, {
            Timber.i("Creating new ConfigPersistentComponent id=$fragmentId")
            (context!!.applicationContext as ChamaCaseApplication).applicationComponent.plus(
                PresenterModule()
            )
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_FRAGMENT_ID, fragmentId)
    }

    override fun onDestroy() {
        if (!activity!!.isChangingConfigurations) {
            Timber.i("Clearing ConfigPersistentComponent id=$fragmentId")
            componentsMap.remove(fragmentId)
            getPresenter()?.detachView()
        }

        super.onDestroy()
    }

    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(activity is BaseActivity){
            baseActivity = activity as BaseActivity
        }else{
            throw Throwable("Fragment doesn't implements ${BaseActivity::javaClass.name}")
        }
    }

    open fun showProgressIndicator(){
        baseActivity.showProgressIndicator()
    }

    open fun hideProgressIndicator(){
        baseActivity.hideProgressIndicator()
    }

    open fun showError(error: String){
        baseActivity.showError(error)
    }

    open fun showError(t: Throwable){
        baseActivity.showError(t)
    }

    fun handleException(t: Throwable) = baseActivity.apiErrorHandler.handleException(t)

    fun finish(){
        activity!!.finish()
    }

    fun setResult(resultCode: Int, data: Intent){
        activity!!.setResult(resultCode, data)
    }

    open fun getPresenter(): BasePresenter<out MvpView>? = null
    open fun setError() {}
}