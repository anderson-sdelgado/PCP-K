package br.com.usinasantafe.pcpk.features.domain.usecases.initial

import br.com.usinasantafe.pcpk.common.utils.FlagUpdate
import br.com.usinasantafe.pcpk.features.domain.repositories.variable.ConfigRepository
import javax.inject.Inject

interface SetCheckUpdateBDConfig {
    suspend operator fun invoke(flagUpdate: FlagUpdate)
}

class SetCheckUpdateBDConfigImpl @Inject constructor(
    private val configRepository: ConfigRepository,
): SetCheckUpdateBDConfig {

    override suspend fun invoke(flagUpdate: FlagUpdate) {
        val config = configRepository.getConfig()
        config.flagUpdate = flagUpdate
        configRepository.saveConfig(config)
    }

}