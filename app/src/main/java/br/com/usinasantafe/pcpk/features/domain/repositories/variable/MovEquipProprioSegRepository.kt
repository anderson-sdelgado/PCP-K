package br.com.usinasantafe.pcpk.features.domain.repositories.variable

import br.com.usinasantafe.pcpk.features.domain.entities.variable.MovEquipProprioSeg


interface MovEquipProprioSegRepository {

    suspend fun addEquipSeg(idEquip: Long): Boolean

    suspend fun addEquipSeg(idEquip: Long, idMov: Long): Boolean

    suspend fun clearEquipSeg(): Boolean

    suspend fun deleteEquipSeg(pos: Int): Boolean

    suspend fun listEquipSeg(): List<MovEquipProprioSeg>

    suspend fun listEquipSegIdMov(idMov: Long): List<MovEquipProprioSeg>

    suspend fun saveEquipSeg(idMov: Int): Boolean

}