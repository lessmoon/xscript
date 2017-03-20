native<extension.system>{
    "SimpleFile":struct SimpleFile{
        def this(string path);
        def string getName();
        def bool canRead();
        def bool canWrite();
        def bool isDirectory();
        def bool isFile();
        def bool isHidden();
        def bool exists();
        def bool mkdir();
        def bool createNewFile();
        def SimpleFile getParent();
    };
    "SimpleFileInputStream":struct FileInputStream{
        def this(SimpleFile file);
        def void open(SimpleFile file);
        def int readChar();
        def void close();
        def bigint skip(bigint i);
    };
    "SimpleFileOutputStream":struct FileOutputStream{
        def this(SimpleFile file,bool append);
        def void open(SimpleFile file,bool append);
        def void writeInt(int aInt);
        def void close();
        def void flush();
    };
}

struct File {
    string fname;
    SimpleFile file;
    FileInputStream fis;
    FileOutputStream fos;
    
    def this(){}

    def void close(){
        if(this.fis != null){
            this.fis.close();
        }
        if(this.fos != null){
            this.fos.close();
        }
        this.file = null;
    }
    
    def bool open(string fname,bool append){
        this.close();
        this.fname = fname;
        this.file = new SimpleFile(this.fname);
        if(!this.file.exists()){
            this.file.createNewFile();
        }
        if(this.fis == null)
            this.fis = new FileInputStream(this.file);
        else
            this.fis.open(this.file);
        if(this.fos == null)
            this.fos = new FileOutputStream(this.file,append);
        else 
            this.fos.open(this.file,append);
    }

    def bool is_open(){
        return this.file != null;
    }

    def string file_name(){
        return this.fname;
    }

    def int readch(){
        return this.fis.readChar();
    }

    def void writech(char c){
        this.fos.writeInt(c);
    }

}