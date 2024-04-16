directory="/"
configFiles=( $(find $directory -iname "*.conf") )
echo "Number of config files found: " ${#configFiles[@]}
