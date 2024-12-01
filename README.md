# instacloneanother

## Run this to transfer the schema to the sql container
docker cp ./schema.sql testsql-web:/schema.sql

# Run this on the testsql-web container after it is build 
mysql -u root -p${MYSQL_ROOT_PASSWORD} -e "CREATE DATABASE test"
mysql -u root -p${MYSQL_ROOT_PASSWORD} -D test < /app/schema.sql