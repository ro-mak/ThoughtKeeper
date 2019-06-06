package ru.makproductions.thoughtkeeper.viewmodel.splash

import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.errors.NoAuthException
import ru.makproductions.thoughtkeeper.view.base.BaseViewModel

class SplashViewModel : BaseViewModel<Boolean?, SplashViewState>() {
    fun requestUser() {
        NoteRepo.getCurrentUser().observeForever {
            viewStateLiveData.value = it?.let { SplashViewState(authenticated = true) }
                ?: SplashViewState(error = NoAuthException())
        }
    }
}