-- init tables if not exist
CREATE TABLE IF NOT EXISTS public.crm_role(
  id uuid PRIMARY KEY,
  name varchar(20)
);

CREATE TABLE IF NOT EXISTS public.crm_user(
  id uuid PRIMARY KEY,
  name varchar(100),
  password varchar(100),
  email varchar(254),
  role_id uuid REFERENCES crm_role(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS public.crm_permission(
  id uuid PRIMARY KEY,
  role_id uuid REFERENCES crm_role(id) ON DELETE RESTRICT,
  authority varchar(100)
);

CREATE TABLE IF NOT EXISTS public.crm_company(
  id uuid PRIMARY KEY,
  name varchar(100),
  address varchar(200),
  created_by uuid,
  created_at timestamp,
  updated_by uuid,
  updated_at timestamp
);

CREATE TABLE IF NOT EXISTS public.crm_client(
  id uuid PRIMARY KEY,
  company_id uuid NOT NULL REFERENCES public.crm_company(id) ON DELETE RESTRICT,
  name varchar(100),
  email varchar(254),
  phone varchar(50),
  created_by uuid,
  created_at timestamp,
  updated_by uuid,
  updated_at timestamp
);