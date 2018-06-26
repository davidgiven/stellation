package shared

open class SGalaxy : SThing() {
    val stars: Aggregate<SStar>
        get() = aggregate(STARS)

    fun initialiseGalaxy() {
        for (i in 0..99) {
            val star = create<SStar>()
            star.name = "Beetleguice"
            stars.add(star)
        }
    }
}

