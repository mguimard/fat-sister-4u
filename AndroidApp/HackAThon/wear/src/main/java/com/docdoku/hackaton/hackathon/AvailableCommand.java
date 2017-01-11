package com.docdoku.hackaton.hackathon;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lucas-PCP on 11/01/2017.
 */

public class AvailableCommand {

    private final String GIT_PULL_PROJECT = "mettre à jour mon projet";
    private final String GIT_PULL_PROJECT_KEYWORD = "pull";

    private final String LAUNCH_IDE = "lancer mon environnement de développement";
    private final String LAUNCH_IDE_KEYWORD = "IDE";

    private final String LAUNCH_CHROME = "lancer mon navigateur";
    private final String LAUNCH_CHROME_KEYWORD = "WEB";

    private final String LAUNCH_SHELL = "lancer mon terminal";
    private final String LAUNCH_SHELL_KEWORD = "TERMINAL";

    private final String SHUTDOWN_COMPUTER = "eteindre mon ordniateur";
    private final String SHUTDOWN_COMPUTER_KEYWORD = "ALT";

    private final String RESTART_COMPUTER = "redémarrer mon ordinateur";
    private final String RESTART_COMPUTER_KEYWORD = "REBOOT";

    private static Map<String, Args> commands;

    public AvailableCommand() {
        commands = new HashMap<String, Args>();

        commands.put(GIT_PULL_PROJECT, new Args(GIT_PULL_PROJECT_KEYWORD));
        commands.put(LAUNCH_IDE, new Args(LAUNCH_IDE_KEYWORD));
        commands.put(LAUNCH_CHROME, new Args(LAUNCH_CHROME_KEYWORD));
        commands.put(LAUNCH_SHELL, new Args(LAUNCH_SHELL_KEWORD));
        commands.put(SHUTDOWN_COMPUTER, new Args(SHUTDOWN_COMPUTER_KEYWORD));
        commands.put(RESTART_COMPUTER, new Args(RESTART_COMPUTER_KEYWORD));
    }

    public static Args foundCommand(String text) {

        for (String command : commands.keySet()) {

            if (text.contains(command)) {
                return commands.get(command);
            }
        }
        
        return null;
    }
}
