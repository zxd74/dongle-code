浏览器存储核心选型原则：
* **简单键值对** → `localStorage/sessionStorage`；
* **大容量结构化数据** → `IndexedDB`；
* **离线资源缓存** → `Cache Storage/Service Worker Cache`；
* **隐私保护跨域场景** → `Shared Storage/Private State Tokens`；
* **扩展开发** → `Extension Storage`；
* **本地文件交互** → `File System Access API`。

# localStorage（永久存储，除非手动清除）
```js
// 存储数据（支持字符串，复杂数据需JSON序列化）
localStorage.setItem('username', 'zhangsan');
localStorage.setItem('userInfo', JSON.stringify({ age: 25, gender: 'male' }));

// 获取数据
const username = localStorage.getItem('username');
const userInfo = JSON.parse(localStorage.getItem('userInfo'));
console.log('localStorage获取：', username, userInfo);

// 遍历所有key
for (let i = 0; i < localStorage.length; i++) {
  const key = localStorage.key(i);
  console.log(`localStorage key[${i}]：${key} = ${localStorage.getItem(key)}`);
}

// 删除单个数据
localStorage.removeItem('username');

// 清空所有数据
// localStorage.clear();
```
# sessionStorage（会话存储，关闭标签页即清除）
```js
// 存储数据
sessionStorage.setItem('token', 'abc123456');
sessionStorage.setItem('cart', JSON.stringify([{ id: 1, name: '手机' }, { id: 2, name: '电脑' }]));

// 获取数据
const token = sessionStorage.getItem('token');
const cart = JSON.parse(sessionStorage.getItem('cart'));
console.log('sessionStorage获取：', token, cart);

// 删除单个数据
sessionStorage.removeItem('token');

// 清空所有数据
// sessionStorage.clear();
```
# Cookie（小型存储，可设置过期时间）
```js
// 设置Cookie（参数：key=value; expires=过期时间; path=/; domain=域名）
function setCookie(name, value, days = 7) {
  const date = new Date();
  date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
  const expires = `expires=${date.toUTCString()}`;
  document.cookie = `${name}=${encodeURIComponent(value)}; ${expires}; path=/`;
}

// 获取Cookie
function getCookie(name) {
  const cookies = document.cookie.split('; ');
  for (const cookie of cookies) {
    const [key, val] = cookie.split('=');
    if (key === name) return decodeURIComponent(val);
  }
  return '';
}

// 删除Cookie（设置过期时间为过去）
function deleteCookie(name) {
  setCookie(name, '', -1);
}

// 调用示例
setCookie('theme', 'dark', 3);
setCookie('lang', 'zh-CN');
console.log('Cookie获取theme：', getCookie('theme'));
deleteCookie('lang');
```
# IndexedDB（大容量结构化存储）
```js
// 打开/创建数据库（库名：myDB，版本：1）
const request = indexedDB.open('myDB', 1);

// 数据库升级/初始化（首次创建/版本升级时触发）
request.onupgradeneeded = function (e) {
  const db = e.target.result;
  // 创建对象仓库（表名：products，主键：id）
  if (!db.objectStoreNames.contains('products')) {
    const store = db.createObjectStore('products', { keyPath: 'id', autoIncrement: true });
    // 创建索引（方便按name查询）
    store.createIndex('nameIndex', 'name', { unique: false });
  }
};

// 打开成功
request.onsuccess = function (e) {
  const db = e.target.result;
  
  // 1. 新增数据（事务：readwrite 读写，readonly 只读）
  function addProduct(product) {
    const tx = db.transaction('products', 'readwrite');
    const store = tx.objectStore('products');
    store.add(product);
    tx.oncomplete = () => console.log('IndexedDB新增成功');
  }
  addProduct({ name: '耳机', price: 199 });
  addProduct({ name: '鼠标', price: 99 });

  // 2. 查询所有数据
  function getAllProducts() {
    const tx = db.transaction('products', 'readonly');
    const store = tx.objectStore('products');
    const req = store.getAll();
    req.onsuccess = () => console.log('IndexedDB所有数据：', req.result);
  }
  setTimeout(getAllProducts, 100); // 延迟确保新增完成

  // 3. 按索引查询（按name）
  function getProductByName(name) {
    const tx = db.transaction('products', 'readonly');
    const store = tx.objectStore('products');
    const index = store.index('nameIndex');
    const req = index.get(name);
    req.onsuccess = () => console.log(`IndexedDB查询${name}：`, req.result);
  }
  setTimeout(() => getProductByName('耳机'), 100);

  // 4. 删除数据（按主键id）
  function deleteProduct(id) {
    const tx = db.transaction('products', 'readwrite');
    const store = tx.objectStore('products');
    store.delete(id);
    tx.oncomplete = () => console.log(`IndexedDB删除id=${id}成功`);
  }
  // setTimeout(() => deleteProduct(1), 200);

  // 关闭数据库（页面关闭时建议执行）
  // db.close();
};

// 打开失败
request.onerror = function (e) {
  console.error('IndexedDB打开失败：', e.target.error);
};
```
# 其他Storage
* **Shared Storage/Interest Groups/Private State Tokens**：**Privacy Sandbox 系列 API**，聚焦隐私保护下的跨域 / 广告场景，需 HTTPS + 浏览器开启对应功能；
* **Cache Storage**：PWA 离线应用核心，用于缓存网络资源；
* **Storage Buckets**：实验性 API，分组管理存储数据，支持过期策略；
* **Extension Storage**：浏览器扩展专属，分 `local/sync/session` 三类，需配置权限。
* **Web SQL**：废弃但兼容老项目，优先用 IndexedDB 替代；
* **File System Access API**：读写本地文件，需用户授权，适合本地文件交互场景；
* **Service Worker Cache**：PWA 离线应用核心，动态缓存网络请求；
* **Clipboard API**：临时存储剪贴板内容，需用户交互触发；
* **Session History**：通过 URL/history state 临时存储数据，适合页面间传参。

## Shared Storage（共享存储，Privacy Sandbox 核心 API）
```js
// 注意：需在HTTPS环境/本地localhost，且浏览器开启Privacy Sandbox
async function useSharedStorage() {
  try {
    // 1. 获取Shared Storage实例
    const sharedStorage = await navigator.sharedStorage;
    if (!sharedStorage) {
      console.error('浏览器不支持Shared Storage');
      return;
    }

    // 2. 存储数据（仅支持字符串，复杂数据需JSON序列化）
    await sharedStorage.set('adPreference', 'sports', {
      ignoreIfPresent: false // 已存在时是否覆盖
    });
    await sharedStorage.set('userSegment', JSON.stringify({ age: 25, region: 'CN' }));

    // 3. 获取数据
    const adPref = await sharedStorage.get('adPreference');
    const userSeg = JSON.parse(await sharedStorage.get('userSegment') || '{}');
    console.log('Shared Storage获取：', adPref, userSeg);

    // 4. 删除数据
    await sharedStorage.delete('adPreference');

    // 5. 清空所有数据
    // await sharedStorage.clear();

    // 6. 跨域/上下文访问（核心能力，需配置权限）
    // 运行在Shared Storage Worklet中（示例）
    const workletCode = `
      class SelectionOperation {
        async run() {
          const userPref = await this.sharedStorage.get('adPreference');
          return userPref || 'default';
        }
      }
      register('select-ad', SelectionOperation);
    `;
    const blob = new Blob([workletCode], { type: 'text/javascript' });
    await sharedStorage.worklet.addModule(URL.createObjectURL(blob));
    const adResult = await sharedStorage.run('select-ad');
    console.log('Shared Storage Worklet执行结果：', adResult);
  } catch (e) {
    console.error('Shared Storage操作失败：', e);
  }
}
useSharedStorage();
```
## Cache Storage（缓存存储，PWA / 离线应用核心）
```js
// 注意：需在HTTPS环境/localhost，基于Service Worker/Window上下文
async function useCacheStorage() {
  try {
    // 1. 打开/创建缓存（缓存名：my-app-cache-v1）
    const cache = await caches.open('my-app-cache-v1');

    // 2. 缓存资源（请求+响应）
    // 缓存网络资源
    await cache.add('https://cdn.jsdelivr.net/npm/vue@3/dist/vue.global.prod.js');
    // 批量缓存
    await cache.addAll([
      '/index.html',
      '/static/css/main.css',
      '/static/img/logo.png'
    ]);

    // 3. 读取缓存资源
    const response = await cache.match('/index.html');
    if (response) {
      const html = await response.text();
      console.log('Cache Storage读取HTML：', html.substring(0, 100));
    }

    // 4. 遍历所有缓存项
    const cacheKeys = await cache.keys();
    console.log('Cache Storage所有缓存项：', cacheKeys.map(key => key.url));

    // 5. 删除指定缓存项
    await cache.delete('/static/img/logo.png');

    // 6. 删除整个缓存
    // await caches.delete('my-app-cache-v1');

    // 7. 列出所有缓存名
    const allCacheNames = await caches.keys();
    console.log('所有缓存名：', allCacheNames);
  } catch (e) {
    console.error('Cache Storage操作失败：', e);
  }
}
// 若在Service Worker中，需先注册SW；Window上下文可直接调用
useCacheStorage();
```
## Storage Buckets（存储桶，分组管理存储数据）
```js
// 注意：实验性API，需浏览器开启Experimental Web Platform Features
async function useStorageBuckets() {
  try {
    // 1. 获取Storage Buckets管理器
    const bucketManager = navigator.storageBuckets;
    if (!bucketManager) {
      console.error('浏览器不支持Storage Buckets');
      return;
    }

    // 2. 创建/打开存储桶（桶名：user-settings，配置过期策略）
    const bucket = await bucketManager.open('user-settings', {
      expiration: {
        ttl: 86400 * 7 * 1000 // 7天过期（毫秒）
      },
      durability: 'strict' // 严格持久化（vs 'relaxed'）
    });

    // 3. 操作桶内的localStorage（API与原生localStorage一致）
    const bucketLocalStorage = bucket.localStorage;
    bucketLocalStorage.setItem('theme', 'dark');
    bucketLocalStorage.setItem('fontSize', '16px');
    console.log('Storage Bucket获取：', bucketLocalStorage.getItem('theme'));

    // 4. 获取桶的存储配额
    const quota = await bucket.quota();
    console.log('Storage Bucket配额：', quota);

    // 5. 删除存储桶
    // await bucketManager.delete('user-settings');

    // 6. 列出所有存储桶
    const allBuckets = await bucketManager.keys();
    console.log('所有存储桶：', allBuckets);
  } catch (e) {
    console.error('Storage Buckets操作失败：', e);
  }
}
useStorageBuckets();
```
## Private State Tokens（私有状态令牌，防欺诈 / 匿名认证）
```js
// 注意：需HTTPS环境，浏览器开启Privacy Sandbox
async function usePrivateStateTokens() {
  try {
    // 1. 生成私有令牌（客户端）
    const tokenRequest = await navigator.createPrivateTokenRequest({
      issuer: 'https://example-issuer.com', // 令牌发行方域名
      refreshPolicy: 'on-use' // 刷新策略
    });
    if (!tokenRequest) {
      console.error('生成令牌请求失败');
      return;
    }

    // 2. 向发行方请求令牌（需后端配合）
    const response = await fetch('https://example-issuer.com/issue-token', {
      method: 'POST',
      body: JSON.stringify({ tokenRequest: tokenRequest.serialize() }),
      headers: { 'Content-Type': 'application/json' }
    });
    const tokenData = await response.json();
    
    // 3. 验证令牌（接收方/服务端）
    const verifyResult = await navigator.verifyPrivateToken({
      token: tokenData.token,
      challenge: 'random-challenge-string', // 随机挑战串
      issuer: 'https://example-issuer.com'
    });
    console.log('私有令牌验证结果：', verifyResult);

    // 4. 使用令牌进行匿名认证
    await fetch('https://example.com/api/auth', {
      method: 'POST',
      body: JSON.stringify({
        privateToken: tokenData.token,
        context: 'login'
      }),
      headers: { 'Content-Type': 'application/json' }
    });
  } catch (e) {
    console.error('Private State Tokens操作失败：', e);
  }
}
usePrivateStateTokens();
```
## Interest Groups（兴趣组，Privacy Sandbox 广告定向）
```js
// 注意：需HTTPS环境，浏览器开启Privacy Sandbox
async function useInterestGroups() {
  try {
    // 1. 加入兴趣组
    const navigatorParams = {
      name: 'sports-lovers', // 兴趣组名
      owner: 'https://example-adtech.com', // 兴趣组所属方
      biddingLogicUrl: 'https://example-adtech.com/bidding-logic.js', // 竞价逻辑
      dailyUpdateUrl: 'https://example-adtech.com/update-interest-group', // 每日更新
      ads: [ // 关联广告
        {
          renderUrl: 'https://example-adtech.com/sport-ad.html',
          metadata: { adId: 'sport-123', bid: 0.5 }
        }
      ],
      expiry: 86400 * 30 * 1000 // 30天过期
    };
    await navigator.joinAdInterestGroup(navigatorParams);

    // 2. 退出兴趣组
    // await navigator.leaveAdInterestGroup({
    //   name: 'sports-lovers',
    //   owner: 'https://example-adtech.com'
    // });

    // 3. 运行兴趣组竞价（仅在FLEDGE Auction中生效）
    const auctionConfig = {
      seller: 'https://example-seller.com',
      decisionLogicUrl: 'https://example-seller.com/decision-logic.js',
      interestGroupBuyers: ['https://example-adtech.com'],
      auctionStartTime: Date.now() + 1000,
      duration: 5000
    };
    const auctionResult = await navigator.runAdAuction(auctionConfig);
    console.log('兴趣组竞价结果：', auctionResult);
  } catch (e) {
    console.error('Interest Groups操作失败：', e);
  }
}
useInterestGroups();
```
## Extension Storage（扩展存储，Chrome/Firefox 扩展开发）
```js
// 注意：需在浏览器扩展环境（manifest v3/v2），需声明storage权限
// manifest.json（v3）配置示例：
// {
//   "manifest_version": 3,
//   "permissions": ["storage"],
//   "background": { "service_worker": "background.js" }
// }

// 1. Chrome/Firefox扩展通用示例（background.js/内容脚本）
// a. storage.local（本地存储，扩展内持久化）
chrome.storage.local.set({
  extensionSettings: { theme: 'dark', autoUpdate: true },
  history: ['page1', 'page2', 'page3']
}, () => {
  // 存储成功回调
  chrome.storage.local.get(['extensionSettings', 'history'], (result) => {
    console.log('Extension Storage Local：', result);
  });
});

// b. storage.sync（同步存储，跨设备同步）
chrome.storage.sync.set({
  userPref: 'sync-theme-light'
}, () => {
  chrome.storage.sync.get('userPref', (result) => {
    console.log('Extension Storage Sync：', result.userPref);
  });
});

// c. storage.session（会话存储，扩展重启即清除）
chrome.storage.session.set({
  tempToken: 'abc-123-xyz'
}, () => {
  chrome.storage.session.get('tempToken', (result) => {
    console.log('Extension Storage Session：', result.tempToken);
  });
});

// d. 删除数据
chrome.storage.local.remove('history', () => {
  console.log('Extension Storage删除history成功');
});

// e. 清空所有数据
// chrome.storage.local.clear(() => {
//   console.log('Extension Storage Local清空成功');
// });

// f. 监听存储变化
chrome.storage.onChanged.addListener((changes, namespace) => {
  for (const [key, { oldValue, newValue }] of Object.entries(changes)) {
    console.log(`Extension Storage ${namespace} 中${key}变化：`, oldValue, '→', newValue);
  }
});
```
## Web SQL Database（基于 SQL 的结构化存储，虽已废弃但仍有兼容场景）
```js
// 注意：仅Chrome/Safari等webkit内核浏览器支持，已被IndexedDB替代
function useWebSQL() {
  // 打开/创建数据库（库名：myWebSQL，版本：1.0，描述：测试库，预估大小：5MB）
  const db = openDatabase('myWebSQL', '1.0', 'Test WebSQL DB', 5 * 1024 * 1024);
  
  // 创建表
  db.transaction(tx => {
    tx.executeSql(
      'CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT, age INTEGER)',
      [],
      () => console.log('WebSQL表创建成功'),
      (tx, err) => console.error('WebSQL建表失败：', err)
    );
  });

  // 插入数据
  db.transaction(tx => {
    tx.executeSql(
      'INSERT INTO users (name, age) VALUES (?, ?)',
      ['张三', 28],
      (tx, res) => console.log('WebSQL插入成功，ID：', res.insertId),
      (tx, err) => console.error('WebSQL插入失败：', err)
    );
  });

  // 查询数据
  db.transaction(tx => {
    tx.executeSql(
      'SELECT * FROM users',
      [],
      (tx, res) => {
        for (let i = 0; i < res.rows.length; i++) {
          console.log('WebSQL查询结果：', res.rows.item(i));
        }
      },
      (tx, err) => console.error('WebSQL查询失败：', err)
    );
  });

  // 删除数据
  db.transaction(tx => {
    tx.executeSql(
      'DELETE FROM users WHERE id = ?',
      [1],
      () => console.log('WebSQL删除成功'),
      (tx, err) => console.error('WebSQL删除失败：', err)
    );
  });
}
useWebSQL();
```
## File System Access API（文件系统存储，读写本地文件）
```js
// 注意：需HTTPS环境，仅现代浏览器支持，需用户授权
async function useFileSystemAccess() {
  try {
    // 1. 打开文件选择器，选择文件（读文件）
    const [fileHandle] = await window.showOpenFilePicker({
      types: [{
        description: '文本文件',
        accept: { 'text/plain': ['.txt'] }
      }]
    });
    const file = await fileHandle.getFile();
    const content = await file.text();
    console.log('File System读取文件内容：', content);

    // 2. 保存文件到本地（写文件）
    const saveHandle = await window.showSaveFilePicker({
      suggestedName: 'test.txt',
      types: [{
        description: '文本文件',
        accept: { 'text/plain': ['.txt'] }
      }]
    });
    const writable = await saveHandle.createWritable();
    await writable.write('Hello File System Access API!');
    await writable.close();
    console.log('File System文件保存成功');

    // 3. 访问目录（创建/读写目录内文件）
    const dirHandle = await window.showDirectoryPicker();
    const newFileHandle = await dirHandle.getFileHandle('new-file.txt', { create: true });
    const newWritable = await newFileHandle.createWritable();
    await newWritable.write('目录内新建文件内容');
    await newWritable.close();
    console.log('File System目录内创建文件成功');
  } catch (e) {
    if (e.name !== 'AbortError') { // 排除用户取消选择的情况
      console.error('File System Access操作失败：', e);
    }
  }
}
// 需用户手动触发（如点击按钮），浏览器禁止自动调用
// document.querySelector('#fileBtn').addEventListener('click', useFileSystemAccess);
```
## Service Worker Cache（进阶缓存，结合 SW 的动态缓存）
```js
// 注意：需先注册Service Worker
// 1. 注册Service Worker（主页面代码）
async function registerSW() {
  if ('serviceWorker' in navigator) {
    try {
      const registration = await navigator.serviceWorker.register('/sw.js');
      console.log('Service Worker注册成功');
      return registration;
    } catch (e) {
      console.error('Service Worker注册失败：', e);
    }
  }
}
registerSW();

// 2. sw.js（Service Worker文件）
/*
self.addEventListener('install', (e) => {
  // 安装时缓存核心资源
  e.waitUntil(
    caches.open('sw-cache-v1').then(cache => {
      return cache.addAll([
        '/',
        '/index.html',
        '/static/css/style.css'
      ]);
    })
  );
});

self.addEventListener('fetch', (e) => {
  // 拦截请求，优先从缓存读取
  e.respondWith(
    caches.match(e.request).then(response => {
      // 缓存命中则返回，否则请求网络
      return response || fetch(e.request).then(networkRes => {
        // 动态缓存新请求的资源
        caches.open('sw-cache-v1').then(cache => {
          cache.put(e.request, networkRes.clone());
        });
        return networkRes;
      });
    })
  );
});
*/
```
## Clipboard API（剪贴板存储，临时存储文本 / 图片）
```js
// 注意：需用户交互触发（如点击），禁止自动调用
async function useClipboard() {
  try {
    // 写入剪贴板
    await navigator.clipboard.writeText('剪贴板存储的临时内容');
    console.log('剪贴板写入成功');

    // 读取剪贴板
    const text = await navigator.clipboard.readText();
    console.log('剪贴板读取内容：', text);

    // 读写图片（进阶）
    // 写入图片
    const imgBlob = await fetch('/img/test.png').then(res => res.blob());
    await navigator.clipboard.write([
      new ClipboardItem({
        'image/png': imgBlob
      })
    ]);
    // 读取图片
    const clipboardItems = await navigator.clipboard.read();
    for (const item of clipboardItems) {
      for (const type of item.types) {
        if (type === 'image/png') {
          const blob = await item.getType(type);
          const imgUrl = URL.createObjectURL(blob);
          console.log('剪贴板读取图片URL：', imgUrl);
        }
      }
    }
  } catch (e) {
    console.error('剪贴板操作失败：', e);
  }
}
// 需用户点击触发
// document.querySelector('#clipboardBtn').addEventListener('click', useClipboard);
```
## Session History（会话历史存储，URL 参数 /history state）
```js
// 1. URL参数存储（临时传递数据，刷新不丢失）
function useUrlParams() {
  // 设置URL参数
  const params = new URLSearchParams(window.location.search);
  params.set('userId', '123');
  params.set('page', '2');
  window.history.replaceState({}, '', `${window.location.pathname}?${params.toString()}`);
  console.log('URL参数设置成功：', window.location.href);

  // 读取URL参数
  const userId = params.get('userId');
  const page = params.get('page');
  console.log('URL参数读取：', userId, page);

  // 删除URL参数
  params.delete('page');
  window.history.replaceState({}, '', `${window.location.pathname}?${params.toString()}`);
}
useUrlParams();

// 2. History State存储（不修改URL参数，存储复杂数据）
function useHistoryState() {
  // 存储state
  window.history.pushState(
    { data: { name: '张三', age: 28 } }, // 存储的任意数据
    '', // 标题（无实际作用）
    '/new-page' // 可选：修改URL路径（不刷新页面）
  );
  console.log('History State存储成功');

  // 读取state
  console.log('History State读取：', window.history.state);

  // 监听state变化（前进/后退触发）
  window.addEventListener('popstate', (e) => {
    console.log('History State变化：', e.state);
  });
}
useHistoryState();
```