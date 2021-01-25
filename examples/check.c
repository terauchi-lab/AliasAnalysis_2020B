int *pt(int *x,int *y){
    return x;
}

int main(){
    int a,b,p,q,r;
    p = &a;
    q = p;
    p = &b;
    r = pt(p,b);
}