package com.osem.vpar.service.impl;

import com.osem.vpar.ConsoleRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile("parser")
@RequiredArgsConstructor
public class CommandListener {
    private final ConsoleRunner consoleRunner;

    public void commandHandler(String command) {
        log.info("-------------------------------------");
        log.info("Received command: {}", command);
        if ("START_SCAN".equals(command)) {
            new Thread(consoleRunner::run).start();
        }
    }
}
