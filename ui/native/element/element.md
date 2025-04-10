# Element对象
* `document.createElement(tagName)` 创建一个元素
* `document.createTextNode(text)` 创建一个文本节点
* `document.createDocumentFragment()` 创建一个文档片段
* `document.getElementById(id)` 获取一个元素
* `document.getElementsByClassName(className)` 获取一个类名的元素列表
* `document.getElementsByTagName(tagName)` 获取一个标签名的元素列表
* `document.getElementsByName(name)` 获取一个名称的元素列表
* `document.querySelector(selector)` 获取一个选择器的第一个元素
* `document.querySelectorAll(selector)` 获取一个选择器的所有元素
* `element.appendChild(child)` 将一个子元素添加到元素的子元素列表的末尾
* `element.insertBefore(newChild, refChild)` 将一个新子元素插入到指定子元素之前
* `element.removeChild(child)` 从元素的子元素列表中移除一个子元素
* `element.replaceChild(newChild, oldChild)` 用一个新子元素替换指定子元素
* `element.cloneNode(deep)` 克隆一个元素
* `element.contains(child)` 判断一个元素是否是另一个元素的子元素

# 属性操作
* `element.getAttribute(name)` 获取属性值
* `element.setAttribute(name, value)` 设置属性值
* `element.removeAttribute(name)` 移除属性值
* `element.hasAttribute(name)` 判断属性是否存在
* `element.attributes` 获取所有属性
* `element.dataset` 获取所有 `data-*` 属性
* `element.style` 获取或设置元素的行内样式
* `element.classList` 获取或设置元素的类名
* `element.classList.value` 获取所有类名
* `element.innerHTML` 获取或设置元素的 HTML 内容
* `element.outerHTML` 获取或设置元素的 HTML 内容（包括元素本身）
* `element.textContent` 获取或设置元素的文本内容
* `element.value` 获取或设置元素的值（适用于表单元素）
* `element.checked` 获取或设置复选框或单选按钮的选中状态
* `element.disabled` 获取或设置元素的禁用状态
* `element.hidden` 获取或设置元素的隐藏状态
* `element.id` 获取或设置元素的 ID
* `element.name` 获取或设置元素的名称
* `element.title` 获取或设置元素的标题
* `element.lang` 获取或设置元素的语言
* `element.[attr]` 获取或设置元素的属性值（适用于自定义属性）
* `element.onclick` 获取或设置元素的点击事件处理函数
* `element.onchange` 获取或设置元素的值改变事件处理函数
* `element.oninput` 获取或设置元素的输入事件处理函数
* `element.onfocus` 获取或设置元素的获取焦点事件处理函数
* `element.onblur` 获取或设置元素的失去焦点事件处理函数
* `element.onmouseXXX` 获取或设置元素的鼠标事件处理函数
* `element.onkeyXXX` 获取或设置元素的键盘事件处理函数

# 方法操作
* `element.addEventListener(type, listener, options)` 添加事件监听器
* `element.removeEventListener(type, listener, options)` 移除事件监听器
* `element.dispatchEvent(event)` 触发事件
* `element.focus()` 使元素获取焦点
* `element.blur()` 使元素失去焦点
* `element.scrollIntoView(options)` 滚动元素进入视口
* `element.scrollBy(options)` 滚动元素
* `element.scrollTo(options)` 滚动元素到指定位置
* `element.getBoundingClientRect()` 获取元素的位置和大小
* `element.querySelector(selector)` 获取元素的选择器
* `element.querySelectorAll(selector)` 获取元素的所有选择器
* `element.closest(selector)` 获取元素最近的父元素
* `element.matches(selector)` 判断元素是否匹配选择器
* `element.animate(keyframes, options)` 创建动画
* `element.requestFullscreen()` 使元素全屏显示
* `element.exitFullscreen()` 退出全屏显示
* `element.requestPointerLock()` 请求指针锁定
* `element.exitPointerLock()` 退出指针锁定
* `element.requestAnimationFrame(callback)` 请求动画帧
* `element.cancelAnimationFrame(id)` 取消动画帧
* `element.requestIdleCallback(callback, options)` 请求空闲回调
* `element.cancelIdleCallback(id)` 取消空闲回调
