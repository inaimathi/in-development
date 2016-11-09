(ns in-development.core
  (:require [org.httpkit.server :as server]
            [compojure.route :as route]
            [hiccup.page :as pg]
            [cheshire.core :as js]

            [clojure.string :as string])
  (:use [compojure.core :only [defroutes GET POST DELETE ANY context]])
  (:gen-class))

(defn home [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (pg/html5
          {:lang "en"}
          [:head
           [:title "in development"]
           [:link {:rel "stylesheet" :href "/static/css/in-development.css" :type "text/css" :media "screen"}]
           [:script {:type "text/javascript" :src "https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"}]
           [:script {:type "text/javascript" :src "/static/js/in-development.js"}]]
          [:body
           [:div {:class "page-header"}]
           [:div {:class "content"}
            [:p "In Development is a Canadian Open Source/Free Software space and fund. We help developers build great software, promote and support local open source projects, and build connections from Canadian cities to the broader open source ecosystem."]
            [:p "We're currently, appropriately, in development."]]
           [:div {:class "email-submission"}
            [:p
             [:input {:class "email-input" :name "email" :placeholder "Enter email here" :pattern ".*?@.*?"}]
             [:button "Keep me in the loop"]]]
           [:div {:class "page-footer"}
            [:p "This page uses " [:a {:href "https://commons.wikimedia.org/wiki/File:Joy_Oil_gas_station_blueprints.jpg"} "an image from wikimedia commons"] ", released from the Canadian public domain."]
            [:p "This page uses " [:a {:href "http://www.dafont.com/wc-roughtrad-bta.font?text=in+development&l[]=10&l[]=1&back=theme"} "a free font from dafont"] "."]]])})

(def record-email
  (fn [req]
    (if (>= 500 (.available (req :body)))
      (let [raw (java.net.URLDecoder/decode (slurp (req :body)))
            pairs (into {} (map #(string/split % #"=") (string/split raw #"&")))]
        (spit "emails.txt" (str (get pairs "email") "\n") :append true)
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (js/generate-string "ok")})
      {:status 400
       :headers {"Content-Type" "application/json"}
       :body (js/generate-string {:error "given parameters too long"})})))

(defroutes routes
  (GET "/" [] home)
  (POST "/api/v0/submit-email" [] record-email)
  (route/resources "/static/"))

(defn -main
  ([] (-main "8000"))
  ([port]
   (println "Listening on" port "...")
   (server/run-server routes {:port (read-string port)})))
