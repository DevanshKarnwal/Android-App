package com.example.livechat

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.livechat.data.ChatData
import com.example.livechat.data.Event
import com.example.livechat.data.USER_NODE
import com.example.livechat.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db : FirebaseFirestore,
    val storage : FirebaseStorage
) : ViewModel() {

    var inProgress = mutableStateOf(false)
    var inProgressChats = mutableStateOf(false)
    var eventMutable = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(emptyList())

init{
    val currentUser = auth.currentUser
    if(currentUser != null){
        signIn.value = currentUser !=null
        currentUser?.uid?.let{
            getUserData(it)
        }
    }
}

    fun signUp(name: String, number: String, email: String, password: String) {
        Log.d("SignUp", "Starting sign up process")
        inProgress.value = true
        if (name.isEmpty() || number.isEmpty() || email.isEmpty() || password.isEmpty()) {
            handleException(customMessage = "Please fill all fields")
            inProgress.value = false
            return
        }

        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        signIn.value = true
                        createOrUpdateProfile(name, number)
                        Log.d("SignUp", "User created successfully")
                    } else {
                        handleException(task.exception, "Sign up failed")
                    }
                    inProgress.value = false
                }
            } else {
                handleException(customMessage = "Number already exists")
                inProgress.value = false
            }
        }.addOnFailureListener { exception ->
            handleException(exception, "Error checking number")
            inProgress.value = false
        }
    }

//    fun signUp(name: String, number: String, email: String, password: String) {
//        inProgress.value = true
//        if(name.isEmpty() || number.isEmpty() || email.isEmpty() || password.isEmpty()){
//            handleException(customMessage = "Please fill all fields")
//            inProgress.value = false
//            return
//        }
//        inProgress.value = true
//        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
//            if(it.isEmpty){
//                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        signIn.value = true
//                        createOrUpdateProfile(name, number)
//                        inProgress.value = false
//                        Log.d("TAG", "User logged in")
//                    } else {
//                        inProgress.value = false
//                        handleException(it.exception, "Sign up failed")
//                    }
//                }
//            }else{
//                handleException(customMessage = "Number already exists")
//                inProgress.value = false
//            }
//        }
 //   }

    fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid ?: return
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            number = number ?: userData.value?.number,
            imageUrl = imageUrl ?: userData.value?.imageUrl
        )

        Log.d("CreateOrUpdateProfile", "Creating/updating profile for user ID: $uid")
        inProgress.value = true
        db.collection(USER_NODE).document(uid).set(userData)
            .addOnSuccessListener {
                Log.d("CreateOrUpdateProfile", "Profile created/updated successfully")
                getUserData(uid)
            }
            .addOnFailureListener { exception ->
                Log.e("CreateOrUpdateProfile", "Error creating/updating profile", exception)
                handleException(exception, "Can not retrieve user")
                inProgress.value = false
            }
    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener {
            value , error ->
            if(error != null){
                handleException(error,"Does not retrive user")
            }
            var user = value?.toObject(UserData::class.java)
            userData.value = user
            inProgress.value = false
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.d("TAGSignUpException", "Sign up exception", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errorMsg else customMessage
        eventMutable.value = Event(message)
    }
    fun logIn(email: String, password: String){
        if(email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "Please fill all the details")
            return
        }
        else{
            inProgress.value = true
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful){
                    signIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let{
                        getUserData(it)
                    }
                }else{
                    handleException(it.exception, customMessage = "Login Failed")
                }
            }
        }
    }

    fun uploadProfileImage(uri: Uri){
            uploadImage(uri){
                createOrUpdateProfile(imageUrl = it.toString())
            }
    }

    fun uploadImage(uri : Uri , onSuccss : (Uri) ->Unit){
        inProgress.value = true
        val storageref = storage.reference
        val uid = UUID.randomUUID()
        val imageRef = storageref.child("images/$uid")
        val uploadTask = imageRef.putFile(uri).addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccss)
                inProgress.value = false
        }
            .addOnFailureListener{
                handleException(it)
                inProgress.value = false
            }
    }

    fun logOut() {
        auth.signOut()
        signIn.value = false
        userData.value = null
        eventMutable.value = Event("Logged out")
    }

    fun onAddChat(it: String) {

    }

}

