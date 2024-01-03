package br.com.usinasantafe.pcpk.features.presenter.model

import br.com.usinasantafe.pcpk.features.domain.entities.variable.Config

data class ConfigModel(
    val nroAparelho: Long,
    val senha: String,
)

fun Config.toConfigModel(): ConfigModel {
    return with(this){
        ConfigModel(
            nroAparelho = this.nroAparelhoConfig!!,
            senha = this.passwordConfig!!,
        )
    }
}
