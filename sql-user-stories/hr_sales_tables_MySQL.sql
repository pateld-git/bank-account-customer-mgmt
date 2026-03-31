/*
DROP SCHEMA IF EXISTS hr_sales;
CREATE SCHEMA hr_sales;
USE hr_sales;
*/
--  ALTER TABLE departments 
--  	DROP CONSTRAINT dept_emp_mgr_id_fk; 
SET FOREIGN_KEY_CHECKS = 0; -- ADDED BY DUNCAN PATEL: Allows tables to be dropped in any order to prevent circular reference deadlock
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS customers ;
DROP TABLE IF EXISTS consultants ;
DROP table IF EXISTS job_history;
DROP table IF EXISTS employees;
DROP table IF EXISTS departments;
DROP table IF EXISTS locations;
DROP table IF EXISTS countries;
DROP table IF EXISTS regions;
DROP table IF EXISTS jobs;
DROP table IF EXISTS sal_grades;
DROP view  IF EXISTS emp_details_view;
SET FOREIGN_KEY_CHECKS = 1; -- ADDED BY DUNCAN PATEL: Reinstates foreign key checks for safety
--  ***************************************************
--  Regions table to hold region information.
--  Countries table has a foreign key that references this table.

-- 
CREATE TABLE regions
(
    region_id   INT PRIMARY KEY COMMENT 'Primary key of regions table.',
    region_name VARCHAR(25) COMMENT 'Names of regions. Locations are in the countries of these regions.'
);


--  ***************************************************
--  Countries table to hold country information.
--  Locations table has a foreign key referencing this table.


-- 
CREATE TABLE countries
(
    country_id   CHAR(2) PRIMARY KEY COMMENT 'Primary key of countries table.',
    country_name VARCHAR(40) COMMENT 'Country name',
    region_id    INT comment 'Region ID for the country. Foreign key to region_id column in the departments table.',
        FOREIGN KEY (region_id) references regions(region_id)
);

--  ***************************************************
--  Locations table to hold address information for company departments.
--  Departments has a foreign key that references this table.
 
CREATE TABLE locations
(
    location_id    INT /*loc_id_pk*/ PRIMARY KEY COMMENT 'Primary key of locations table',
    street_address VARCHAR(40) COMMENT 'Street address of an office, warehouse, or production site of a company.
         Contains building number and street name',
    postal_code    VARCHAR(12) COMMENT 'Postal code of the location of an office, warehouse, or production site
         of a company. ',
    city           VARCHAR(30) NOT NULL COMMENT 'A not null column that shows city where an office, warehouse, or
         production site of a company is located. ',
    state_province VARCHAR(25) COMMENT 'State or Province where an office, warehouse, or production site of a
         company is located.',
    country_id     CHAR(2) comment 'Country where an office, warehouse, or production site of a company is
         located. Foreign key to country_id column of the countries table.',
    CONSTRAINT loc_country_id_fk FOREIGN KEY (country_id) REFERENCES countries(country_id)
);


--  ***************************************************
--  Departments table to hold company department information.
--  Employees, job_history, and consultants tables each have a foreign key referencing this table.


-- 
CREATE TABLE departments
(
    department_id   INT PRIMARY KEY COMMENT 'Primary key column of departments table.',
    department_name VARCHAR(30) NOT NULL COMMENT 'A not null column that shows name of a department. Administration,
         Marketing, Purchasing, Human Resources, Shipping, IT, Executive, Public
         Relations, Sales, Finance, and Accounting. ',
    manager_id      INT COMMENT 'Manager_id of a department. Foreign key to employee_id column of employees table. The manager_id column of the employee table references this column.',
    location_id     INT COMMENT 'Location id where a department is located. Foreign key to location_id column of locations table.',
    FOREIGN KEY (location_id) REFERENCES locations(location_id)
);


--  ***************************************************
--  able to hold the different names of job roles within the company.
--  HR.CONSULTANTS have a foreign key to this table.


-- 
CREATE TABLE jobs
(
    job_id     VARCHAR(10) PRIMARY KEY COMMENT 'Primary key of jobs table.',
    job_title  VARCHAR(35) NOT NULL COMMENT 'A not null column that shows job title, e.g. AD_VP, FI_ACCOUNTANT',
    min_salary DECIMAL(8,2)NOT NULL COMMENT 'Minimum salary for a job title.',
    max_salary DECIMAL(8,2)NOT NULL COMMENT 'Maximum salary for a job title'
);

--  ***************************************************
--  EES table to hold the employee personnel
--  he company.
--  a self referencing foreign key to this table.
--  s a foreign key to this table.


-- 
CREATE TABLE employees
(
    employee_id  INT PRIMARY KEY COMMENT 'Primary key of employees table.',
    first_name   VARCHAR(20) NOT NULL COMMENT 'First name of the employee. A not null column.',
    last_name    VARCHAR(25) NOT NULL COMMENT 'Last name of the employee. A not null column.',
    email        VARCHAR(25) NOT NULL UNIQUE COMMENT 'Email id of the employee',
    phone_number VARCHAR(20) NOT NULL COMMENT 'Phone number of the employee; includes country code and area code',
    hire_date    DATE    	 NOT NULL COMMENT 'Date when the employee started on this job. A not null column.',
    job_id       VARCHAR(10) NOT NULL comment 'Current job of the employee; foreign key to job_id column of the jobs table. A not null column.',
    salary 		 DECIMAL(8,2) NOT NULL CONSTRAINT emp_salary_ck CHECK (salary > 0),
	commission_pct DECIMAL(2,2),
    manager_id 	 INT,
    department_id INT,
    CONSTRAINT emp_job_id_fk FOREIGN KEY (job_id) REFERENCES jobs(job_id),
    CONSTRAINT emp_mgr_id_fk FOREIGN KEY (manager_id) references employees(employee_id),
    CONSTRAINT emp_dept_id_fk FOREIGN KEY (department_id) references departments(department_id)
   
);

	-- ***************************************************************
	-- The CONSULTANTS table will hold the consultant information.
	-- It is nearly identical to the employees table.
 
CREATE TABLE consultants
(
    consultant_id INT PRIMARY KEY COMMENT 'Primary key of consultants table.',
    first_name    VARCHAR(20) not null comment 'First name of the consultant. A not null column.',
    last_name     VARCHAR(25) not null comment 'Last name of the consultant. A not null column.',
    email         VARCHAR(25) NOT NULL UNIQUE COMMENT 'Email id of the consultant',
    phone_number  VARCHAR(20) NOT NULL COMMENT 'Phone number of the consultant; includes country code and area code',
    hire_date     DATE		  NOT NULL COMMENT 'Date when the consultant started this job. A not null column.',
    job_id        VARCHAR(10) NOT NULL COMMENT 'Current job of the consultant; foreign key to job_id column of the
         jobs table. A not null column.',
    salary 		  	DECIMAL(8,2) NOT NULL CONSTRAINT cons_salary_ck CHECK (salary > 0),
    commission_pct 	DECIMAL(2,2),
    manager_id 		INT,
    department_id 	INT,
    CONSTRAINT cons_job_id_fk  FOREIGN KEY (job_id) REFERENCES jobs(job_id),
    CONSTRAINT cons_mgr_id_fk  FOREIGN KEY (manager_id) references employees(employee_id),
    CONSTRAINT cons_dept_id_fk FOREIGN KEY (department_id) REFERENCES departments(department_id)
);


--  ***************************************************
--  Job_history table holds the history of jobs that employees did in the past.
--  Has foreign keys to employees, departments, and jobs tables.


-- 
CREATE TABLE job_history
(
    employee_id   INT         NOT NULL,
    start_date    DATE		  NOT NULL,
    end_date      DATE	      NOT NULL COMMENT 'Last day of the employee in this job role. A not null column. Must be
         greater than the start_date of the job_history table.
         (enforced by constraint jhist_date_interval)',
    job_id        VARCHAR(10) NOT NULL comment 'Job role in which the employee worked in the past; foreign key to
         job_id column in the jobs table. A not null column.',
    department_id INT    NOT NULL comment 'Department id in which the employee worked in the past; foreign key to deparment_id column in the departments table',
    CONSTRAINT jhist_date_interval CHECK (end_date > start_date),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id),
    FOREIGN KEY (job_id) REFERENCES jobs(job_id),
    FOREIGN KEY (department_id) REFERENCES departments(department_id),
    CONSTRAINT jhist_emp_id_st_date_pk PRIMARY KEY (employee_id, start_date)
);

--  ***************************************************
--  Sal_grades table to hold the grade for each salary range

-- 
CREATE TABLE sal_grades
(
    grade_level CHAR(3) PRIMARY KEY COMMENT 'Primary key of sal_grades table.',
    lowest_sal  DECIMAL(8, 2) NOT NULL COMMENT 'Lowest salary in the grade range. A not null column.',
    highest_sal DECIMAL(8, 2) NOT NULL COMMENT 'Highest salary in the grade range. A not null column.',
    CONSTRAINT sgrade_sal_ck CHECK (lowest_sal < highest_sal)
);

--  ***************************************************
--  Emp_details_view joins the employees, departments, 
--  jobs, locations, countries, and regions tables to provide details
--  about employees.


-- 
CREATE OR REPLACE VIEW emp_details_view
            (employee_id, job_id, manager_id, department_id, location_id, country_id,
             first_name, last_name, salary, commission_pct, department_name, job_title,
             city, state_province, country_name, region_name)
AS
SELECT e.employee_id,
       e.job_id,
       e.manager_id,
       e.department_id,
       d.location_id,
       l.country_id,
       e.first_name,
       e.last_name,
       e.salary,
       e.commission_pct,
       d.department_name,
       j.job_title,
       l.city,
       l.state_province,
       c.country_name,
       r.region_name
FROM employees e, departments d, jobs j, locations l, countries c, regions r
WHERE e.department_id = d.department_id
  AND d.location_id = l.location_id
  AND l.country_id = c.country_id
  AND c.region_id = r.region_id
  AND j.job_id = e.job_id
;

--  **********insert data into the REGIONS table

-- 
INSERT INTO regions
VALUES ( 1
       , 'Europe');

-- 
INSERT INTO regions
VALUES ( 2
       , 'Americas');

-- 
INSERT INTO regions
VALUES ( 3
       , 'Asia');

-- 
INSERT INTO regions
VALUES ( 4
       , 'Middle East and Africa');

--  **********insert data into the COUNTRIES table


INSERT INTO countries
VALUES ( 'IT'
       , 'Italy'
       , 1);

-- 
INSERT INTO countries
VALUES ( 'JP'
       , 'Japan'
       , 3);

-- 
INSERT INTO countries
VALUES ( 'US'
       , 'United States of America'
       , 2);

-- 
INSERT INTO countries
VALUES ( 'CA'
       , 'Canada'
       , 2);

-- 
INSERT INTO countries
VALUES ( 'CN'
       , 'China'
       , 3);

-- 
INSERT INTO countries
VALUES ( 'IN'
       , 'India'
       , 3);

-- 
INSERT INTO countries
VALUES ( 'AU'
       , 'Australia'
       , 3);

-- 
INSERT INTO countries
VALUES ( 'ZW'
       , 'Zimbabwe'
       , 4);

-- 
INSERT INTO countries
VALUES ( 'SG'
       , 'Singapore'
       , 3);

-- 
INSERT INTO countries
VALUES ( 'UK'
       , 'United Kingdom'
       , 1);

-- 
INSERT INTO countries
VALUES ( 'FR'
       , 'France'
       , 1);

-- 
INSERT INTO countries
VALUES ( 'DE'
       , 'Germany'
       , 1);

-- 
INSERT INTO countries
VALUES ( 'ZM'
       , 'Zambia'
       , 4);

-- 
INSERT INTO countries
VALUES ( 'EG'
       , 'Egypt'
       , 4);

-- 
INSERT INTO countries
VALUES ( 'BR'
       , 'Brazil'
       , 2);

-- 
INSERT INTO countries
VALUES ( 'CH'
       , 'Switzerland'
       , 1);

-- 
INSERT INTO countries
VALUES ( 'NL'
       , 'Netherlands'
       , 1);

-- 
INSERT INTO countries
VALUES ( 'MX'
       , 'Mexico'
       , 2);

-- 
INSERT INTO countries
VALUES ( 'KW'
       , 'Kuwait'
       , 4);

-- 
INSERT INTO countries
VALUES ( 'IL'
       , 'Israel'
       , 4);

-- 
INSERT INTO countries
VALUES ( 'DK'
       , 'Denmark'
       , 1);

-- 
INSERT INTO countries
VALUES ( 'HK'
       , 'HongKong'
       , 3);

-- 
INSERT INTO countries
VALUES ( 'NG'
       , 'Nigeria'
       , 4);

-- 
INSERT INTO countries
VALUES ( 'AR'
       , 'Argentina'
       , 2);

-- 
INSERT INTO countries
VALUES ( 'BE'
       , 'Belgium'
       , 1);

--  **********insert data into the LOCATIONS table


INSERT INTO locations
VALUES ( 1000
       , '1297 Via Cola di Rie'
       , '00989'
       , 'Rome'
       , NULL
       , 'IT');

-- 
INSERT INTO locations
VALUES ( 1100
       , '93091 Calle della Testa'
       , '10934'
       , 'Venice'
       , NULL
       , 'IT');

-- 
INSERT INTO locations
VALUES ( 1200
       , '2017 Shinjuku-ku'
       , '1689'
       , 'Tokyo'
       , 'Tokyo Prefecture'
       , 'JP');

-- 
INSERT INTO locations
VALUES ( 1300
       , '9450 Kamiya-cho'
       , '6823'
       , 'Hiroshima'
       , NULL
       , 'JP');

-- 
INSERT INTO locations
VALUES ( 1400
       , '2014 Jabberwocky Rd'
       , '26192'
       , 'Southlake'
       , 'Texas'
       , 'US');

-- 
INSERT INTO locations
VALUES ( 1500
       , '2011 Interiors Blvd'
       , '99236'
       , 'South San Francisco'
       , 'California'
       , 'US');

-- 
INSERT INTO locations
VALUES ( 1600
       , '2007 Zagora St'
       , '50090'
       , 'South Brunswick'
       , 'New Jersey'
       , 'US');

-- 
INSERT INTO locations
VALUES ( 1700
       , '2004 Charade Rd'
       , '98199'
       , 'Seattle'
       , 'Washington'
       , 'US');

-- 
INSERT INTO locations
VALUES ( 1800
       , '147 Spadina Ave'
       , 'M5V 2L7'
       , 'Toronto'
       , 'Ontario'
       , 'CA');

-- 
INSERT INTO locations
VALUES ( 1900
       , '6092 Boxwood St'
       , 'YSW 9T2'
       , 'Whitehorse'
       , 'Yukon'
       , 'CA');

-- 
INSERT INTO locations
VALUES ( 2000
       , '40-5-12 Laogianggen'
       , '190518'
       , 'Beijing'
       , NULL
       , 'CN');

-- 
INSERT INTO locations
VALUES ( 2100
       , '1298 Vileparle (E)'
       , '490231'
       , 'Bombay'
       , 'Maharashtra'
       , 'IN');

-- 
INSERT INTO locations
VALUES ( 2200
       , '12-98 Victoria Street'
       , '2901'
       , 'Sydney'
       , 'New South Wales'
       , 'AU');

-- 
INSERT INTO locations
VALUES ( 2300
       , '198 Clementi North'
       , '540198'
       , 'Singapore'
       , NULL
       , 'SG');

-- 
INSERT INTO locations
VALUES ( 2400
       , '8204 Arthur St'
       , NULL
       , 'London'
       , NULL
       , 'UK');

-- 
INSERT INTO locations
VALUES ( 2500
       , 'Magdalen Centre, The Oxford Science Park'
       , 'OX9 9ZB'
       , 'Oxford'
       , 'Oxford'
       , 'UK');

-- 
INSERT INTO locations
VALUES ( 2600
       , '9702 Chester Road'
       , '09629850293'
       , 'Stretford'
       , 'Manchester'
       , 'UK');

-- 
INSERT INTO locations
VALUES ( 2700
       , 'Schwanthalerstr. 7031'
       , '80925'
       , 'Munich'
       , 'Bavaria'
       , 'DE');

-- 
INSERT INTO locations
VALUES ( 2800
       , 'Rua Frei Caneca 1360 '
       , '01307-002'
       , 'Sao Paulo'
       , 'Sao Paulo'
       , 'BR');

-- 
INSERT INTO locations
VALUES ( 2900
       , '20 Rue des Corps-Saints'
       , '1730'
       , 'Geneva'
       , 'Geneve'
       , 'CH');

-- 
INSERT INTO locations
VALUES ( 3000
       , 'Murtenstrasse 921'
       , '3095'
       , 'Bern'
       , 'BE'
       , 'CH');

-- 
INSERT INTO locations
VALUES ( 3100
       , 'Pieter Breughelstraat 837'
       , '3029SK'
       , 'Utrecht'
       , 'Utrecht'
       , 'NL');

-- 
INSERT INTO locations
VALUES ( 3200
       , 'Mariano Escobedo 9991'
       , '11932'
       , 'Mexico City'
       , 'Distrito Federal'
       , 'MX');

--  ***********insert data into the DEPARTMENTS table

INSERT INTO departments
VALUES ( 10
       , 'Administration'
       , 200
       , 1700);

-- 
INSERT INTO departments
VALUES ( 20
       , 'Marketing'
       , 201
       , 1800);

-- 
INSERT INTO departments
VALUES ( 30
       , 'Purchasing'
       , 114
       , 1700);

-- 
INSERT INTO departments
VALUES ( 40
       , 'Human Resources'
       , 203
       , 2400);

-- 
INSERT INTO departments
VALUES ( 50
       , 'Shipping'
       , 121
       , 1500);

-- 
INSERT INTO departments
VALUES ( 60
       , 'IT'
       , 103
       , 1400);

-- 
INSERT INTO departments
VALUES ( 70
       , 'Public Relations'
       , 204
       , 2700);

-- 
INSERT INTO departments
VALUES ( 80
       , 'Sales'
       , 145
       , 2500);

-- 
INSERT INTO departments
VALUES ( 90
       , 'Executive'
       , 100
       , 1700);

-- 
INSERT INTO departments
VALUES ( 100
       , 'Finance'
       , 108
       , 1700);

-- 
INSERT INTO departments
VALUES ( 110
       , 'Accounting'
       , 205
       , 1800);

-- 
INSERT INTO departments
VALUES ( 120
       , 'Treasury'
       , NULL
       , 1700);

-- 
INSERT INTO departments
VALUES ( 130
       , 'Corporate Tax'
       , NULL
       , 1800);

-- 
INSERT INTO departments
VALUES ( 140
       , 'Control And Credit'
       , NULL
       , 1700);

-- 
INSERT INTO departments
VALUES ( 150
       , 'Shareholder Services'
       , NULL
       , 1900);

-- 
INSERT INTO departments
VALUES ( 160
       , 'Benefits'
       , NULL
       , 2000);

-- 
INSERT INTO departments
VALUES ( 170
       , 'Manufacturing'
       , NULL
       , 2000);

-- 
INSERT INTO departments
VALUES ( 180
       , 'Construction'
       , NULL
       , 2100);

-- 
INSERT INTO departments
VALUES ( 190
       , 'Contracting'
       , NULL
       , 2200);

-- 
INSERT INTO departments
VALUES ( 200
       , 'Operations'
       , NULL
       , 2300);

-- 
INSERT INTO departments
VALUES ( 210
       , 'IT Support'
       , NULL
       , 2400);

-- 
INSERT INTO departments
VALUES ( 220
       , 'NOC'
       , NULL
       , 2500);

-- 
INSERT INTO departments
VALUES ( 230
       , 'IT Helpdesk'
       , NULL
       , 2500);

-- 
INSERT INTO departments
VALUES ( 240
       , 'Government Sales'
       , NULL
       , 1700);

-- 
INSERT INTO departments
VALUES ( 250
       , 'Retail Sales'
       , NULL
       , 2700);

-- 
INSERT INTO departments
VALUES ( 260
       , 'Recruiting'
       , NULL
       , 2800);

-- 
INSERT INTO departments
VALUES ( 270
       , 'Payroll'
       , NULL
       , 2900);

--  **********insert data into the JOBS table

INSERT INTO jobs
VALUES ( 'AD_PRES'
       , 'President'
       , 20000
       , 40000);
-- 
INSERT INTO jobs
VALUES ( 'AD_VP'
       , 'Administration Vice President'
       , 15000
       , 30000);

-- 
INSERT INTO jobs
VALUES ( 'AD_ASST'
       , 'Administration Assistant'
       , 3000
       , 6000);

-- 
INSERT INTO jobs
VALUES ( 'FI_MGR'
       , 'Finance Manager'
       , 8200
       , 16000);

-- 
INSERT INTO jobs
VALUES ( 'FI_ACCOUNT'
       , 'Accountant'
       , 4200
       , 9000);

-- 
INSERT INTO jobs
VALUES ( 'AC_MGR'
       , 'Accounting Manager'
       , 8200
       , 16000);

-- 
INSERT INTO jobs
VALUES ( 'AC_ACCOUNT'
       , 'Public Accountant'
       , 4200
       , 9000);
-- 
INSERT INTO jobs
VALUES ( 'SA_MAN'
       , 'Sales Manager'
       , 10000
       , 20000);

-- 
INSERT INTO jobs
VALUES ( 'SA_REP'
       , 'Sales Representative'
       , 6000
       , 12000);

-- 
INSERT INTO jobs
VALUES ( 'PU_MAN'
       , 'Purchasing Manager'
       , 8000
       , 15000);

-- 
INSERT INTO jobs
VALUES ( 'PU_CLERK'
       , 'Purchasing Clerk'
       , 2500
       , 5500);

-- 
INSERT INTO jobs
VALUES ( 'ST_MAN'
       , 'Stock Manager'
       , 5500
       , 8500);
-- 
INSERT INTO jobs
VALUES ( 'ST_CLERK'
       , 'Stock Clerk'
       , 2000
       , 5000);

-- 
INSERT INTO jobs
VALUES ( 'SH_CLERK'
       , 'Shipping Clerk'
       , 2500
       , 5500);

-- 
INSERT INTO jobs
VALUES ( 'IT_PROG'
       , 'Programmer'
       , 4000
       , 10000);

-- 
INSERT INTO jobs
VALUES ( 'MK_MAN'
       , 'Marketing Manager'
       , 9000
       , 15000);

-- 
INSERT INTO jobs
VALUES ( 'MK_REP'
       , 'Marketing Representative'
       , 4000
       , 9000);

-- 
INSERT INTO jobs
VALUES ( 'HR_REP'
       , 'Human Resources Representative'
       , 4000
       , 9000);

-- 
INSERT INTO jobs
VALUES ( 'PR_REP'
       , 'Public Relations Representative'
       , 4500
       , 10500);

--  **********insert data into the EMPLOYEES table

INSERT INTO employees
VALUES ( 100
       , 'Steven'
       , 'King'
       , 'SKING'
       , '515.123.4567'
       , STR_TO_DATE('17-JUN-07', '%d-%b-%y')
       , 'AD_PRES'
       , 24000
       , NULL
       , NULL
       , 90);

-- 
INSERT INTO employees
VALUES ( 101
       , 'Neena'
       , 'Kochhar'
       , 'NKOCHHAR'
       , '515.123.4568'
       , STR_TO_DATE('28-OCT-03', '%d-%b-%y')
       , 'AD_VP'
       , 17000
       , NULL
       , 100
       , 90);

-- 
INSERT INTO employees
VALUES ( 102
       , 'Lex'
       , 'De Haan'
       , 'LDEHAAN'
       , '515.123.4569'
       , STR_TO_DATE('13-JAN-03', '%d-%b-%y')
       , 'AD_VP'
       , 17000
       , NULL
       , 100
       , 90);

-- 
INSERT INTO employees
VALUES ( 103
       , 'Alexander'
       , 'Hunold'
       , 'AHUNOLD'
       , '590.423.4567'
       , STR_TO_DATE('03-JAN-10', '%d-%b-%y')
       , 'IT_PROG'
       , 9000
       , NULL
       , 102
       , 60);

-- 
INSERT INTO employees
VALUES ( 104
       , 'Bruce'
       , 'Ernst'
       , 'BERNST'
       , '590.423.4568'
       , STR_TO_DATE('21-MAY-01', '%d-%b-%y')
       , 'IT_PROG'
       , 6000
       , NULL
       , 103
       , 60);

-- 
INSERT INTO employees
VALUES ( 105
       , 'David'
       , 'Austin'
       , 'DAUSTIN'
       , '590.423.4569'
       , STR_TO_DATE('25-JUN-13', '%d-%b-%y')
       , 'IT_PROG'
       , 4800
       , NULL
       , 103
       , 60);

-- 
INSERT INTO employees
VALUES ( 106
       , 'Valli'
       , 'Pataballa'
       , 'VPATABAL'
       , '590.423.4560'
       , STR_TO_DATE('05-FEB-08', '%d-%b-%y')
       , 'IT_PROG'
       , 4800
       , NULL
       , 103
       , 60);

-- 
INSERT INTO employees
VALUES ( 107
       , 'Diana'
       , 'Lorentz'
       , 'DLORENTZ'
       , '590.423.5567'
       , STR_TO_DATE('07-FEB-09', '%d-%b-%y')
       , 'IT_PROG'
       , 4200
       , NULL
       , 103
       , 60);

-- 
INSERT INTO employees
VALUES ( 108
       , 'Nancy'
       , 'Greenberg'
       , 'NGREENBE'
       , '515.421.4569'
       , STR_TO_DATE('17-AUG-04', '%d-%b-%y')
       , 'FI_MGR'
       , 12000
       , NULL
       , 101
       , 100);

-- 
INSERT INTO employees
VALUES ( 109
       , 'Daniel'
       , 'Faviet'
       , 'DFAVIET'
       , '515.421.4169'
       , STR_TO_DATE('16-AUG-04', '%d-%b-%y')
       , 'FI_ACCOUNT'
       , 9000
       , NULL
       , 108
       , 100);

-- 
INSERT INTO employees
VALUES ( 110
       , 'John'
       , 'Chen'
       , 'JCHEN'
       , '515.421.4269'
       , STR_TO_DATE('28-SEP-07', '%d-%b-%y')
       , 'FI_ACCOUNT'
       , 8200
       , NULL
       , 108
       , 100);

-- 
INSERT INTO employees
VALUES ( 111
       , 'Ismael'
       , 'Sciarra'
       , 'ISCIARRA'
       , '515.124.4369'
       , STR_TO_DATE('30-SEP-07', '%d-%b-%y')
       , 'FI_ACCOUNT'
       , 7700
       , NULL
       , 108
       , 100);

-- 
INSERT INTO employees
VALUES ( 112
       , 'Jose Manuel'
       , 'Urman'
       , 'JMURMAN'
       , '515.124.4469'
       , STR_TO_DATE('07-MAR-08', '%d-%b-%y')
       , 'FI_ACCOUNT'
       , 7800
       , NULL
       , 108
       , 100);

-- 
INSERT INTO employees
VALUES ( 113
       , 'Luis'
       , 'Popp'
       , 'LPOPP'
       , '515.124.4567'
       , STR_TO_DATE('07-DEC-09', '%d-%b-%y')
       , 'FI_ACCOUNT'
       , 6900
       , NULL
       , 108
       , 100);

-- 
INSERT INTO employees
VALUES ( 114
       , 'Den'
       , 'Raphaely'
       , 'DRAPHEAL'
       , '515.127.4561'
       , STR_TO_DATE('24-MAR-08', '%d-%b-%y')
       , 'PU_MAN'
       , 11000
       , NULL
       , 100
       , 30);

-- 
INSERT INTO employees
VALUES ( 115
       , 'Alexander'
       , 'Khoo'
       , 'AKHOO'
       , '515.127.4562'
       , STR_TO_DATE('18-MAY-05', '%d-%b-%y')
       , 'PU_CLERK'
       , 3100
       , NULL
       , 114
       , 30);

-- 
INSERT INTO employees
VALUES ( 116
       , 'Shelli'
       , 'Baida'
       , 'SBAIDA'
       , '515.127.4563'
       , STR_TO_DATE('24-DEC-14', '%d-%b-%y')
       , 'PU_CLERK'
       , 2900
       , NULL
       , 114
       , 30);

-- 
INSERT INTO employees
VALUES ( 117
       , 'Sigal'
       , 'Tobias'
       , 'STOBIAS'
       , '515.127.4564'
       , STR_TO_DATE('24-JUL-07', '%d-%b-%y')
       , 'PU_CLERK'
       , 2800
       , NULL
       , 114
       , 30);

-- 
INSERT INTO employees
VALUES ( 118
       , 'Guy'
       , 'Himuro'
       , 'GHIMURO'
       , '515.127.4565'
       , STR_TO_DATE('15-NOV-08', '%d-%b-%y')
       , 'PU_CLERK'
       , 2600
       , NULL
       , 114
       , 30);

-- 
INSERT INTO employees
VALUES ( 119
       , 'Karen'
       , 'Colmenares'
       , 'KCOLMENA'
       , '515.127.4566'
       , STR_TO_DATE('10-AUG-09', '%d-%b-%y')
       , 'PU_CLERK'
       , 2500
       , NULL
       , 114
       , 30);

-- 
INSERT INTO employees
VALUES ( 120
       , 'Matthew'
       , 'Weiss'
       , 'MWEISS'
       , '650.123.1234'
       , STR_TO_DATE('18-JUL-12', '%d-%b-%y')
       , 'ST_MAN'
       , 8000
       , NULL
       , 100
       , 50);

-- 
INSERT INTO employees
VALUES ( 121
       , 'Adam'
       , 'Fripp'
       , 'AFRIPP'
       , '650.123.2234'
       , STR_TO_DATE('10-APR-07', '%d-%b-%y')
       , 'ST_MAN'
       , 8200
       , NULL
       , 100
       , 50);

-- 
INSERT INTO employees
VALUES ( 122
       , 'Payam'
       , 'Kaufling'
       , 'PKAUFLIN'
       , '650.123.3234'
       , STR_TO_DATE('01-JAN-09', '%d-%b-%y')
       , 'ST_MAN'
       , 7900
       , NULL
       , 100
       , 50);

-- 
INSERT INTO employees
VALUES ( 123
       , 'Shanta'
       , 'Vollman'
       , 'SVOLLMAN'
       , '650.123.4234'
       , STR_TO_DATE('10-OCT-14', '%d-%b-%y')
       , 'ST_MAN'
       , 6500
       , NULL
       , 100
       , 50);

-- 
INSERT INTO employees
VALUES ( 124
       , 'Kevin'
       , 'Mourgos'
       , 'KMOURGOS'
       , '650.123.5234'
       , STR_TO_DATE('16-NOV-19', '%d-%b-%y')
       , 'ST_MAN'
       , 5800
       , NULL
       , 100
       , 50);

-- 
INSERT INTO employees
VALUES ( 125
       , 'Julia'
       , 'Nayer'
       , 'JNAYER'
       , '650.124.1214'
       , STR_TO_DATE('16-JUL-07', '%d-%b-%y')
       , 'ST_CLERK'
       , 3200
       , NULL
       , 120
       , 50);

-- 
INSERT INTO employees
VALUES ( 126
       , 'Irene'
       , 'Mikkilineni'
       , 'IMIKKILI'
       , '650.124.1224'
       , STR_TO_DATE('28-SEP-08', '%d-%b-%y')
       , 'ST_CLERK'
       , 2700
       , NULL
       , 120
       , 50);

-- 
INSERT INTO employees
VALUES ( 127
       , 'James'
       , 'Landry'
       , 'JLANDRY'
       , '650.124.1334'
       , STR_TO_DATE('14-JAN-19', '%d-%b-%y')
       , 'ST_CLERK'
       , 2400
       , NULL
       , 120
       , 50);

-- 
INSERT INTO employees
VALUES ( 128
       , 'Steven'
       , 'Markle'
       , 'SMARKLE'
       , '650.124.1434'
       , STR_TO_DATE('08-MAR-00', '%d-%b-%y')
       , 'ST_CLERK'
       , 2200
       , NULL
       , 120
       , 50);

-- 
INSERT INTO employees
VALUES ( 129
       , 'Laura'
       , 'Bissot'
       , 'LBISSOT'
       , '650.124.5234'
       , STR_TO_DATE('20-AUG-07', '%d-%b-%y')
       , 'ST_CLERK'
       , 3300
       , NULL
       , 121
       , 50);

-- 
INSERT INTO employees
VALUES ( 130
       , 'Mozhe'
       , 'Atkinson'
       , 'MATKINSO'
       , '650.124.6234'
       , STR_TO_DATE('30-OCT-07', '%d-%b-%y')
       , 'ST_CLERK'
       , 2800
       , NULL
       , 121
       , 50);

-- 
INSERT INTO employees
VALUES ( 131
       , 'James'
       , 'Marlow'
       , 'JAMRLOW'
       , '650.124.7234'
       , STR_TO_DATE('16-FEB-15', '%d-%b-%y')
       , 'ST_CLERK'
       , 2500
       , NULL
       , 121
       , 50);

-- 
INSERT INTO employees
VALUES ( 132
       , 'TJ'
       , 'Olson'
       , 'TJOLSON'
       , '650.124.8234'
       , STR_TO_DATE('10-APR-09', '%d-%b-%y')
       , 'ST_CLERK'
       , 2100
       , NULL
       , 121
       , 50);

-- 
INSERT INTO employees
VALUES ( 133
       , 'Jason'
       , 'Mallin'
       , 'JMALLIN'
       , '650.127.1934'
       , STR_TO_DATE('14-JUN-06', '%d-%b-%y')
       , 'ST_CLERK'
       , 3300
       , NULL
       , 122
       , 50);

-- 
INSERT INTO employees
VALUES ( 134
       , 'Michael'
       , 'Rogers'
       , 'MROGERS'
       , '650.127.1834'
       , STR_TO_DATE('26-AUG-08', '%d-%b-%y')
       , 'ST_CLERK'
       , 2900
       , NULL
       , 122
       , 50);

-- 
INSERT INTO employees
VALUES ( 135
       , 'Ki'
       , 'Gee'
       , 'KGEE'
       , '650.127.1734'
       , STR_TO_DATE('12-DEC-09', '%d-%b-%y')
       , 'ST_CLERK'
       , 2400
       , NULL
       , 122
       , 50);

-- 
INSERT INTO employees
VALUES ( 136
       , 'Hazel'
       , 'Philtanker'
       , 'HPHILTAN'
       , '650.127.1634'
       , STR_TO_DATE('06-FEB-00', '%d-%b-%y')
       , 'ST_CLERK'
       , 2200
       , NULL
       , 122
       , 50);

-- 
INSERT INTO employees
VALUES ( 137
       , 'Renske'
       , 'Ladwig'
       , 'RLADWIG'
       , '650.121.1234'
       , STR_TO_DATE('14-JUL-05', '%d-%b-%y')
       , 'ST_CLERK'
       , 3600
       , NULL
       , 123
       , 50);

-- 
INSERT INTO employees
VALUES ( 138
       , 'Stephen'
       , 'Stiles'
       , 'SSTILES'
       , '650.121.2034'
       , STR_TO_DATE('26-OCT-07', '%d-%b-%y')
       , 'ST_CLERK'
       , 3200
       , NULL
       , 123
       , 50);

-- 
INSERT INTO employees
VALUES ( 139
       , 'John'
       , 'Seo'
       , 'JSEO'
       , '650.121.2019'
       , STR_TO_DATE('12-FEB-08', '%d-%b-%y')
       , 'ST_CLERK'
       , 2700
       , NULL
       , 123
       , 50);

-- 
INSERT INTO employees
VALUES ( 140
       , 'Joshua'
       , 'Patel'
       , 'JPATEL'
       , '650.121.1834'
       , STR_TO_DATE('06-APR-15', '%d-%b-%y')
       , 'ST_CLERK'
       , 2500
       , NULL
       , 123
       , 50);

-- 
INSERT INTO employees
VALUES ( 141
       , 'Trenna'
       , 'Rajs'
       , 'TRAJS'
       , '650.121.8009'
       , STR_TO_DATE('17-OCT-05', '%d-%b-%y')
       , 'ST_CLERK'
       , 3500
       , NULL
       , 124
       , 50);

-- 
INSERT INTO employees
VALUES ( 142
       , 'Curtis'
       , 'Davies'
       , 'CDAVIES'
       , '650.121.2994'
       , STR_TO_DATE('29-JAN-07', '%d-%b-%y')
       , 'ST_CLERK'
       , 3100
       , NULL
       , 124
       , 50);

-- 
INSERT INTO employees
VALUES ( 143
       , 'Randall'
       , 'Matos'
       , 'RMATOS'
       , '650.121.2874'
       , STR_TO_DATE('15-MAR-08', '%d-%b-%y')
       , 'ST_CLERK'
       , 2600
       , NULL
       , 124
       , 50);

-- 
INSERT INTO employees
VALUES ( 144
       , 'Peter'
       , 'Vargas'
       , 'PVARGAS'
       , '650.121.2004'
       , STR_TO_DATE('09-JUL-18', '%d-%b-%y')
       , 'ST_CLERK'
       , 2500
       , NULL
       , 124
       , 50);

-- 
INSERT INTO employees
VALUES ( 145
       , 'John'
       , 'Russell'
       , 'JRUSSEL'
       , '011.44.1344.429268'
       , STR_TO_DATE('01-OCT-06', '%d-%b-%y')
       , 'SA_MAN'
       , 14000
       , .4
       , 100
       , 80);

-- 
INSERT INTO employees
VALUES ( 146
       , 'Karen'
       , 'Partners'
       , 'KPARTNER'
       , '011.44.1344.467268'
       , STR_TO_DATE('05-JAN-07', '%d-%b-%y')
       , 'SA_MAN'
       , 13500
       , .3
       , 100
       , 80);

-- 
INSERT INTO employees
VALUES ( 147
       , 'Alberto'
       , 'Errazuriz'
       , 'AERRAZUR'
       , '011.44.1344.429278'
       , STR_TO_DATE('10-MAR-16', '%d-%b-%y')
       , 'SA_MAN'
       , 12000
       , .3
       , 100
       , 80);

-- 
INSERT INTO employees
VALUES ( 148
       , 'Gerald'
       , 'Cambrault'
       , 'GCAMBRAU'
       , '011.44.1344.619268'
       , STR_TO_DATE('15-OCT-09', '%d-%b-%y')
       , 'SA_MAN'
       , 11000
       , .3
       , 100
       , 80);

-- 
INSERT INTO employees
VALUES ( 149
       , 'Eleni'
       , 'Zlotkey'
       , 'EZLOTKEY'
       , '011.44.1344.429018'
       , STR_TO_DATE('29-JAN-10', '%d-%b-%y')
       , 'SA_MAN'
       , 10500
       , .2
       , 100
       , 80);

-- 
INSERT INTO employees
VALUES ( 150
       , 'Peter'
       , 'Tucker'
       , 'PTUCKER'
       , '011.44.1344.129268'
       , STR_TO_DATE('30-JAN-07', '%d-%b-%y')
       , 'SA_REP'
       , 10000
       , .3
       , 145
       , 80);

-- 
INSERT INTO employees
VALUES ( 151
       , 'David'
       , 'Bernstein'
       , 'DBERNSTE'
       , '011.44.1344.345268'
       , STR_TO_DATE('24-MAR-07', '%d-%b-%y')
       , 'SA_REP'
       , 9500
       , .25
       , 145
       , 80);

-- 
INSERT INTO employees
VALUES ( 152
       , 'Peter'
       , 'Hall'
       , 'PHALL'
       , '011.44.1344.478968'
       , STR_TO_DATE('20-AUG-16', '%d-%b-%y')
       , 'SA_REP'
       , 9000
       , .25
       , 145
       , 80);

-- 
INSERT INTO employees
VALUES ( 153
       , 'Christopher'
       , 'Olsen'
       , 'COLSEN'
       , '011.44.1344.498718'
       , STR_TO_DATE('30-MAR-08', '%d-%b-%y')
       , 'SA_REP'
       , 8000
       , .2
       , 145
       , 80);

-- 
INSERT INTO employees
VALUES ( 154
       , 'Nanette'
       , 'Cambrault'
       , 'NCAMBRAU'
       , '011.44.1344.987668'
       , STR_TO_DATE('09-DEC-18', '%d-%b-%y')
       , 'SA_REP'
       , 7500
       , .2
       , 145
       , 80);

-- 
INSERT INTO employees
VALUES ( 155
       , 'Oliver'
       , 'Tuvault'
       , 'OTUVAULT'
       , '011.44.1344.486508'
       , STR_TO_DATE('23-NOV-09', '%d-%b-%y')
       , 'SA_REP'
       , 7000
       , .15
       , 145
       , 80);

-- 
INSERT INTO employees
VALUES ( 156
       , 'Janette'
       , 'King'
       , 'JKING'
       , '011.44.1345.429268'
       , STR_TO_DATE('30-JAN-12', '%d-%b-%y')
       , 'SA_REP'
       , 10000
       , .35
       , 146
       , 80);

-- 
INSERT INTO employees
VALUES ( 157
       , 'Patrick'
       , 'Sully'
       , 'PSULLY'
       , '011.44.1345.929268'
       , STR_TO_DATE('04-MAR-06', '%d-%b-%y')
       , 'SA_REP'
       , 9500
       , .35
       , 146
       , 80);

-- 
INSERT INTO employees
VALUES ( 158
       , 'Allan'
       , 'McEwen'
       , 'AMCEWEN'
       , '011.44.1345.829268'
       , STR_TO_DATE('01-AUG-13', '%d-%b-%y')
       , 'SA_REP'
       , 9000
       , .35
       , 146
       , 80);

-- 
INSERT INTO employees
VALUES ( 159
       , 'Lindsey'
       , 'Smith'
       , 'LSMITH'
       , '011.44.1345.729268'
       , STR_TO_DATE('10-MAR-07', '%d-%b-%y')
       , 'SA_REP'
       , 8000
       , .3
       , 146
       , 80);

-- 
INSERT INTO employees
VALUES ( 160
       , 'Louise'
       , 'Doran'
       , 'LDORAN'
       , '011.44.1345.629268'
       , STR_TO_DATE('15-DEC-07', '%d-%b-%y')
       , 'SA_REP'
       , 7500
       , .3
       , 146
       , 80);

-- 
INSERT INTO employees
VALUES ( 161
       , 'Sarath'
       , 'Sewall'
       , 'SSEWALL'
       , '011.44.1345.529268'
       , STR_TO_DATE('03-NOV-18', '%d-%b-%y')
       , 'SA_REP'
       , 7000
       , .25
       , 146
       , 80);

-- 
INSERT INTO employees
VALUES ( 162
       , 'Clara'
       , 'Vishney'
       , 'CVISHNEY'
       , '011.44.1346.129268'
       , STR_TO_DATE('11-NOV-07', '%d-%b-%y')
       , 'SA_REP'
       , 10500
       , .25
       , 147
       , 80);

-- 
INSERT INTO employees
VALUES ( 163
       , 'Danielle'
       , 'Greene'
       , 'DGREENE'
       , '011.44.1346.229268'
       , STR_TO_DATE('19-MAR-19', '%d-%b-%y')
       , 'SA_REP'
       , 9500
       , .15
       , 147
       , 80);

-- 
INSERT INTO employees
VALUES ( 164
       , 'Mattea'
       , 'Marvins'
       , 'MMARVINS'
       , '011.44.1346.329268'
       , STR_TO_DATE('24-JAN-00', '%d-%b-%y')
       , 'SA_REP'
       , 7200
       , .10
       , 147
       , 80);

-- 
INSERT INTO employees
VALUES ( 165
       , 'David'
       , 'Lee'
       , 'DLEE'
       , '011.44.1346.529268'
       , STR_TO_DATE('23-FEB-00', '%d-%b-%y')
       , 'SA_REP'
       , 6800
       , .1
       , 147
       , 80);

-- 
INSERT INTO employees
VALUES ( 166
       , 'Sundar'
       , 'Ande'
       , 'SANDE'
       , '011.44.1346.629268'
       , STR_TO_DATE('24-MAR-00', '%d-%b-%y')
       , 'SA_REP'
       , 6400
       , .10
       , 147
       , 80);

-- 
INSERT INTO employees
VALUES ( 167
       , 'Amit'
       , 'Banda'
       , 'ABANDA'
       , '011.44.1346.729268'
       , STR_TO_DATE('21-APR-10', '%d-%b-%y')
       , 'SA_REP'
       , 6200
       , .10
       , 147
       , 80);

-- 
INSERT INTO employees
VALUES ( 168
       , 'Lisa'
       , 'Ozer'
       , 'LOZER'
       , '011.44.1343.929268'
       , STR_TO_DATE('11-MAR-07', '%d-%b-%y')
       , 'SA_REP'
       , 11500
       , .25
       , 148
       , 80);

-- 
INSERT INTO employees
VALUES ( 169
       , 'Harrison'
       , 'Bloom'
       , 'HBLOOM'
       , '011.44.1343.829268'
       , STR_TO_DATE('23-MAR-08', '%d-%b-%y')
       , 'SA_REP'
       , 10000
       , .20
       , 148
       , 80);

-- 
INSERT INTO employees
VALUES ( 170
       , 'Tayler'
       , 'Fox'
       , 'TFOX'
       , '011.44.1343.729268'
       , STR_TO_DATE('24-JAN-15', '%d-%b-%y')
       , 'SA_REP'
       , 9600
       , .20
       , 148
       , 80);

-- 
INSERT INTO employees
VALUES ( 171
       , 'William'
       , 'Smith'
       , 'WSMITH'
       , '011.44.1343.629268'
       , STR_TO_DATE('23-FEB-09', '%d-%b-%y')
       , 'SA_REP'
       , 7400
       , .15
       , 148
       , 80);

-- 
INSERT INTO employees
VALUES ( 172
       , 'Elizabeth'
       , 'Bates'
       , 'EBATES'
       , '011.44.1343.529268'
       , STR_TO_DATE('24-MAR-09', '%d-%b-%y')
       , 'SA_REP'
       , 7300
       , .15
       , 148
       , 80);

-- 
INSERT INTO employees
VALUES ( 173
       , 'Sundita'
       , 'Kumar'
       , 'SKUMAR'
       , '011.44.1343.329268'
       , STR_TO_DATE('21-APR-00', '%d-%b-%y')
       , 'SA_REP'
       , 6100
       , .10
       , 148
       , 80);

-- 
INSERT INTO employees
VALUES ( 174
       , 'Ellen'
       , 'Abel'
       , 'EABEL'
       , '011.44.1644.429267'
       , STR_TO_DATE('11-MAY-06', '%d-%b-%y')
       , 'SA_REP'
       , 11000
       , .30
       , 149
       , 80);

-- 
INSERT INTO employees
VALUES ( 175
       , 'Alyssa'
       , 'Hutton'
       , 'AHUTTON'
       , '011.44.1644.429266'
       , STR_TO_DATE('19-MAR-07', '%d-%b-%y')
       , 'SA_REP'
       , 8800
       , .25
       , 149
       , 80);

-- 
INSERT INTO employees
VALUES ( 176
       , 'Jonathon'
       , 'Taylor'
       , 'JTAYLOR'
       , '011.44.1644.429265'
       , STR_TO_DATE('24-MAR-08', '%d-%b-%y')
       , 'SA_REP'
       , 8600
       , .20
       , 149
       , 80);

-- 
INSERT INTO employees
VALUES ( 177
       , 'Jack'
       , 'Livingston'
       , 'JLIVINGS'
       , '011.44.1644.429264'
       , STR_TO_DATE('23-APR-08', '%d-%b-%y')
       , 'SA_REP'
       , 8400
       , .20
       , 149
       , 80);

-- 
INSERT INTO employees
VALUES ( 178
       , 'Kimberely'
       , 'Grant'
       , 'KGRANT'
       , '011.44.1644.429263'
       , STR_TO_DATE('24-MAY-19', '%d-%b-%y')
       , 'SA_REP'
       , 7000
       , .15
       , 149
       , NULL);

-- 
INSERT INTO employees
VALUES ( 179
       , 'Charles'
       , 'Johnson'
       , 'CJOHNSON'
       , '011.44.1644.429262'
       , STR_TO_DATE('04-JAN-00', '%d-%b-%y')
       , 'SA_REP'
       , 6200
       , .10
       , 149
       , 80);

-- 
INSERT INTO employees
VALUES ( 180
       , 'Winston'
       , 'Taylor'
       , 'WTAYLOR'
       , '650.507.9876'
       , STR_TO_DATE('24-JAN-95', '%d-%b-%y')
       , 'SH_CLERK'
       , 3200
       , NULL
       , 120
       , 50);

-- 
INSERT INTO employees
VALUES ( 181
       , 'Jean'
       , 'Fleaur'
       , 'JFLEAUR'
       , '650.507.9877'
       , STR_TO_DATE('23-FEB-98', '%d-%b-%y')
       , 'SH_CLERK'
       , 3100
       , NULL
       , 120
       , 50);

-- 
INSERT INTO employees
VALUES ( 182
       , 'Martha'
       , 'Sullivan'
       , 'MSULLIVA'
       , '650.507.9878'
       , STR_TO_DATE('21-JUN-99', '%d-%b-%y')
       , 'SH_CLERK'
       , 2500
       , NULL
       , 120
       , 50);

-- 
INSERT INTO employees
VALUES ( 183
       , 'Girard'
       , 'Geoni'
       , 'GGEONI'
       , '650.507.9879'
       , STR_TO_DATE('03-FEB-10', '%d-%b-%y')
       , 'SH_CLERK'
       , 2800
       , NULL
       , 120
       , 50);

-- 
INSERT INTO employees
VALUES ( 184
       , 'Nandita'
       , 'Sarchand'
       , 'NSARCHAN'
       , '650.509.1876'
       , STR_TO_DATE('27-JAN-96', '%d-%b-%y')
       , 'SH_CLERK'
       , 4200
       , NULL
       , 121
       , 50);

-- 
INSERT INTO employees
VALUES ( 185
       , 'Alexis'
       , 'Bull'
       , 'ABULL'
       , '650.509.2876'
       , STR_TO_DATE('20-FEB-97', '%d-%b-%y')
       , 'SH_CLERK'
       , 4100
       , NULL
       , 121
       , 50);

-- 
INSERT INTO employees
VALUES ( 186
       , 'Julia'
       , 'Dellinger'
       , 'JDELLING'
       , '650.509.3876'
       , STR_TO_DATE('24-JUN-08', '%d-%b-%y')
       , 'SH_CLERK'
       , 3400
       , NULL
       , 121
       , 50);

-- 
INSERT INTO employees
VALUES ( 187
       , 'Anthony'
       , 'Cabrio'
       , 'ACABRIO'
       , '650.509.4876'
       , STR_TO_DATE('07-FEB-09', '%d-%b-%y')
       , 'SH_CLERK'
       , 3000
       , NULL
       , 121
       , 50);

-- 
INSERT INTO employees
VALUES ( 188
       , 'Kelly'
       , 'Chung'
       , 'KCHUNG'
       , '650.505.1876'
       , STR_TO_DATE('14-JUN-07', '%d-%b-%y')
       , 'SH_CLERK'
       , 3800
       , NULL
       , 122
       , 50);

-- 
INSERT INTO employees
VALUES ( 189
       , 'Jennifer'
       , 'Dilly'
       , 'JDILLY'
       , '650.505.2876'
       , STR_TO_DATE('13-AUG-07', '%d-%b-%y')
       , 'SH_CLERK'
       , 3600
       , NULL
       , 122
       , 50);

-- 
INSERT INTO employees
VALUES ( 190
       , 'Timothy'
       , 'Gates'
       , 'TGATES'
       , '650.505.3876'
       , STR_TO_DATE('11-JUL-08', '%d-%b-%y')
       , 'SH_CLERK'
       , 2900
       , NULL
       , 122
       , 50);

-- 
INSERT INTO employees
VALUES ( 191
       , 'Randall'
       , 'Perkins'
       , 'RPERKINS'
       , '650.505.4876'
       , STR_TO_DATE('19-DEC-09', '%d-%b-%y')
       , 'SH_CLERK'
       , 2500
       , NULL
       , 122
       , 50);

-- 
INSERT INTO employees
VALUES ( 192
       , 'Sarah'
       , 'Bell'
       , 'SBELL'
       , '650.501.1876'
       , STR_TO_DATE('04-FEB-06', '%d-%b-%y')
       , 'SH_CLERK'
       , 4000
       , NULL
       , 123
       , 50);

-- 
INSERT INTO employees
VALUES ( 193
       , 'Britney'
       , 'Everett'
       , 'BEVERETT'
       , '650.501.2876'
       , STR_TO_DATE('03-MAR-07', '%d-%b-%y')
       , 'SH_CLERK'
       , 3900
       , NULL
       , 123
       , 50);

-- 
INSERT INTO employees
VALUES ( 194
       , 'Samuel'
       , 'McCain'
       , 'SMCCAIN'
       , '650.501.3876'
       , STR_TO_DATE('01-JUL-08', '%d-%b-%y')
       , 'SH_CLERK'
       , 3200
       , NULL
       , 123
       , 50);

-- 
INSERT INTO employees
VALUES ( 195
       , 'Vance'
       , 'Jones'
       , 'VJONES'
       , '650.501.4876'
       , STR_TO_DATE('17-MAR-19', '%d-%b-%y')
       , 'SH_CLERK'
       , 2800
       , NULL
       , 123
       , 50);

-- 
INSERT INTO employees
VALUES ( 196
       , 'Alana'
       , 'Walsh'
       , 'AWALSH'
       , '650.507.9811'
       , STR_TO_DATE('24-APR-08', '%d-%b-%y')
       , 'SH_CLERK'
       , 3100
       , NULL
       , 124
       , 50);

-- 
INSERT INTO employees
VALUES ( 197
       , 'Kevin'
       , 'Feeney'
       , 'KFEENEY'
       , '650.507.9822'
       , STR_TO_DATE('23-MAY-08', '%d-%b-%y')
       , 'SH_CLERK'
       , 3000
       , NULL
       , 124
       , 50);

-- 
INSERT INTO employees
VALUES ( 198
       , 'Donald'
       , 'O''Connell'
       , 'DOCONNEL'
       , '650.507.9833'
       , STR_TO_DATE('21-JUN-09', '%d-%b-%y')
       , 'SH_CLERK'
       , 2600
       , NULL
       , 124
       , 50);

-- 
INSERT INTO employees
VALUES ( 199
       , 'Douglas'
       , 'Grant'
       , 'DGRANT'
       , '650.507.9844'
       , STR_TO_DATE('13-JAN-00', '%d-%b-%y')
       , 'SH_CLERK'
       , 2600
       , NULL
       , 124
       , 50);

-- 
INSERT INTO employees
VALUES ( 200
       , 'Jennifer'
       , 'Whalen'
       , 'JWHALEN'
       , '515.123.4444'
       , STR_TO_DATE('01-JUL-04', '%d-%b-%y')
       , 'AD_ASST'
       , 4400
       , NULL
       , 101
       , 10);

-- 
INSERT INTO employees
VALUES ( 201
       , 'Michael'
       , 'Hartstein'
       , 'MHARTSTE'
       , '515.123.5555'
       , STR_TO_DATE('17-FEB-14', '%d-%b-%y')
       , 'MK_MAN'
       , 13000
       , NULL
       , 100
       , 20);

-- 
INSERT INTO employees
VALUES ( 202
       , 'Pat'
       , 'Fay'
       , 'PFAY'
       , '603.123.6666'
       , STR_TO_DATE('17-AUG-07', '%d-%b-%y')
       , 'MK_REP'
       , 6000
       , NULL
       , 201
       , 20);

-- 
INSERT INTO employees
VALUES ( 203
       , 'Susan'
       , 'Mavris'
       , 'SMAVRIS'
       , '515.123.7777'
       , STR_TO_DATE('07-JUN-11', '%d-%b-%y')
       , 'HR_REP'
       , 6500
       , NULL
       , 101
       , 40);

-- 
INSERT INTO employees
VALUES ( 204
       , 'Hermann'
       , 'Baer'
       , 'HBAER'
       , '515.123.8888'
       , STR_TO_DATE('07-JUN-04', '%d-%b-%y')
       , 'PR_REP'
       , 10000
       , NULL
       , 101
       , 70);

-- 
INSERT INTO employees
VALUES ( 205
       , 'Shelley'
       , 'Higgins'
       , 'SHIGGINS'
       , '515.123.8080'
       , STR_TO_DATE('07-JUN-12', '%d-%b-%y')
       , 'AC_MGR'
       , 12000
       , NULL
       , 101
       , 110);

-- 
INSERT INTO employees
VALUES ( 206
       , 'William'
       , 'Gietz'
       , 'WGIETZ'
       , '515.123.8181'
       , STR_TO_DATE('07-JUN-04', '%d-%b-%y')
       , 'AC_ACCOUNT'
       , 8300
       , NULL
       , 205
       , 110);

--   *********************************************
-- Add a foreign key to departments table. Manager_id in departments table must be an employee.
		
        ALTER TABLE departments 
			ADD CONSTRAINT dept_emp_mgr_id_fk FOREIGN KEY (manager_id) REFERENCES employees(employee_id);

-- Insert data into the CONSULTANT table

-- 
INSERT INTO consultants
VALUES ( 1
       , 'Ron'
       , 'Soltani'
       , 'RSOLTANI'
       , '515.321.1234'
       , STR_TO_DATE('17-MAR-20', '%d-%b-%y')
       , 'SA_REP'
       , 8300
       , .35
       , 149
       , 80);

-- 
INSERT INTO consultants
VALUES ( 2
       , 'Eric'
       , 'Siglin'
       , 'ESIGLIN'
       , '515.321.2345'
       , STR_TO_DATE('11-JUL-20', '%d-%b-%y')
       , 'SA_REP'
       , 8300
       , .30
       , 149
       , 80);

-- 
INSERT INTO consultants
VALUES ( 3
       , 'Joe'
       , 'Roch'
       , 'JROCH'
       , '515.321.3456'
       , STR_TO_DATE('10-AUG-20', '%d-%b-%y')
       , 'SA_REP'
       , 8300
       , .30
       , 149
       , 80);

-- 
INSERT INTO consultants
VALUES ( 4
       , 'Sean'
       , 'Kim'
       , 'SKIM'
       , '515.321.4567'
       , STR_TO_DATE('11-AUG-20', '%d-%b-%y')
       , 'SA_REP'
       , 8300
       , .30
       , 149
       , 80);

-- 
INSERT INTO consultants
VALUES ( 5
       , 'Tim'
       , 'LeBlanc'
       , 'TLEBLANC'
       , '515.321.5678'
       , STR_TO_DATE('07-AUG-21', '%d-%b-%y')
       , 'SA_REP'
       , 8300
       , .25
       , 148
       , 80);

-- 
INSERT INTO consultants
VALUES ( 6
       , 'Tammy'
       , 'McCullough'
       , 'TMCCULLOUGH'
       , '515.321.6789'
       , STR_TO_DATE('07-AUG-21', '%d-%b-%y')
       , 'SA_REP'
       , 8300
       , .25
       , 148
       , 80);

-- 
INSERT INTO consultants
VALUES ( 7
       , 'Nancy'
       , 'Greenberg'
       , 'NGREENBE'
       , '515.321.7890'
       , STR_TO_DATE('07-AUG-21', '%d-%b-%y')
       , 'SA_REP'
       , 8300
       , .25
       , 147
       , 80);

-- 
INSERT INTO consultants
VALUES ( 8
       , 'Gerry'
       , 'Jurrens'
       , 'JGURRENS'
       , '515.321.8901'
       , STR_TO_DATE('11-AUG-21', '%d-%b-%y')
       , 'SA_REP'
       , 9000
       , .40
       , 146
       , 80);

-- 
INSERT INTO consultants
VALUES ( 9
       , 'Steve'
       , 'Jones'
       , 'SJONES'
       , '515.321.9012'
       , STR_TO_DATE('15-SEP-21', '%d-%b-%y')
       , 'SA_REP'
       , 9000
       , .40
       , 146
       , 80);

-- 
INSERT INTO consultants
VALUES ( 10
       , 'Wayne'
       , 'Abbott'
       , 'WABBOTT'
       , '515.321.0123'
       , STR_TO_DATE('15-SEP-21', '%d-%b-%y')
       , 'SA_REP'
       , 9000
       , .40
       , 146
       , 80);

-- 
INSERT INTO consultants
VALUES ( 11
       , 'Vance'
       , 'Jones'
       , 'VJONES'
       , '515.421.1234'
       , STR_TO_DATE('04-APR-22', '%d-%b-%y')
       , 'SA_REP'
       , 9500
       , .45
       , 145
       , 80);

-- 
INSERT INTO consultants
VALUES ( 12
       , 'Bill'
       , 'Mayers'
       , 'BMAYERS'
       , '515.421.2345'
       , STR_TO_DATE('02-MAY-22', '%d-%b-%y')
       , 'SA_REP'
       , 8300
       , .25
       , 149
       , 80);

-- 
INSERT INTO consultants
VALUES ( 13
       , 'Bob'
       , 'Witty'
       , 'BWITTY'
       , '515.421.3456'
       , STR_TO_DATE('01-JUN-22', '%d-%b-%y')
       , 'SA_REP'
       , 8300
       , .25
       , 148
       , 80);

-- 
INSERT INTO consultants
VALUES ( 14
       , 'Michael'
       , 'Thum'
       , 'MTHUM'
       , '515.421.4567'
       , STR_TO_DATE('07-AUG-22', '%d-%b-%y')
       , 'SA_REP'
       , 8300
       , .25
       , 147
       , 80);

-- 
INSERT INTO consultants
VALUES ( 15
       , 'James'
       , 'Little'
       , 'JLITTLE'
       , '515.421.5678'
       , STR_TO_DATE('07-AUG-22', '%d-%b-%y')
       , 'SA_REP'
       , 8500
       , .30
       , 146
       , 80);

-- 
INSERT INTO consultants
VALUES ( 16
       , 'Angie'
       , 'Seydel'
       , 'ASEYDEL'
       , '515.421.6789'
       , STR_TO_DATE('11-AUG-22', '%d-%b-%y')
       , 'SA_REP'
       , 8500
       , .25
       , 146
       , 80);

-- 
INSERT INTO consultants
VALUES ( 17
       , 'Heidi'
       , 'Taylor'
       , 'HTAYLOR'
       , '515.421.7890'
       , DATE_ADD(CURDATE(), INTERVAL -6 DAY) 
       , 'SA_REP'
       , 8700
       , .35
       , 147
       , 80);

--  Insert data into the JOB_HISTORY table

INSERT INTO job_history
VALUES ( 102
       , STR_TO_DATE('13-JAN-03', '%d-%b-%y')
       , STR_TO_DATE('24-JUL-08', '%d-%b-%y')
       , 'IT_PROG'
       , 60);

INSERT INTO job_history
VALUES ( 101
       , STR_TO_DATE('28-OCT-03', '%d-%b-%y')
       , STR_TO_DATE('27-SEP-04', '%d-%b-%y')
       , 'AC_ACCOUNT'
       , 110);

-- 
INSERT INTO job_history
VALUES ( 101
       , STR_TO_DATE('28-SEP-04', '%d-%b-%y')
       , STR_TO_DATE('15-MAR-07', '%d-%b-%y')
       , 'AC_MGR'
       , 110);

-- 
INSERT INTO job_history
VALUES ( 201
       , STR_TO_DATE('17-FEB-14', '%d-%b-%y')
       , STR_TO_DATE('19-DEC-19', '%d-%b-%y')
       , 'MK_REP'
       , 20);

-- 
INSERT INTO job_history
VALUES ( 114
       , STR_TO_DATE('24-MAR-08', '%d-%b-%y')
       , STR_TO_DATE('31-DEC-09', '%d-%b-%y')
       , 'ST_CLERK'
       , 50);

-- 
INSERT INTO job_history
VALUES ( 122
       , STR_TO_DATE('01-JAN-09', '%d-%b-%y')
       , STR_TO_DATE('31-DEC-09', '%d-%b-%y')
       , 'ST_CLERK'
       , 50);

-- 
INSERT INTO job_history
VALUES ( 200
       , STR_TO_DATE('01-JUL-04', '%d-%b-%y')
       , STR_TO_DATE('31-DEC-08', '%d-%b-%y')
       , 'AD_ASST'
       , 90);

-- 
INSERT INTO job_history
VALUES ( 176
       , STR_TO_DATE('24-MAR-08', '%d-%b-%y')
       , STR_TO_DATE('31-DEC-08', '%d-%b-%y')
       , 'SA_REP'
       , 80);

-- 
INSERT INTO job_history
VALUES ( 176
       , STR_TO_DATE('01-JAN-09', '%d-%b-%y')
       , STR_TO_DATE('31-DEC-09', '%d-%b-%y')
       , 'SA_MAN'
       , 80);

-- 
INSERT INTO job_history
VALUES ( 200
       , STR_TO_DATE('01-JAN-09', '%d-%b-%y')
       , STR_TO_DATE('20-JUL-15', '%d-%b-%y')
       , 'AC_ACCOUNT'
       , 90);

--  Insert data into the SAL_GRADES table

INSERT INTO sal_grades
VALUES ( 'A'
       , 1000
       , 2999);

-- 
INSERT INTO sal_grades
VALUES ( 'B'
       , 3000
       , 5999);

-- 
INSERT INTO sal_grades
VALUES ( 'C'
       , 6000
       , 9999);

-- 
INSERT INTO sal_grades
VALUES ( 'D'
       , 10000
       , 14999);

-- 
INSERT INTO sal_grades
VALUES ( 'E'
       , 15000
       , 24999);

-- 
INSERT INTO sal_grades
VALUES ( 'F'
       , 25000
       , 40000);


/*   statement trigger to allow dmls during business hours:
         CREATE OR REPLACE PROCEDURE secure_dml
         IS
         BEGIN
   IF TO_CHAR (SYSDATE, 'HH24:MI') NOT BETWEEN '08:00' AND '18:00'
   OR TO_CHAR (SYSDATE, 'DY') IN ('SAT', 'SUN') THEN
   RAISE_APPLICATION_ERROR (-20205,
   'You may only make changes during normal office hours');
   END IF;
   END secure_dml;
   /

CREATE OR REPLACE TRIGGER secure_employees
   BEFORE INSERT OR UPDATE OR DELETE ON employees
   BEGIN
   secure_dml;
   END secure_employees;
   /

ALTER TRIGGER secure_employees DISABLE;
*/
--  *********************************************************
--  a row to the JOB_HISTORY table and row trigger
--  dure when data is updated in the job_id or
--  umns in the EMPLOYEES table:
/*  E PROCEDURE add_job_history
   ( p_emp_id job_history.employee_id%type
   , p_start_date job_history.start_date%type
   , p_end_date job_history.end_date%type
   , p_job_id job_history.job_id%type
   , p_department_id job_history.department_id%type
   )
   IS
   BEGIN
   INSERT INTO job_history (employee_id, start_date, end_date,
   job_id, department_id)
   VALUES(p_emp_id, p_start_date, p_end_date, p_job_id, p_department_id);
   END add_job_history;
   /

CREATE OR REPLACE TRIGGER update_job_history
   AFTER UPDATE OF job_id, department_id ON employees
   FOR EACH ROW
   BEGIN
   add_job_history(:old.employee_id, :old.hire_date, sysdate,
   :old.job_id, :old.department_id);
   END;
   /
*/

ALTER TABLE regions
    COMMENT 'Regions table that contains region numbers and names. Contains 4 rows; references with the Countries table.';

ALTER TABLE locations
    COMMENT 'Locations table that contains specific address of a specific office,
         warehouse, and/or production site of a company. Does not store addresses /
         locations of customers. Contains 23 rows; references with the
         departments and countries tables. ';

--  ****************************

ALTER TABLE departments
    COMMENT 'Departments table that shows details of departments where employees
         work. Contains 27 rows; references with locations, employees, and job_history tables.';

--  ****************************

ALTER TABLE job_history
    COMMENT 'Table that stores job history of the employees. If an employee
         changes departments within the job or changes jobs within the department,
         new rows get inserted into this table with old job information of the
         employee. Contains a complex primary key: employee_id+start_date.
         Contains 25 rows. References with jobs, employees, and departments tables.';

--  ****************************

ALTER TABLE countries
    COMMENT 'country table. Contains 25 rows. References with locations table.';

--  ****************************

ALTER TABLE jobs
    COMMENT 'jobs table with job titles and salary ranges. Contains 19 rows.
         References with employees and job_history table.';

--  ****************************

ALTER TABLE employees
    COMMENT 'employees table. Contains 107 rows. References with departments,
         jobs, job_history tables. Contains a self reference.';

--  ****************************

ALTER TABLE consultants
    COMMENT 'consultants table. Contains 16 rows. References with departments,
         jobs, employees tables.';

ALTER TABLE sal_grades
    COMMENT 'sal_grades table. Contains 6 rows. 
			Does not reference any other table.';


--  **************************************************
--  Create customers and sales tables 
--  Sales table provides transactional data
--  MYSQL uses auto_increment clause rather than identity clause, used by Oracle and SQL Server.
CREATE TABLE customers
(
    cust_id             INT AUTO_INCREMENT PRIMARY KEY,
    cust_email          VARCHAR(30) NOT NULL UNIQUE,
    cust_fname          VARCHAR(20) NOT NULL,
    cust_lname          VARCHAR(20) NOT NULL,
    cust_address        VARCHAR(50) NOT NULL,
    cust_city           VARCHAR(50) NOT NULL,
    cust_state_province CHAR(2),
    cust_postal_code    VARCHAR(20) NOT NULL,
    cust_country        VARCHAR(20) NOT NULL,
    cust_phone          VARCHAR(20) NOT NULL,
    cust_credit_limit   DECIMAL(11, 2) DEFAULT 1000
        CHECK (cust_credit_limit > 0)
);

-- 
CREATE Table sales
(
    sales_id        INT AUTO_INCREMENT,
    sales_timestamp DATETIME NOT NULL,
    sales_amt       DECIMAL(8,2) NOT NULL,
    sales_cust_id   INT,
    sales_rep_id    INT,
    CONSTRAINT sales_id_pk 		PRIMARY KEY (sales_id),
    CONSTRAINT sales_cust_id_fk FOREIGN KEY (sales_cust_id) REFERENCES customers (cust_id),
    CONSTRAINT sales_rep_id_fk 	FOREIGN KEY (sales_rep_id)   REFERENCES employees (employee_id)
);

--   Insert data into the customers table

INSERT INTO CUSTOMERS(cust_email, cust_fname, cust_lname, cust_address, cust_city,
                      cust_state_province, cust_postal_code, cust_country, cust_phone, cust_credit_limit)
VALUES ('bjayne@shu.edu', 'Bill', 'Jayne', '52 Main St.', 'Madison', 'NJ', '07940',
        'US', '+1 973 555 1212', 2000.99);
-- 
INSERT INTO CUSTOMERS(cust_email, cust_fname, cust_lname, cust_address, cust_city,
                      cust_state_province, cust_postal_code, cust_country, cust_phone, cust_credit_limit)
VALUES ('aoconnell@aol.com', 'Audrey', 'O''Connell', '15 W. Park St.', 'Butte', 'MT', '57911',
        'US', '+1 406 555 1212', 2200);
-- 
INSERT INTO CUSTOMERS(cust_email, cust_fname, cust_lname, cust_address, cust_city,
                      cust_postal_code, cust_country, cust_phone, cust_credit_limit)
VALUES ('efrey@vodafone.net', 'Evelyn', 'Frey', '17 Brooksby St.', 'London', 'N1 1HE',
        'GB', '+44 020 755 1212', 1800.50);
-- 
INSERT INTO CUSTOMERS(cust_email, cust_fname, cust_lname, cust_address, cust_city,
                      cust_state_province, cust_postal_code, cust_country, cust_phone, cust_credit_limit)
VALUES ('dtone@abc.com', 'Deborah', 'Tone', '234 Beverley St.', 'Winnipeg', 'MB', 'R3G 1T6',
        'CA', '+1 204 555 1212', 2000);
-- 
INSERT INTO CUSTOMERS(cust_email, cust_fname, cust_lname, cust_address, cust_city,
                      cust_state_province, cust_postal_code, cust_country, cust_phone, cust_credit_limit)
VALUES ('fterziotti@alitalia.com', 'Fabio', 'Terziotti', '72 Via Belviglieri', 'Verona', 'VR', '37131',
        'IT', '+39 045 555 1212', 5000);
-- 
INSERT INTO CUSTOMERS(cust_email, cust_fname, cust_lname, cust_address, cust_city,
                      cust_postal_code, cust_country, cust_phone, cust_credit_limit)
VALUES ('eikeloa@bluebird.net', 'Emanuel', 'Ikeloa', '745 Agbe Rd.', 'Lagos', '100212',
        'NG', '+234 1 555 1212', 2300);
-- 
INSERT INTO CUSTOMERS(cust_email, cust_fname, cust_lname, cust_address, cust_city,
                      cust_postal_code, cust_country, cust_phone, cust_credit_limit)
VALUES ('chenliu@bochk.com', 'Chen', 'Liu', '39 Dai Shing St.', 'Wan Chai',
        '000000', 'HK', '+852 2555 1212', 2300);    -- Hong Kong does not use postal codes
-- 
INSERT INTO CUSTOMERS(cust_email, cust_fname, cust_lname, cust_address, cust_city,
                      cust_state_province, cust_postal_code, cust_country, cust_phone, cust_credit_limit)
VALUES ('alotero@caracol.co', 'Andres', 'Lotero', '405 Carrera 93', 'Bogota', 'DC', '110721',
        'CO', '+57 300 794 5529', 3600);
-- 
INSERT INTO CUSTOMERS(cust_email, cust_fname, cust_lname, cust_address, cust_city,
                      cust_postal_code, cust_country, cust_phone, cust_credit_limit)
VALUES ('gong.li@uobgroup.com', 'Gong', 'Li', '42 Cambridge Rd.', 'Cambridge', '210042',
        'SG', '+65 973 555 1212', 3500);
 
INSERT INTO CUSTOMERS(cust_email, cust_fname, cust_lname, cust_address, cust_city,
                      cust_state_province, cust_postal_code, cust_country, cust_phone, cust_credit_limit)
VALUES ('sganesan@abanoffshore.com', 'Shivaji', 'Ganesan', '15 Adithanar Rd.', 'Chennai', 'TN', '600 018',
        'IN', '+91 406 555 1212', 2800);

--   -----------------------------------------------------------------------
--  Insert data into the sales table:


INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
	VALUES (LOCALTIMESTAMP - interval '936' hour, 483.92, 1, 176);

INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
	VALUES (LOCALTIMESTAMP - interval '865' hour, 123.45, 4, 150);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
	VALUES (LOCALTIMESTAMP - interval '71' hour, 9374.61, 6, 163);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
	VALUES (LOCALTIMESTAMP - interval '746' hour, 492.58, 5, 176);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
	VALUES (LOCALTIMESTAMP - interval '39' hour, 104.93, 9, 164);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '840' hour, 808.73, 8, 174);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '583' hour, 938.65, 7, 170);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '291' hour, 274.98, 8, 176);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '82' hour, 2483.76, 5, 152);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '394' hour, 364.92, NULL, 155);

-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '485' hour, 33.13, 5, 168);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '36' hour, 789.34, 9, 156);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '19' hour, 4958.36, 6, 170);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '26' hour, 6028.38, 8, 153);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '623' hour, 9024.30, 4, 178);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '698' hour, 430.73, 5, 159);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '294' hour, 329.65, 7, 163);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '409' hour, 215.98, 2, 158);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '836' hour, 364.92, NULL, 161);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '51' hour, 293.76, 1, 174);

-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '45' hour, 343.13, 8, 160);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '376' hour, 8279.16, 4, 162);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '194' hour, 5829.47, 6, 151);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '260' hour, 9375.27, 1, 154);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '23' hour, 914.30, 2, 168);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '98' hour, 573.82, 9, 166);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '148' hour, 827.51, NULL, 167);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '29' hour, 319.65, 5, 169);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '49' hour, 695.89, 8, 164);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '534' hour, 259.76, 5, 157);

-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '295' hour, 463.31, 8, 152);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '626' hour, 7589.34, 1, 165);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '54' hour, 3052.42, 8, 151);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '391' hour, 381.53, NULL, 173);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '22' hour, 6828.38, 5, 154);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '271' hour, 739.51, 7, 171);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '918' hour, 590.73, 4, 177);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '38' hour, 639.27, 5, 175);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '41' hour, 965.98, 2, 152);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '729' hour, 6274.22, NULL, 179);

-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '92' hour, 769.52, 4, 172);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '72' hour, 5293.46, 8, 150);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '825' hour, 4385.97, 4, 175);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '92' hour, 2937.28, 5, 152);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '17' hour, 3948.88, 5, 159);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '647' hour, 992.45, 7, 160);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '175' hour, 917.38, 8, 157);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '28' hour, 723.85, 2, 159);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '73' hour, 826.42, 8, 156);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '412' hour, 259.34, 4, 172);

-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '49' hour, 3781.39, 8, 174);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '39' hour, 4381.39, 8, 170);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '389' hour, 2367.82, 9, 170);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '47' hour, 4211.39, 1, 177);
-- 
INSERT INTO sales(sales_timestamp, sales_amt, sales_cust_id, sales_rep_id)
VALUES (LOCALTIMESTAMP - interval '463' hour, 611.31, 6, 158);

COMMIT;
