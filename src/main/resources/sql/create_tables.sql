-- we don't know how to generate database H2 (class Database) :(
create table DIVISION
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_26334689_3E12_4D1C_9AA6_57161FCEC292)
		primary key,
	NAME_DIVISION VARCHAR(50) not null
		unique,
	DESCRIPTION VARCHAR(150)
)
;

create table ENVIRONNEMENT
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_485B9D74_EF00_4B3D_8325_B39B04DF9300) not null
		primary key,
	ENVIRONNEMENT_NAME VARCHAR(20)
)
;

create table FORMULAIRE_LIST
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_2FFFA502_F23C_4770_B251_49DF2CEC7FF4)
		primary key,
	LIBELLE_FORMULAIRE VARCHAR(50),
	DATE_CREATION DATE
)
;

create table FORMULAIRE_PARAMETER
(
	CODE VARCHAR(50) not null,
	LIBELLE VARCHAR(50),
	TYPE_REPRESENTATION VARCHAR(50)
)
;

create unique index CODE_PARAMETER_CODE_PARAMETER_UINDEX
	on FORMULAIRE_PARAMETER (CODE)
;

create unique index PRIMARY_KEY_5
	on FORMULAIRE_PARAMETER (CODE)
;

create unique index PRIMARY_KEY_D1
	on FORMULAIRE_PARAMETER (CODE)
;

alter table FORMULAIRE_PARAMETER
	add primary key (CODE)
;

create table FORMULAIRE_QUESTION
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_9A2A1EFA_55DD_4489_B456_7FE730570148)
		primary key,
	ID_FORMULAIRE INTEGER,
	CODE_PARAMETER VARCHAR(50),
	LIBELLE VARCHAR(50),
	DESCRIPTION VARCHAR(50),
	FLAG_MANDATORY INTEGER not null,
	QUESTION_ORDER INTEGER,
	TYPE_FIELD VARCHAR(10),
	LARGEUR INTEGER,
	constraint FORMULAIRE_LIST_CODE_PARAMETER_CODE_PARAMETER_FK
		foreign key (CODE_PARAMETER) references FORMULAIRE_PARAMETER
)
;

create table FORMULAIRE_VALUE
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_B906C4FC_EC95_4E45_87A4_3FFC664E0A08)
		primary key,
	CODE_VALUE VARCHAR(50),
	LIBELLE_VALUE VARCHAR(50),
	FORMULAIREPARAMETER_CODE VARCHAR(10),
	constraint PARAMETER_CODE_PARAMETER_CODE_PARAMETER_FK
		foreign key (FORMULAIREPARAMETER_CODE) references FORMULAIRE_PARAMETER
)
;

create table PRODUCT
(
	ID BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_1B846486_55E2_4BF0_94CA_BD5B7E8698F8)
		primary key,
	ADMIN BOOLEAN,
	NAME VARCHAR(255)
)
;

create table SHARE_SPACE
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_575D7426_D116_4F53_80D4_AB612B718663)
		primary key,
	SHARE_SPACE_NAME VARCHAR(50),
	NAME_DIRECTORY VARCHAR(50)
)
;

create table TEAM
(
	NOMTEAM VARCHAR(50) not null,
	ID BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_B03472B2_B227_4849_B30B_7B86CDF7DBC3)
		primary key,
	USERDIVISION_ID BIGINT,
	TEAMBOSSNAME VARCHAR(50),
	TEAMBOSSEMAIL VARCHAR(50),
	constraint FK18SAUD0G1WGP083F2WCW44T96
		foreign key (USERDIVISION_ID) references DIVISION,
	constraint TEAM_DIVISION_ID_FK
		foreign key (USERDIVISION_ID) references DIVISION
)
;

create table CONTACTS
(
	ID BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_15CA1E19_B9FE_4248_A2F7_E5CFFD02B9C4)
		primary key,
	NAME VARCHAR(50),
	CONTACT_EMAIL VARCHAR(50),
	TEAM_ID BIGINT not null,
	constraint CONTACTS_TEAM_ID_FK
		foreign key (TEAM_ID) references TEAM
)
;

create table FORMULAIRE_RESULTAT
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_377E63E1_CC52_4650_A32C_D410463D5369)
		primary key,
	DATE_CREATION DATE,
	TEAM_ID INTEGER,
	FORMULAIRE_ID INTEGER,
	constraint FORMULAIRE_RESULTAT_FORMULAIRE_LIST_ID_FK
		foreign key (FORMULAIRE_ID) references FORMULAIRE_LIST,
	constraint FORMULAIRE_RESULTAT_TEAM_ID_FK
		foreign key (TEAM_ID) references TEAM
)
;

create table FORMULAIRE_REPONSE
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_2B538E9D_9C66_4F23_8157_89BBC19D1458)
		primary key,
	ID_QUESTION INTEGER,
	REPONSE VARCHAR(2000),
	ID_RESULTAT INTEGER,
	constraint FORMULAIRE_REPONSE_FORMULAIRE_QUESTION_ID_FK
		foreign key (ID_QUESTION) references FORMULAIRE_QUESTION,
	constraint FORMULAIRE_REPONSE_FORMULAIRE_RESULTAT_ID_FK
		foreign key (ID_RESULTAT) references FORMULAIRE_RESULTAT
)
;

create table FORMULAIRE_TEAM
(
	ID_FORMULAIRE INTEGER,
	ID_TEAM INTEGER,
	ID BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_139E36F9_38D3_4EE2_95C9_076C34057BCF) not null
		primary key,
	constraint FORMULAIRE_TEAM_FORMULAIRE_LIST_ID_FK
		foreign key (ID_FORMULAIRE) references FORMULAIRE_LIST,
	constraint FORMULAIRE_TEAM_TEAM_ID_FK
		foreign key (ID_TEAM) references TEAM
)
;

create table USER
(
	ID BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_1879A31A_9A7D_466C_A76C_4929CE3F1974)
		primary key,
	NOM VARCHAR(255) not null,
	ACTIVE BOOLEAN,
	DATECREATION TIMESTAMP,
	EMAIL VARCHAR(255) not null,
	GENDER INTEGER,
	MATRICULE INTEGER not null,
	PASSWORD VARCHAR(100) not null,
	MAINPRODUCT_ID BIGINT,
	TEAMID_ID BIGINT,
	constraint FK8GA285XNNMH27TUXWDWHQMMNP
		foreign key (TEAMID_ID) references TEAM,
	constraint FKCO3GFS3N2QEVRS4UG842KQKW2
		foreign key (MAINPRODUCT_ID) references PRODUCT,
	constraint FKEPSVYLIC4X63C14ISTDNV0D2P
		foreign key (TEAMID_ID) references TEAM,
	constraint FKOYT5LTRM670TRXVXFGS7FJTVB
		foreign key (TEAMID_ID) references TEAM,
	constraint USER_TEAM_ID_FK
		foreign key (TEAMID_ID) references TEAM
)
;

create table TEAM_SECONDARY
(
	ID INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_BBE1C301_D11A_400A_8BE0_338DE79E31E5)
		primary key,
	TEAM_ID BIGINT,
	USER_ID INTEGER,
	constraint TEAM_SECONDARY_TEAM_ID_FK
		foreign key (TEAM_ID) references TEAM,
	constraint TEAM_SECONDARY_USER_ID_FK
		foreign key (USER_ID) references USER
)
;

create table USER_ENVIRONNEMENT
(
	USER_ID BIGINT not null,
	ENVIRONNEMENTS_ID BIGINT not null,
	constraint USER_ENVIRONNEMENT_PK_2
		primary key (USER_ID, ENVIRONNEMENTS_ID),
	constraint USER_ENVIRONNEMENT_ENVIRONNEMENT_ID_FK
		foreign key (ENVIRONNEMENTS_ID) references ENVIRONNEMENT,
	constraint USER_ENVIRONNEMENT_USER_ID_FK
		foreign key (USER_ID) references USER
)
;

create table USER_PRODUCT
(
	USER_ID BIGINT not null,
	PRODUCTS_ID BIGINT not null,
	primary key (USER_ID, PRODUCTS_ID),
	constraint FKBT8RTEPMVTSBHBJCS6ADUI5BY
		foreign key (USER_ID) references USER,
	constraint FKIO1UHULOD679XJAYF81CNVNCO
		foreign key (PRODUCTS_ID) references PRODUCT
)
;

