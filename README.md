# XVM(自己手写的Java VM)
手写JVM,为了目录比较清晰，该项目的目录结构是根据HotSpot的目录划分的，也是为了加深我们对HotSpot的理解
## 参考HotSpot版本1.8
源码下载链接: https://pan.baidu.com/s/158pDFPa2WYEVnJZ6qclxUw 提取码: 4p6f
## 主要模块
HotSpot源码结构相当庞大,在该项目中并没有完完全全地实现其中每个功能,一方面由于语言限制,Java不像C++那样可以直接操作内存,其实也可以实现，不过实现起来需要更多的
模块结构去支撑这一体系,难度可想而知。
### 1.字节码文件解析器
#### class文件组成
```java
public class HelloWorld {
    
    private static final int a = 1;
    
    private static final int b = 2;
    public static void main(String[] args) {
        System.out.println("Hello Wolrd!");

        System.out.println(add(a, b));
    }
    
    public static int add(int a, int b) {
        return a + b;
    }
}
```
以HelloWorld代码为例,经过javac编译之后生成HelloWorld.class,那么我们怎么解析呢？
![img.png](img.png)
其实在class文件中,虚拟机是有自己的规范的,按照一定的顺序来存放的,顺序如下:
![img_1.png](img_1.png)
1.魔数 4B(CAFE BABE)
2.次版本号 2B
3.主版本号 2B
4.常量池大小 2B
5.类的访问权限 2B
6.类名 2B
7.父类名 2B
8.实现的接口个数2B、实现的接口
9.属
10.方法数量 2B
11.方法
12.属性数量 2B
13.属性
在HotSpot源码中，class字节码文件解析的过程在/hotspot/src/share/vm/classfile/classFileParser.cpp中的parseClassFile方法当中
#### Java对象在C++中的存储形式

### 2.数据类型的处理

### 3.方法的栈帧处理、流程控制

### 4.支持lambda表达式

### 5.异常处理