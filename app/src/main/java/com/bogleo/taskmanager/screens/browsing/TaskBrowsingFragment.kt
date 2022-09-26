package com.bogleo.taskmanager.screens.browsing

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.databinding.FragmentTaskBrowsingBinding
import com.bogleo.taskmanager.screens.MainViewModel
import com.bogleo.taskmanager.common.TextUtils

private val TAG = TaskBrowsingFragment::class.qualifiedName

class TaskBrowsingFragment : Fragment(), MenuProvider {

    private var _binding: FragmentTaskBrowsingBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: TaskBrowsingViewModel by viewModels()
    private val mArgs by navArgs<TaskBrowsingFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.mainViewModel = mainViewModel
        _binding = FragmentTaskBrowsingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        configureMenu()
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    private fun bindData() {
        with(binding) {
            val task = mArgs.task
            tvTxtTitle.text = task.title
            tvTxtTitle.compoundDrawableTintList = ColorStateList.valueOf(task.colorTag)
            tvTxtDate.text = task.date
            tvTxtTime.text = task.time
            tvTxtTags.text = TextUtils.makeTagString(task.tags)
            tvBtnSetComplete.text = getText(
                if (task.isDone) R.string.set_unfulfilled
                else R.string.set_complete
            )

            tvBtnSetComplete.setOnClickListener {
                viewModel.switchTaskComplete(task, requireContext()) {
                    navigateTo(
                        TaskBrowsingFragmentDirections.actionTaskBrowsingToTaskList()
                    )
                }
            }
        }
    }

    private fun navigateTo(action: NavDirections) {
        try {
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e(TAG, "NavController error: ${e.localizedMessage}}")
        }
    }

    private fun configureMenu() {
        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.editMenu){
            navigateTo(
                TaskBrowsingFragmentDirections.actionTaskBrowsingToTaskEditFragment(
                    task = mArgs.task
                )
            )
            return true
        }
        return false
    }
}