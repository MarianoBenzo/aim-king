package model

class Position(val x: Int, val y: Int) {
    constructor(data: LinkedHashMap<String, Int>) : this(data["x"] ?: 0, data["y"] ?: 0)//TODO
}
