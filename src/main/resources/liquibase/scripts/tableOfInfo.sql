-- liquibase formatted sql

-- changeSet sli:1
CREATE TABLE IF NOT EXISTS info
(
    id                                      BIGSERIAL PRIMARY KEY,
    about_shelter                           TEXT,
    work_mode                               TEXT,
    address                                 TEXT,
    contacts                                TEXT,
    safety_precautions                      TEXT,
    dating_rules                            TEXT,
    tips_of_dog_handler                     TEXT,
    list_of_dog_handler                     TEXT,
    reasons_for_refusal                     TEXT,
    list_of_documents                       TEXT,
    advice_for_transporting                 TEXT,
    advice_for_home_for_baby               TEXT,
    advice_for_home_for_adult_pet           TEXT,
    advice_for_home_for_pet_with_disability TEXT,
    media_type                              TEXT,
    location                                OID
)