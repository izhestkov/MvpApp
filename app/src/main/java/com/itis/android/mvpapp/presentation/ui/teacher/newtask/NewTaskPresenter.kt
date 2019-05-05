package com.itis.android.mvpapp.presentation.ui.teacher.newtask

import com.arellomobile.mvp.InjectViewState
import com.itis.android.mvpapp.data.repository.DisciplinesRepository
import com.itis.android.mvpapp.data.repository.TasksRepository
import com.itis.android.mvpapp.presentation.base.BasePresenter
import com.itis.android.mvpapp.presentation.model.FileModel
import com.itis.android.mvpapp.presentation.model.UploadTaskModel
import com.itis.android.mvpapp.presentation.rx.transformer.PresentationSingleTransformer
import com.itis.android.mvpapp.router.MainRouter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class NewTaskPresenter @Inject constructor() : BasePresenter<NewTaskView>() {

    @Inject
    lateinit var disciplinesRepository: DisciplinesRepository

    @Inject
    lateinit var tasksRepository: TasksRepository

    @Inject
    lateinit var router: MainRouter

    private var taskName: String? = null
    private var taskDescription: String? = null
    private var taskDeadline: String? = null
    private var taskGroup: String? = null
    private var taskFile: FileModel? = null
    private var taskDiscipline: String? = null

    fun init(groupName: String) {
        this.taskGroup = groupName
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showTaskGroup(taskGroup.orEmpty())
        getDisciplines()
        checkButtonState()
    }

    private fun getDisciplines() {
        disciplinesRepository
                .getDisciplinesSingle()
                .compose(PresentationSingleTransformer())
                .doOnSubscribe {
                    viewState.showProgress()
                    viewState.hideRetry()
                }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    viewState.setSpinnerAdapter(it
                            .filter { item -> item.group_id == taskGroup }
                            .map { item -> item.subject_name.orEmpty() }
                            .filter { s -> s.isNotEmpty() }
                            .distinct()
                    )
                }, {
                    viewState.showRetry("Ошибка")
                }).disposeWhenDestroy()
    }

    fun onRetry() {
        getDisciplines()
    }

    fun onFileChoose() {
        viewState.openFileChoose()
    }

    fun onFileChange(file: FileModel) {
        taskFile = file
        file.fileName?.let { viewState.showChoosedFile(it) }
        checkButtonState()
    }

    fun onNameChange(name: String) {
        taskName = if (name.isEmpty()) null else name
        checkButtonState()
    }

    fun onDescriptionChange(desc: String) {
        taskDescription = if (desc.isEmpty()) null else desc
        checkButtonState()
    }

    fun onDeadlineChange(deadLine: String) {
        taskDeadline = if (deadLine.length != 10) null else deadLine
        checkButtonState()
    }

    fun onDisciplineChange(discipline: String?) {
        taskDiscipline = discipline
        checkButtonState()
    }

    private fun checkButtonState() {
        viewState.setButtonEnabled(
                taskName != null &&
                        taskDescription != null &&
                        taskDeadline != null &&
                        taskFile != null &&
                        taskGroup != null &&
                        taskDiscipline != null

        )
    }

    fun onAdd() {
        tasksRepository
                .addTask(
                        UploadTaskModel(
                                taskName,
                                taskDescription,
                                taskDeadline,
                                taskGroup,
                                taskDiscipline,
                                taskFile?.file
                        )
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showWaitDialog() }
                .subscribe({
                    viewState.hideWaitDialog()
                    router.goBack()
                }, {
                    viewState.hideWaitDialog()
                    viewState.showErrorDialog("Ошибка при создании таска")
                }).disposeWhenDestroy()
    }
}