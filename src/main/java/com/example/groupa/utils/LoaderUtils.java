package com.example.groupa.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class LoaderUtils {

    public List<String[]> CSVReader(String fileName) {
        String path = "src/main/resources/com/example/groupa/database/data";
        try (CSVReader reader = new CSVReader(new FileReader("%s/%s.csv".formatted(path, fileName)))) {
            List<String[]> data = reader.readAll();
            data.remove(0);

            return data;
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
