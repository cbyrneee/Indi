package dev.cbyrne.indi.config

object Configuration {
    val token = getVariable("TOKEN", true)
    val prefix = getVariable("PREFIX", false, "i!")

    private fun getVariable(key: String, required: Boolean = false, defaultValue: String? = null): String {
        val value = System.getenv(key) ?: defaultValue
        if (value == null && required) throw IllegalStateException("The $key environment variable was not set!")

        return value!!
    }
}
