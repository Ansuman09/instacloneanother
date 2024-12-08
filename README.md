# instacloneanother

##Steps to deploy.
Install docker and docker compose on you machine.

Use the Docker file to create the images as mentioned in the file.

###SQL
for linux: docker build -t testsql-web-img . 

###Backend
for linux: docker build -t instaclone-backend-img .

## Run this to transfer the schema to the sql container
docker cp ./schema.sql testsql-web:/schema.sql

# Run this on the testsql-web container after it is build 
mysql -u root -p${MYSQL_ROOT_PASSWORD} -e "CREATE DATABASE test"
mysql -u root -p${MYSQL_ROOT_PASSWORD} -D test < /app/schema.sql

Next find the ip of the backend contianer checking the docker bridge
docker network ls  --to find the web_dev_bridge

docker instpet bridge <brige name >

##add the ip to the hosts file with hostname my-backend:
my-backend <ip>

# Clone instaclone-frontend repository from my account and deploy
