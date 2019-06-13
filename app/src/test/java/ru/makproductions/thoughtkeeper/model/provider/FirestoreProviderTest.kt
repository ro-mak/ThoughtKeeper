package ru.makproductions.thoughtkeeper.model.provider

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.makproductions.thoughtkeeper.model.entity.Note

class FirestoreProviderTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockDb = mock<FirebaseFirestore>()
    private val mockAuth = mock<FirebaseAuth>()

    private val mockUserCollection = mock<CollectionReference>()
    private val mockUserDocument = mock<DocumentReference>()
    private val mockResultCollection = mock<CollectionReference>()
    private val mockUsers = mock<CollectionReference>()

    private val mockDocument1 = mock<DocumentSnapshot>()
    private val mockDocument2 = mock<DocumentSnapshot>()
    private val mockDocument3 = mock<DocumentSnapshot>()

    private val testNotes = listOf(Note(id = "1"), Note(id = "2"), Note(id = "3"))

    private val provider = FirestoreProvider(mockAuth, mockDb)

    @Before
    fun setUp() {
    }

    @Test
    fun getCurrentUser() {
    }

    @Test
    fun subscribeToAllNotes() {
    }

    @Test
    fun getNoteById() {
    }

    @Test
    fun saveNote() {
    }

    @Test
    fun deleteNote() {
    }
}