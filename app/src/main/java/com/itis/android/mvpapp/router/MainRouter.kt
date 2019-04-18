package com.itis.android.mvpapp.router

import com.itis.android.mvpapp.router.initparams.TestInitParams

interface MainRouter {
    fun openTestScreen(testInitParams: TestInitParams)

    fun openProfileScreen()

    fun openGroupListScreen()

    fun openLoadTaskScreen()

    fun openGroupTaskScreen()

    fun openTaskSolutionScreen()

    fun goBack()
}