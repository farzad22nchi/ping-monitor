package com.fto.monitor_tool.ping.service;
import com.fto.monitor_tool.ping.model.Ping;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class FileManagerService {

    private static final Logger logger = Logger.getLogger(FileManagerService.class.getName());

    public void saveAsJson(List<?> list, String fileName){
        Gson gson = new Gson();
        try (FileWriter file = new FileWriter(fileName + ".json")){
            gson.toJson(list, file);
            logger.log(Level.INFO, "Json file has been exported");
        } catch (IOException e) {
            logger.log(Level.WARNING, "fail to export Json file");
        }
    }
    public void saveAsXML(List<?> list, String fileName) {
        XMLEncoder encoder=null;
        try(FileOutputStream fileOutputStream = new FileOutputStream(fileName+".xml")){
            encoder=new XMLEncoder(new BufferedOutputStream(fileOutputStream));
            encoder.writeObject(list);
            encoder.close();
            logger.log(Level.INFO, "XML file has been exported");
        }catch(FileNotFoundException fileNotFound){
            logger.log(Level.WARNING, "fail to export XML file file can not created");
        } catch (IOException e) {
            logger.log(Level.WARNING, "fail to export XML file");
        }
    }
    public void saveAsCsv(List<?extends Ping> list, String fileName) {
        try (FileWriter file = new FileWriter(fileName + ".csv");
             CSVWriter writer = new CSVWriter(file)){
            writer.writeAll(list.stream().map(a -> a.toListString()).collect(Collectors.toList()));
            logger.log(Level.INFO, "CSV file has been exported");
        } catch (IOException e) {
            logger.log(Level.WARNING, "fail to export XML file");
        }
     }
}
