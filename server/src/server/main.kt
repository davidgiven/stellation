package server

import log
import utils.getopt

fun main(argv: Array<String>) {
    log("Yes, this is still the server!")
    getopt(argv)
}
