package com.itis.android.mvpapp.presentation.ui.student.tasks

import android.support.v4.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class StudentTasksScreen: SupportAppScreen() {

    override fun getFragment(): Fragment {
        return StudentTasksFragment.getInstance()
    }

    override fun getScreenKey(): String {
        return StudentTasksFragment::class.java.name
    }

}