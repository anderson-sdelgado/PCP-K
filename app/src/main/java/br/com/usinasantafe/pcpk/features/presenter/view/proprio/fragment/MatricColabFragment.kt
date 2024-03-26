package br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment


import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
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
import br.com.usinasantafe.pcpk.common.utils.TypeAddEquip
import br.com.usinasantafe.pcpk.common.utils.TypeAddOcupante
import br.com.usinasantafe.pcpk.databinding.FragmentMatricColabBinding
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.FragmentAttachListenerProprio
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment.DetalheMovEquipProprioFragment.Companion.POS_DETALHE_PROPRIO
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment.DetalheMovEquipProprioFragment.Companion.REQUEST_KEY_DETALHE_PROPRIO
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment.NomeColabFragment.Companion.MATRIC_COLAB
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment.NomeColabFragment.Companion.POS_NOME_COLAB
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment.NomeColabFragment.Companion.REQUEST_KEY_NOME_COLAB
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment.NomeColabFragment.Companion.TYPE_ADD_OCUPANTE_NOME_COLAB
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment.PassagColabListFragment.Companion.POS_PASSAG_COLAB_LIST
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment.PassagColabListFragment.Companion.REQUEST_KEY_PASSAG_COLAB_LIST
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment.PassagColabListFragment.Companion.TYPE_ADD_OCUPANTE_PASSAG_COLAB_LIST
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment.VeiculoSegProprioListFragment.Companion.POS_VEICULO_SEG_PROPRIO
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment.VeiculoSegProprioListFragment.Companion.REQUEST_KEY_VEICULO_SEG_PROPRIO
import br.com.usinasantafe.pcpk.features.presenter.view.proprio.fragment.VeiculoSegProprioListFragment.Companion.TYPE_ADD_EQUIP_VEICULO_SEG_PROPRIO
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
        const val REQUEST_KEY_MATRIC_COLAB = "requestKeyMatricColab"
        const val TYPE_ADD_OCUPANTE_MATRIC_COLAB = "typeAddOcupanteMatricColab"
        const val POS_MATRIC_COLAB = "posMatricColab"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(REQUEST_KEY_MATRIC_COLAB) { _, bundle ->
            this.typeAddOcupante = TypeAddOcupante.values()[bundle.getInt(TYPE_ADD_OCUPANTE_MATRIC_COLAB)]
            this.pos = bundle.getInt(POS_MATRIC_COLAB)
        }

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
            setBundleNomeColab(matricColab, typeAddOcupante, pos)
            fragmentAttachListenerProprio?.goNomeColab()
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
                TypeAddOcupante.ADDMOTORISTA -> {
                    setBundleVeicSegProprio(TypeAddEquip.ADDVEICULOSEG, 0)
                    fragmentAttachListenerProprio?.goVeicSegList()
                }
                TypeAddOcupante.CHANGEMOTORISTA -> {
                    setBundleDetalhe(pos)
                    fragmentAttachListenerProprio?.goDetalhe()
                }
                TypeAddOcupante.ADDPASSAGEIRO,
                TypeAddOcupante.CHANGEPASSAGEIRO -> {
                    setBundlePassagList(typeAddOcupante, pos)
                    fragmentAttachListenerProprio?.goPassagList()
                }
            }
        }
    }

    private fun setBundleDetalhe(pos: Int){
        val bundle = Bundle()
        bundle.putInt(POS_DETALHE_PROPRIO, pos)
        setFragmentResult(REQUEST_KEY_DETALHE_PROPRIO, bundle)
    }

    private fun setBundleNomeColab(matricColab: String, typeAddOcupante: TypeAddOcupante, pos: Int){
        val bundle = Bundle()
        bundle.putInt(TYPE_ADD_OCUPANTE_NOME_COLAB, typeAddOcupante.ordinal)
        bundle.putInt(POS_NOME_COLAB, pos)
        bundle.putString(MATRIC_COLAB, matricColab)
        setFragmentResult(REQUEST_KEY_NOME_COLAB, bundle)
    }

    private fun setBundleVeicSegProprio(typeAddEquip: TypeAddEquip, pos: Int){
        val bundle = Bundle()
        bundle.putInt(TYPE_ADD_EQUIP_VEICULO_SEG_PROPRIO, typeAddEquip.ordinal)
        bundle.putInt(POS_VEICULO_SEG_PROPRIO, pos)
        setFragmentResult(REQUEST_KEY_VEICULO_SEG_PROPRIO, bundle)
    }

    private fun setBundlePassagList(typeAddOcupante: TypeAddOcupante, pos: Int){
        val bundle = Bundle()
        bundle.putInt(TYPE_ADD_OCUPANTE_PASSAG_COLAB_LIST, typeAddOcupante.ordinal)
        bundle.putInt(POS_PASSAG_COLAB_LIST, pos)
        setFragmentResult(REQUEST_KEY_PASSAG_COLAB_LIST, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAttachListenerProprio = null
    }

}