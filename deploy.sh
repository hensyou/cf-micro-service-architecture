#!/bin/sh
CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#Write out the manifest in user friendly format
writeManifest() {
  while read line
  do
      printf "%s\n" "$line"
  done < "$1"
}

echo ' _______    ______   ________        _______                                    ';
echo '/       \  /      \ /        |      /       \                                   ';
echo '$$$$$$$  |/$$$$$$  |$$$$$$$$/       $$$$$$$  |  ______   _____  ____    ______  ';
echo '$$ |__$$ |$$ |  $$/ $$ |__          $$ |  $$ | /      \ /     \/    \  /      \ ';
echo '$$    $$/ $$ |      $$    |         $$ |  $$ |/$$$$$$  |$$$$$$ $$$$  |/$$$$$$  |';
echo '$$$$$$$/  $$ |   __ $$$$$/          $$ |  $$ |$$    $$ |$$ | $$ | $$ |$$ |  $$ |';
echo '$$ |      $$ \__/  |$$ |            $$ |__$$ |$$$$$$$$/ $$ | $$ | $$ |$$ \__$$ |';
echo '$$ |      $$    $$/ $$ |            $$    $$/ $$       |$$ | $$ | $$ |$$    $$/ ';
echo '$$/        $$$$$$/  $$/             $$$$$$$/   $$$$$$$/ $$/  $$/  $$/  $$$$$$/  ';
echo "                                                                                ";
echo " "
echo "Running: https://api.run.pivotal.io"
echo "This is running PCF on AWS"
echo " "
echo " "
echo "Services To Push:"
echo "1. Authorization Service"
echo "2. Marketplace Service"
echo "3. Order Service"
echo "4. Product Service"
echo " "
echo "Deploying Authorization Server: $CURRENT_DIR/auth-server"
cd $CURRENT_DIR/auth-server
echo "Pushing auth-server"
echo "Manifest for deployment: "
echo " "
writeManifest $CURRENT_DIR/auth-server/manifest.yml
cf push
echo " "
echo "Deploying Marketplace Service: $CURRENT_DIR/marketplace-service"
cd $CURRENT_DIR/marketplace-service
echo "Pushing Marketplace Service"
echo "Manifest for deployment: "
echo " "
writeManifest $CURRENT_DIR/marketplace-service/manifest.yml
echo " "
cf push
echo " "
echo "Deploying Product Service: $CURRENT_DIR/product-service"
cd $CURRENT_DIR/product-service
echo "Pushing Product Service"
echo "Manifest for deployment: "
echo " "
writeManifest $CURRENT_DIR/product-service/manifest.yml
cf push
echo " "
echo "Deploying Order Service: $CURRENT_DIR/order-service"
cd $CURRENT_DIR/order-service
echo "Pushing Order Service"
echo "Manifest for deployment: "
echo " "
writeManifest $CURRENT_DIR/order-service/manifest.yml
cf push
echo " "
