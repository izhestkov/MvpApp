package com.itis.android.mvpapp.data.repository.impl

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.itis.android.mvpapp.data.network.pojo.firebase.response.UserItem
import com.itis.android.mvpapp.data.network.isOnline
import com.itis.android.mvpapp.data.repository.UserRepository
import com.itis.android.mvpapp.presentation.model.*
import io.reactivex.Single
import io.reactivex.subjects.AsyncSubject
import java.lang.Exception
import java.lang.IllegalArgumentException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor() : UserRepository {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var firebaseDB: FirebaseDatabase

    @Inject
    lateinit var context: Context

    private var firebaseUser: FirebaseUser? = null

    override fun getUser(): Single<UserItem> {
        firebaseUser = firebaseAuth.currentUser
        val ref = firebaseDB.getReference("users")

        val subject = AsyncSubject.create<Pair<String, UserItem>>()

        firebaseUser?.uid.let { ref.child(it.toString()) }
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user = dataSnapshot.getValue(UserItem::class.java)
                        subject.onNext(Pair("", user
                                ?: throw IllegalArgumentException("firebase user is null")))
                        subject.onComplete()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        subject.onNext(Pair(error.message, UserItem()))
                        subject.onComplete()
                    }
                })

        return Single.just(isOnline(context)).flatMap { isConnected ->
            if (isConnected) {
                subject.singleOrError().flatMap { (errorMessage, user) ->
                    when {
                        errorMessage.isEmpty() -> Single.just(user)
                        else -> Single.error(Exception())
                    }
                }
            } else {
                Single.error(Exception())
            }
        }
    }

    override fun getUserById(id: String): Single<UserItem> {
        firebaseUser = firebaseAuth.currentUser
        val ref = firebaseDB.getReference("users").child(id)

        val subject = AsyncSubject.create<Pair<String, UserItem>>()

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(UserItem::class.java)
                subject.onNext(Pair("", user ?: UserItem()))

                subject.onComplete()
            }

            override fun onCancelled(error: DatabaseError) {
                subject.onNext(Pair(error.message, UserItem()))
                subject.onComplete()
            }
        })

        return subject.singleOrError().flatMap { (errorMessage, user) ->
            when {
                errorMessage.isEmpty() -> Single.just(user)
                else -> Single.error(Exception())
            }
        }
    }
}