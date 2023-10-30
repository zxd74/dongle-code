package web

import (
	"fmt"
	"net/http"

	"github.com/gorilla/mux"
)

func StartRouterWeb() {
	r := mux.NewRouter()

	r.HandleFunc("/books/{title}/page/{page}", func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		title, page := vars["title"], vars["page"]
		fmt.Fprintf(w, "You've requested the book: %s on page %s\n", title, page)
	}).Methods("GET").Schemes("http").Host("www.mybookstore.com")

	// bookrouter := r.PathPrefix("/books").Subrouter()
	// bookrouter.HandleFunc("/", funcname )

	http.ListenAndServe(":80", r)
}
