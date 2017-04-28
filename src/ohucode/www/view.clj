(ns ohucode.www.view
  (:require
   [hiccup.core :refer [h html]]
   [hiccup.page :refer [html5 include-css include-js]]
   [markdown.core :refer [md-to-html-string]]))

(defn layout
  "HTML 기본 레이아웃"
  [& contents]
  (html5 [:head
          [:meta {:charset "utf-8"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
          [:title "오후코드 - 웹서비스 개발 서비스"]
          (map include-css
               ["https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
                "https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css"
                "http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/styles/default.min.css"
                "css/jumbotron.css"
                "css/ohucode.css"])]
         [:body contents
          (map include-js ["https://unpkg.com/vue"
                           "https://unpkg.com/axios/dist/axios.min.js"
                           "https://d3js.org/d3.v4.min.js"
                           "http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/highlight.min.js"
                           "js/ohucode.js"
                           "js/navigation.js"])]))

(defn cover
  "커버 페이지 레이아웃"
  [& contents]
  (layout [:div.container
           [:div.header.clearfix
            [:nav
             [:ul.nav.nav-pills.pull-right
              (for [[href title] [["index.html" "소개"]
                                  ["projects.html" "프로젝트"]
                                  ["contact.html" "연락처"]]]
                [:li {:role "presentation"} [:a {:href href} title]])]]
            [:h3.text-muted "오후코드"]]
           [:div.jumbotron
            [:h2 "프리랜서 서버 개발자"]
            [:p "국내 최대 수준의 웹서비스 트래픽을 처리한 경험을 바탕으로, 탄탄하고 빠른 서버 소프트웨어를 개발해드립니다"]]
           [:div.row.marketing contents]
           [:footer.footer
            [:p.pull-right [:a {:href "https://medium.com/@hatemogi"}
                            [:i.fa.fa-medium] " hatemogi"]]
            [:p [:a {:href "https://kr.linkedin.com/in/hatemogi"}
                 [:i.fa.fa-linkedin] " 링크드인"]]]]))

(defn md->html
  "마크다운을 HTML로 변환"
  [text]
  (md-to-html-string text
                     :reference-links? true
                     :footnotes? true))

(def ^:private about-content
  [:div [:h1 "오후코드"]
   [:img.profile {:src "img/profile.jpg"}]])

(defn index-content []
  (html [:div.col-lg-6 "왼쪽 영역" [:svg]]
        [:div.col-lg-6 "오른쪽 영역"]))
