package cypher.hushlet.ui.screes

interface Destination {
    var route: String
}

object Pager : Destination {
    override var route: String = "pager"
}
object Dashboard : Destination {
    override var route: String = "dashboard"
}

object AddNewCredential : Destination {
    override var route: String = "new_credential"
}

object Settings : Destination {
    override var route: String = "settings"
}