package web

import (
	"dongle/go-web/model"
	"fmt"
	"html/template"
	"net/http"
)

func indexHandle(writer http.ResponseWriter, request *http.Request) {
	_, err := fmt.Fprintf(writer, "Welcom to my website!")
	if err != nil {
		return
	}
}

func staticHandle() http.Handler {
	fs := http.FileServer(http.Dir("static/")) // http.Dir以项目目录为根路径
	return http.StripPrefix("/static/", fs)
}

func templateHandle(writer http.ResponseWriter, request *http.Request) {
	tmpl, err := template.ParseFiles("static/THome.html")
	// tmpl := template.Must(template.ParseFiles("static/THome.html"))
	if err != nil {
		return
	}
	data := model.TodoPageData{
		PageTitle: "My TODO list",
		Todos: []model.Todo{
			{Title: "Task 1", Done: false},
			{Title: "Task 2", Done: true},
			{Title: "Task 3", Done: true},
		},
	}
	tmpl.Execute(writer, data)
}

func httpHandle() {
	http.HandleFunc("/", indexHandle)       //首页
	http.Handle("/static/", staticHandle()) // 静态资源目录服务
	// 默认为index.html页面，如果木有就显示文件目录，如果有则显示自定义index.html
	http.HandleFunc("/tmpl", templateHandle)
}

func StartWeb(host string, port int) {
	if port <= 0 {
		port = 80
	}
	// if host == nil{
	// 	host = "localhost"
	// }
	address := fmt.Sprintf("%s:%d", host, port)
	httpHandle()
	fmt.Println("web is starting!")
	err := http.ListenAndServe(address, nil)
	if err != nil {
		fmt.Println("web start is error!", err)
	}
}
