package model

import java.util.Collections.synchronizedList

class Game{
    val height = 1000
    val width = 2200
    val targets: MutableList<Target> = synchronizedList(mutableListOf())
}
