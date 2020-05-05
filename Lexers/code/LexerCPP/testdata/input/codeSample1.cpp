#include <iostream>       /

#define MAX_NUM 1222 /   / 
#ifdef rjerhier


int main(){

    string s1 = "i am string";
    string s2 = "string number2 ??/ 
";
    string s3 = "string number 3 \       
continues";

/*   this is multiline comment
*/

/*this is multiline comment2*/

    /*this is multiline comment3
            */

    double a,b, c, d;
    int f = 012;
    int g = f;
    int h = 10 * 12;

    long long ll1 = 123ll;
    long long ll2 = 123LL;

    unsigned long ll3 = 123ul;
    unsigned long ll4 = 123uL;
    unsigned long ll5 = 123Ul;
    unsigned long ll6 = 123UL;

    unsigned long long ll7 = 123ull;
    unsigned long long ll8 = 123uLL;
    unsigned long long ll9 = 123Ull;
    unsigned long long ll10 = 123ULL;

    int bin1 = 0b00101;
    int bin1 = 0B00101;
    int hex1 = 0x190abcde;
    int hex2 = 0X190abcde;
    int oct1 = 017;

//float type literals
    double d1 = .1234;
    double d2 = 0.1234;
    double d3 = 0.1234e+12;
    double d4 = 0.1234e-12;
    double d5 = 0.1234e12;
    double d6 = 0.1234E+12;
    double d7 = 0.1234E-12;
    double d8 = 0.1234E12;

//float type double(D)
    double d9 = .1234D;
    double d10 = 0.1234D;
    double d11 = 0.1234e+12D;
    double d12 = 0.1234e-12D;
    double d13 = 0.1234e12D;
    double d14 = 0.1234E+12D;
    double d15 = 0.1234E-12D;
    double d16 = 0.1234E12D;

//float type double(D) long (L)
    double d17 = .1234DL;
    double d18 = 0.1234DL;
    double d19 = 0.1234e+12DL;
    double d20 = 0.1234e-12DL;
    double d21 = 0.1234e12DL;
    double d22 = 0.1234E+12DL;
    double d23 = 0.1234E-12DL;
    double d24 = 0.1234E12DL;

//float type double(d)
    double d25 = .1234d;
    double d26 = 0.1234d;
    double d27 = 0.1234e+12d;
    double d28 = 0.1234e-12d;
    double d29 = 0.1234e12d;
    double d30 = 0.1234E+12d;
    double d31 = 0.1234E-12d;
    double d32 = 0.1234E12d;

//float type double(d) long (l)
    double d33 = .1234dl;
    double d34 = 0.1234dl;
    double d35 = 0.1234e+12dl;
    double d36 = 0.1234e-12dl;
    double d37 = 0.1234e12dl;
    double d38 = 0.1234E+12dl;
    double d39 = 0.1234E-12dl;
    double d40 = 0.1234E12dl;

//char literals
    char c1 = 'c';
    char c2 = 'abcd'; //it is taken only last symbol
    char c3 = '\   
    ';
    char c4 = '\\';
    char c5 = '\
    \
    a'; //it is correct

    return 0;
}

class CustomClass{
    int myA;

public : 
    CustomClass(int a){
        myA = a;
    }

    void showA(){
        cout << myA;
    }

    int getA(){
        return myA;
    }
}

/* not comment but error because not closed







