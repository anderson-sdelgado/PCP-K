package br.com.usinasantafe.pcpk.features.module.database.room.dao

import br.com.usinasantafe.pcpk.features.external.room.AppDatabaseRoom
import br.com.usinasantafe.pcpk.features.external.room.dao.variable.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VariableDaoRoomModule {

    @Provides
    @Singleton
    fun provideMovEquipProprioDao(database: AppDatabaseRoom): MovEquipProprioDao {
        return database.movEquipProprioDao()
    }

    @Provides
    @Singleton
    fun provideMovEquipProprioSegDao(database: AppDatabaseRoom): MovEquipProprioSegDao {
        return database.movEquipProprioSegDao()
    }

    @Provides
    @Singleton
    fun provideMovEquipProprioPassagDao(database: AppDatabaseRoom): MovEquipProprioPassagDao {
        return database.movEquipProprioPassagDao()
    }

    @Provides
    @Singleton
    fun provideMovEquipVisitTercDao(database: AppDatabaseRoom): MovEquipVisitTercDao {
        return database.movEquipVisitTercDao()
    }

    @Provides
    @Singleton
    fun provideMovEquipVisitTercPassagDao(database: AppDatabaseRoom): MovEquipVisitTercPassagDao {
        return database.movEquipVisitTercPassagDao()
    }

    @Provides
    @Singleton
    fun provideMovEquipResidenciaDao(database: AppDatabaseRoom): MovEquipResidenciaDao {
        return database.movEquipResidenciaDao()
    }

}