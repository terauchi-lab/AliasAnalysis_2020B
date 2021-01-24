int *pt(){
    int a,d;
    return &a;
}

int main(){
    int a,b,p,q,r;
    p = &a;
    q = p;
    p = &b;
    r = p;
}