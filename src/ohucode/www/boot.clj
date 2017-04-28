(ns ohucode.www.boot
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [boot.core :refer :all]
            [boot.task.built-in :as task]
            [boot.util :refer [info]]
            [ohucode.www.view :as v]))

(defn convert [f in out]
  (io/make-parents out)
  (spit out (f (slurp in))))

(deftask cover-html
  "본문만 있는 걸 커버를 감싼다"
  []
  (let [tmp (tmp-dir!)]
    (with-pre-wrap fileset
      (doseq [body (by-ext ["._html"] (input-files fileset))]
        (let [in-path  (tmp-path body)
              out-path (s/replace in-path #"\._html$" ".html")
              out-file (io/file tmp out-path)]
          (info "covering %s => %s\n" in-path out-path)
          (convert v/cover (tmp-file body) out-file)))
      (commit! (add-resource fileset tmp)))))

(deftask index
  "첫페이지 변환"
  []
  (let [tmp (tmp-dir!)]
    (with-pre-wrap fileset
      (info "writing index._html\n")
      (spit (io/file tmp "index._html") (v/index-content))
      (commit! (add-source fileset tmp)))))

(deftask markdown
  "마크다운 페이지 변환"
  []
  (let [tmp (tmp-dir!)]
    (with-pre-wrap fileset
      (doseq [md (by-ext [".md" ".markdown"] (input-files fileset))]
        (let [in-path  (tmp-path md)
              out-path (s/replace in-path #"\.(md|markdown)$" "._html")
              out-file (io/file tmp out-path)]
          (info "%s => %s\n" in-path out-path)
          (convert v/md->html
                   (tmp-file md) out-file)))
      (commit! (add-source fileset tmp)))))

(deftask build
  "빌드 태스크"
  []
  (comp (markdown)
        (index)
        (cover-html)
        (task/sift :to-resource [#"CNAME"])
        (task/target :dir ["docs"])))
