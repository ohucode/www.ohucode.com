(ns ohucode.www.boot
  {:boot/export-tasks true}
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [boot.core :as b]
            [boot.task.built-in :as task]
            [hiccup.core :refer [h]]
            [hiccup.page :refer [html5 include-css include-js]]))

(defn layout
  "HTML 기본 레이아웃"
  [content]
  (html5 [:head
          [:meta {:charset "utf-8"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
          [:title "타이틀"]
          (map include-css
               ["https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
                "https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css"
                "css/ohucode.css"])]
         [:body
          [:main
           content
           (map include-js ["https://code.jquery.com/jquery-3.1.0.min.js"
                            "https://d3js.org/d3.v4.min.js"
                            "js/ohucode.js"])]]))

(defn convert [f in out]
  (io/make-parents out)
  (spit out (f (slurp in))))

(b/deftask markdown
  "레이아웃 생성"
  []
  (let [tmp (b/tmp-dir!)]
    (b/with-pre-wrap fileset
      (let [in  (b/input-files fileset)
            mds (b/by-ext [".md" ".markdown"] in)]
        (doseq [md mds]
          (let [infile  (b/tmp-file md)
                inpath  (b/tmp-path md)
                outpath (s/replace inpath #"\.(md|markdown)$" ".html")
                outfile (io/file tmp outpath)]
            (println "--- " inpath  " => " outpath " ---")
            (convert identity infile outfile))))
      (-> fileset
          (b/add-resource tmp)
          b/commit!))))

(b/deftask build
  "빌드 태스크"
  []
  (comp (markdown) (task/target)))
