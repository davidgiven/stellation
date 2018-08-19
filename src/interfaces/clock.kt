package interfaces

interface IClock {
    fun setTime(serverTime: Double)
    fun getTime(): Double
}
