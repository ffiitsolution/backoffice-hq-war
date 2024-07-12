package com.ffi.backofficehq.config;

/**
 *
 * @author USER
 */
import java.io.PrintStream;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        printBanner(System.out);
    }

    // ANSI escape codes for colors
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private void printBanner(PrintStream out) {
        out.println(ANSI_RED);
        out.println("______               _      _____   __   __  _                             ____ ");
        out.println("| ___ \\             | |    |  _  | / _| / _|(_)                   /\\  /\\  /___ \\");
        out.println("| |_/ /  __ _   ___ | | __ | | | || |_ | |_  _   ___   ___       / /_/ / //  / /");
        out.println("| ___ \\ / _` | / __|| |/ / | | | ||  _||  _|| | / __| / _ \\     / __  / / \\_/ / ");
        out.println("| |_/ /| (_| || (__ |   <  \\ \\_/ /| |  | |  | || (__ |  __/     \\/ /_/  \\___,_\\ ");
        out.println("\\____/  \\__,_| \\___||_|\\_\\  \\___/ |_|  |_|  |_| \\___| \\___|");
        out.println(ANSI_RESET);
        out.println("==== IT Solution Department PT.Fast Food Indonesia, Tbk. ====");
        out.println(ANSI_YELLOW + " :: BackOffice HQ ::    (backend v 24.06.03a)");
        out.println(ANSI_RESET);
    }
}
