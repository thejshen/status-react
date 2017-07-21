(ns status-im.contacts.db
  (:require [cljs.spec.alpha :as s]
            [clojure.string :as str]
            [status-im.data-store.contacts :as contacts]
            [status-im.js-dependencies :as dependencies]))

(defn is-address? [s]
  (.isAddress dependencies/Web3.prototype s))

(defn contact-can-be-added? [identity]
  (if (contacts/exists? identity)
    (:pending? (contacts/get-by-id identity))
    true))

(defn hex-string? [s]
  (let [s' (if (str/starts-with? s "0x")
             (subs s 2)
             s)]
    (boolean (re-matches #"(?i)[0-9a-f]+" s'))))

(defn valid-length? [identity]
  (let [length (count identity)]
    (and
      (hex-string? identity)
      (or
        (and (= 128 length) (not (str/includes? identity "0x")))
        (and (= 130 length) (str/starts-with? identity "0x"))
        (and (= 132 length) (str/starts-with? identity "0x04"))
        (is-address? identity)))))

(s/def ::identity-length valid-length?)
(s/def ::contact-can-be-added contact-can-be-added?)
(s/def ::not-empty-string (s/and string? not-empty))
(s/def ::name ::not-empty-string)
(s/def ::whisper-identity (s/and ::not-empty-string
                                 ::identity-length))

(s/def ::contact (s/keys :req-un [::name ::whisper-identity]
                         :opt-un [::phone ::photo-path ::address]))


(s/def :contacts/contacts (s/nilable map?))                                     ;; {id (string) contact (map)}
(s/def :contacts/new-contacts (s/nilable seq?))
(s/def :contacts/new-contact-identity (s/nilable string?))                      ;;public key of new contact during adding this new contact
(s/def :contacts/new-contact-public-key-error (s/nilable string?))
(s/def :contacts/contact-identity (s/nilable string?))                          ;;on showing this contact profile
(s/def :contacts/contacts-ui-props (s/nilable map?))
(s/def :contacts/contact-list-ui-props (s/nilable map?))
(s/def :contacts/contacts-click-handler (s/nilable fn?))            ;;used in modal list (for example for wallet)
(s/def :contacts/contacts-click-action (s/nilable keyword?))        ;;used in modal list (for example for wallet)
(s/def :contacts/contacts-click-params (s/nilable map?))                        ;;used in modal list (for example for wallet)



