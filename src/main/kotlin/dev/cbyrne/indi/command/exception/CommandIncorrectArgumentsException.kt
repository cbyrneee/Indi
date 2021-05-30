package dev.cbyrne.indi.command.exception

class CommandIncorrectArgumentsException(received: Int, expected: Int) :
    CommandExecutionException(if (expected == 1) "This command requires at least one argument!" else "This command requires at least $expected arguments! You gave $received")
