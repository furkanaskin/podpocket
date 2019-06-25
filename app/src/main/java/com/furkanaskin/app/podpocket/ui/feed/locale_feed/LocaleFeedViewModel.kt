package com.furkanaskin.app.podpocket.ui.feed.locale_feed

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

class LocaleFeedViewModel(app: Application) : BaseViewModel(app) {

    init {
        (app as? Podpocket)?.component?.inject(this)
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
                    if (dataSnapshot.getValue(Post::class.java)?.region == currentLocation)
                        posts.add(dataSnapshot.getValue(Post::class.java))

                    // Write locale post data to HashMap
                }
            }
        })
    }
}