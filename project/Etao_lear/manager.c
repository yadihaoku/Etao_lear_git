#include <stdio.h>
#include <stdlib.h>

#define MAX 100
struct Course{
	int c_no;
	char * c_name;
	char * c_feature;
};
typedef struct Course *COURSE;

void loadData(COURSE []);
char * i2s(int);

int main(char **args){
	
	
	
	COURSE courses [MAX] ;
	
	loadData(courses);
	
	struct Course zhangsan;
	zhangsan.c_name = "我是谁\0"; 
	
	courses[0] = &zhangsan;
	
//	free(courses[0]);
	
	
	printf("hello! %s\n", courses[10]->c_name);
	printf("%s", i2s(1990));
	char test[100];
	printf("\n%s", itoa(12312, test,10));
	system("pause");
	
	return 0;
}


/**
* 从保存至本地的文件中 读取数据 
**/
void loadData( COURSE course [] ){
	
	int i=0 ;
	for(; i < MAX ; i++){
		struct Course *c1 =  malloc(sizeof(struct Course));
		c1->c_no = i;
		c1->c_name = i2s(i);
		course[i] = c1;
	}	

}

/***
 int 类型转换为 char *
**/
char* i2s(int i){
	char *s = malloc(sizeof(char) * 20);
	
	 int index = 0;
	 int t = 0;
	do{
	 	t = i % 10;
	 	i /= 10;
		s[index++] = t + '0';
	}while(i > 0);

	
	//反转数组
	int currentIndex = 0;
	for(;currentIndex < index / 2; currentIndex++){
		char tmp = s[currentIndex];
		int realIndex = index - 1 - currentIndex ;
		s[currentIndex] = s[realIndex];
		s[realIndex] = tmp;
	}
	s[index] = '\0';
	return s;
}
