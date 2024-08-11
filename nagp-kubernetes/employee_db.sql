CREATE SCHEMA IF NOT EXISTS employee_db;

USE employee_db;

CREATE TABLE IF NOT EXISTS employee_details (
		id bigint not null auto_increment,
        emp_designation varchar(255),
        emp_id varchar(255),
        emp_joining_date date,
        emp_name varchar(255),
        emp_organisation varchar(255),
        primary key (id),
        unique (emp_id)
);

INSERT INTO employee_details (emp_designation, emp_id, emp_joining_date, emp_name, emp_organisation) VALUES ('Associate Staff Engineer', 'EMP11111', '2022-02-10', 'Neymar D Silva', 'Nagarro');
INSERT INTO employee_details (emp_designation, emp_id, emp_joining_date, emp_name, emp_organisation) VALUES ('Staff Engineer', 'EMP22222', '2022-02-01', 'Lionel Messi', 'Cognizant');
INSERT INTO employee_details (emp_designation, emp_id, emp_joining_date, emp_name, emp_organisation) VALUES ('Associate Staff Engineer', 'EMP33333', '2022-02-02', 'Marquinhos', 'TCS');
INSERT INTO employee_details (emp_designation, emp_id, emp_joining_date, emp_name, emp_organisation) VALUES ('Staff Engineer', 'EMP44444', '2022-02-03', 'Mbappe', 'Nagarro');
INSERT INTO employee_details (emp_designation, emp_id, emp_joining_date, emp_name, emp_organisation) VALUES ('Principal Engineer', 'EMP55555', '2022-02-10', 'Parades', 'CTS');