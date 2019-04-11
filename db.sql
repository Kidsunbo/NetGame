CREATE TABLE accounting (
    ID PRIMARY KEY,
    username VARCHAR(25), not null, unique
    password VARCHAR(25),
    phone VARCHAR(15), not null, unique

); 

--CHECK if username exists
SELECT username 
    from accounting 
    where username="";

--VETIFY password by username
select *
  from accounting
 where username = "" 
    and password = ""; 

--VETIFY password by phone
select *
  from accounting
 where phone = "" 
    and password = ""; 

--sign up 
    INSERT INTO accounting (username, password, Phone)
    VALUES ("", "", "");
