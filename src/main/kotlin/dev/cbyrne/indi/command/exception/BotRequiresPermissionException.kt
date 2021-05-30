package dev.cbyrne.indi.command.exception

import net.dv8tion.jda.api.Permission

class BotRequiresPermissionException(permission: Permission) :
    CommandExecutionException("I do not have permission to do that. Make sure I have the ${permission.name} permission!")