package br.com.usinasantafe.pcpk.features.domain.usecases.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.usinasantafe.pcpk.common.utils.StatusSend
import br.com.usinasantafe.pcpk.features.domain.usecases.common.SetStatusSendConfig
import br.com.usinasantafe.pcpk.features.domain.usecases.proprio.ReceiverSentDataMovEquipProprio
import br.com.usinasantafe.pcpk.features.domain.usecases.proprio.CheckDataSendMovEquipProprio
import br.com.usinasantafe.pcpk.features.domain.usecases.proprio.SendDataMovEquipProprio
import br.com.usinasantafe.pcpk.features.domain.usecases.residencia.CheckDataSendMovEquipResidencia
import br.com.usinasantafe.pcpk.features.domain.usecases.residencia.ReceiverSentDataMovEquipResidencia
import br.com.usinasantafe.pcpk.features.domain.usecases.residencia.SendDataMovEquipResidencia
import br.com.usinasantafe.pcpk.features.domain.usecases.visitterc.CheckDataSendMovEquipVisitTerc
import br.com.usinasantafe.pcpk.features.domain.usecases.visitterc.ReceiverSentDataMovEquipVisitTerc
import br.com.usinasantafe.pcpk.features.domain.usecases.visitterc.SendDataMovEquipVisitTerc
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.lang.Exception

@HiltWorker
class ProcessWorkManager @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val setStatusSendConfig: SetStatusSendConfig,
    private val checkDataSendMovEquipProprio: CheckDataSendMovEquipProprio,
    private val sendDataMovEquipProprio: SendDataMovEquipProprio,
    private val receiverSentDataMovEquipProprio: ReceiverSentDataMovEquipProprio,
    private val checkDataSendMovEquipVisitTerc: CheckDataSendMovEquipVisitTerc,
    private val sendDataMovEquipVisitTerc: SendDataMovEquipVisitTerc,
    private val receiverSentDataMovEquipVisitTerc: ReceiverSentDataMovEquipVisitTerc,
    private val checkDataSendMovEquipResidencia: CheckDataSendMovEquipResidencia,
    private val sendDataMovEquipResidencia: SendDataMovEquipResidencia,
    private val receiverSentDataMovEquipResidencia: ReceiverSentDataMovEquipResidencia,
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        try {
            setStatusSendConfig(StatusSend.SENDING)
            if(checkDataSendMovEquipProprio()){
                val result = sendDataMovEquipProprio()
                result.fold(
                    onSuccess = {
                        if(!receiverSentDataMovEquipProprio(it)){
                            return Result.retry()
                        }
                    },
                    onFailure = {
                        return Result.retry()
                    }
                )
            }
            if(checkDataSendMovEquipVisitTerc()){
                val result = sendDataMovEquipVisitTerc()
                result.fold(
                    onSuccess = {
                        if(!receiverSentDataMovEquipVisitTerc(it)){
                            return Result.retry()
                        }
                    },
                    onFailure = {
                        return Result.retry()
                    }
                )
            }
            if(checkDataSendMovEquipResidencia()){
                val result = sendDataMovEquipResidencia()
                result.fold(
                    onSuccess = {
                        if(!receiverSentDataMovEquipResidencia(it)){
                            return Result.retry()
                        }
                    },
                    onFailure = {
                        return Result.retry()
                    }
                )
            }
        } catch (e: Exception) {
            Log.i("PCP", "ERRO = $e")
            setStatusSendConfig(StatusSend.SEND)
            return Result.retry()
        }
        Log.i("PCP", "SUCESSO")
        setStatusSendConfig(StatusSend.SENT)
        return Result.success()
    }

}