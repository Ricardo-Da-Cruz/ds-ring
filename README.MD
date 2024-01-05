## Projecto de Sistemas Distribuidos

### Rodar o projecto

Para correr o projecto é necessário ter o docker instalado e correr o seguinte comando:

Compilar o projecto

``` mvn clean package ```

Criar a imagem docker

``` docker build -t ring . ```

Rodar o Servidor

``` docker run -d ring java -jar server.jar ```

Rodar os Peers

``` docker run -d ring java -jar peer.jar <ip-prox-peer> <ip-servidor> <true|false tem o token?> ```


