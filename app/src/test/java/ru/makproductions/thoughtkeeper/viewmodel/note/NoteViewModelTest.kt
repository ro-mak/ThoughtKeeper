//package ru.makproductions.thoughtkeeper.viewmodel.note
//
//import android.arch.core.executor.testing.InstantTaskExecutorRule
//import android.arch.lifecycle.MutableLiveData
//import com.nhaarman.mockitokotlin2.*
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.BeforeClass
//import org.junit.Rule
//import org.junit.Test
//import ru.makproductions.thoughtkeeper.model.NoteRepo
//import ru.makproductions.thoughtkeeper.model.entity.Note
//import ru.makproductions.thoughtkeeper.model.provider.NoteResult
//import timber.log.Timber
//
//class NoteViewModelTest {
//
//    @get:Rule
//    val taskExecutorRule = InstantTaskExecutorRule()
//    private val mockRepository = mock<NoteRepo>()
//    private val notesLiveData = MutableLiveData<NoteResult>()
//    private val repositoryNote = MutableLiveData<NoteResult>()
//    private lateinit var viewModel: NoteViewModel
//
//    companion object {
//
//        @JvmStatic
//        @BeforeClass
//        fun setupClass() {
//            Timber.plant(Timber.DebugTree())
//            Timber.d("setup class")
//        }
//    }
//
//    @Before
//    fun setUp() {
//        reset(mockRepository)
////        whenever(mockRepository.getNotes()).thenReturn(notesLiveData)
////        whenever(mockRepository.getNoteById(any())).thenReturn(repositoryNote)
////        whenever(mockRepository.deleteNote(any())).thenReturn(notesLiveData.apply {
////            value = NoteResult.NoteLoadSuccess(NoteData.Data(isDeleted = true))
////        })
//        viewModel = NoteViewModel(mockRepository)
//    }
//
//    @Test
//    fun `load note should invoke noteRepo getNoteById`() {
//        val testId = "1"
//        viewModel.loadNote(testId)
//        verify(mockRepository, times(1)).getNoteById(testId)
//    }
//
//    @Test
//    fun `save note should set note`() {
//        val testId = "1"
//        val testNote = Note(testId)
//        viewModel.saveNote(testNote)
//        assertEquals(testNote, viewModel.viewStateLiveData.value?.data.note)
//    }
//
//    @Test
//    fun `delete should invoke noteRepo deleteNote`() {
//        val testId = "1"
//        val testNote = Note(testId)
//        viewModel.saveNote(testNote)
//        viewModel.deleteNote()
//        verify(mockRepository, times(1)).deleteNote(any())
//    }
//
//
//    @Test
//    fun `should return error when repository note is error`() {
//        var result: Throwable? = null
////        val testData = Throwable("error")
////        viewModel.getViewState().observeForever {
////            result = it.error
//        }
//        viewModel.loadNote("1")
//        repositoryNote.value = NoteResult.NoteLoadError(testData)
//        assertEquals(testData, result)
//    }
//}