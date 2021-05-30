package dev.cbyrne.indi.command

enum class CommandCategory(val displayName: String, val icon: String) {
    UTILITIES("Utilities", "⚒"),
    USER("User", "🧑"),
    MODERATION("Moderation", "🚔"),
    OTHER("Other", "❓"),
    ADMINISTRATOR("Admin", "✨"),
    MUSIC("Music", "🎶")
}