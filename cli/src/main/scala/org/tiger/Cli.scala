package org.tiger

import org.tiger.Backend

open class Mai {
}

object Mai :
    def main(args: Array[String]): Unit = {
        Backend.invoke()
    }

    