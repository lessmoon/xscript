native<extension.system>{
    int open(string fname);
    int close(int fid);
    int readch(int fid);
    int writech(int fid,char c);
}

struct File {
	string fname;
	int fid;

	def this(){
		this.fname = "";
		this.fid   = -1;
	}

	def bool open(string fname){
		this.fname = fname;
		return ((this.fid = open(fname)) < 0);
	}

	def bool is_open(){
		return this.fid < 0;
	}

	def string file_name(){
		return this.fname;
	}

	def int readch(){
		return readch(this.fid);
	}

	def void close(){
		close(this.fid);
		this.fid   = -1;
	}

	def void writech(char c){
		writech(this.fid,c);
	}

}