package ru.makproductions.thoughtkeeper.view.splash

import android.os.Handler
import org.koin.android.viewmodel.ext.android.viewModel
import ru.makproductions.thoughtkeeper.view.base.BaseActivity
import ru.makproductions.thoughtkeeper.view.main.MainActivity
import ru.makproductions.thoughtkeeper.viewmodel.splash.SplashViewModel

class SplashActivity : BaseActivity<Boolean?>() {

    companion object {
        private const val START_DELAY = 500L
    }

    override val viewModel: SplashViewModel by viewModel()
    override val layoutRes: Int? = null

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean?) {
        data?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }
}