pip install --upgrade pip
pip install --user awscli
export PATH=$PATH:$HOME/.local/bin

add-apt-repository ppa:eugenesan/ppa
apt-get update
apt-get install jq -y

curl https://raw.githubusercontent.com/silinternational/ecs-deploy/master/ecs-deploy | sudo tee -a /usr/bin/ecs-deploy
sudo chmod +x /usr/bin/ecs-deploy

# Use this for AWS ECR
# eval $(aws ecr get-login --region us-east-1)

ecs-deploy -c bonmunch-cluster -n bonmunch-service -i prithvi425/restaurant:latest