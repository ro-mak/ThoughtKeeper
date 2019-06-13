package ru.makproductions.thoughtkeeper.viewmodel.main

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import ru.makproductions.thoughtkeeper.model.NoteRepo
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.provider.NoteResult
import timber.log.Timber

class MainViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    private val mockRepository = mock<NoteRepo>()
    private val notesLiveData = MutableLiveData<NoteResult>()
    private lateinit var viewModel: MainViewModel

    companion object {

        @JvmStatic
        @BeforeClass
        fun setupClass() {
            Timber.plant(Timber.DebugTree())
            Timber.d("setup class")
        }
    }

    @Before
    fun setUp() {
        reset(mockRepository)
        whenever(mockRepository.getNotes()).thenReturn(notesLiveData)
        viewModel = MainViewModel(mockRepository)
    }


    @Test
    fun `should call getNotes once`() {
        verify(mockRepository, times(1)).getNotes()
    }

    @Test
    fun `should return notes`() {
        var result: List<Note>? = null
        val testData = listOf(Note("1"), Note("2"))
        viewModel.getViewState().observeForever {
            result = it?.data
        }
        notesLiveData.value = NoteResult.NoteLoadSuccess(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        notesLiveData.value = NoteResult.NoteLoadError(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`() {
        val method = viewModel::class.java.getDeclaredMethod("onCleared")
        method.isAccessible = true
        method.invoke(viewModel)
        assertFalse(notesLiveData.hasActiveObservers())
    }
}