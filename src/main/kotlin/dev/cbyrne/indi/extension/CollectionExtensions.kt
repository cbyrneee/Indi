package dev.cbyrne.indi.extension

val Collection<*>.stringSuffix: String
    get() = if (this.size == 1) "" else "s"