package ru.makproductions.thoughtkeeper.model.provider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.Channel
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.entity.User
import ru.makproductions.thoughtkeeper.model.errors.NoAuthException
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreProvider(private val firebaseAuth: FirebaseAuth, private val store: FirebaseFirestore) :
    RemoteDataProvider {
    companion object {
        private const val USERS_COLLECTION = "users"
        private const val NOTES_COLLECTION = "notes"
    }

    private val currentUser
        get() = firebaseAuth.currentUser


    override suspend fun getCurrentUser(): User? = suspendCoroutine { continuation ->
        continuation.resume(currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        })
    }

    private fun getUsersNotesCollection() = currentUser?.let {
        store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override fun subscribeToAllNotes() = Channel<NoteResult>(Channel.CONFLATED).apply {
        var registration: ListenerRegistration? = null
        Timber.d("subscribeToAllNotes")
        try {
            registration = getUsersNotesCollection().addSnapshotListener { snapshot, e ->
                try {
                    Timber.d("subscribeToAllNotes inside")
                    val value = e?.let {
                        Timber.d("subscribeToAllNotes throwing =")
                        throw it
                    } ?: snapshot?.let {
                        val notes = it.documents.map { it.toObject(Note::class.java) }
                        NoteResult.NoteLoadSuccess(notes)
                    }
                    value?.let { offer(it) }
                } catch (t: Throwable) {
                    Timber.d("catch")
                    offer(NoteResult.NoteLoadError(t))
                }
            }
        } catch (e: Exception) {
            offer(NoteResult.NoteLoadError(e))
        }
        invokeOnClose { registration?.remove() }
    }

    override suspend fun getNoteById(id: String): Note = suspendCoroutine { continuation ->
        Timber.e("Load note by id")
        try {
            getUsersNotesCollection().document(id).get().addOnSuccessListener { snapshot ->
                snapshot.toObject(Note::class.java)?.let {
                    continuation.resume(it)
                } ?: Timber.e("getNoteById null")

            }.addOnFailureListener { e -> continuation.resumeWithException(e) }
        } catch (e: Exception) {
            continuation.resumeWithException(e)
            Timber.e(e)
        }

    }

    override suspend fun saveNote(note: Note): Note = suspendCoroutine { continuation ->
        Timber.e("Trying to save")
        try {
            getUsersNotesCollection().document(note.id)
                .set(note)
                .addOnSuccessListener {
                    Timber.e("Note is saved")
                    continuation.resume(note)
                }.addOnFailureListener {
                    Timber.e("Error saving note $note, message: ${it.message}")
                    continuation.resumeWithException(it)
                }
        } catch (e: Exception) {
            continuation.resumeWithException(e)
            Timber.e(e)
        }

    }

    override suspend fun deleteNote(id: String): Unit = suspendCoroutine { continuation ->
        getUsersNotesCollection().document(id).delete().addOnSuccessListener {
            continuation.resume(Unit)
        }.addOnFailureListener {
            continuation.resumeWithException(it)
        }
    }
}