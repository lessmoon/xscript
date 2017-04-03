import"../container/content.xs";
struct Comparator{
    def default virtual int compare(Content a,Content b);
}

def void sortArray(Content[] s,int l,int r,Comparator less){
    while (l < r){
        int i = l, j = r;
        auto x = s[l];  
        while (i < j){  
            while(i < j && less.compare(s[j],x)>=0) j--;   
            if(i < j) s[i++] = s[j];  
            while(i < j && less.compare(s[i],x)<0) i++;
            if(i < j) s[j--] = s[i];
        }
        s[i] = x;
        sortArray(s, l, i - 1,less);
        l++;
    }
}