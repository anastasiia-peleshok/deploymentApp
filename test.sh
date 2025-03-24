docker image rm -f deployment-app:latest
docker build -t deployment-app .
docker run -e ENV_TYPE=prod -p 6969:6969 deployment-app