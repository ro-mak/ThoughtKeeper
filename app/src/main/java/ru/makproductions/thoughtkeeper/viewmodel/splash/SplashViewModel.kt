package ru.makproductions.thoughtkeeper.viewmodel.splash

import kotlinx.coroutines.launch
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.errors.NoAuthException
import ru.makproductions.thoughtkeeper.view.base.BaseViewModel

class SplashViewModel(private val noteRepo: NoteRepo) : BaseViewModel<Boolean?>() {
    fun requestUser() {
        launch {
            noteRepo.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())

        }
    }
}