# 类型限定
* class类型限定
```kotlin
class Dongle(t:Any)
class Dongle<T:Any>(t:T)
```
* function类型限定
```kotlin
fun setClass(cls:Class<Activity>)  // 明确类 
fun setClass(cls:Class<*>)  // 任意类
fun <T:Activity> setClass(cls:Class<T>)  // 任意Activity子类(泛型)
```
