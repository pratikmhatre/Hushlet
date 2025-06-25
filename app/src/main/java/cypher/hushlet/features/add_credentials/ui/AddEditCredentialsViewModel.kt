package cypher.hushlet.features.add_credentials.ui

import androidx.lifecycle.ViewModel
import cypher.hushlet.core.domain.usecases.GetAccountDetails
import cypher.hushlet.core.domain.usecases.GetCardDetails
import cypher.hushlet.features.add_credentials.domain.usecases.DeleteAccount
import cypher.hushlet.features.add_credentials.domain.usecases.DeleteCard
import cypher.hushlet.features.add_credentials.domain.usecases.SaveNewAccount
import cypher.hushlet.features.add_credentials.domain.usecases.SaveNewCard
import cypher.hushlet.core.domain.usecases.UpdateAccount
import cypher.hushlet.core.domain.usecases.UpdateCard
import cypher.hushlet.features.add_credentials.domain.usecases.CheckIfAccountNameExists
import cypher.hushlet.features.add_credentials.domain.usecases.CheckIfCardNameExists
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditCredentialsViewModel @Inject constructor(
    private val saveNewAccount: SaveNewAccount,
    private val saveNewCard: SaveNewCard,
    private val updateAccount: UpdateAccount,
    private val updateCard: UpdateCard,
    private val deleteAccount: DeleteAccount,
    private val deleteCard: DeleteCard,
    private val checkIfAccountNameExists: CheckIfAccountNameExists,
    private val checkIfCardNameExists: CheckIfCardNameExists,
    private val getAccountDetails: GetAccountDetails,
    private val getCardDetails: GetCardDetails
): ViewModel() {
}

