int main(){
    int p,q,r,s,t,a,b,c;
    p = &a
    q = &b
    *p = q;
    r = &c;
    s = p;
    t = *p;
    *s = r;
}