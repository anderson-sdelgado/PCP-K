package br.com.usinasantafe.pcpk.features.external.room.dao.variable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.usinasantafe.pcpk.common.utils.StatusData
import br.com.usinasantafe.pcpk.common.utils.StatusSend
import br.com.usinasantafe.pcpk.common.utils.TB_MOV_EQUIP_RESIDENCIA
import br.com.usinasantafe.pcpk.features.infra.models.room.variable.MovEquipResidenciaRoomModel

@Dao
interface MovEquipResidenciaDao {

    @Insert
    suspend fun insert(movEquipResidenciaRoomModel: MovEquipResidenciaRoomModel): Long

    @Update
    suspend fun update(movEquipResidenciaRoomModel: MovEquipResidenciaRoomModel): Int

    @Query("SELECT * FROM $TB_MOV_EQUIP_RESIDENCIA WHERE statusMovEquipResidencia = :status")
    suspend fun listMovStatus(status: StatusData): List<MovEquipResidenciaRoomModel>

    @Query("SELECT * FROM $TB_MOV_EQUIP_RESIDENCIA WHERE statusSendMovEquipResidencia = :statusEnvio")
    suspend fun listMovStatusEnvio(statusEnvio: StatusSend): List<MovEquipResidenciaRoomModel>

    @Query("SELECT MAX(idMovEquipResidencia) FROM $TB_MOV_EQUIP_RESIDENCIA WHERE statusSendMovEquipResidencia = :statusEnvio")
    suspend fun lastIdMovStatusEnvio(statusEnvio: StatusSend): Int

    @Query("SELECT * FROM $TB_MOV_EQUIP_RESIDENCIA WHERE idMovEquipResidencia = :idMov")
    suspend fun listMovId(idMov: Long): List<MovEquipResidenciaRoomModel>

}