####################################
### User Story 1
####################################
SELECT 
    e.first_name,
    e.last_name,
    e.salary,
    e.job_id,
    d.department_name,
    l.city,
    c.country_name
FROM
    employees e
        LEFT JOIN
    departments d ON e.department_id = d.department_id
        LEFT JOIN
    locations l ON d.location_id = l.location_id
        LEFT JOIN
    countries c ON l.country_id = c.country_id
WHERE
    e.salary = (SELECT 
            MAX(salary)
        FROM
            employees);
####################################
### User Story 2
####################################
SELECT 
    first_name, last_name, job_id, salary, manager_id
FROM
    employees
WHERE
    email IN (SELECT 
            email
        FROM
            consultants)
ORDER BY last_name;
####################################
### User Story 3
####################################
SELECT 
    c.cust_id,
    c.cust_fname,
    c.cust_lname,
    c.cust_city,
    COALESCE(MAX(s.sales_amt), 0) AS largest_sale,
    COALESCE(SUM(s.sales_amt), 0) AS total_sale,
    ROUND(COALESCE(MAX(s.sales_amt) / NULLIF(SUM(s.sales_amt), 0) * 100,
                    0),
            2) AS largest_sale_pct_of_total,
    COALESCE(ROUND(AVG(s.sales_amt), 2), 0) AS avg_sale,
    COUNT(s.sales_amt) AS sale_count
FROM
    customers c
        LEFT JOIN
    sales s ON c.cust_id = s.sales_cust_id
GROUP BY c.cust_id , c.cust_fname , c.cust_lname , c.cust_city
ORDER BY c.cust_id;
####################################
### User Story 4
####################################
SELECT 
    e.first_name,
    e.last_name,
    d.department_name,
    l.street_address,
    l.city,
    l.state_province
FROM
    departments d
        JOIN
    employees e ON d.manager_id = e.employee_id
        JOIN
    locations l ON d.location_id = l.location_id
ORDER BY d.department_id;
####################################
### User Story 5
####################################
SELECT 
    e.first_name AS employee_first,
    e.last_name AS employee_last,
    e.job_id AS employee_job_id,
    e.salary AS employee_salary,
    m.first_name AS manager_first,
    m.last_name AS manager_last,
    m.job_id AS manager_job_id,
    m.salary AS manager_salary
FROM
    employees e
        JOIN
    employees m ON e.manager_id = m.employee_id
WHERE
    e.salary >= m.salary
ORDER BY m.employee_id;
####################################
### User Story 6
####################################
SELECT DISTINCT
    e.employee_id, e.first_name, e.last_name, e.job_id, e.salary
FROM
    employees e
        JOIN
    job_history j ON e.employee_id = j.employee_id
WHERE
    e.job_id = j.job_id;
####################################
### User Story 7
####################################
SELECT 
    first_name, last_name, job_id, salary
FROM
    employees
WHERE
    employee_id NOT IN (SELECT 
            manager_id
        FROM
            employees
        WHERE
            manager_id IS NOT NULL)
        AND salary > (SELECT 
            MAX(salary)
        FROM
            employees
        WHERE
            employee_id IN (SELECT DISTINCT
                    manager_id
                FROM
                    employees))
ORDER BY salary;
####################################
### User Story 8
####################################
SELECT 
    COALESCE(r.region_name, 'Unassigned') AS assigned_region,
    COUNT(e.employee_id) AS total_employees
FROM
    regions r
        LEFT JOIN
    countries c ON r.region_id = c.region_id
        LEFT JOIN
    locations l ON c.country_id = l.country_id
        LEFT JOIN
    departments d ON l.location_id = d.location_id
        LEFT JOIN
    employees e ON d.department_id = e.department_id
GROUP BY assigned_region 
UNION SELECT 
    'Unassigned', COUNT(employee_id)
FROM
    employees
WHERE
    department_id IS NULL
ORDER BY assigned_region;
####################################
### User Story 9
####################################
/*******************
* SECTION A: PART I
*******************/
START TRANSACTION;
UPDATE employees 
SET 
    department_id = (SELECT 
            department_id
        FROM
            departments
        WHERE
            UPPER(department_name) = 'SALES'),
    first_name = 'Kimberly'
WHERE
    first_name = 'Kimberely'
        AND last_name = 'Grant';
SAVEPOINT after_part1;
/********************
* SECTION A: PART II
********************/
UPDATE employees 
SET 
    salary = (SELECT 
            salary
        FROM
            consultants
        WHERE
            last_name = 'Taylor')
WHERE
    last_name = 'Weiss'
        OR last_name = 'Fripp';
SAVEPOINT after_part2;
/*********************
* SECTION A: PART III
*********************/
UPDATE regions 
SET 
    region_name = 'North America'
WHERE
    region_name = 'Americas';
UPDATE regions 
SET 
    region_name = 'Middle East'
WHERE
    region_name = 'Middle East and Africa';
SAVEPOINT after_part3;
/***********
* SECTION B
***********/
DELETE FROM consultants 
WHERE
    email IN (SELECT 
        email
    FROM
        employees);
SAVEPOINT after_sectionB;
/***********
* SECTION C
***********/
-- regions table does not have primary key set to auto-increment
SET @next_id = (SELECT MAX(region_id) FROM regions);
INSERT INTO regions (region_id, region_name) 
VALUES 
    (@next_id + 1, 'South America'), 
    (@next_id + 2, 'Africa');