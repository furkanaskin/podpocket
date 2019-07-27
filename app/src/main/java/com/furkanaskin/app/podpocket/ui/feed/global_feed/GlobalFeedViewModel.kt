package com.furkanaskin.app.podpocket.ui.feed.global_feed

import androidx.databinding.ObservableArrayList
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.db.AppDatabase
import com.furkanaskin.app.podpocket.db.entities.PostEntity
import com.furkanaskin.app.podpocket.model.Post
import com.furkanaskin.app.podpocket.service.PodpocketAPI
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.doAsync
import javax.inject.Inject

/**
 * Created by Furkan on 2019-05-26
 */

class GlobalFeedViewModel @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase) {
    var posts: ObservableArrayList<Post?> = ObservableArrayList()

    init {
        getGlobalPosts()
    }

    private fun getGlobalPosts() {
        val postsRef = FirebaseDatabase.getInstance().getReference("posts")
        postsRef.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val allPosts = snapshot.children
                posts.clear()

                allPosts.reversed().forEachIndexed { _, dataSnapshot ->
                    val post = dataSnapshot.getValue(Post::class.java)
                    posts.add(post)
                    post?.let { writePostToDB(it) }
                }
            }
        })
    }

    private fun writePostToDB(post: Post) {
        doAsync {
            val postEntity = PostEntity(post)
            db?.postsDao()?.insertPost(postEntity)
        }
    }
}