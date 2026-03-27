# Nodejs

## MCP Server
1. 环境准备
   1. python:`pip install "mcp[cli]"`
   2. node: `@modelcontextprotocol/sdk-typescript`
      1. 第三方：`@mcp/node node-fetch`
   3. java: `dev.mcp:mcp-java`
  ```bash
  # Create project folder
  mkdir my-first-mcp-server
  cd my-first-mcp-server

  # Initialize Node.js project
  npm init -y

  # Install MCP SDK
  npm install @modelcontextprotocol/sdk-typescript
  ```
2. 实现MCP Server
   1. 创建基本结构
  ```js
  // server.ts
  import { Server } from '@modelcontextprotocol/sdk-typescript';

  const server = new Server({
    name: 'personal-tasks',
    version: '1.0.0'
  });

  // We'll add functionality here

  server.start();
  ```
  2. 添加任务存储
  ```js
  // Simple in-memory storage
  let tasks: Array<{id: number, text: string, done: boolean}> = [];
  let nextId = 1;
  ```
  3. 添加第一个工具
  ```js
  server.tool('add-task', 'Add a new task', {
    text: { type: 'string', description: 'Task description' }
  }, async (args) => {
    const task = {
      id: nextId++,
      text: args.text,
      done: false
    };
    tasks.push(task);
    return `Added task: ${task.text}`;
  });
  ```
  4. 验证测试: `npx tsx server.ts`
3. 扩展：
   - 添加更多Tools
    ```js
    // List all tasks
    server.tool('list-tasks', 'Show all tasks', {}, async () => {
      if (tasks.length === 0) return 'No tasks yet!';
      
      return tasks.map(t => 
        `${t.id}. [${t.done ? '✅' : '⭐'}] ${t.text}`
      ).join('\n');
    });

    // Complete a task
    server.tool('complete-task', 'Mark task as done', {
      id: { type: 'number', description: 'Task ID' }
    }, async (args) => {
      const task = tasks.find(t => t.id === args.id);
      if (!task) return 'Task not found!';
      
      task.done = true;
      return `Completed: ${task.text}`;
    });

    // Clear completed tasks
    server.tool('clear-completed', 'Remove all completed tasks', {}, async () => {
      const completedCount = tasks.filter(t => t.done).length;
      tasks = tasks.filter(t => !t.done);
      return `Cleared ${completedCount} completed tasks`;
    });
    ``` 
    - 添加持久化存储
    ```js
    import fs from 'fs';

    const TASKS_FILE = 'tasks.json';

    // Load tasks on startup
    function loadTasks() {
      try {
        const data = fs.readFileSync(TASKS_FILE, 'utf8');
        tasks = JSON.parse(data);
        nextId = Math.max(...tasks.map(t => t.id), 0) + 1;
      } catch {
        tasks = [];
        nextId = 1;
      }
    }

    // Save tasks after changes
    function saveTasks() {
      fs.writeFileSync(TASKS_FILE, JSON.stringify(tasks, null, 2));
    }
    ```

# Python
`uv`或`conda`环境管理器

```bash
curl -LsSf https://astral.sh/uv/install.sh | sh
# or
powershell -ExecutionPolicy ByPass -c "irm https://astral.sh/uv/install.ps1 | iex"
```

## MCP Server
1. 创建项目
2. 构建MCP Server
   1. 创建实例
    ```py
    from typing import Any
    import httpx
    from mcp.server.fastmcp import FastMCP

    # 初始化FastMCP服务器
    mcp = FastMCP("weather")

    # 常量
    NWS_API_BASE = "https://api.weather.gov"
    USER_AGENT = "weather-app/1.0"
    ```
   2. 添加工具(及辅助函数)
    ```py
    # -------------------------辅助函数-----------------------
    async def make_nws_request(url: str) -> dict[str, Any] | None:
        """向NWS API发出GET请求，处理错误并返回JSON响应"""
        headers = {
            "User-Agent": USER_AGENT,
            "Accept": "application/geo+json"
        }
        async with httpx.AsyncClient() as client:
            try:
                response = await client.get(url, headers=headers, timeout=30.0)
                response.raise_for_status()
                return response.json()
            except Exception:
                return None

    def format_alert(feature: dict) -> str:
        """将警报特征格式化为可读字符串。"""
        props = feature["properties"]
        return f"""
    Event: {props.get('event', 'Unknown')}
    Area: {props.get('areaDesc', 'Unknown')}
    Severity: {props.get('severity', 'Unknown')}
    Description: {props.get('description', 'No description available')}
    Instructions: {props.get('instruction', 'No specific instructions provided')}
    """

    # ------------------------- 工具执行-----------------------
    @mcp.tool()
    async def get_alerts(state: str) -> str:
        """获取指定州的天气警报（使用两字母州代码如CA/NY）"""
        url = f"{NWS_API_BASE}/alerts/active/area/{state}"
        data = await make_nws_request(url)

        if not data or "features" not in data:
            return "无法获取警报或未找到警报。"

        if not data["features"]:
            return "该州没有活动警报。"

        alerts = [format_alert(feature) for feature in data["features"]]
        return "\n---\n".join(alerts)

    @mcp.tool()
    async def get_forecast(latitude: float, longitude: float) -> str:
        """获取位置的天气预报。

        Args:
            latitude: 位置的纬度
            longitude: 位置的经度
        """
        # 首先获取预报网格端点
        points_url = f"{NWS_API_BASE}/points/{latitude},{longitude}"
        points_data = await make_nws_request(points_url)

        if not points_data:
            return "无法为此位置获取预报数据。"

        # 从点响应中获取预报URL
        forecast_url = points_data["properties"]["forecast"]
        forecast_data = await make_nws_request(forecast_url)

        if not forecast_data:
            return "无法获取详细预报。"

        # 将时段格式化为可读预报
        periods = forecast_data["properties"]["periods"]
        forecasts = []
        for period in periods[:5]:  # 只显示接下来的5个时段
            forecast = f"""
    {period['name']}:
    Temperature: {period['temperature']}°{period['temperatureUnit']}
    Wind: {period['windSpeed']} {period['windDirection']}
    Forecast: {period['detailedForecast']}
    """
            forecasts.append(forecast)

        return "\n---\n".join(forecasts)
    ```
3. 运行MCP Server
    ```py
    if __name__ == "__main__":
        # 初始化并运行服务器
        mcp.run(transport='stdio')
    ```

## MCP Client
1. 环境准备:`mcp anthropic python-dotenv`
    ```bash
    # 创建项目目录
    uv init mcp-client
    cd mcp-client

    # 创建虚拟环境
    uv venv

    # 激活虚拟环境
    # Windows系统:
    .venv\Scripts\activate
    # Unix或MacOS系统:
    source .venv/bin/activate

    # 安装必需的包
    uv add mcp anthropic python-dotenv

    # 删除模板文件
    rm hello.py

    # 创建主文件
    touch client.py
    ```
2. 配置API 密钥
    ```bash
    # 创建 .env 文件
    touch .env
    echo ".env" >> .gitignore # 若为git项目，也将其加入.gitignore
    ```
    ```ini
    ANTHROPIC_API_KEY=<你的密钥>
    ```
3. 实现MCP Client
   1. 创建基本Client
    ```py
    import asyncio
    from typing import Optional
    from contextlib import AsyncExitStack

    from mcp import ClientSession, StdioServerParameters
    from mcp.client.stdio import stdio_client

    from anthropic import Anthropic
    from dotenv import load_dotenv

    load_dotenv()  # 从 .env 加载环境变量

    class MCPClient:
        def __init__(self):
            # 初始化会话和客户端对象
            self.session: Optional[ClientSession] = None
            self.exit_stack = AsyncExitStack()
            self.anthropic = Anthropic()
    ```
   2. MCP Server连接
   ```py
   async def connect_to_server(self, server_script_path: str):
       """连接到 MCP 服务器

       参数：
           server_script_path: 服务器脚本路径 (.py 或 .js)
       """
       is_python = server_script_path.endswith('.py')
       is_js = server_script_path.endswith('.js')
       if not (is_python or is_js):
           raise ValueError("服务器脚本必须是 .py 或 .js 文件")

       command = "python" if is_python else "node"
       server_params = StdioServerParameters(
           command=command,
           args=[server_script_path],
           env=None
       )

       stdio_transport = await self.exit_stack.enter_async_context(stdio_client(server_params))
       self.stdio, self.write = stdio_transport
       self.session = await self.exit_stack.enter_async_context(ClientSession(self.stdio, self.write))

       await self.session.initialize()

       # 列出可用工具
       response = await self.session.list_tools()
       tools = response.tools
       print("\n已连接到服务器，可用工具:", [tool.name for tool in tools])
   ```
   3. 查询处理逻辑 
    ```py
    async def process_query(self, query: str) -> str:
        """使用 Claude 和可用工具处理查询"""
        messages = [
            {
                "role": "user",
                "content": query
            }
        ]

        response = await self.session.list_tools()
        available_tools = [{
            "name": tool.name,
            "description": tool.description,
            "input_schema": tool.inputSchema
        } for tool in response.tools]

        # 初始 Claude API 调用
        response = self.anthropic.messages.create(
            model="claude-3-5-sonnet-20241022",
            max_tokens=1000,
            messages=messages,
            tools=available_tools
        )

        # 处理响应和工具调用
        tool_results = []
        final_text = []

        assistant_message_content = []
        for content in response.content:
            if content.type == 'text':
                final_text.append(content.text)
                assistant_message_content.append(content)
            elif content.type == 'tool_use':
                tool_name = content.name
                tool_args = content.input

                # 执行工具调用
                result = await self.session.call_tool(tool_name, tool_args)
                tool_results.append({"call": tool_name, "result": result})
                final_text.append(f"[调用工具 {tool_name}，参数 {tool_args}]")

                assistant_message_content.append(content)
                messages.append({
                    "role": "assistant",
                    "content": assistant_message_content
                })
                messages.append({
                    "role": "user",
                    "content": [
                        {
                            "type": "tool_result",
                            "tool_use_id": content.id,
                            "content": result.content
                        }
                    ]
                })

                # 获取 Claude 的下一个响应
                response = self.anthropic.messages.create(
                    model="claude-3-5-sonnet-20241022",
                    max_tokens=1000,
                    messages=messages,
                    tools=available_tools
                )

                final_text.append(response.content[0].text)

        return "\n".join(final_text)
    ```
   4. 交互式聊天界面
    ```py
    async def chat_loop(self):
        """运行交互式聊天循环"""
        print("\nMCP 客户端已启动！")
        print("输入你的查询或 'quit' 退出。")

        while True:
            try:
                query = input("\n查询: ").strip()

                if query.lower() == 'quit':
                    break

                response = await self.process_query(query)
                print("\n" + response)

            except Exception as e:
                print(f"\n错误: {str(e)}")

    async def cleanup(self):
        """清理资源"""
        await self.exit_stack.aclose()
    ```
   5. 主入口点
    ```py
    async def main():
        if len(sys.argv) < 2:
            print("用法: python client.py <服务器脚本路径>")
            sys.exit(1)

        client = MCPClient()
        try:
            await client.connect_to_server(sys.argv[1])
            await client.chat_loop()
        finally:
            await client.cleanup()

    if __name__ == "__main__":
        import sys
        asyncio.run(main())
    ```

# Java
Java17+，Maven 3.6+，SpringBoot项目

## MCP Server
### 原生版(非SpringBoot)
1. 依赖 `dev.mcp:mcp-java:1.2.0`
2. 构建MCP Server
   1. 主类
    ```java

    import dev.mcp.MCPServer;
    import dev.mcp.Tool;

    import java.net.*;

    import com.fasterxml.jackson.databind.ObjectMapper;

    public class Weather {
        private static final String NWS_API_BASE = "https://api.weather.gov";
        private static final String USER_AGENT = "weather-app/1.0";
        private static final HttpClient client = HttpClient.newHttpClient();
        private static final ObjectMapper mapper = new ObjectMapper();

        public static void main(String[] args) {
            MCPServer mcp = new MCPServer("weather");
            setupTools(mcp);
            mcp.start();
        }

        private static void setupTools(MCPServer mcp) {
            // 工具设置代码将在这里
        }
    }
    ```
   2. 工具设置及辅助函数
   ```py
   private static Map<String, Object> makeNWSRequest(String url) {
       try {
           HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(url))
               .header("User-Agent", USER_AGENT)
               .header("Accept", "application/geo+json")
               .build();

           HttpResponse<String> response = client.send(request, 
               HttpResponse.BodyHandlers.ofString());

           if (response.statusCode() != 200) {
               return null;
           }

           return mapper.readValue(response.body(), Map.class);
       } catch (Exception e) {
           return null;
       }
   }

   private static String formatAlert(Map<String, Object> feature) {
       Map<String, Object> props = (Map<String, Object>) feature.get("properties");
       return String.format("""
           Event: %s
           Area: %s
           Severity: %s
           Description: %s
           Instructions: %s
           """,
           props.getOrDefault("event", "Unknown"),
           props.getOrDefault("areaDesc", "Unknown"),
           props.getOrDefault("severity", "Unknown"),
           props.getOrDefault("description", "No description available"),
           props.getOrDefault("instruction", "No specific instructions provided"));
   }

   private static void setupTools(MCPServer mcp) {
       // 获取警报工具
       mcp.tool(new Tool.Builder()
           .name("get-alerts")
           .description("获取美国州的天气警报")
           .parameter("state", "string", "两字母美国州代码（例如CA、NY）")
           .execute(params -> {
               String state = params.get("state").toString();
               String url = String.format("%s/alerts/active/area/%s", NWS_API_BASE, state);
               Map<String, Object> data = makeNWSRequest(url);

               if (data == null || !data.containsKey("features")) {
                   return "无法获取警报或未找到警报。";
               }

               List<Map<String, Object>> features = (List<Map<String, Object>>) data.get("features");
               if (features.isEmpty()) {
                   return "该州没有活动警报。";
               }

               return features.stream()
                   .map(Weather::formatAlert)
                   .collect(Collectors.joining("\n---\n"));
           })
           .build());

       // 获取预报工具
       mcp.tool(new Tool.Builder()
           .name("get-forecast")
           .description("获取位置的天气预报")
           .parameter("latitude", "number", "位置的纬度")
           .parameter("longitude", "number", "位置的经度")
           .execute(params -> {
               double latitude = Double.parseDouble(params.get("latitude").toString());
               double longitude = Double.parseDouble(params.get("longitude").toString());

               // 首先获取预报网格端点
               String pointsUrl = String.format("%s/points/%f,%f", NWS_API_BASE, latitude, longitude);
               Map<String, Object> pointsData = makeNWSRequest(pointsUrl);

               if (pointsData == null) {
                   return "无法为此位置获取预报数据。";
               }

               // 从点响应中获取预报URL
               Map<String, Object> properties = (Map<String, Object>) pointsData.get("properties");
               String forecastUrl = properties.get("forecast").toString();
               Map<String, Object> forecastData = makeNWSRequest(forecastUrl);

               if (forecastData == null) {
                   return "无法获取详细预报。";
               }

               // 将时段格式化为可读预报
               Map<String, Object> forecastProps = (Map<String, Object>) forecastData.get("properties");
               List<Map<String, Object>> periods = (List<Map<String, Object>>) forecastProps.get("periods");
               
               return periods.stream()
                   .limit(5)
                   .map(period -> String.format("""
                       %s:
                       Temperature: %s°%s
                       Wind: %s %s
                       Forecast: %s
                       """,
                       period.get("name"),
                       period.get("temperature"),
                       period.get("temperatureUnit"),
                       period.get("windSpeed"),
                       period.get("windDirection"),
                       period.get("detailedForecast")))
                   .collect(Collectors.joining("\n---\n"));
           })
           .build());
   }
   ```
3. 启动 MCP Server :`mvn compile exec:java -Dexec.mainClass="com.example.Weather"`

### SpringBoot版
1. 依赖 `org.modelcontext:mcp-spring-boot-starter:0.4.0`
2. 配置`application.yml`
    ```yaml
    spring:
    ai:
        anthropic:
        api-key: ${ANTHROPIC_API_KEY}
    mcp:
        servers:
        filesystem:
            command: npx
            args:
            - "-y"
            - "@modelcontextprotocol/server-filesystem"
            - "C:\\Users\\username\\Desktop"
            - "C:\\Users\\username\\Downloads"
    ```
3. 创建启动类(Spring Boot)
4. 打包并运行

## MCP Client
1. 依赖
    ```xml
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-mcp-client-spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-anthropic-spring-boot-starter</artifactId>
    </dependency>
    ```
2. 配置
    ```yml
    spring:
    ai:
        mcp:
        client:
            enabled: true
            name: brave-search-client
            version: 1.0.0
            type: SYNC
            request-timeout: 20s
            stdio:
            root-change-notification: true
            servers-configuration: classpath:/mcp-servers-config.json
        anthropic:
        api-key: ${ANTHROPIC_API_KEY}
    ```
3. MCP 服务器配置（`mcp-servers-config.json`）
    ```json
    {
    "mcpServers": {
        "brave-search": {
        "command": "npx",
        "args": [
            "-y",
            "@modelcontextprotocol/server-brave-search"
        ],
        "env": {
            "BRAVE_API_KEY": "<PUT YOUR BRAVE API KEY>"
        }
        }
    }
    }
    ```
4. 使用：聊天实现
    ```java
    var chatClient = chatClientBuilder
        .defaultSystem("You are useful assistant, expert in AI and Java.")
        .defaultTools((Object[]) mcpToolAdapter.toolCallbacks())
        .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
        .build();
    ```
5. 高级配置
   - 通过 McpSyncClientCustomizer 或 McpAsyncClientCustomizer 自定义客户端
   - 支持多种传输类型：STDIO 和 SSE（服务器发送事件）
   - 与 Spring AI 的工具执行框架集成
   - 自动客户端初始化和生命周期管理