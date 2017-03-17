native<extension.system>{
    //todo : add function contract
    "SimpleFile":struct SimpleFile;
    "SimpleFileInputStream":struct FileInputStream;
    "SimpleFileOutputStream":struct FileOutputStream;
    
}

struct File {
    string fname;
    SimpleFile file;
    FileInputStream fis;
    FileOutputStream fos;
    
    def this(){}

    def bool open(string fname,bool append){
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

    def void close(){
        if(this.fis != null){
            this.fis.close();
        }
        if(this.fos != null){
            this.fos.close();
        }
        this.file = null;
    }

    def void writech(char c){
        this.fos.writeInt(c);
    }

}