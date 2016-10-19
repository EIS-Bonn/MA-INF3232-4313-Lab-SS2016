--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: address_ledger_entries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE address_ledger_entries (
    id integer NOT NULL,
    address_id bigint,
    transaction_id bigint,
    input_id bigint,
    output_id bigint,
    amount bigint NOT NULL
);


ALTER TABLE address_ledger_entries OWNER TO postgres;

--
-- Name: address_ledger_entries_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE address_ledger_entries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE address_ledger_entries_id_seq OWNER TO postgres;

--
-- Name: address_ledger_entries_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE address_ledger_entries_id_seq OWNED BY address_ledger_entries.id;


--
-- Name: addresses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE addresses (
    id integer NOT NULL,
    address text,
    hash160 text,
    compressed boolean,
    label text,
    address_type smallint,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    total_received bigint DEFAULT 0,
    total_sent bigint DEFAULT 0
);


ALTER TABLE addresses OWNER TO postgres;

--
-- Name: addresses_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE addresses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE addresses_id_seq OWNER TO postgres;

--
-- Name: addresses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE addresses_id_seq OWNED BY addresses.id;


--
-- Name: addresses_outputs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE addresses_outputs (
    id integer NOT NULL,
    address_id integer,
    output_id integer NOT NULL
);


ALTER TABLE addresses_outputs OWNER TO postgres;

--
-- Name: addresses_outputs_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE addresses_outputs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE addresses_outputs_id_seq OWNER TO postgres;

--
-- Name: addresses_outputs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE addresses_outputs_id_seq OWNED BY addresses_outputs.id;


--
-- Name: blocks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE blocks (
    id integer NOT NULL,
    hsh text NOT NULL,
    height bigint,
    prev_block text,
    mrkl_root text,
    "time" bigint,
    bits bigint,
    nonce bigint,
    ver bigint,
    branch smallint,
    size integer,
    work bytea,
    fees bigint,
    total_in_value bigint,
    total_out_value bigint,
    transactions_count integer,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE blocks OWNER TO postgres;

--
-- Name: blocks_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE blocks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE blocks_id_seq OWNER TO postgres;

--
-- Name: blocks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE blocks_id_seq OWNED BY blocks.id;


--
-- Name: blocks_transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE blocks_transactions (
    block_id integer NOT NULL,
    transaction_id integer NOT NULL,
    "position" integer
);


ALTER TABLE blocks_transactions OWNER TO postgres;

--
-- Name: inputs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE inputs (
    id integer NOT NULL,
    hsh text NOT NULL,
    prev_out text NOT NULL,
    index bigint NOT NULL,
    script bytea,
    sequence bytea,
    "position" integer
);


ALTER TABLE inputs OWNER TO postgres;

--
-- Name: inputs_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE inputs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE inputs_id_seq OWNER TO postgres;

--
-- Name: inputs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE inputs_id_seq OWNED BY inputs.id;


--
-- Name: outputs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE outputs (
    id integer NOT NULL,
    hsh text NOT NULL,
    amount bigint NOT NULL,
    script bytea NOT NULL,
    "position" bigint,
    spent boolean DEFAULT false,
    branch smallint,
    type text
);


ALTER TABLE outputs OWNER TO postgres;

--
-- Name: outputs_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE outputs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE outputs_id_seq OWNER TO postgres;

--
-- Name: outputs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE outputs_id_seq OWNED BY outputs.id;


--
-- Name: peers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE peers (
    id integer NOT NULL,
    hostname text,
    ip text,
    port bigint,
    services bigint,
    last_seen timestamp without time zone,
    connected boolean DEFAULT false,
    favorite boolean DEFAULT false,
    worker_name text,
    connection_id text
);


ALTER TABLE peers OWNER TO postgres;

--
-- Name: peers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE peers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE peers_id_seq OWNER TO postgres;

--
-- Name: peers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE peers_id_seq OWNED BY peers.id;


--
-- Name: raw_blocks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE raw_blocks (
    id integer NOT NULL,
    hsh text NOT NULL,
    payload bytea
);


ALTER TABLE raw_blocks OWNER TO postgres;

--
-- Name: raw_blocks_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE raw_blocks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE raw_blocks_id_seq OWNER TO postgres;

--
-- Name: raw_blocks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE raw_blocks_id_seq OWNED BY raw_blocks.id;


--
-- Name: raw_transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE raw_transactions (
    id integer NOT NULL,
    hsh text NOT NULL,
    payload bytea
);


ALTER TABLE raw_transactions OWNER TO postgres;

--
-- Name: raw_transactions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE raw_transactions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE raw_transactions_id_seq OWNER TO postgres;

--
-- Name: raw_transactions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE raw_transactions_id_seq OWNED BY raw_transactions.id;


--
-- Name: schema_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE schema_info (
    version integer DEFAULT 0 NOT NULL
);


ALTER TABLE schema_info OWNER TO postgres;

--
-- Name: transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE transactions (
    id integer NOT NULL,
    hsh text NOT NULL,
    ver bigint,
    lock_time bigint,
    size integer,
    pool smallint NOT NULL,
    fee bigint,
    total_in_value bigint,
    total_out_value bigint,
    inputs_count integer,
    outputs_count integer,
    height bigint DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE transactions OWNER TO postgres;

--
-- Name: transactions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE transactions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE transactions_id_seq OWNER TO postgres;

--
-- Name: transactions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE transactions_id_seq OWNED BY transactions.id;


--
-- Name: unconfirmed_addresses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE unconfirmed_addresses (
    id integer NOT NULL,
    address text,
    hash160 text,
    compressed boolean,
    balance bigint,
    label text,
    address_type smallint,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE unconfirmed_addresses OWNER TO postgres;

--
-- Name: unconfirmed_addresses_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE unconfirmed_addresses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE unconfirmed_addresses_id_seq OWNER TO postgres;

--
-- Name: unconfirmed_addresses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE unconfirmed_addresses_id_seq OWNED BY unconfirmed_addresses.id;


--
-- Name: unconfirmed_addresses_outputs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE unconfirmed_addresses_outputs (
    address_id integer NOT NULL,
    output_id integer NOT NULL
);


ALTER TABLE unconfirmed_addresses_outputs OWNER TO postgres;

--
-- Name: unconfirmed_inputs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE unconfirmed_inputs (
    id integer NOT NULL,
    hsh text NOT NULL,
    prev_out text NOT NULL,
    index bigint NOT NULL,
    script bytea,
    sequence bytea,
    "position" integer
);


ALTER TABLE unconfirmed_inputs OWNER TO postgres;

--
-- Name: unconfirmed_inputs_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE unconfirmed_inputs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE unconfirmed_inputs_id_seq OWNER TO postgres;

--
-- Name: unconfirmed_inputs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE unconfirmed_inputs_id_seq OWNED BY unconfirmed_inputs.id;


--
-- Name: unconfirmed_ledger_entries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE unconfirmed_ledger_entries (
    id integer NOT NULL,
    address_id bigint,
    transaction_id bigint,
    input_id bigint,
    output_id bigint,
    amount bigint NOT NULL
);


ALTER TABLE unconfirmed_ledger_entries OWNER TO postgres;

--
-- Name: unconfirmed_ledger_entries_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE unconfirmed_ledger_entries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE unconfirmed_ledger_entries_id_seq OWNER TO postgres;

--
-- Name: unconfirmed_ledger_entries_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE unconfirmed_ledger_entries_id_seq OWNED BY unconfirmed_ledger_entries.id;


--
-- Name: unconfirmed_outputs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE unconfirmed_outputs (
    id integer NOT NULL,
    hsh text NOT NULL,
    amount bigint NOT NULL,
    script bytea NOT NULL,
    "position" bigint,
    spent boolean DEFAULT false,
    type text
);


ALTER TABLE unconfirmed_outputs OWNER TO postgres;

--
-- Name: unconfirmed_outputs_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE unconfirmed_outputs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE unconfirmed_outputs_id_seq OWNER TO postgres;

--
-- Name: unconfirmed_outputs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE unconfirmed_outputs_id_seq OWNED BY unconfirmed_outputs.id;


--
-- Name: unconfirmed_raw_transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE unconfirmed_raw_transactions (
    id integer NOT NULL,
    hsh text NOT NULL,
    payload bytea
);


ALTER TABLE unconfirmed_raw_transactions OWNER TO postgres;

--
-- Name: unconfirmed_raw_transactions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE unconfirmed_raw_transactions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE unconfirmed_raw_transactions_id_seq OWNER TO postgres;

--
-- Name: unconfirmed_raw_transactions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE unconfirmed_raw_transactions_id_seq OWNED BY unconfirmed_raw_transactions.id;


--
-- Name: unconfirmed_transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE unconfirmed_transactions (
    id integer NOT NULL,
    hsh text NOT NULL,
    ver bigint,
    lock_time bigint,
    size integer,
    fee bigint,
    pool smallint,
    total_in_value bigint,
    total_out_value bigint,
    inputs_count integer,
    outputs_count integer,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE unconfirmed_transactions OWNER TO postgres;

--
-- Name: unconfirmed_transactions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE unconfirmed_transactions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE unconfirmed_transactions_id_seq OWNER TO postgres;

--
-- Name: unconfirmed_transactions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE unconfirmed_transactions_id_seq OWNED BY unconfirmed_transactions.id;


--
-- Name: unspent_outputs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE unspent_outputs (
    id integer NOT NULL,
    output_id bigint NOT NULL,
    amount bigint NOT NULL,
    address_id bigint
);


ALTER TABLE unspent_outputs OWNER TO postgres;

--
-- Name: unspent_outputs_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE unspent_outputs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE unspent_outputs_id_seq OWNER TO postgres;

--
-- Name: unspent_outputs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE unspent_outputs_id_seq OWNED BY unspent_outputs.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address_ledger_entries ALTER COLUMN id SET DEFAULT nextval('address_ledger_entries_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY addresses ALTER COLUMN id SET DEFAULT nextval('addresses_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY addresses_outputs ALTER COLUMN id SET DEFAULT nextval('addresses_outputs_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY blocks ALTER COLUMN id SET DEFAULT nextval('blocks_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inputs ALTER COLUMN id SET DEFAULT nextval('inputs_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY outputs ALTER COLUMN id SET DEFAULT nextval('outputs_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peers ALTER COLUMN id SET DEFAULT nextval('peers_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY raw_blocks ALTER COLUMN id SET DEFAULT nextval('raw_blocks_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY raw_transactions ALTER COLUMN id SET DEFAULT nextval('raw_transactions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY transactions ALTER COLUMN id SET DEFAULT nextval('transactions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_addresses ALTER COLUMN id SET DEFAULT nextval('unconfirmed_addresses_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_inputs ALTER COLUMN id SET DEFAULT nextval('unconfirmed_inputs_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_ledger_entries ALTER COLUMN id SET DEFAULT nextval('unconfirmed_ledger_entries_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_outputs ALTER COLUMN id SET DEFAULT nextval('unconfirmed_outputs_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_raw_transactions ALTER COLUMN id SET DEFAULT nextval('unconfirmed_raw_transactions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_transactions ALTER COLUMN id SET DEFAULT nextval('unconfirmed_transactions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unspent_outputs ALTER COLUMN id SET DEFAULT nextval('unspent_outputs_id_seq'::regclass);


--
-- Data for Name: address_ledger_entries; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY address_ledger_entries (id, address_id, transaction_id, input_id, output_id, amount) FROM stdin;
\.


--
-- Name: address_ledger_entries_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('address_ledger_entries_id_seq', 1, false);


--
-- Data for Name: addresses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY addresses (id, address, hash160, compressed, label, address_type, created_at, total_received, total_sent) FROM stdin;
\.


--
-- Name: addresses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('addresses_id_seq', 1, false);


--
-- Data for Name: addresses_outputs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY addresses_outputs (id, address_id, output_id) FROM stdin;
\.


--
-- Name: addresses_outputs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('addresses_outputs_id_seq', 1, false);


--
-- Data for Name: blocks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY blocks (id, hsh, height, prev_block, mrkl_root, "time", bits, nonce, ver, branch, size, work, fees, total_in_value, total_out_value, transactions_count, created_at) FROM stdin;
\.


--
-- Name: blocks_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('blocks_id_seq', 1, false);


--
-- Data for Name: blocks_transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY blocks_transactions (block_id, transaction_id, "position") FROM stdin;
\.


--
-- Data for Name: inputs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY inputs (id, hsh, prev_out, index, script, sequence, "position") FROM stdin;
\.


--
-- Name: inputs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('inputs_id_seq', 1, false);


--
-- Data for Name: outputs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY outputs (id, hsh, amount, script, "position", spent, branch, type) FROM stdin;
\.


--
-- Name: outputs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('outputs_id_seq', 1, false);


--
-- Data for Name: peers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peers (id, hostname, ip, port, services, last_seen, connected, favorite, worker_name, connection_id) FROM stdin;
\.


--
-- Name: peers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('peers_id_seq', 1, false);


--
-- Data for Name: raw_blocks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY raw_blocks (id, hsh, payload) FROM stdin;
\.


--
-- Name: raw_blocks_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('raw_blocks_id_seq', 1, false);


--
-- Data for Name: raw_transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY raw_transactions (id, hsh, payload) FROM stdin;
\.


--
-- Name: raw_transactions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('raw_transactions_id_seq', 1, false);


--
-- Data for Name: schema_info; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY schema_info (version) FROM stdin;
3
\.


--
-- Data for Name: transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY transactions (id, hsh, ver, lock_time, size, pool, fee, total_in_value, total_out_value, inputs_count, outputs_count, height, created_at) FROM stdin;
\.


--
-- Name: transactions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('transactions_id_seq', 1, false);


--
-- Data for Name: unconfirmed_addresses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY unconfirmed_addresses (id, address, hash160, compressed, balance, label, address_type, created_at) FROM stdin;
\.


--
-- Name: unconfirmed_addresses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('unconfirmed_addresses_id_seq', 1, false);


--
-- Data for Name: unconfirmed_addresses_outputs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY unconfirmed_addresses_outputs (address_id, output_id) FROM stdin;
\.


--
-- Data for Name: unconfirmed_inputs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY unconfirmed_inputs (id, hsh, prev_out, index, script, sequence, "position") FROM stdin;
\.


--
-- Name: unconfirmed_inputs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('unconfirmed_inputs_id_seq', 1, false);


--
-- Data for Name: unconfirmed_ledger_entries; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY unconfirmed_ledger_entries (id, address_id, transaction_id, input_id, output_id, amount) FROM stdin;
\.


--
-- Name: unconfirmed_ledger_entries_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('unconfirmed_ledger_entries_id_seq', 1, false);


--
-- Data for Name: unconfirmed_outputs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY unconfirmed_outputs (id, hsh, amount, script, "position", spent, type) FROM stdin;
\.


--
-- Name: unconfirmed_outputs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('unconfirmed_outputs_id_seq', 1, false);


--
-- Data for Name: unconfirmed_raw_transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY unconfirmed_raw_transactions (id, hsh, payload) FROM stdin;
\.


--
-- Name: unconfirmed_raw_transactions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('unconfirmed_raw_transactions_id_seq', 1, false);


--
-- Data for Name: unconfirmed_transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY unconfirmed_transactions (id, hsh, ver, lock_time, size, fee, pool, total_in_value, total_out_value, inputs_count, outputs_count, created_at) FROM stdin;
\.


--
-- Name: unconfirmed_transactions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('unconfirmed_transactions_id_seq', 1, false);


--
-- Data for Name: unspent_outputs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY unspent_outputs (id, output_id, amount, address_id) FROM stdin;
\.


--
-- Name: unspent_outputs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('unspent_outputs_id_seq', 1, false);


--
-- Name: address_ledger_entries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address_ledger_entries
    ADD CONSTRAINT address_ledger_entries_pkey PRIMARY KEY (id);


--
-- Name: addresses_outputs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY addresses_outputs
    ADD CONSTRAINT addresses_outputs_pkey PRIMARY KEY (id);


--
-- Name: addresses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY addresses
    ADD CONSTRAINT addresses_pkey PRIMARY KEY (id);


--
-- Name: blocks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY blocks
    ADD CONSTRAINT blocks_pkey PRIMARY KEY (id);


--
-- Name: blocks_transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY blocks_transactions
    ADD CONSTRAINT blocks_transactions_pkey PRIMARY KEY (block_id, transaction_id);


--
-- Name: inputs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inputs
    ADD CONSTRAINT inputs_pkey PRIMARY KEY (id);


--
-- Name: outputs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY outputs
    ADD CONSTRAINT outputs_pkey PRIMARY KEY (id);


--
-- Name: peers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peers
    ADD CONSTRAINT peers_pkey PRIMARY KEY (id);


--
-- Name: raw_blocks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY raw_blocks
    ADD CONSTRAINT raw_blocks_pkey PRIMARY KEY (id);


--
-- Name: raw_transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY raw_transactions
    ADD CONSTRAINT raw_transactions_pkey PRIMARY KEY (id);


--
-- Name: transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (id);


--
-- Name: unconfirmed_addresses_outputs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_addresses_outputs
    ADD CONSTRAINT unconfirmed_addresses_outputs_pkey PRIMARY KEY (address_id, output_id);


--
-- Name: unconfirmed_addresses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_addresses
    ADD CONSTRAINT unconfirmed_addresses_pkey PRIMARY KEY (id);


--
-- Name: unconfirmed_inputs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_inputs
    ADD CONSTRAINT unconfirmed_inputs_pkey PRIMARY KEY (id);


--
-- Name: unconfirmed_ledger_entries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_ledger_entries
    ADD CONSTRAINT unconfirmed_ledger_entries_pkey PRIMARY KEY (id);


--
-- Name: unconfirmed_outputs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_outputs
    ADD CONSTRAINT unconfirmed_outputs_pkey PRIMARY KEY (id);


--
-- Name: unconfirmed_raw_transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_raw_transactions
    ADD CONSTRAINT unconfirmed_raw_transactions_pkey PRIMARY KEY (id);


--
-- Name: unconfirmed_transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_transactions
    ADD CONSTRAINT unconfirmed_transactions_pkey PRIMARY KEY (id);


--
-- Name: unspent_outputs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unspent_outputs
    ADD CONSTRAINT unspent_outputs_pkey PRIMARY KEY (id);


--
-- Name: address_ledger_entries_address_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX address_ledger_entries_address_id_index ON address_ledger_entries USING btree (address_id);


--
-- Name: address_ledger_entries_transaction_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX address_ledger_entries_transaction_id_index ON address_ledger_entries USING btree (transaction_id);


--
-- Name: addresses_address_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX addresses_address_index ON addresses USING btree (address);


--
-- Name: addresses_outputs_address_id_output_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX addresses_outputs_address_id_output_id_index ON addresses_outputs USING btree (address_id, output_id);


--
-- Name: addresses_outputs_output_id_address_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX addresses_outputs_output_id_address_id_index ON addresses_outputs USING btree (output_id, address_id);


--
-- Name: blocks_branch_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX blocks_branch_index ON blocks USING btree (branch);


--
-- Name: blocks_height_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX blocks_height_index ON blocks USING btree (height);


--
-- Name: blocks_hsh_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX blocks_hsh_index ON blocks USING btree (hsh);


--
-- Name: blocks_prev_block_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX blocks_prev_block_index ON blocks USING btree (prev_block);


--
-- Name: blocks_transactions_transaction_id_block_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX blocks_transactions_transaction_id_block_id_index ON blocks_transactions USING btree (transaction_id, block_id);


--
-- Name: inputs_hsh_position_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX inputs_hsh_position_index ON inputs USING btree (hsh, "position");


--
-- Name: inputs_prev_out_index_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX inputs_prev_out_index_index ON inputs USING btree (prev_out, index);


--
-- Name: outputs_branch_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX outputs_branch_index ON outputs USING btree (branch);


--
-- Name: outputs_hsh_position_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX outputs_hsh_position_index ON outputs USING btree (hsh, "position");


--
-- Name: outputs_position_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX outputs_position_index ON outputs USING btree ("position");


--
-- Name: outputs_spent_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX outputs_spent_index ON outputs USING btree (spent);


--
-- Name: peers_connected_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX peers_connected_index ON peers USING btree (connected);


--
-- Name: peers_favorite_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX peers_favorite_index ON peers USING btree (favorite);


--
-- Name: peers_ip_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX peers_ip_index ON peers USING btree (ip);


--
-- Name: peers_last_seen_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX peers_last_seen_index ON peers USING btree (last_seen);


--
-- Name: raw_blocks_hsh_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX raw_blocks_hsh_index ON raw_blocks USING btree (hsh);


--
-- Name: raw_transactions_hsh_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX raw_transactions_hsh_index ON raw_transactions USING btree (hsh);


--
-- Name: transactions_hsh_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX transactions_hsh_index ON transactions USING btree (hsh);


--
-- Name: transactions_pool_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX transactions_pool_index ON transactions USING btree (pool);


--
-- Name: unconfirmed_addresses_address_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX unconfirmed_addresses_address_index ON unconfirmed_addresses USING btree (address);


--
-- Name: unconfirmed_addresses_address_type_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_addresses_address_type_index ON unconfirmed_addresses USING btree (address_type);


--
-- Name: unconfirmed_addresses_outputs_output_id_address_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_addresses_outputs_output_id_address_id_index ON unconfirmed_addresses_outputs USING btree (output_id, address_id);


--
-- Name: unconfirmed_inputs_hsh_position_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_inputs_hsh_position_index ON unconfirmed_inputs USING btree (hsh, "position");


--
-- Name: unconfirmed_inputs_position_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_inputs_position_index ON unconfirmed_inputs USING btree ("position");


--
-- Name: unconfirmed_inputs_prev_out_index_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_inputs_prev_out_index_index ON unconfirmed_inputs USING btree (prev_out, index);


--
-- Name: unconfirmed_ledger_entries_address_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_ledger_entries_address_id_index ON unconfirmed_ledger_entries USING btree (address_id);


--
-- Name: unconfirmed_ledger_entries_input_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_ledger_entries_input_id_index ON unconfirmed_ledger_entries USING btree (input_id);


--
-- Name: unconfirmed_ledger_entries_output_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_ledger_entries_output_id_index ON unconfirmed_ledger_entries USING btree (output_id);


--
-- Name: unconfirmed_ledger_entries_transaction_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_ledger_entries_transaction_id_index ON unconfirmed_ledger_entries USING btree (transaction_id);


--
-- Name: unconfirmed_outputs_hsh_position_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_outputs_hsh_position_index ON unconfirmed_outputs USING btree (hsh, "position");


--
-- Name: unconfirmed_outputs_position_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_outputs_position_index ON unconfirmed_outputs USING btree ("position");


--
-- Name: unconfirmed_outputs_spent_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_outputs_spent_index ON unconfirmed_outputs USING btree (spent);


--
-- Name: unconfirmed_outputs_type_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unconfirmed_outputs_type_index ON unconfirmed_outputs USING btree (type);


--
-- Name: unconfirmed_raw_transactions_hsh_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX unconfirmed_raw_transactions_hsh_index ON unconfirmed_raw_transactions USING btree (hsh);


--
-- Name: unconfirmed_transactions_hsh_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX unconfirmed_transactions_hsh_index ON unconfirmed_transactions USING btree (hsh);


--
-- Name: unspent_outputs_address_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unspent_outputs_address_id_index ON unspent_outputs USING btree (address_id);


--
-- Name: unspent_outputs_amount_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unspent_outputs_amount_index ON unspent_outputs USING btree (amount);


--
-- Name: unspent_outputs_output_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX unspent_outputs_output_id_index ON unspent_outputs USING btree (output_id);


--
-- Name: address_ledger_entries_address_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address_ledger_entries
    ADD CONSTRAINT address_ledger_entries_address_id_fkey FOREIGN KEY (address_id) REFERENCES addresses(id);


--
-- Name: address_ledger_entries_input_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address_ledger_entries
    ADD CONSTRAINT address_ledger_entries_input_id_fkey FOREIGN KEY (input_id) REFERENCES inputs(id);


--
-- Name: address_ledger_entries_output_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address_ledger_entries
    ADD CONSTRAINT address_ledger_entries_output_id_fkey FOREIGN KEY (output_id) REFERENCES outputs(id);


--
-- Name: address_ledger_entries_transaction_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address_ledger_entries
    ADD CONSTRAINT address_ledger_entries_transaction_id_fkey FOREIGN KEY (transaction_id) REFERENCES transactions(id);


--
-- Name: addresses_outputs_address_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY addresses_outputs
    ADD CONSTRAINT addresses_outputs_address_id_fkey FOREIGN KEY (address_id) REFERENCES addresses(id);


--
-- Name: addresses_outputs_output_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY addresses_outputs
    ADD CONSTRAINT addresses_outputs_output_id_fkey FOREIGN KEY (output_id) REFERENCES outputs(id);


--
-- Name: blocks_transactions_block_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY blocks_transactions
    ADD CONSTRAINT blocks_transactions_block_id_fkey FOREIGN KEY (block_id) REFERENCES blocks(id);


--
-- Name: blocks_transactions_transaction_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY blocks_transactions
    ADD CONSTRAINT blocks_transactions_transaction_id_fkey FOREIGN KEY (transaction_id) REFERENCES transactions(id);


--
-- Name: unconfirmed_addresses_outputs_address_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_addresses_outputs
    ADD CONSTRAINT unconfirmed_addresses_outputs_address_id_fkey FOREIGN KEY (address_id) REFERENCES unconfirmed_addresses(id);


--
-- Name: unconfirmed_addresses_outputs_output_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_addresses_outputs
    ADD CONSTRAINT unconfirmed_addresses_outputs_output_id_fkey FOREIGN KEY (output_id) REFERENCES unconfirmed_outputs(id);


--
-- Name: unconfirmed_ledger_entries_address_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_ledger_entries
    ADD CONSTRAINT unconfirmed_ledger_entries_address_id_fkey FOREIGN KEY (address_id) REFERENCES unconfirmed_addresses(id);


--
-- Name: unconfirmed_ledger_entries_input_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_ledger_entries
    ADD CONSTRAINT unconfirmed_ledger_entries_input_id_fkey FOREIGN KEY (input_id) REFERENCES unconfirmed_inputs(id);


--
-- Name: unconfirmed_ledger_entries_output_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_ledger_entries
    ADD CONSTRAINT unconfirmed_ledger_entries_output_id_fkey FOREIGN KEY (output_id) REFERENCES unconfirmed_outputs(id);


--
-- Name: unconfirmed_ledger_entries_transaction_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unconfirmed_ledger_entries
    ADD CONSTRAINT unconfirmed_ledger_entries_transaction_id_fkey FOREIGN KEY (transaction_id) REFERENCES unconfirmed_transactions(id);


--
-- Name: unspent_outputs_address_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unspent_outputs
    ADD CONSTRAINT unspent_outputs_address_id_fkey FOREIGN KEY (address_id) REFERENCES addresses(id);


--
-- Name: unspent_outputs_output_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unspent_outputs
    ADD CONSTRAINT unspent_outputs_output_id_fkey FOREIGN KEY (output_id) REFERENCES outputs(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

