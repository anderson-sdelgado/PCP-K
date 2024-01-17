package br.com.usinasantafe.pcpk.features.presenter.view.residencia.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import br.com.usinasantafe.pcpk.R
import br.com.usinasantafe.pcpk.common.base.BaseFragment
import br.com.usinasantafe.pcpk.common.extension.showGenericAlertDialog
import br.com.usinasantafe.pcpk.common.utils.TypeMov
import br.com.usinasantafe.pcpk.databinding.FragmentMovEquipResidenciaListBinding
import br.com.usinasantafe.pcpk.features.presenter.model.HeaderModel
import br.com.usinasantafe.pcpk.features.presenter.model.MovEquipResidenciaModel
import br.com.usinasantafe.pcpk.features.presenter.view.residencia.FragmentAttachListenerResidencia
import br.com.usinasantafe.pcpk.features.presenter.view.residencia.adapter.MovEquipResidenciaAdapter
import br.com.usinasantafe.pcpk.features.presenter.view.visitterc.adapter.MovEquipVisitTercAdapter
import br.com.usinasantafe.pcpk.features.presenter.viewmodel.residencia.MovEquipResidenciaListFragmentState
import br.com.usinasantafe.pcpk.features.presenter.viewmodel.residencia.MovEquipResidenciaListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovEquipResidenciaListFragment : BaseFragment<FragmentMovEquipResidenciaListBinding>(
    R.layout.fragment_mov_equip_residencia_list,
    FragmentMovEquipResidenciaListBinding::bind,
) {

    private val viewModel: MovEquipResidenciaListViewModel by viewModels()
    private var fragmentAttachListenerResidencia: FragmentAttachListenerResidencia? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        setListener()
        startEvents()

    }

    private fun observeState() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) {
            state -> handleStateChange(state)
        }
    }

    private fun startEvents() {
        viewModel.recoverDataHeader()
        viewModel.recoverListMov()
    }

    private fun setListener() {
        with(binding) {
            buttonEntradaMovEquipResidencia.setOnClickListener {
                viewModel.checkSetInitialMov()
            }
            buttonRetornarMovEquipResidencia.setOnClickListener {
                fragmentAttachListenerResidencia?.goInicial()
            }
        }
    }

    private fun handleStateChange(state: MovEquipResidenciaListFragmentState) {
        when(state){
            is MovEquipResidenciaListFragmentState.RecoverHeader -> handleHeader(state.header)
            is MovEquipResidenciaListFragmentState.ListMovEquip -> handleListMov(state.movEquipResidenciaList)
            is MovEquipResidenciaListFragmentState.CheckInitialMovEquip -> handleStartMov(state.check)
        }
    }

    private fun handleHeader(header: HeaderModel){
        with(binding) {
            textViewVigia.text = header.nomeVigia
            textViewLocal.text = header.local
        }
    }

    private fun handleListMov(movEquipResidenciaList: List<MovEquipResidenciaModel>){
        val listAdapter = MovEquipResidenciaAdapter(movEquipResidenciaList).apply {
            onItemClick = { pos ->
                fragmentAttachListenerResidencia?.goObserv(TypeMov.OUTPUT, pos)
            }
        }
        binding.listViewMovResidencia.run {
            setHasFixedSize(true)
            adapter = listAdapter
        }
    }

    private fun handleStartMov(check: Boolean) {
        if (check) {
            fragmentAttachListenerResidencia?.goVeiculo()
            return
        }
        showGenericAlertDialog(
            getString(
                R.string.texto_failure_app,
            ), requireContext()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentAttachListenerResidencia) {
            fragmentAttachListenerResidencia = context
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAttachListenerResidencia = null
    }

}