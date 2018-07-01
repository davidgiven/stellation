package shared

private val syllables1 = arrayOf(
        "An", "Ca", "Jo", "Ka", "Kri", "Da", "Re", "De", "Ed", "Ma", "Ni",
        "Qua", "Qa", "Li", "La", "In", "On", "An", "Un", "Ci", "Cu", "Ce",
        "Co", "Xa", "Xef", "Xii", "Xo'o", "Xu", "Ram", "Noq", "Mome", "Pawa",
        "Limi", "Ney"
)

private val syllables2 = arrayOf(
        "the", "ru", "shu", "be", "po", "fol", "boo", "qwa", "xi", "lo", "fi"
)

private val syllables3 = arrayOf(
        "drew", "rine", "vid", "a", "na", "sten", "niel", "cca", "vin", "ven",
        "cor", "rion", "rath", "tong", "lar", "bol", "ting", "narg", "aq", "blan",
        "sim", "pil", "rib", "org", "lig", "zim", "frob", "cha", "poo", "tang"
)

fun <E> Array<E>.getRandomElement() = this[random(this.indices)]

fun create_star_name(): String {
    val sb = StringBuilder()
    sb.append(syllables1.getRandomElement())
    if (random(0..1) == 1) {
        sb.append(syllables2.getRandomElement())
    }
    sb.append(syllables3.getRandomElement())
    return sb.toString()
}
