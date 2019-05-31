package com.furkanaskin.app.podpocket.ui.feed.global_feed

import android.app.Application
import androidx.databinding.ObservableArrayList
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.model.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by Furkan on 2019-05-26
 */

class GlobalFeedViewModel(app: Application) : BaseViewModel(app) {
    var posts: ObservableArrayList<Post?> = ObservableArrayList()

    init {
        (app as? Podpocket)?.component?.inject(this)
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
                    posts.add(dataSnapshot.getValue(Post::class.java))
                    // Write all post data to HashMap
                }
            }
        })
    }
}