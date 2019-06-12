package ru.makproductions.thoughtkeeper.viewmodel.splash

import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.errors.NoAuthException
import ru.makproductions.thoughtkeeper.view.base.BaseViewModel

class SplashViewModel(private val noteRepo: NoteRepo) : BaseViewModel<Boolean?, SplashViewState>() {
    fun requestUser() {
        noteRepo.getCurrentUser().observeForever {
            viewStateLiveData.value = it?.let { SplashViewState(authenticated = true) }
                ?: SplashViewState(error = NoAuthException())
        }
    }
}