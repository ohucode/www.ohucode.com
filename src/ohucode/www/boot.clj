(ns ohucode.www.boot
  {:boot/export-tasks true}
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [boot.core :as b]
            [boot.task.built-in :as task]
            [boot.util :refer [info]]
            [markdown.core]
            [ohucode.www.view :refer :all]))

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
          (convert (comp cover md->html)
                   (b/tmp-file md) out-file)))
      (b/commit! (b/add-resource fileset tmp)))))

(b/deftask build
  "빌드 태스크"
  []
  (comp (markdown)
        (task/sift :to-resource [#"CNAME"])
        (task/target :dir ["docs"])))
