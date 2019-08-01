struct point{
    int x;
    int y;
}

//int RAND_SEED = 0;
def int abs(int a){
    return a > 0? a : -a;
}

def int square(int a){
    return a * a;
}

def bigreal sqrt(bigreal n){
	if ( n > -0.000001 && n < 0.000001) {
		return 0;
	}
    bigreal a = n/2;
    for(int i = 0;i < 40;i++){
        a = (n/a+a)/2;
    }
    return a;
}

def int max(int a,int b){
    return a > b? a:b;
}

def int min(int a,int b){
    return a < b?a:b;
}

//def int srand(int v){
//    return RAND_SEED = v;
//}

//def int rand(){
//    RAND_SEED *= 134775813;
//    RAND_SEED += 1073676287;
//    RAND_SEED %= 2147483647;
//    return abs(RAND_SEED);
//}

def int qsort(int[] s,int l,int r){
    while (l < r){
        int i = l, j = r, x = s[l];  
        while (i < j)  
        {  
            while(i < j && s[j]>= x) j--;   
            if(i < j) s[i++] = s[j];  
            while(i < j && s[i]< x) i++;
            if(i < j) s[j--] = s[i];
        }
        s[i] = x;  
        qsort(s, l, i - 1);
        l++;
    }
    return 0;
}

def void top_n(int[] s, int n) {
    int l = 0;
    int r = sizeof(s) - 1;
    while (l < r) {
        int i = l;
        int j = r;
        int x = s[l];
        
        while (i < j) {
            while(i < j && s[j] >= x) j--;
            if(i < j) s[i++] = s[j];  
            while(i < j && s[i]< x) i++;
            if(i < j) s[j--] = s[i];
        }
        s[i] = x;
        if (i == n - 1 || i == n) {
            return;
        }
        if (i < n - 1) {
            l = i + 1;
        } else {
            r = i - 1;
        }
    }
    return;
}

{
    int[] tmp = {2, 3, 1 ,7, 6, 5, 4, 2, 5, 8};
    //qsort(tmp, 0, sizeof tmp - 1);
    top_n(tmp, 9);
    for (int i = 0; i < 9; i++) {
        print("" + tmp[i] + " ");
    }
    print("\n");
}

native<extension.math.functions>{
    "sin":real sin(real theta);
    "cos":real cos(real theta);
    "SetSeed":void srand(int seed);
    "Random":int rand();
}