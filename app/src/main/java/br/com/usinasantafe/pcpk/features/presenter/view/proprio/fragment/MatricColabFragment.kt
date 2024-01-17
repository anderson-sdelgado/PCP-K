package br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment


import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import br.com.usinasantafe.pcpk.R
import br.com.usinasantafe.pcpk.common.base.BaseFragment
import br.com.usinasantafe.pcpk.common.dialog.GenericDialogProgressBar
import br.com.usinasantafe.pcpk.common.extension.onBackPressed
import br.com.usinasantafe.pcpk.common.extension.setListenerButtonsGeneric
import br.com.usinasantafe.pcpk.common.extension.showGenericAlertDialog
import br.com.usinasantafe.pcpk.common.extension.showToast
import br.com.usinasantafe.pcpk.common.utils.ResultUpdateDatabase
import br.com.usinasantafe.pcpk.common.utils.StatusUpdate
import br.com.usinasantafe.pcpk.common.utils.TypeAddOcupante
import br.com.usinasantafe.pcpk.databinding.FragmentMatricColabBinding
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.FragmentAttachListenerProprio
import br.com.usinasantafe.pcpk.features.presenter.viewmodel.proprio.MatricColabFragmentState
import br.com.usinasantafe.pcpk.features.presenter.viewmodel.proprio.MatricColabViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatricColabFragment : BaseFragment<FragmentMatricColabBinding>(
    R.layout.fragment_matric_colab,
    FragmentMatricColabBinding::bind
) {

    private val viewModel: MatricColabViewModel by viewModels()
    private var genericDialogProgressBar: GenericDialogProgressBar? = null
    private var fragmentAttachListenerProprio: FragmentAttachListenerProprio? = null
    private lateinit var describeUpdate: String
    private lateinit var typeAddOcupante: TypeAddOcupante
    private lateinit var matricColab: String
    private var pos: Int = 0

    companion object {
        const val KEY_TYPE_OCUPANTE_VEIC_PROPRIO = "key_type_ocupante_veic_proprio";
        const val KEY_POS_MATRIC_COLAB = "key_pos_matric_colab";
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        typeAddOcupante = TypeAddOcupante.values()[arguments?.getInt(KEY_TYPE_OCUPANTE_VEIC_PROPRIO)!!]
        pos = arguments?.getInt(KEY_POS_MATRIC_COLAB)!!
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
            setListenerButtonsGeneric(layoutBotoes, editTextPadrao)
            layoutBotoes.buttonOkPadrao.setOnClickListener {
                if (editTextPadrao.text.isEmpty()) {
                    showGenericAlertDialog(
                        getString(
                            R.string.texto_campo_vazio,
                            "MATRICULA DO VIGIA"
                        ), requireContext()
                    )
                    return@setOnClickListener
                }
                matricColab = editTextPadrao.text.toString()
                viewModel.checkMatricColaborador(matricColab)
            }
            layoutBotoes.buttonAtualPadrao.setOnClickListener {
                viewModel.updateDataColab()
            }
        }
    }

    private fun handleStateChange(state: MatricColabFragmentState) {
        when (state) {
            is MatricColabFragmentState.CheckMatric -> handleCheckMatric(state.checkMatric)
            is MatricColabFragmentState.FeedbackUpdate -> handleFeedbackUpdate(state.statusUpdate)
            is MatricColabFragmentState.SetResultUpdate -> handleSetResultUpdate(state.resultUpdateDatabase)
        }
    }

    private fun handleCheckMatric(checkMatric: Boolean) {
        if (checkMatric) {
            fragmentAttachListenerProprio?.goNomeColab(matricColab, typeAddOcupante, pos)
            return
        }
        showGenericAlertDialog(
            getString(
                R.string.texto_dado_invalido_com_atual,
                "MATRICULA DO COLABORADOR"
            ), requireContext()
        )
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
                showToast(
                    getString(R.string.texto_msg_atualizacao, "COLABORADORES"),
                    requireContext()
                )
            }
            StatusUpdate.FAILURE -> {
                hideProgressBar()
                showToast(
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
        if (context is FragmentAttachListenerProprio) {
            fragmentAttachListenerProprio = context
        }
        onBackPressed {
            when(typeAddOcupante) {
                TypeAddOcupante.ADDMOTORISTA,
                TypeAddOcupante.ADDPASSAGEIRO -> fragmentAttachListenerProprio?.goMovProprioList()
                TypeAddOcupante.CHANGEMOTORISTA,
                TypeAddOcupante.CHANGEPASSAGEIRO -> fragmentAttachListenerProprio?.goDetalhe(pos)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAttachListenerProprio = null
    }

}