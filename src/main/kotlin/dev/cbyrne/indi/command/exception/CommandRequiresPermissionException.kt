package dev.cbyrne.indi.command.exception

import net.dv8tion.jda.api.Permission

class CommandRequiresPermissionException(permission: Permission) :
    CommandExecutionException("You do not have permission to use this command. You need to have the ${permission.name} permission!")