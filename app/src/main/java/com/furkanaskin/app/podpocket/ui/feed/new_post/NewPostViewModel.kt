package com.furkanaskin.app.podpocket.ui.feed.new_post

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.model.Post
import com.furkanaskin.app.podpocket.model.User
import com.google.firebase.database.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

/**
 * Created by Furkan on 2019-05-26
 */

class NewPostViewModel(app: Application) : BaseViewModel(app) {
    lateinit var currentUser: User
    var postText: ObservableField<String> = ObservableField("")
    var pushPostSuccess: ObservableField<Boolean> = ObservableField(false)

    var databaseReference: DatabaseReference? = null

    init {
        (app as? Podpocket)?.component?.inject(this)
        //get reference to our db
        databaseReference = FirebaseDatabase.getInstance().reference
        getUser() // This is required because BaseViewModel not reached user data yet.
        createFirebaseListener()
    }

    fun shareClicked() {
        if (postText.get() != null && postText.get()!!.length > 5 && databaseReference != null) {
            val newPost = Post(
                    currentUser.podcaster,
                    convertDate(LocalDate.now()),
                    currentUser.verifiedUser,
                    currentUser.userName,
                    postText.get(),
                    createUniquePostId(),
                    user?.uniqueId,
                    currentLocation)
            insertPostToFirebase(newPost)
        }
    }


    private fun insertPostToFirebase(post: Post) {
        databaseReference?.child("posts")?.child(post.postId
                ?: "")?.setValue(post)?.addOnCompleteListener { task ->

            if (task.isSuccessful) {
                pushPostSuccess.set(true)
            } else {
                pushPostSuccess.set(false)
            }
        }

    }

    private fun createFirebaseListener() {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = snapshot.children
                userList.forEachIndexed { _, dataSnapshot ->
                    if (dataSnapshot.key == user?.uniqueId) {
                        currentUser = dataSnapshot.getValue(User::class.java)!!
                    }
                }
            }

        })
    }

    private fun convertDate(date: LocalDate) = date.format(DateTimeFormatter.ISO_DATE)

    private fun createUniquePostId(): String {
        return "${(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()))}${user?.uniqueId}"
    }
}