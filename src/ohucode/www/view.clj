(ns ohucode.www.view
  (:require [hiccup.core :refer [h html]]
            [hiccup.page :refer [html5 include-css include-js]]
            [ohucode.www.markdown :as md]))

(defn layout
  "HTML 기본 레이아웃"
  [& contents]
  (html5 [:head
          [:meta {:charset "utf-8"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
          [:title "오후코드 - 웹서비스 개발 서비스"]
          (map include-css ["css/bootstrap.min.css"
                            "css/font-awesome.min.css"
                            "css/ohucode.css"])]
         [:body contents
          (map include-js [#_"https://unpkg.com/vue"
                           #_"https://d3js.org/d3.v4.min.js"
                           "js/ohucode.js"])]))

(defn cover
  "커버 페이지 레이아웃"
  [& contents]
  (layout [:nav
           (for [주제 ["오후코드" "프로젝트" "대표경력" "연락처"]]
             [:a {:href (str "/#" 주제)} 주제])]
          contents
          [:footer
           [:a {:href "https://medium.com/@hatemogi"}
            [:i.fa.fa-medium] " hatemogi"]
           [:a {:href "https://kr.linkedin.com/in/hatemogi"}
            [:i.fa.fa-linkedin] " 링크드인"]]))

(defn index-page
  []
  (let [문서 (md/문서변환 (slurp "src/index.md"))]
    (html (into [:main] 문서))))

(defn not-found
  []
  (html [:main [:section#404 [:div [:h1 "Not Found"]
                              [:a {:href "/"} "첫페이지로"]]]]))
