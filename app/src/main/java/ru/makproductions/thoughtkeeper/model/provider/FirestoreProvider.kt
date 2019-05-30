package ru.makproductions.thoughtkeeper.model.provider

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import ru.makproductions.thoughtkeeper.model.entity.Note
import timber.log.Timber

class FirestoreProvider : RemoteDataProvider {
    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener { snapshot, e ->
            if (e != null) {
                result.value = NoteResult.NoteLoadError(e)
            } else if (snapshot != null) {
                val notes = mutableListOf<Note>()
                for (doc: QueryDocumentSnapshot in snapshot) {
                    notes.add(doc.toObject(Note::class.java))
                }
                result.value = NoteResult.NoteLoadSuccess(notes)
            }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get().addOnSuccessListener { snapshot ->
            result.value = NoteResult.NoteLoadSuccess(snapshot.toObject(Note::class.java))
        }.addOnFailureListener { e -> result.value = NoteResult.NoteLoadError(e) }
        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(note.id).set(note).addOnSuccessListener {
            Timber.e("Note is saved")
            result.value = NoteResult.NoteLoadSuccess(note)
        }.addOnFailureListener {
            Timber.e("Error saving note $note, message: ${it.message}")
            result.value = NoteResult.NoteLoadError(it)
        }
        return result
    }

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    private val store = FirebaseFirestore.getInstance()
    private val notesReference = store.collection(NOTES_COLLECTION)


}