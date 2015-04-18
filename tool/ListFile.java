package tool;

import java.io.File;

public class ListFile{
    
    public static void listFile(File f,String prefix,String suffix){
        for(File i : f.listFiles()){
            if(i.isDirectory()){
                listFile(i,prefix + "/" + i.getName(),suffix);
            } else {
                if(i.getName().endsWith(suffix))
                    System.out.println(prefix + "/" + i.getName());
            }
        }
    }

    public static void main(String[] argv){
        String suffix= "",prefix = ".";
        File f;

        if(argv.length > 0){/*path*/
            suffix = argv[0];
        } else {
            System.exit(1);
        }
        if(argv.length > 1){
            prefix = argv[1];
            f = new File(prefix);
            if(!f.exists()){
                System.exit(2);
            }
        } else {
            f = new File(prefix);
        }
        listFile(f,prefix,suffix);
        return;
    }
}