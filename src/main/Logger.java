package main;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    public String LogName;
    private List<String[]> Content;

    private boolean flushed;

    public Logger(String logName){
        this.LogName = logName;
        this.Content = new ArrayList<>();
        this.flushed = false;
    }

    public void AddRows(List<String[]> data)
    {
        Content.addAll(data);
    }

    public void AddRow(List<String> logHeader)
    {
        Content.add(logHeader.toArray(new String[0]));
    }

    public void WriteLog()
    {
        String filePath = GetFilepath();
        File logFile = new File(filePath);
        try{
            logFile.createNewFile();
            FileWriter file = new FileWriter(filePath);
            CSVWriter writer = new CSVWriter(file);
            writer.writeAll(Content);
            writer.close();
            this.flushed = true;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String GetFilepath()
    {
        return "/root/UvA/EC/EvComp.Group46/stats/" + LogName + ".csv";
    }

    @Override
    protected void finalize() throws Throwable {
        try{
            if(!this.flushed && Content.size() > 0){
                WriteLog();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void Print(List<String> result)
    {
        for (String s: result) {
            System.out.print(s);
            System.out.print("   ");
        }
        System.out.println();
    }
}
