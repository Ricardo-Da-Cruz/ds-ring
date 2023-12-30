mvn package

docker build -t ring .

docker run -d ring java -jar server.jar

docker run -d ring java -jar peer.jar 172.17.0.3 172.17.0.2 true

docker run -d ring java -jar peer.jar 172.17.0.4 172.17.0.2 true

docker run -d ring java -jar peer.jar 172.17.0.5 172.17.0.2 true

docker run -d ring java -jar peer.jar 172.17.0.6 172.17.0.2 true

docker run -d ring java -jar peer.jar 172.17.0.3 172.17.0.2 true

