-- liquibase formatted sql

-- changeSet sli:1
CREATE TABLE IF NOT EXISTS info
(
    id                                      BIGSERIAL PRIMARY KEY,
    about_Shelter                           TEXT,
    work_Mode                               TEXT,
    address                                 TEXT,
    contacts                                TEXT,
    safety_Precautions                      TEXT,
    dating_Rules                            TEXT,
    tips_Of_Dog_Handler                     TEXT,
    list_Of_Dog_Handler                     TEXT,
    reasons_For_Refusal                     TEXT,
    list_Of_Documents                       TEXT,
    advice_For_Transporting                 TEXT,
    advice_For_Home_For_Puppy               TEXT,
    advice_For_Home_For_Adult_Dog           TEXT,
    advice_For_Home_For_Dog_With_Disability TEXT,
    media_Type                              TEXT,
    location                                OID
)