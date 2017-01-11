package com.docdoku.hackaton.hackathon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

/**
 * Created by Lucas-PCP on 11/01/2017.
 */

public class Args {

    private String command;
    private String[] args;

    public String getCommand() {
        return command;
    }

    public Args(String command, String... args) {
        this.command = command;
        this.args = args;
    }

    public Args(String command) {
        this.command = command;
    }

    public String toJson() throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(this);

        return json;
    }
}
