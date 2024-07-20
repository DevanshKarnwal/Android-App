package com.example.livechat

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.livechat.data.CHATS
import com.example.livechat.data.ChatData
import com.example.livechat.data.ChatUser
import com.example.livechat.data.Event
import com.example.livechat.data.MESSAGE
import com.example.livechat.data.Message
import com.example.livechat.data.USER_NODE
import com.example.livechat.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {

    var inProgress = mutableStateOf(false)
    var inProgressChats = mutableStateOf(false)
    var eventMutable = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(emptyList())
    var chatMessages = mutableStateOf<List<Message>>(listOf())
    var inProgressChatMessage = mutableStateOf(false)
    var currentChatMessageListner: ListenerRegistration? = null

    init {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            signIn.value = currentUser != null
            currentUser?.uid?.let {
                getUserData(it)
            }
        }
    }

    fun populateMessage(chatId: String) {
        inProgressChatMessage.value = true
        currentChatMessageListner =
            db.collection(CHATS).document(chatId).collection(MESSAGE).addSnapshotListener {
                value, error ->
                    if(error != null){
                        handleException(error)
                    }
                if(value != null){
                    chatMessages.value = value.documents.mapNotNull {
                        it.toObject(Message::class.java)
                    }.sortedBy { it.timeStamp }
                    inProgressChatMessage.value = false
                }
            }
    }

    fun depopulateMessage(){
        chatMessages.value = emptyList()
        currentChatMessageListner = null
    }

    fun populateChats() {
        inProgressChats.value = true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId),
            )
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
            if (value != null) {
                chats.value = value.documents.mapNotNull {
                    it.toObject(ChatData::class.java)
                }
                inProgressChats.value = false
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

        db.collection(USER_NODE).whereEqualTo("number", number).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
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
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error, "Does not retrive user")
            }
            var user = value?.toObject(UserData::class.java)
            userData.value = user
            inProgress.value = false
            populateChats()
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.d("TAGSignUpException", "Sign up exception", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errorMsg else customMessage
        eventMutable.value = Event(message)
    }

    fun logIn(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill all the details")
            return
        } else {
            inProgress.value = true
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    signIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                } else {
                    handleException(it.exception, customMessage = "Login Failed")
                }
            }
        }
    }

    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) {
            createOrUpdateProfile(imageUrl = it.toString())
        }
    }

    fun uploadImage(uri: Uri, onSuccss: (Uri) -> Unit) {
        inProgress.value = true
        val storageref = storage.reference
        val uid = UUID.randomUUID()
        val imageRef = storageref.child("images/$uid")
        val uploadTask = imageRef.putFile(uri).addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccss)
            inProgress.value = false
        }
            .addOnFailureListener {
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

    fun onAddChat(number: String) {

        if (number.isEmpty() or !number.isDigitsOnly()) {
            handleException(customMessage = "Please enter a valid number and only number")
            return
        } else {
            db.collection(CHATS).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.number", number),
                        Filter.equalTo("user2.number", userData.value?.number)
                    ),
                    Filter.and(
                        Filter.equalTo("user2.number", userData.value?.number),
                        Filter.equalTo("user1.number", number)
                    )
                )
            ).get().addOnSuccessListener { it ->
                if (it.isEmpty) {
                    db.collection(USER_NODE).whereEqualTo("number", number).get()
                        .addOnSuccessListener {
                            if (it.isEmpty) {
                                handleException(customMessage = "Number not found")
                            } else {
                                val chatPartner = it.toObjects(UserData::class.java)[0]
                                val id = db.collection(CHATS).document().id
                                var chat = ChatData(
                                    chatId = id,
                                    ChatUser(
                                        userData.value?.userId,
                                        userData.value?.name,
                                        userData.value?.imageUrl,
                                        userData.value?.number
                                    ),
                                    ChatUser(
                                        chatPartner.userId,
                                        chatPartner.name,
                                        chatPartner.imageUrl,
                                        chatPartner.number
                                    )
                                )
                                db.collection(CHATS).document(id).set(chat)
                            }
                        }.addOnFailureListener {
                            handleException(it)
                        }
                } else {
                    handleException(customMessage = "Chat already exists")
                }
            }
        }

    }

    fun onSendReply(
        chatId: String, message: String
    ) {

        val time = Calendar.getInstance().time.toString()
        var msg: Message = Message(userData.value?.userId, message, time)
        db.collection(CHATS).document(chatId).collection(MESSAGE).document().set(msg)
    }
}



