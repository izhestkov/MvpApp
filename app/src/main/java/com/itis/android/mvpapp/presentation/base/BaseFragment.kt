package com.itis.android.mvpapp.presentation.base

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.itis.android.mvpapp.R
import com.itis.android.mvpapp.presentation.ui.teacher.TeacherActivity
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment : MvpAppCompatFragment(), BaseView {

    protected abstract val mainContentLayout: Int

    protected abstract val enableBackArrow: Boolean

    protected abstract val toolbarTitle: Int?

    protected abstract val menu: Int?

    protected val baseActivity
        get() = activity as BaseActivity

    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(mainContentLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        toolbar = view.findViewById<Toolbar>(R.id.toolbar).also { it?.title = getString(R.string.empty) }
        setToolbarTitle(toolbarTitle)

        baseActivity.setSupportActionBar(toolbar)
        baseActivity.setBackArrow(enableBackArrow)

        (baseActivity as? TeacherActivity)?.setBottomBarEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu?.let { inflater?.inflate(it, menu) }
    }

    override fun showWaitDialog() {
        (activity as? BaseActivity)?.showWaitDialog()
    }

    override fun hideWaitDialog() {
        (activity as? BaseActivity)?.hideWaitDialog()
    }

    override fun showErrorDialog(text: Int) {
        (activity as? BaseActivity)?.showErrorDialog(text)
    }

    override fun showErrorDialog(text: String) {
        (activity as? BaseActivity)?.showErrorDialog(text)
    }

    fun setToolbarTitle(title: Int?) {
        toolbar?.findViewById<TextView>(R.id.toolbar_title)?.text = getString(title
                ?: R.string.app_name)
    }

    fun setToolbarTitle(title: String?) {
        toolbar?.findViewById<TextView>(R.id.toolbar_title)?.text = title
    }
}