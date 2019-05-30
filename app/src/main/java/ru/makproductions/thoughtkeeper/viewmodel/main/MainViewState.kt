package ru.makproductions.thoughtkeeper.viewmodel.main

import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.view.base.BaseViewState

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null) :
    BaseViewState<List<Note>?>(notes, error)