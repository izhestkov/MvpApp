package com.itis.android.mvpapp.presentation.ui.teacher

import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.itis.android.mvpapp.R
import com.itis.android.mvpapp.presentation.base.BaseActivity
import com.itis.android.mvpapp.presentation.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.terrakok.cicerone.Navigator
import javax.inject.Inject
import javax.inject.Provider

class TeacherActivity : BaseActivity(), TeacherView {

    @InjectPresenter
    lateinit var presenter: TeacherPresenter

    @Inject
    lateinit var presenterProvider: Provider<TeacherPresenter>

    @Inject
    lateinit var navigator: Navigator

    @ProvidePresenter
    fun providePresenter(): TeacherPresenter = presenterProvider.get()

    override val mainContentLayout: Int
        get() = R.layout.activity_main

    override val enableBackArrow: Boolean
        get() = true

    val fragmentContainer: Int = R.id.main_wrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        menu_bottom_bar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main_menu_groups -> presenter.onGroups()
                R.id.main_menu_messages -> presenter.onMessages()
                else -> presenter.onProfile()
            }
            true
        }
    }

    override fun onResumeFragments() {
        presenter.setNavigator(navigator)
        super.onResumeFragments()
    }

    override fun onPause() {
        presenter.removeNavigator()
        super.onPause()
    }

    override fun startSignIn() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun signedIn() {
        presenter.onGroups()
    }
}