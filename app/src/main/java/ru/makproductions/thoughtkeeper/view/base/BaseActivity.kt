package ru.makproductions.thoughtkeeper.view.base

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import ru.makproductions.thoughtkeeper.R
import ru.makproductions.thoughtkeeper.model.errors.NoAuthException
import timber.log.Timber

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {

    companion object {
        private const val AUTH_REQUEST_CODE = 9182
    }
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int?
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let { setContentView(it) }
        viewModel.getViewState().observe(this, object : Observer<S> {
            override fun onChanged(t: S?) {
                if (t == null) return
                if (t.error != null) {
                    renderError(t.error)
                    return
                }
                renderData(t.data)
            }
        })
    }

    protected fun renderError(error: Throwable?) {
        when (error) {
            is NoAuthException -> {
                startLogin()
            }
            else -> {
                error?.let {
                    Timber.e(it)
                    it.message?.let { showError(it) }
                }
            }
        }
    }

    private fun startLogin() {
        val provider = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.ic_launcher_foreground)
                .setTheme(R.style.LoginStyle)
                .setAvailableProviders(provider)
                .build(), AUTH_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTH_REQUEST_CODE && resultCode != Activity.RESULT_OK) {
            finish()
        }
    }

    protected fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    abstract fun renderData(data: T)
}