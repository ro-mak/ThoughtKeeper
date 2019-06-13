package ru.makproductions.thoughtkeeper.model.provider

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.makproductions.thoughtkeeper.model.entity.Note
import ru.makproductions.thoughtkeeper.model.entity.User
import ru.makproductions.thoughtkeeper.model.errors.NoAuthException
import timber.log.Timber

class FirestoreProvider(private val firebaseAuth: FirebaseAuth, private val store: FirebaseFirestore) :
    RemoteDataProvider {
    companion object {
        private const val USERS_COLLECTION = "users"
        private const val NOTES_COLLECTION = "notes"
    }

    private val notesReference by lazy { store.collection(NOTES_COLLECTION) }
    private val currentUser
        get() = firebaseAuth.currentUser


    override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User?>().apply {
        value = currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        }
    }

    private fun getUsersNotesCollection() = currentUser?.let {
        store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override fun subscribeToAllNotes() = MutableLiveData<NoteResult>().apply {
        Timber.d("subscribeToAllNotes")
        try {
            getUsersNotesCollection().addSnapshotListener { snapshot, e ->
                try {
                    Timber.d("subscribeToAllNotes inside")
                    value = e?.let {
                        Timber.d("subscribeToAllNotes throwing =")
                        throw it
                    } ?: snapshot?.let {
                        val notes = it.documents.map { it.toObject(Note::class.java) }
                        NoteResult.NoteLoadSuccess(notes)
                    }
                } catch (t: Throwable) {
                    Timber.d("catch")
                    value = NoteResult.NoteLoadError(e)
                }
            }
        } catch (e: Exception) {
            value = NoteResult.NoteLoadError(e)
        }

    }

    override fun getNoteById(id: String) = MutableLiveData<NoteResult>().apply {
        try {
            getUsersNotesCollection().document(id).get().addOnSuccessListener { snapshot ->
                value = NoteResult.NoteLoadSuccess(snapshot.toObject(Note::class.java))
            }.addOnFailureListener { e -> value = NoteResult.NoteLoadError(e) }
        } catch (e: Exception) {
            value = NoteResult.NoteLoadError(e)
            Timber.e(e)
        }

    }

    override fun saveNote(note: Note) = MutableLiveData<NoteResult>().apply {
        try {
            getUsersNotesCollection().document(note.id).set(note).addOnSuccessListener {
                Timber.e("Note is saved")
                value = NoteResult.NoteLoadSuccess(note)
            }.addOnFailureListener {
                Timber.e("Error saving note $note, message: ${it.message}")
                value = NoteResult.NoteLoadError(it)
            }
        } catch (e: Exception) {
            value = NoteResult.NoteLoadError(e)
            Timber.e(e)
        }

    }

    override fun deleteNote(id: String) = MutableLiveData<NoteResult>().apply {
        getUsersNotesCollection().document(id).delete().addOnSuccessListener {
            value = NoteResult.NoteLoadSuccess(null)
        }.addOnFailureListener {
            value = NoteResult.NoteLoadError(it)
        }
    }
}