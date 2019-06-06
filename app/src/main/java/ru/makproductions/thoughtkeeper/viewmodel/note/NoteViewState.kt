package ru.makproductions.thoughtkeeper.viewmodel.note

import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.view.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error) {
    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}