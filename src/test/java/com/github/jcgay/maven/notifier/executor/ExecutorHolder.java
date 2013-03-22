package com.github.jcgay.maven.notifier.executor;

public class ExecutorHolder implements Executor {

    private String[] command;

    @Override
    public void exec(String[] command) {
        if (command != null) {
            this.command = command;
        }
    }

    public String[] getCommand() {
        return command;
    }
}
