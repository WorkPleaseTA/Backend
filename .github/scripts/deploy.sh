#!/bin/bash
set -e

HOST=$EC2_HOST
USER=$EC2_USER
KEY_PATH="key.pem"
APP_DIR="/home/$USER/workmanager"
CONTAINER_NAME="workmanager-container"
IMAGE_NAME="${ECR_REGISTRY_URL}/${ECR_REPOSITORY}:latest"

printf "%s" "$EC2_SSH_KEY" > $KEY_PATH
chmod 600 $KEY_PATH

mkdir -p ~/.ssh
ssh-keyscan -H $HOST >> ~/.ssh/known_hosts

printf "%s" "$ENV_FILE" > .env

ssh -i $KEY_PATH $USER@$HOST "mkdir -p $APP_DIR"

scp -i $KEY_PATH .env $USER@$HOST:$APP_DIR/.env

ssh -i $KEY_PATH $USER@$HOST <<EOF
  set -e

  echo "Login to ECR..."
  aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY_URL}

  echo "Stop old container..."
  docker stop ${CONTAINER_NAME} || true
  docker rm ${CONTAINER_NAME} || true

  echo "Pull latest image..."
  docker pull ${IMAGE_NAME}

  echo "Run new container..."
  docker run -d --name ${CONTAINER_NAME} \
    -p 8080:8080 \
    --restart always \
    --env SPRING_PROFILES_ACTIVE=prod \
    --env-file $APP_DIR/.env \
    ${IMAGE_NAME}

  echo "Prune old images..."
  docker image prune -f
EOF