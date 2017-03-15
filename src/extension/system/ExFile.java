package extension.system;

import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

class Node {
    private File file;
    FileInputStream fis;
    FileOutputStream fos;
    Node(File f,FileInputStream i,FileOutputStream o){
        file = f;
        fis  = i;
        fos  = o;
    }
}

public class ExFile {
    static private int id = 1;
    private static HashMap<Integer,Node> filemap = new HashMap<Integer,Node>();
    
    static public int openfile(String filename){
        try {
            File f = new File(filename);
            if(!f.exists())
                f.createNewFile();
            Node n = new Node(f,new FileInputStream(f),new FileOutputStream(f,true));
            filemap.put(id,n);
            return id++;
        } catch (Exception e){
            return -1;
        }
    }
    
    static public int readch(int fid){
        try{
            Node n = filemap.get(fid);
            if(n == null){
                return -2;/*file not open*/
            }
            return n.fis.read();
        } catch (Exception e){
            return -1;
        }
    }
    
    static public int writech(int fid,char c){
        try{
            Node n = filemap.get(fid);
            if(n == null){
                return -2;/*file not open*/
            }
            n.fos.write((int)c);
            return fid;
        } catch (Exception e){
            return -1;
        }
    }

    static public int close(int fid){
        try{
            Node n = filemap.get(fid);
            if(n == null){
                return -2;/*file not open*/
            }
            n.fos.flush();
            n.fos.close();
            n.fis.close();
            filemap.remove(fid);
            return fid;
        } catch (Exception e){
            return -1;
        }
    }
}