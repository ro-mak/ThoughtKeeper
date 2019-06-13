package ru.makproductions.thoughtkeeper.viewmodel.splash

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import ru.makproductions.thoughtkeeper.model.NoteRepo
import timber.log.Timber

class SplashViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    private val mockRepository = mock<NoteRepo>()
    private lateinit var viewModel: SplashViewModel

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
        whenever(mockRepository.getCurrentUser()).thenReturn(mock())
        viewModel = SplashViewModel(mockRepository)
    }

    @Test
    fun `requestUser should invoke noteRepo getCurrentUser`() {
        viewModel.requestUser()
        verify(mockRepository, times(1)).getCurrentUser()
    }
}