(require '[reagent.core :as r]
         '[reagent.dom :as rdom])

(defn make-cm
  [value id]
  (js/CodeMirror
   (.getElementById js/document id)
   {:value value
    :lineNumbers true
    :tabSize 2
    :matchBrackets true
    :mode "clojure"
    :theme "nord"
    :width "500px"
    #_#_:on-change #(reset! value (-> % .-target .-value))}))

(defn src-component
  [src-str]
  (let [state (r/atom src-str)
        id (str (gensym "src"))]
    (fn []
      [:div
       [:div {:id id}]
       [make-cm @state id]
       #_[:textarea {:type "text"
                   :value @state
                   :cols 70
                   :rows 10
                   :on-change #(reset! state (-> % .-target .-value))
                   :style {:font-family "Menlo"}}]
       [:p "RESULT:"]
       [:<> (try
              (js/scittle.core.eval_string @state)
              (catch :default e
                (.-message e)))]])))

(defn run-src
  [elem]
  (let [src-str (.-innerText elem)
        parent (.-parentNode elem)
        component [src-component src-str]]
    (rdom/render component parent)))

(defn run! []
  (let [blocks (vec (.getElementsByClassName js/document "src-babashka"))]
    (mapv run-src blocks)))

(run!)
(rdom/render [:div {:id "test"}] (.getElementById js/document "app"))
(make-cm "test")
