package com.example.addressbook.utils;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.addressbook.bean.People;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class TXTExporter {

    public void exportToTXT(List<People> peopleList, OutputStream out) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
            for (People person : peopleList) {
                writer.append("Name: ").append(person.getName()).append(", ")
                        .append("Group: ").append(person.getG()).append(", ")
                        .append("Telephone: ").append(person.getTelephone()).append(", ")
                        .append("Information: ").append(person.getInformation())
                        .append("\n\n");  // 添加额外的换行以便阅读
            }
            writer.flush();
            System.out.println("成功创建文本文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

