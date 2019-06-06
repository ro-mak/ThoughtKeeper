package ru.makproductions.thoughtkeeper.viewmodel.splash

import ru.makproductions.thoughtkeeper.view.base.BaseViewState

class SplashViewState(authenticated: Boolean? = null, error: Exception? = null) :
    BaseViewState<Boolean?>(authenticated, error)