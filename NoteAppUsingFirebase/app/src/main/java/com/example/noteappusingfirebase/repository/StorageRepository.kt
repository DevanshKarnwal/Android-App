package com.example.noteappusingfirebase.repository

import com.example.noteappusingfirebase.models.Notes
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val NOTES_COLLECTION_REF = "notes"

class StorageRepository(){
    fun user() = Firebase.auth.currentUser

    fun hasUser() : Boolean = Firebase.auth.currentUser != null

    fun getUserId() : String = Firebase.auth.currentUser?.uid.orEmpty()

   private val notesRef : CollectionReference = Firebase.firestore.collection(NOTES_COLLECTION_REF)

    fun getUserNotes(
        userId : String
    ): Flow<Resource<List<Notes>>> = callbackFlow {
            var snapshotStateListener : ListenerRegistration? = null

        try{
            snapshotStateListener = notesRef
                .orderBy("timestamp")
                .whereEqualTo("userId", userId)
                .addSnapshotListener{
                    snapshot,e->
                    val response = if(snapshot!=null){
                        val notes = snapshot.toObjects(Notes::class.java)
                        Resource.Success(notes)
                    }else{
                        Resource.Error(throwable = e?.cause)
                    }
                        trySend(response)
                }
        }catch (e:Exception){
                trySend(Resource.Error(e.cause))
            e.printStackTrace()
        }
        awaitClose{
            snapshotStateListener?.remove()
        }
    }

    fun getNote(
        noteId : String,
        onError : (Throwable?) ->Unit,
        onSuccess : (Notes?) ->Unit,
    ){
        notesRef.document(noteId).get().addOnSuccessListener {
            onSuccess(it.toObject(Notes::class.java))
        }
            .addOnFailureListener {
                result -> onError(result.cause)
            }
    }

    fun addNote(
        userId: String,
        title: String,
        description: String,
        colorIndex: Int,
        onComplete: (Boolean) -> Unit,
        timestamp: Timestamp
    ){
        val documentId = notesRef.document().id // generate a new id
        val note = Notes(userId, title, description, colorIndex = colorIndex, documentId = documentId, timestamp = timestamp)
        notesRef.document(documentId)
            .set(note)
            .addOnCompleteListener{
                result -> onComplete(result.isSuccessful)
            }
    }

    fun deleteNote(noteId : String , onComplete : (Boolean) -> Unit){
        notesRef.document(noteId)
            .delete()
            .addOnCompleteListener{
                onComplete(it.isSuccessful)
            }
    }

    fun updateNote(noteId : String , title: String , note : String , color : Int, onResult : (Boolean) -> Unit){
        val updateData = hashMapOf<String,Any>(
            "colorIndex" to color,
            "title" to title,
            "description" to note
        )
        notesRef.document(noteId).update(updateData)
            .addOnCompleteListener{
                onResult(it.isSuccessful)
            }
    }
    fun signOut(){
        Firebase.auth.signOut()
    }

}

sealed class Resource<T>(val data: T? = null, val throwable: Throwable? = null){
    class Loading<T> : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(throwable: Throwable?) : Resource<T>(throwable = throwable)
}