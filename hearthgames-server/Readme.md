Regex to find lines that do not contain:  GameState.DebugPrintPower()

(?!^.*GameState\.DebugPrintPower\(\).*$)^.+

Regex to find lines that do not contain:  PowerTaskList.DebugPrintPower()

(?!^.*PowerTaskList\.DebugPrintPower\(\).*$)^.+

Regex to find all empty lines

^(?:[\t ]*(?:\r?\n|\r))+