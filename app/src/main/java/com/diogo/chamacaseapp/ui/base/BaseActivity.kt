package com.diogo.chamacaseapp.ui.base

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.diogo.chamacaseapp.ChamaCaseApplication
import com.diogo.chamacaseapp.R
import com.diogo.chamacaseapp.injection.component.ActivityComponent
import com.diogo.chamacaseapp.injection.component.ConfigPersistentComponent
import com.diogo.chamacaseapp.injection.module.ActivityModule
import com.diogo.chamacaseapp.injection.module.PresenterModule
import com.diogo.chamacaseapp.util.ApiErrorHandler
import com.diogo.chamacaseapp.util.extension.toast
import timber.log.Timber
import java.util.*
import java.util.concurrent.atomic.AtomicLong


open class BaseActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    lateinit var apiErrorHandler: ApiErrorHandler

    companion object {
        @JvmStatic
        private val KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID"

        @JvmStatic
        private val NEXT_ID = AtomicLong(0)

        @JvmStatic
        private val componentsMap = HashMap<Long, ConfigPersistentComponent>()
    }

    private var activityId: Long = 0
    lateinit var activityComponent: ActivityComponent
        get

    @Suppress("UsePropertyAccessSyntax")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        activityId = savedInstanceState?.getLong(KEY_ACTIVITY_ID) ?: NEXT_ID.getAndIncrement()

        if (componentsMap[activityId] != null)
            Timber.i("Reusing ConfigPersistentComponent id = $activityId")

        val configPersistentComponent = componentsMap.getOrPut(activityId, {
            Timber.i("Creating new ConfigPersistentComponent id=$activityId")

            val component = (applicationContext as ChamaCaseApplication).applicationComponent

            component + PresenterModule()
        })

        activityComponent = configPersistentComponent + ActivityModule(this)
        apiErrorHandler = ApiErrorHandler(this)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_ACTIVITY_ID, activityId)
    }

    override fun onDestroy() {
        if (!isChangingConfigurations) {
            Timber.i("Clearing ConfigPersistentComponent id=$activityId")
            componentsMap.remove(activityId)

            getPresenter()?.detachView()
        }

        super.onDestroy()
    }

    override fun setTitle(titleId: Int) {
        actionBar?.title = getString(titleId)
        supportActionBar?.title = getString(titleId)
    }

    fun setTitle(title: String) {
        actionBar?.title = title
        supportActionBar?.title = title
    }

    fun displayHomeAsUpEnabled() {
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    fun displayHomeAsUpEnabled(@DrawableRes iconRes: Int) {
        displayHomeAsUpEnabled()
        supportActionBar?.setHomeAsUpIndicator(iconRes)
    }

    open fun hideProgressIndicator() {
        progressDialog?.dismiss()
        progressDialog = null
    }


    open fun showProgressIndicator(
        title: String? = null,
        message: String? = null,
        cancelable: Boolean = true
    ) {
        if (progressDialog == null || progressDialog?.isShowing == false) {
            val dialog = ProgressDialog(this)

            if (title != null) {
                dialog.setTitle(title)
            }

            if (message != null) {
                dialog.setMessage(message)
            }

            dialog.isIndeterminate = true
            dialog.setCancelable(cancelable)
            dialog.show()

            progressDialog = dialog
        }
    }

    open fun showProgressIndicator() {
        showProgressIndicator(message = getString(R.string.wait), cancelable = false)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    fun getParentView(): View? {
        return findViewById(android.R.id.content)
    }

    open fun showError(error: String) {
        toast(error)
    }

    open fun showError(t: Throwable) {
        showError(apiErrorHandler.handleException(t))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setFullScreen() {
        window.statusBarColor = Color.TRANSPARENT
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    open fun getPresenter(): BasePresenter<out MvpView>? = null
}
