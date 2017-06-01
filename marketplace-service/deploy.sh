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
echo " Deploying $CURRENT_DIR"
./gradlew clean build
echo "Pushing To PCF"
echo "Manifest for deployment: "
echo " "
writeManifest $CURRENT_DIR/manifest.yml
cf push
echo " "