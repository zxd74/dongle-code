# 数据类型
## 构建批量属性
```ts
/**
type xxx={ // 类似类型定义
    p0:string;
    p1:string;
    p2:string;
    ...
    p99:string;
}
type GenerateObject={ 
    [K in Fileds]:string;
} // 定义批量属性的类型

type Fileds<Props extends string[]>= Props[number] // 定义Fileds递归属性生成器

type Fileds<Props extends string[]=[]> = Props['length'] extends 99? Props[number]:Fileds<[...Props,`p${Props['length']}`]>; // 定义指定长度的递归属性生成器： 通过三目运算符执行递归

type Fileds<Count,Props extends string[]=[]> = Props['length'] extends Count? Props[number]:Fileds<Count,[...Props,`p${Props['length']}`]>; // 定义可变长度的递归属性生成器

type GenerateObject<Count>={ // 根据递归属性变动修改GenerateObject
    [K in Fileds<Count>]:string;
} 
 */

type Fileds<Count,Props extends string[]=[]> = Props['length'] extends Count? Props[number]:Fileds<Count,[...Props,`p${Props['length']}`]>;
type GenerateObject<Count>={
    [K in Fileds<Count>]:string;
}

// 对于GenerateObject<Count>动态类型实例化时无法通过new实现，因GenerateObject<Count>是一个纯类型，而非值或函数，类型只在编译期存在，运行时会被擦除
// 方法一：直接定义完整类型: 最类型安全
var obj: GenerateObject<3> = {
    p0: "",
    p1: "",
    p2: "",
};

// 方法二：class+泛型：constructor
class GenerateObject<Count> {
    // 声明动态属性（需手动初始化）
    [key: `p${number}`]: string;

    constructor(count: Count) {
        // 初始化属性（运行时动态赋值）
        for (let i = 0; i < count; i++) {
            this[`p${i}` as `p${number}`] = ""; // 默认空字符串
        }
    }
}
const obj = new GenerateObject(3);

// 方法三：proxy代理：动态属性
function createDynamicObject<Count extends number>(count: Count) {
    const obj = {} as Record<`p${number}`, string>;
    return new Proxy(obj, {
        get(target, prop: string) {
            return target[prop as `p${number}`] || "";
        },
        set(target, prop: string, value: string) {
            if (/^p\d+$/.test(prop)) {
                target[prop as `p${number}`] = value;
                return true;
            }
            throw new Error(`Invalid property name: ${prop}`);
        }
    });
}
const obj = createDynamicObject(3);
```
