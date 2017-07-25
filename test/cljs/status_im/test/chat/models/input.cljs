(ns status-im.test.chat.models.input
  (:require [cljs.test :refer-macros [deftest is]]
            [status-im.chat.models.input :as input]))

(deftest text->emoji
  (is (nil? (input/text->emoji nil)))
  (is (= "" (input/text->emoji "")))
  (is (= "test" (input/text->emoji "test")))
  (is (= "word1 \uD83D\uDC4D word2" (input/text->emoji "word1 :+1: word2"))))

(deftest text-ends-with-space?
  (is (false? (input/text-ends-with-space? nil)))
  (is (false? (input/text-ends-with-space? "")))
  (is (false? (input/text-ends-with-space? "word1 word2 word3")))
  (is (true? (input/text-ends-with-space? "word1 word2 "))))

(deftest starts-as-command?
  (is (false? (input/starts-as-command? nil)))
  (is (false? (input/text-ends-with-space? "")))
  (is (false? (input/text-ends-with-space? "word1 word2 word3")))
  (is (true? (input/text-ends-with-space? "word1 word2 "))))

(deftest possible-chat-actions
  (let []
    ))

(deftest split-command-args
  (is (nil? (input/split-command-args nil)))
  (is (= [""] (input/split-command-args "")))
  (is (= ["@browse" "google.com"] (input/split-command-args "@browse google.com")))
  (is (= ["@browse" "google.com"] (input/split-command-args "  @browse   google.com  ")))
  (is (= ["/send" "1.0" "John Doe"] (input/split-command-args "/send 1.0 \"John Doe\"")))
  (is (= ["/send" "1.0" "John Doe"] (input/split-command-args "/send     1.0     \"John     Doe\"   "))))

(deftest join-command-args
  (is (nil? (input/join-command-args nil)))
  (is (= "" (input/join-command-args [""])))
  (is (= "/send 1.0 \"John Doe\"" (input/join-command-args ["/send" "1.0" "John Doe"]))))

(deftest selected-chat-command)

(deftest current-chat-argument-position)

(deftest argument-position)

(deftest command-completion)

(deftest args->params
  (is (= {} (input/args->params nil)))
  (is (= {} (input/args->params {})))
  (is (= {} (input/args->params {:args ["1.0"]})))
  (is (= {:amount "1.0"}
         (input/args->params {:command {:params [{:name "amount"}]}
                              :args    ["1.0"]})))
  (is (= {:amount "1.0"}
         (input/args->params {:command {:params [{:name "amount"}]}
                              :args    ["1.0" "2.0" "3.0"]})))
  (is (= {:amount "1.0"}
         (input/args->params {:command {:params [{:name "amount"} {:name "recipient"}]}
                              :args    ["1.0"]})))
  (is (= {:amount "1.0" :recipient "John Doe"}
         (input/args->params {:command {:params [{:name "amount"} {:name "recipient"}]}
                              :args    ["1.0" "John Doe"]}))))

(deftest command-dependent-context-params
  (is (= {} (input/command-dependent-context-params "any" {:name "any"})))
  (is (= {} (input/command-dependent-context-params "console" {:name "any"}))))

(deftest modified-db-after-change)