package ru.makproductions.thoughtkeeper.model.provider

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.errors.NoAuthException
import timber.log.Timber

class FirestoreProviderTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockDb = mock<FirebaseFirestore>()
    private val mockAuth = mock<FirebaseAuth>()

    private val mockUserCollection = mock<CollectionReference>()
    private val mockUserDocument = mock<DocumentReference>()
    private val mockResultCollection = mock<CollectionReference>()
    private val mockUser = mock<FirebaseUser>()

    private val mockDocument1 = mock<DocumentSnapshot>()
    private val mockDocument2 = mock<DocumentSnapshot>()
    private val mockDocument3 = mock<DocumentSnapshot>()

    private val testNotes = listOf(Note(id = "1"), Note(id = "2"), Note(id = "3"))

    private val provider = FirestoreProvider(mockAuth, mockDb)

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
        reset(
            mockUserCollection,
            mockUserDocument,
            mockResultCollection,
            mockUser,
            mockDocument1,
            mockDocument2,
            mockDocument3
        )
        whenever(mockAuth.currentUser).thenReturn(mockUser)
        whenever(mockUser.uid).thenReturn("")
        whenever(mockDb.collection(any())).thenReturn(mockUserCollection)
        whenever(mockUserCollection.document(any())).thenReturn(mockUserDocument)
        whenever(mockUserDocument.collection(any())).thenReturn(mockResultCollection)
        whenever(mockDocument1.toObject(Note::class.java)).thenReturn(testNotes[0])
        whenever(mockDocument2.toObject(Note::class.java)).thenReturn(testNotes[1])
        whenever(mockDocument3.toObject(Note::class.java)).thenReturn(testNotes[2])
    }

    @Test
    fun `should throw NoAuthException if no auth`() {
        var result: Any? = null
        whenever(mockAuth.currentUser).thenReturn(null)
        provider.subscribeToAllNotes().observeForever {
            result = (it as NoteResult.NoteLoadError).throwable
        }
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `subscribeToAllNotes returns notes`() {
        var result: List<Note>? = null
        val mockSnapshot = mock<QuerySnapshot>()
        val captor = argumentCaptor<EventListener<QuerySnapshot>>()
        whenever(mockSnapshot.documents).thenReturn(listOf(mockDocument1, mockDocument2, mockDocument3))
        whenever(mockResultCollection.addSnapshotListener(captor.capture())).thenReturn(mock())
        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.NoteLoadSuccess<List<Note>>)?.data
        }
        captor.firstValue.onEvent(mockSnapshot, null)
        assertEquals(testNotes, result)
    }

    @Test
    fun `subscribeToAllNotes returns error`() {
        var result: Throwable? = null
        val testError = mock<FirebaseFirestoreException>()
        val captor = argumentCaptor<EventListener<QuerySnapshot>>()
        whenever(mockResultCollection.addSnapshotListener(captor.capture())).thenReturn(mock())
        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.NoteLoadError)?.throwable
        }

        captor.firstValue.onEvent(null, testError)
        assertEquals(testError, result)
    }

    @Test
    fun `saveNote calls set`() {
        val mockDocumentReference = mock<DocumentReference>()
        whenever(mockResultCollection.document(testNotes[0].id)).thenReturn(mockDocumentReference)
        provider.saveNote(testNotes[0])
        verify(mockDocumentReference, times(1)).set(testNotes[0])
    }

    @Test
    fun `saveNote returns note`() {
        var result: Note? = null
        val mockDocumentReference = mock<DocumentReference>()
        val captor = argumentCaptor<OnSuccessListener<Void>>()
        val mockTask = mock<Task<Void>>()
        whenever(mockDocumentReference.set(testNotes[0])).thenReturn(mockTask)
        whenever(mockTask.addOnSuccessListener(captor.capture())).thenReturn(mockTask)
        whenever(mockResultCollection.document(testNotes[0].id)).thenReturn(mockDocumentReference)
        provider.saveNote(testNotes[0]).observeForever {
            result = (it as? NoteResult.NoteLoadSuccess<Note>)?.data
        }
        captor.firstValue.onSuccess(null)
        assertEquals(testNotes[0], result)
    }

    @Test
    fun `deleteNote calls document delete`() {
        val mockDocumentReference = mock<DocumentReference>()
        val mockTask = mock<Task<Void>>()
        whenever(mockTask.addOnSuccessListener(any())).thenReturn(mockTask)
        whenever(mockTask.addOnFailureListener(any())).thenReturn(mockTask)
        whenever(mockDocumentReference.delete()).thenReturn(mockTask)
        whenever(mockResultCollection.document(testNotes[0].id)).thenReturn(mockDocumentReference)
        provider.deleteNote(testNotes[0].id)

        verify(mockDocumentReference, times(1)).delete()
    }
}