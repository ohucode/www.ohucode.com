(ns ohucode.www.boot
  {:boot/export-tasks true}
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [boot.core :as b]
            [boot.task.built-in :as task]
            [boot.util :refer [info]]
            [hiccup.core :refer [h]]
            [hiccup.page :refer [html5 include-css include-js]]))

(defn layout
  "HTML 기본 레이아웃"
  [& contents]
  (html5 [:head
          [:meta {:charset "utf-8"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
          [:title "타이틀"]
          (map include-css
               ["https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
                "https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css"
                "css/ohucode.css"])]
         (-> [:body]
             (into contents)
             (into (map include-js ["https://code.jquery.com/jquery-3.1.0.min.js"
                                    "https://d3js.org/d3.v4.min.js"
                                    "js/ohucode.js"])))))

(defn page
  "네비게이션 포함 레이아웃"
  [& contents]
  (layout [:nav]
          (into [:main] contents)
          [:footer "푸터"]))

(defn convert [f in out]
  (io/make-parents out)
  (spit out (f (slurp in))))

(b/deftask markdown
  "마크다운 페이지 변환"
  []
  (let [tmp (b/tmp-dir!)]
    (b/with-pre-wrap fileset
      (doseq [md (b/by-ext [".md" ".markdown"] (b/input-files fileset))]
        (let [in-path  (b/tmp-path md)
              out-path (s/replace in-path #"\.(md|markdown)$" ".html")
              out-file (io/file tmp out-path)]
          (info "%s => %s\n" in-path out-path)
          (convert layout (b/tmp-file md) out-file)))
      (b/commit! (b/add-resource fileset tmp)))))

(b/deftask build
  "빌드 태스크"
  []
  (comp (markdown) (task/target)))
