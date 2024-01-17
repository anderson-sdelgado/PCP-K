package br.com.usinasantafe.pcpk.features.presenter.view.visitterc.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import br.com.usinasantafe.pcpk.R
import br.com.usinasantafe.pcpk.common.base.BaseFragment
import br.com.usinasantafe.pcpk.common.extension.showGenericAlertDialog
import br.com.usinasantafe.pcpk.common.utils.TypeMov
import br.com.usinasantafe.pcpk.databinding.FragmentDestinoVisitTercBinding
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.FragmentAttachListenerProprio
import br.com.usinasantafe.pcpk.features.presenter.view.visitterc.FragmentAttachListenerVisitTerc
import br.com.usinasantafe.pcpk.features.presenter.viewmodel.visitterc.DestinoVisitTercFragmentState
import br.com.usinasantafe.pcpk.features.presenter.viewmodel.visitterc.DestinoVisitTercViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DestinoVisitTercFragment : BaseFragment<FragmentDestinoVisitTercBinding>(
    R.layout.fragment_destino_visit_terc,
    FragmentDestinoVisitTercBinding::bind
) {

    private val viewModel: DestinoVisitTercViewModel by viewModels()
    private var fragmentAttachListenerVisitTerc: FragmentAttachListenerVisitTerc? = null

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
            buttonOkDestino.setOnClickListener {
                if (editTextDestino.text.isEmpty()) {
                    showGenericAlertDialog(
                        getString(
                            R.string.texto_campo_vazio,
                            "DESTINO"
                        ), requireContext()
                    )
                    return@setOnClickListener
                }
                viewModel.setDestino(editTextDestino.text.toString())
            }
            buttonCancDestino.setOnClickListener {
                fragmentAttachListenerVisitTerc?.goPassagList()
            }
        }
    }

    private fun handleStateChange(state: DestinoVisitTercFragmentState) {
        when (state) {
            is DestinoVisitTercFragmentState.CheckSetDestino -> handleCheckSetDestino(state.check)
        }
    }

    private fun handleCheckSetDestino(checkSetMatricColab: Boolean) {
        if (checkSetMatricColab) {
            fragmentAttachListenerVisitTerc?.goObserv(TypeMov.INPUT)
            return
        }
        showGenericAlertDialog(
            getString(
                R.string.texto_falha_insercao_campo,
                "DESTINO"
            ), requireContext()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentAttachListenerVisitTerc){
            fragmentAttachListenerVisitTerc = context
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAttachListenerVisitTerc = null
    }

}