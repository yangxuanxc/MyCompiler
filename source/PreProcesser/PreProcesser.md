# 预处理器

## 1.说明
    对输入的源程序进行预处理，实现将空白符和注释去掉
## 2.实现效果
### 给出一段c语言代码，其中有错误和正确的词法。
### 处理前的示例：
```c
int main(int a,                      int b){    //1120131802 杨旋
	/*这是一个多行的注释
	 *
	 *
	 * */int          m = 5;
						//   这是一个单行的注释

	 /********/
    return    a     +    b;
}

struct jiegou{//这是一个结构体
	int struct_son;
}
//在这个函数中进行测试
void test(int m){
	char char1 = 'b'; //测试字符
	char char2 = '\'';//测试转义字符
	string _kk = "aaaaaaaaa";//测试字符串
	string _kk1 = "5555\"\b\n";//测试带有转义字符的字符串

	int int_8 = 0222;//测试八进制整数
	float float_8 = 05.77;//测试八进制整数 或者05e4
	float float_10 = -10.22;//测试10进制浮点数
	float int_16 = 0x11af;//测试16进制整数
	double science_num = 11e2;//测试科学计数法
	long long_int = 1155L;
	//测试符号
	int a,b,c;
	c=a+b;
	c++;
	c+=b;
	c=a-b;
	c-=b;
	c--
	c = c*1;
	c*=a;
	c = a%b;
	c = a/b;
	c = a>b;
	c = a>>2;
	c = a&b;
	struct S;
	S.struct_son//测试结构
	//测试输出
	printf("zhende%d\n",m);
	return m;
}

void test_wrong(){

	string haha = "xxxxx
			xx";
	char b = 'dddd';



}

```
### 处理后的示例：

```c
int main(int a, int b){    



int m = 5;



return a + b;
}

struct jiegou{
int struct_son;
}

void test(int m){
char char1 = 'b'; 
char char2 = '\'';
string _kk = "aaaaaaaaa";
string _kk1 = "5555\"\b\n";

int int_8 = 0222;
float float_8 = 05.77;
float float_10 = -10.22;
float int_16 = 0x11af;
double science_num = 11e2;
long long_int = 1155L;

int a,b,c;
c=a+b;
c++;
c+=b;
c=a-b;
c-=b;
c--
c = c*1;
c*=a;
c = a%b;
c = a/b;
c = a>b;
c = a>>2;
c = a&b;
struct S;
S.struct_son

printf("zhende%d\n",m);
return m;
}

void test_wrong(){

string haha = "xxxxx
xx";
char b = 'dddd';



}

```

## 3.代码说明

在包pp下创建包 建立类：''PreProcessor''

IO:使用字符输出流Reader，Writer 对文件实现读写

将文件中的内容存在字符数组 char[] 中进行保存，逐个字符进行处理，判断三类空白字符

空白，tab，以及回车符。注释分别判断"//" 和"/**/"

程序流程
![](source\PreProcesser\3.png))

## 源代码

### 参见项目目录地址下bit.minisys.minicc.pp 下的 PreProcessor.java
