package br.com.usinasantafe.pcpk.features.presenter.viewmodel.visitterc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.usinasantafe.pcpk.common.utils.FlowApp
import br.com.usinasantafe.pcpk.features.domain.usecases.visitterc.SetDestinoVisitTerc
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DestinoVisitTercViewModel @Inject constructor(
    private val setDestinoVisitTerc: SetDestinoVisitTerc,
) : ViewModel() {

    private val _uiLiveData = MutableLiveData<DestinoVisitTercFragmentState>()
    val uiLiveData: LiveData<DestinoVisitTercFragmentState> = _uiLiveData

    private fun checkSetDestino(check: Boolean) {
        _uiLiveData.value = DestinoVisitTercFragmentState.CheckSetDestino(check)
    }

    fun setDestino(destino: String, flowApp: FlowApp, pos: Int) = viewModelScope.launch {
        checkSetDestino(setDestinoVisitTerc(destino, flowApp, pos))
    }

}

sealed class DestinoVisitTercFragmentState {
    data class CheckSetDestino(val check: Boolean) : DestinoVisitTercFragmentState()
}