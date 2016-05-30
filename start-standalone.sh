cd /home/spark/bin/spark/sbin
echo "Starting master on port 10080"
./start-master.sh --webui-port 10080
sleep 3s
echo "Starting slave"
./start-slave.sh spark://spark-VirtualBox:7077
sleep 1s
echo "Open Web UI"
if which xdg-open > /dev/null
then
  xdg-open "http://localhost:10080/"
elif which gnome-open > /dev/null
then
  gnome-open "http://localhost:10080/"
fi
