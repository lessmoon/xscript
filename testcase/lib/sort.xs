struct Comparable{}

struct IntComparable : Comparable{
    int value;
    
    def this(int value){
        this.value = value;
    }
}

struct StringComparable : Comparable{
    string value;
    
    def this(string value){
        this.value = value;
    }
    
}

struct Comparator{
    def virtual int check(Comparable a,Comparable b);
}

def void sortArray(Comparable[] s,int l,int r,Comparator less){
    while (l < r){
        int i = l, j = r, x = s[l];  
        while (i < j){  
            while(i < j && less.check(s[j],x)>=0) j--;   
            if(i < j) s[i++] = s[j];  
            while(i < j && less.check(s[i],x)<0) i++;
            if(i < j) s[j--] = s[i];
        }
        s[i] = x;  
        sortArray(s, l, i - 1,less);
        l++;
    }
}