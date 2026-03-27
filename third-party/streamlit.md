# AI Chat
* 基于streamlit+deepseek实现一个简单的AI聊天页面
  * 系统提示词配置
  * 流式输出
  * 会话管理：加载、保存、新建、删除
```py
import streamlit as st
import os,datetime,json

os.environ["DEEPSEEK_API_KEY"] = "your_api_key"
SESSION_DIR = "sessions"

st.title("AI Chat")
st.header("This is a AI Chat page.")

def generate_session_name():
    return datetime.datetime.now().strftime("%Y%m%d%H%M%S")

# 初始化大模型交互信息
if "messages" not in st.session_state:
    st.session_state.messages = []
for message in st.session_state.messages:
    if message["role"] == "user":
        st.chat_message("user").write(message["content"])
    else:
        st.chat_message("assistant").write(message["content"])

# build system prompt: 定义角色，任务，规则
system_prompt = """
    你的角色现在是%s,一名长相甜美，声音柔和的智能伴侣，请扮演这个角色，并按照以下规则进行回复：
    1. 回复内容必须简单明了
    2. 回复内容必须符合角色性格
    3. 回复内容可以适当加入一些emoji表情
"""
if "nick_name" not in st.session_state:
    st.session_state.nick_name = "小美"
if "current_session" not in st.session_state:
    st.session_state.current_session = generate_session_name()

def save_session():
    session = {
        "nick_name": st.session_state.nick_name,
        "messages": st.session_state.messages,
        "current_session": st.session_state.current_session
    }
    if not os.path.exists(SESSION_DIR):
        os.mkdir(SESSION_DIR)
    with open(f"{SESSION_DIR}/{generate_session_name()}.json","w",encoding="utf-8") as f:
        json.dump(session,f,ensure_ascii=False,indent=4)

def new_session():
    st.session_state.messages = []
    st.session_state.current_session = generate_session_name()
    save_session()

def load_sessions():
    session_list = []
    if os.path.exists(SESSION_DIR):
        file_list = os.listdir(SESSION_DIR)
        for file in file_list:
            if file.endswith(".json"):
               session_list.append(file[:-5])
    return session_list

def load_session(session_name):
    try:
        if os.path.exists(f"{SESSION_DIR}/{session_name}.json"):
            with open(f"{SESSION_DIR}/{session_name}.json","r",encoding="utf-8") as f:
                session_data = json.load(f)
                st.session_state.nick_name = session_data["nick_name"]
                st.session_state.messages = session_data["messages"]
                st.session_state.current_session = session_data["current_session"]
    except Exception as e:
        st.error(f"Failed to load session {session_name}")

def delete_session(session_name):
    try:
        if os.path.exists(f"{SESSION_DIR}/{session_name}.json"):
            os.remove(f"{SESSION_DIR}/{session_name}.json")
    except Exception as e:
        st.error(f"Failed to delete session {session_name}")

# create ai client
from openai import OpenAI
client = OpenAI(api_key=os.environ.get('DEEPSEEK_API_KEY'),base_url="https://api.deepseek.com")

# create sidebar : manage ai session and configrate system prompt
with st.sidebar:
    st.subheader("AI Sesssion Manage")
    if st.button("New Session",width="stretch"):
        save_session()
        # create new session
        if st.session_state.messages:
            new_session()
            st.rerun()

    st.text("AI Sesssion List")
    for session in load_sessions():
        col1,col2 = st.columns([4,1])
        with col1:
            if st.button(session,key=f"load_{session}",width="stretch",type="primary" if session == st.session_state.current_session else "secondary"):
                load_session(session)
                st.rerun()
        with col2:
            if st.button("",width="stretch",key=f"delete_{session}",icon="❌"):
                print(f"delete {session}")
                delete_session(session)
                if session == st.session_state.current_session:
                    new_session()
                st.rerun()

    st.subheader("System Prompt")
    nick_name = st.text_input("昵称",placeholder="请输入昵称")
    if nick_name:
        st.session_state.nick_name = nick_name

# ai chat
prompt = st.chat_input("What's your question?")
if prompt:
    st.chat_message("user").write(prompt)
    st.session_state.messages.append({"role": "user", "content": prompt})
    # 调用ai大模型交互
    response = client.chat.completions.create(
        model="deepseek-chat",
        messages=[
            {"role": "system", "content": system_prompt % st.session_state.nick_name},
            # {"role": "user", "content": prompt},
            # 会话记忆
            *st.session_state.messages # 解包后内容格式与所需格式一致才可以，否则仍需进行处理
        ],
        stream=True # 是否流式输出
    )
    # st.chat_message("assistant").write(response.choices[0].message.content) # 非stream
    
    # 流式输出
    response_message = st.empty() # 构建空容器，已容器形式输出message
    response_content = ""
    for chunk in response:
        if chunk.choices[0].delta.content is not None:
            response_content += chunk.choices[0].delta.content
            response_message.chat_message("assistant").write_stream(chunk.choices[0].delta.content)
    
    st.session_state.messages.append({"role": "assistant", "content": response_content})
    save_session()
```