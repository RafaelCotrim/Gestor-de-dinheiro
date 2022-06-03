# Gestor de dinheiro

## Usar Docker

Para usar o Docker, primeiramente faça package do código e depois execute os seguintes comando a partir da raiz do projeto.

```
docker build -t api-server:latest .
docker run -p 8080:8080 api-server:latest
```

## Usar testes do postman

Clique em “import” no canto superir esquerdo e import o arquivo `postman_collection.json`;

## Conectar ao emulador de Android

1. Encontrar o IP local da sua máquina
2. Substituir o IP que está no aquivo `monneymannagerapp\app\src\main\res\xml\network_security_config.xml` pelo seu IP local
2. Substituir o IP que está na classe `APIClient` pelo seu IP local


