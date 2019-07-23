package com.furkanaskin.app.podpocket.ui.feed.locale_feed

import androidx.databinding.ObservableArrayList
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.entities.PostEntity
import com.furkanaskin.app.podpocket.model.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.doAsync

/**
 * Created by Furkan on 2019-05-26
 */

class LocaleFeedViewModel : BaseViewModel() {

    init {
        getLocalePosts()
    }

    var posts: ObservableArrayList<Post?> = ObservableArrayList()

    private fun getLocalePosts() {
        val postsRef = FirebaseDatabase.getInstance().getReference("posts")
        postsRef.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val localePosts = snapshot.children
                posts.clear()

                localePosts.reversed().forEachIndexed { _, dataSnapshot ->
                    if (dataSnapshot.getValue(Post::class.java)?.region == currentLocation) {
                        val post = dataSnapshot.getValue(Post::class.java)
                        posts.add(post)
                        post?.let { writePostToDB(it) }
                    }
                }
            }
        })
    }

    private fun writePostToDB(post: Post) {
        doAsync {
            val postEntity = PostEntity(post)
            db.postsDao().insertPost(postEntity)
        }
    }
}