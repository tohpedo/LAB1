Except for replicate, all functionality should be working properly.

Replicate works but only if the schema has already been created within the new database. 

I added a URI attribute to MysqlConnector -- URI's should be in the format host/databasename with no leading :'s or /'s

Example of an acceptable URI is localhost/helloworld or localhost/todo



Table must be named 'todo'.

USERNAME must be 'root' and PASSWORD must be 'greatsqldb' (hardcoded in source files)




Torbir Dhaliwal
tsdhlwal








