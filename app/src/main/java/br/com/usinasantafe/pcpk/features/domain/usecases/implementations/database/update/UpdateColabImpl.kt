package br.com.usinasantafe.pcpk.features.domain.usecases.implementations.database.update

import br.com.usinasantafe.pcpk.features.domain.usecases.interfaces.database.update.UpdateColab
import br.com.usinasantafe.pcpk.features.presenter.model.ResultUpdateDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateColabImpl @Inject constructor(
): UpdateColab {
    override suspend fun invoke(contador: Int, qtde: Int): Flow<ResultUpdateDatabase> {
        TODO("Not yet implemented")
    }
}