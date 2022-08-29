databaseChangeLog:
- changeSet:
    id: 1
    author: Sergei Li
    preConditions:
    - onFail: MARK_RAN
    not:
    tableExists:
    tableName: info
    changes:
    - createTable:
    tableName: info
    columns:
    - column:
    name: id
    type: bigserial
    autoIncrement: true
    constraints:
    primaryKey: true
    nullable: false
    - column:
    name: about_Shelter
    type: text
    constraints:
    nullable: true
    - column:
    name: work_Mode
    type: text
    constraints:
    nullable: true
    - column:
    name: address
    type: text
    constraints:
    nullable: true
    - column:
    name: contacts
    type: text
    constraints:
    nullable: true
    - column:
    name: safety_Precautions
    type: text
    constraints:
    nullable: true
    - column:
    name: dating_Rules
    type: text
    constraints:
    nullable: true
    - column:
    name: tips_Of_Dog_Handler
    type: text
    constraints:
    nullable: true
    - column:
    name: list_Of_Dog_Handler
    type: text
    constraints:
    nullable: true
    - column:
    name: reasons_For_Refusal
    type: text
    constraints:
    nullable: true
    - column:
    name: list_Of_Documents
    type: text
    constraints:
    nullable: true
    - column:
    name: advice_For_Transporting
    type: text
    constraints:
    nullable: true
    - column:
    name: advice_For_Home_For_Puppy
    type: text
    constraints:
    nullable: true
    - column:
    name: advice_For_Home_For_Adult_Dog
    type: text
    constraints:
    nullable: true
    - column:
    name: advice_For_Home_For_Dog_With_Disability
    type: text
    constraints:
    nullable: true
    - column:
    name: location
    type: bytea
    constraints:
    nullable: true