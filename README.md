# instacloneproj
# instacloneanother

docker cp ./schema.sql testsql-web:/schema.sql

# Run this on the container after it is build
mysql -u root -p${MYSQL_ROOT_PASSWORD} -e "CREATE DATABASE test"
mysql -u root -p${MYSQL_ROOT_PASSWORD} -D test < /app/schema.sql