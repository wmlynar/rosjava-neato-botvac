package com.github.wmlynar.rosjava.o.nodes.csvplayer.internal;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;

public class CsvLogReader {

    CSVReader reader = null;
    private String[] line;

    public CsvLogReader(String name) {
        try {
            reader = new CSVReader(new FileReader(name), ',');
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasNext() {
        try {
            this.line = reader.readNext();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this.line != null;
    }

    public String[] readLine() {
        return this.line;
    }

}
