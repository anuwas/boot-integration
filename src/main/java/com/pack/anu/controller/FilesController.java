package com.pack.anu.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FilesController {
	
	@Value( "${file.input}" )
	private String INPUT_DIR;
	
	@GetMapping({"/", "/files"})
    public String hello(Model model) {
		List<String> fileNames = new ArrayList<String>();


		File[] files = new File(INPUT_DIR).listFiles();

		for (File file : files) {
		    if (file.isFile()) {
		    	fileNames.add(file.getName());
		    }
		}
        model.addAttribute("fileNames", fileNames);
        return "filelist";
    }
	
	@GetMapping(value = "/files/{filename}")
    public @ResponseBody String rePlayFile(@PathVariable String filename) throws IOException {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       
       File source = new File(INPUT_DIR+"/"+filename);
       File dest = new File(INPUT_DIR+"/"+"Played_"+timestamp.getTime()+"_"+filename);

       //copy file conventional way using Stream
       long start = System.nanoTime();
       copyFileUsingStream(source, dest);
       
        return filename;
    }
	

private static void copyFileUsingStream(File source, File dest) throws IOException {
    InputStream is = null;
    OutputStream os = null;
    try {
        is = new FileInputStream(source);
        os = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    } finally {
        is.close();
        os.close();
    }
}

	
}
