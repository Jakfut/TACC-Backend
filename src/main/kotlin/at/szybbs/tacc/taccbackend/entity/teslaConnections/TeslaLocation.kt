package at.szybbs.tacc.taccbackend.entity.teslaConnections

data class TeslaLocation(
    val address: String,
)   {
    override fun toString(): String {
        return "\naddress: $address"
    }

    fun getDriveTime(tarLocation: TeslaLocation) : Double {
        // TODO implement
        return 0.0
    }
}