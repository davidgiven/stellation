package ui

import model.SThing
import utils.Oid

fun renderOid(oid: Oid) = "#$oid"
fun renderOid(thing: SThing) = renderOid(thing.oid)

fun renderTime(time: Double): String {
    val milliHours = ((time / 36.0) % 1000.0).toInt()
    val hours = ((time / 3600.0) % 1000.0).toInt()
    val kiloHours = (time / 3600000.0).toInt()
    return "$kiloHours.${thousandPad(hours)}.${thousandPad(milliHours)}"
}

private fun thousandPad(value: Int):String {
    val pad = if (value < 10) "00" else if (value < 100) "0" else ""
    return "$pad$value"
}
