package com.furkanaskin.app.podpocket.ui.feed.new_post

import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.db.entities.UserEntity
import com.furkanaskin.app.podpocket.model.Post
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Furkan on 2019-05-26
 */

class NewPostViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) :
    BaseViewModel(api, appDatabase) {
    var postText: ObservableField<String> = ObservableField("")
    var pushPostSuccess: ObservableField<Boolean> = ObservableField(false)

    var databaseReference: DatabaseReference? = null
    var currentUser: UserEntity? = null

    init {
        // get reference to our db
        databaseReference = FirebaseDatabase.getInstance().reference
        getUser() // This is required because BaseViewModel not reached user data yet.
        createFirebaseListener()
    }

    fun shareClicked() {
        if (postText.get() != null && postText.get()!!.length > 5 && databaseReference != null) {
            val newPost = Post(
                user?.podcaster,
                System.currentTimeMillis().toString(),
                user?.verifiedUser,
                user?.userName,
                postText.get(),
                createUniquePostId(),
                user?.uniqueId,
                currentLocation
            )
            insertPostToFirebase(newPost)
        }
    }

    private fun insertPostToFirebase(post: Post) {
        databaseReference?.child("posts")?.child(
            post.postId
                ?: ""
        )?.setValue(post)?.addOnCompleteListener { task ->

            if (task.isSuccessful) {
                pushPostSuccess.set(true)
            } else {
                pushPostSuccess.set(false)
            }
        }
    }

    private fun createFirebaseListener() {
        val usersRef =
            FirebaseDatabase.getInstance().getReference("users").child("${user?.uniqueId}")
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(UserEntity::class.java)
            }
        })
    }

    private fun createUniquePostId(): String {
        return "${(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()))}${user?.uniqueId}"
    }
}