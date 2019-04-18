package com.itis.android.mvpapp.presentation.ui.main.grouplist

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.itis.android.mvpapp.R
import com.itis.android.mvpapp.model.Group
import com.itis.android.mvpapp.presentation.adapter.GroupListViewPagerAdapter
import com.itis.android.mvpapp.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_group_list.*
import javax.inject.Inject
import javax.inject.Provider

class GroupListFragment : BaseFragment(), GroupListView {

    private lateinit var adapter: GroupListViewPagerAdapter

    companion object {
        fun getInstance() = GroupListFragment()
    }

    override val mainContentLayout = R.layout.fragment_group_list

    override val enableBackArrow = true

    override val toolbarTitle = R.string.toolbar_task

    @InjectPresenter
    lateinit var presenter: GroupListPresenter

    @Inject
    lateinit var presenterProvider: Provider<GroupListPresenter>

    @ProvidePresenter
    fun providePresenter(): GroupListPresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setGroups(groups: List<Group>) {
        adapter = GroupListViewPagerAdapter(childFragmentManager, requireContext())
        adapter.addGroups(groups)
        view_pager_tasks.adapter = adapter
        tabGroup.setupWithViewPager(view_pager_tasks)
    }
}