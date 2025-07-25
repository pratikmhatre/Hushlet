package cypher.hushlet.features

sealed class Screens(val routeName: String) {
    object DashboardScreen : Screens("dashboard")
    object AddNewCredentialScreen : Screens("add_new_credential")
}