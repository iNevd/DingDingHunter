package indi.nevd.common;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nevd on 8/3/2017.
 */

public class Util {
    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.toString();
    }

    public static String getDateYMD(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new java.util.Date());
    }

    public static void appendStringToFile(String file, String str){
        File folder = new File(file.substring(0, file.lastIndexOf(File.separator)));
        if(!folder.exists()){
            folder.mkdir();
        }
        BufferedWriter fout = null;
        try{
            str += "\n";
            fout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            fout.write(str);
        }catch  (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String mapToUriString(Map map){
        java.util.Map.Entry entry;
        StringBuffer sb = new StringBuffer();

        for(Iterator iterator = map.entrySet().iterator(); iterator.hasNext();)
        {
            entry = (java.util.Map.Entry)iterator.next();
            sb.append(entry.getKey().toString()).append( "=" ).append(null==entry.getValue()?"":
                    entry.getValue().toString()).append (iterator.hasNext() ? "&" : "");
        }
        return sb.toString();
    }
}
