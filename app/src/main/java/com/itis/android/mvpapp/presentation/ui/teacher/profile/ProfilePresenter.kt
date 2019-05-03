package com.itis.android.mvpapp.presentation.ui.teacher.profile

import com.arellomobile.mvp.InjectViewState
import com.google.firebase.auth.FirebaseAuth
import com.itis.android.mvpapp.data.repository.TasksRepository
import com.itis.android.mvpapp.data.repository.TeacherRepository
import com.itis.android.mvpapp.presentation.base.BasePresenter
import com.itis.android.mvpapp.presentation.model.TaskModel
import com.itis.android.mvpapp.presentation.rx.transformer.PresentationObservableTransformer
import javax.inject.Inject

@InjectViewState
class ProfilePresenter
@Inject constructor() : BasePresenter<ProfileView>() {

    @Inject
    lateinit var teacherRepository: TeacherRepository

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var tasksRepository: TasksRepository

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        test()
        loadProfile()
    }

    fun onLogout() {
        firebaseAuth.signOut()
    }

    fun onRetry() {
        loadProfile()
    }

    private fun loadProfile() {
        teacherRepository
                .getTeacherInfoObservable()
                .compose(PresentationObservableTransformer())
                .doOnSubscribe {
                    viewState.showProgress()
                    viewState.hideRetry()
                }
                .doAfterTerminate {
                    viewState.hideProgress()
                }
                .subscribe({
                    viewState.showProfile(it)
                }, {
                    viewState.showRetry("Ошибка")
                })
                .disposeWhenDestroy()
    }

    fun test() {
        tasksRepository
                .uploadTask(TaskModel())
                .subscribe({
                    val result = "asd"
                },{
                    it.printStackTrace()
                })
                .disposeWhenDestroy()
    }
}