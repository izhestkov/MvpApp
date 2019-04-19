package com.itis.android.mvpapp.presentation.ui.main

import com.arellomobile.mvp.InjectViewState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.itis.android.mvpapp.data.util.CredentialStorage
import com.itis.android.mvpapp.router.MainRouter
import com.itis.android.mvpapp.presentation.base.BasePresenter
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

@InjectViewState
class MainPresenter
@Inject constructor() : BasePresenter<MainView>() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var mainRouter: MainRouter

    @Inject
    lateinit var preferences: CredentialStorage

    private var firebaseAuth: FirebaseAuth? = null

    private var firebaseUser: FirebaseUser? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        checkAuth()
    }

    fun openGroupListScreen() {
        mainRouter.openGroupListScreen()
    }

    private fun checkAuth() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth?.currentUser

        if (firebaseUser == null) {
            viewState.startSignIn()
        } else {
            viewState.signedIn()
        }
    }

    fun setNavigator(navigator: Navigator) {
        navigatorHolder.setNavigator(navigator)
    }

    fun removeNavigator() {
        navigatorHolder.removeNavigator()
    }

    fun onGroups() {
        mainRouter.openGroupListScreen()
    }

    fun onMessages() {
        mainRouter.openMessagesScreen()
    }

    fun onProfile() {
        mainRouter.openProfileScreen()
    }
}