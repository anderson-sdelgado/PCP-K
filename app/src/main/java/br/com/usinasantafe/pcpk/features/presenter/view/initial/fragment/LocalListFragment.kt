package br.com.usinasantafe.pcpk.features.presenter.view.initial.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import br.com.usinasantafe.pcpk.R
import br.com.usinasantafe.pcpk.common.adapter.CustomAdapter
import br.com.usinasantafe.pcpk.common.base.BaseFragment
import br.com.usinasantafe.pcpk.common.dialog.GenericDialogProgressBar
import br.com.usinasantafe.pcpk.common.extension.onBackPressed
import br.com.usinasantafe.pcpk.common.extension.showGenericAlertDialog
import br.com.usinasantafe.pcpk.common.extension.showToast
import br.com.usinasantafe.pcpk.common.utils.ResultUpdateDatabase
import br.com.usinasantafe.pcpk.common.utils.StatusUpdate
import br.com.usinasantafe.pcpk.databinding.FragmentLocalListBinding
import br.com.usinasantafe.pcpk.features.presenter.view.initial.FragmentAttachListenerInitial
import br.com.usinasantafe.pcpk.features.presenter.viewmodel.initial.LocalListFragmentState
import br.com.usinasantafe.pcpk.features.presenter.viewmodel.initial.LocalListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocalListFragment : BaseFragment<FragmentLocalListBinding>(
    R.layout.fragment_local_list,
    FragmentLocalListBinding::bind,
) {

    private val viewModel: LocalListViewModel by viewModels()
    private var fragmentAttachListenerInitial: FragmentAttachListenerInitial? = null
    private var genericDialogProgressBar: GenericDialogProgressBar? = null
    private lateinit var describeUpdate: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        startEvents()
        setListener()

    }

    private fun observeState(){
        viewModel.uiLiveData.observe(viewLifecycleOwner) {
            state -> handleStateChange(state)
        }
    }

    private fun startEvents() {
        viewModel.recoverLocals()
    }

    private fun setListener() {
        with(binding) {
            buttonAtualLocal.setOnClickListener {
                viewModel.updateDataLocal()
            }
            buttonRetornarLocal.setOnClickListener {
                fragmentAttachListenerInitial?.goNomeVigia()
            }
        }
    }

    private fun handleStateChange(state: LocalListFragmentState){
        when(state){
            is LocalListFragmentState.ListLocal -> handleLocalList(state.localList)
            is LocalListFragmentState.CheckSetLocal -> handleCheckSetLocal(state.check)
            is LocalListFragmentState.FeedbackUpdate -> handleFeedbackUpdate(state.statusUpdate)
            is LocalListFragmentState.SetResultUpdate -> handleSetResultUpdate(state.resultUpdateDatabase)
        }
    }

    private fun handleLocalList(localList: List<String>) {
        viewList(localList)
    }

    private fun handleCheckSetLocal(check: Boolean){
        if(check) {
            fragmentAttachListenerInitial?.goMenuApont()
            return
        }
        showToast(getString(R.string.texto_falha_insercao_campo, "LOCAL"), requireContext())
    }

    private fun viewList(localList: List<String>) {

        val localListView = localList.map { it }

        val listAdapter = CustomAdapter(localListView).apply {
            onItemClick = { _, pos ->
                viewModel.setPosLocal(pos)
            }
        }
        binding.listViewLocal.run {
            setHasFixedSize(true)
            adapter = listAdapter
        }
    }

    private fun handleSetResultUpdate(resultUpdateDatabase: ResultUpdateDatabase?) {
        resultUpdateDatabase?.let {
            if (genericDialogProgressBar == null) {
                showProgressBar()
            }
            describeUpdate = resultUpdateDatabase.describe
            genericDialogProgressBar!!.setValue(resultUpdateDatabase)
        }
    }

    private fun handleFeedbackUpdate(statusUpdate: StatusUpdate) {
        when (statusUpdate) {
            StatusUpdate.UPDATED -> {
                hideProgressBar()
                showGenericAlertDialog(
                    getString(R.string.texto_msg_atualizacao, "LOCAL"),
                    requireContext()
                )
            }
            StatusUpdate.FAILURE -> {
                hideProgressBar()
                showGenericAlertDialog(
                    getString(R.string.texto_update_failure, describeUpdate),
                    requireContext()
                )
            }
        }
    }

    private fun showProgressBar() {
        genericDialogProgressBar = GenericDialogProgressBar(requireContext())
        genericDialogProgressBar!!.show()
        genericDialogProgressBar!!.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
    }

    private fun hideProgressBar() {
        if (genericDialogProgressBar != null) {
            genericDialogProgressBar!!.cancel()
        }
        genericDialogProgressBar = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentAttachListenerInitial){
            fragmentAttachListenerInitial = context
        }
        onBackPressed {
            fragmentAttachListenerInitial?.goNomeVigia()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAttachListenerInitial = null
    }

}