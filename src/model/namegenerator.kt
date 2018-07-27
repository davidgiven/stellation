package utils

class NameGenerator {
    val random by injection<Random>()

    companion object {
        private val syllables1 = listOf(
                "An", "Ca", "Jo", "Ka", "Kri", "Da", "Re", "De", "Ed", "Ma", "Ni",
                "Qua", "Qa", "Li", "La", "In", "On", "An", "Un", "Ci", "Cu", "Ce",
                "Co", "Xa", "Xef", "Xii", "Xo'o", "Xu", "Ram", "Noq", "Mome", "Pawa",
                "Limi", "Ney"
        )

        private val syllables2 = listOf(
                "the", "ru", "shu", "be", "po", "fol", "boo", "qwa", "xi", "lo", "fi"
        )

        private val syllables3 = listOf(
                "drew", "rine", "vid", "a", "na", "sten", "niel", "cca", "vin", "ven",
                "cor", "rion", "rath", "tong", "lar", "bol", "ting", "narg", "aq", "blan",
                "sim", "pil", "rib", "org", "lig", "zim", "frob", "cha", "poo", "tang"
        )
    }

    fun generate(): String {
        val sb = StringBuilder()
        sb.append(random.getRandomElement(syllables1))
        if (random.random(0..1) == 1) {
            sb.append(random.getRandomElement(syllables2))
        }
        sb.append(random.getRandomElement(syllables3))
        return sb.toString()
    }
}
