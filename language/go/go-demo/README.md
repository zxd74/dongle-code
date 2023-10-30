# 基础
## 接口
* 定义：`type InterfaceName interface{}`
* 实现：类定义与接口相同的函数(名称，参数，返回)，代表类实现了接口
* 继承：
  * 方式一：相同内容形式，一个接口的定义内容保全另一个接口的全部内容，则该接口表现为继承另一个接口，可以多继承
  * 方式二：通过匿名结构体继承
* 转换：满足类型转换条件，大范围可以转小范围，小范围不能扩大转换
```go
type Dongle interface {
	Hello() string
}
type Kk interface {
	Test()
}
type Kevin interface {
	Hello() string
	Test()
    KD()
}
// Kevin 继承Dongle接口的Hello函数
// Kevin 也继承Kk接口的Test函数
// Kevin 新增KD函数

var d Dongle;
// d 赋值略
k := Kevin(d)   // error 错误转换，Kevin范围更大，Dongle范围小，小范围无法强转为大范围

var e Kevin;
// e 赋值略
d := Dongle(k) // 正确转换，Kevin包含Dongle全部内容，可缩小转换，不能扩大转换
```

## 类/结构
* 定义:`type StructName Struct{}`
* 继承
  * 嵌套另一个匿名结构体，自动可以直接访问其属性和方法
  * 嵌套另一个有名结构体，称为组合
  * 嵌套多个匿名结构体，实现多继承
* 转换：`type(obj)`,只有一个对象包含另一个对象全部属性和方法时，才能显示转换，且不可逆，（接口同样适用）

## 继承/实现
### 匿名属性
* 匿名一个属性，代表单继承
* 匿名多个属性，代表多继承
* 有名属性，代表组成成员，非继承关系
```go
type Hello struct {}
type HelloWorld struct {}
type Demo struct{}
type Test struct {
    Hello // 匿名Hello属性
    HelloWorld //匿名HelloWorld属性
    d Demo // 有名属性，代表组成成员，非继承关系
}
```

### 继承
* 匿名方式：通过匿名接口/结构属性，可自动继承接口/结构的属性或方法
* 接口的继承还可以通过定义相同成员体现
#### 结构继承
```go
type Hello struct {
	name string
	age  int
}
func (h *Hello) Name() {
	h.name = "kk"
}
func (h *Hello) Aage() {
	h.age = 10
}

type HelloWorld struct{
    Hello // 匿名结构体，代表继承
    hello Hello // 有名结构体，代表组成(组合)
}

func main(){
    HelloWorld := new(Dt)
    d.age = 30
	d.Name()  // HelloWorld对象可以通过匿名Hello结构体，直接访问Hell的属性和方法
}
```
#### 接口继承
```go
type IHello interface{
    Hello() string
}
type IHelloWorld interface{
    HelloWorld()
    WorldHello()
}
type ITest interface{
    IHello //匿名方式实现继承
    // 定义全部相同函数名称实现继承
    HelloWorld() 
    WorldHello()
}
```

### 接口实现
* 结构体实现接口方法即可表现实现
* 匿名方式
```go
type IHello interface{
    Hello() string
}
type Hello struct {
}
func (h *Hello)Hello()string{ // Hello结构体实现IHello接口的Hello方法
    return "Hello!"
}
```

## 转换
* type(obj): 要求obj 对象的类型和 Type 是等价类型，即实现了相同的方法
* data.(type): 类型断言，用于验证数据类型
```go
type IHello interface{
    Hello() string
}
type Hello struct {
}
func (h *Hello)Hello()string{
    return "Hello!"
}

type HelloWorld struct {
	IHello
}
func (h *HelloWorld) Hello() string {
	return "HelloWorld"
}
func (h *HelloWorld) Tt() string {
	return "Tt"
}

func main(){
    h:=new(Hello)
    // 转换为接口
    ih:=IHello(h)
    if _,ok:= ih.(IHello);ok == true{ // data.(Type)返回一个接口时容易抛出异常
        fmt.Println("ih type is IHello")
    }

    // 转换为结构体
    hw := (*HelloWorld)(h)
	fmt.Println(hw.Hello())
	fmt.Println(hw.Tt())
    h = (*Hello)(hw)
	println(h.Hello())
}
```
```text
ih type is IHello
ih type is Hello Pointer

HelloWorld
Tt
Hello
```