package br.com.usinasantafe.pcpk.features.presenter.view.visitterc.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import br.com.usinasantafe.pcpk.R
import br.com.usinasantafe.pcpk.common.base.BaseFragment
import br.com.usinasantafe.pcpk.common.extension.showGenericAlertDialog
import br.com.usinasantafe.pcpk.databinding.FragmentPlacaVisitTercBinding
import br.com.usinasantafe.pcpk.features.presenter.view.visitterc.FragmentAttachListenerVisitTerc
import br.com.usinasantafe.pcpk.features.presenter.viewmodel.visitterc.PlacaVisitTercFragmentState
import br.com.usinasantafe.pcpk.features.presenter.viewmodel.visitterc.PlacaVisitTercViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacaVisitTercFragment : BaseFragment<FragmentPlacaVisitTercBinding>(
    R.layout.fragment_placa_visit_terc,
    FragmentPlacaVisitTercBinding::bind
) {

    private val viewModel: PlacaVisitTercViewModel by viewModels()
    private var fragmentAttachListenerVisitTerc: FragmentAttachListenerVisitTerc? = null

    companion object {
        const val KEY_FLOW_PLACA_VISIT_TERC = "key_flow_observ_visit_terc";
        const val KEY_POS_PLACA_VISIT_TERC = "key_pos_observ_visit_terc";
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        setListener()

    }

    private fun observeState() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { state ->
            handleStateChange(state)
        }
    }

    private fun setListener() {
        with(binding) {
            buttonOkPlacaVisitTerc.setOnClickListener {
                if (editTextPlacaVisitTerc.text.isEmpty()) {
                    showGenericAlertDialog(
                        getString(
                            R.string.texto_campo_vazio,
                            "PLACA"
                        ), requireContext()
                    )
                    return@setOnClickListener
                }
                viewModel.setPlaca(editTextPlacaVisitTerc.text.toString())
            }
            buttonCancPlacaVisitTerc.setOnClickListener {
                fragmentAttachListenerVisitTerc?.goVeiculo()
            }
        }
    }

    private fun handleStateChange(state: PlacaVisitTercFragmentState) {
        when (state) {
            is PlacaVisitTercFragmentState.CheckSetPlaca -> handleCheckSetPlaca(state.check)
        }
    }

    private fun handleCheckSetPlaca(check: Boolean) {
        if (check) {
            fragmentAttachListenerVisitTerc?.goTipoVisitTerc()
            return
        }
        showGenericAlertDialog(
            getString(
                R.string.texto_falha_insercao_campo,
                "PLACA"
            ), requireContext()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentAttachListenerVisitTerc) {
            fragmentAttachListenerVisitTerc = context
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAttachListenerVisitTerc = null
    }

}