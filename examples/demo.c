void sub(int *x,int *y){
    int a,d;
}

int main(){
    int a,b,p,q,r;
    p = &a;
    q = p;
    p = &b;
    r = p;
    sub(&r,&b);
}