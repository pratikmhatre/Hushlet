package cypher.hushlet.features.add_credentials.ui.events

sealed class AddCredsUiEvents {
    object CardSaved : AddCredsUiEvents()
    object AccountSaved : AddCredsUiEvents()
    object CardUpdated : AddCredsUiEvents()
    object AccountUpdated : AddCredsUiEvents()
    object ResetFields : AddCredsUiEvents()
    class ShowDeleteDialog(val title: String) : AddCredsUiEvents()
}