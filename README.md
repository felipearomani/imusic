# IMusic

IMusic é um serviço de indicação musical baseado na sua localização.

## Como rodar a aplicação

Para executar os testes e a aplicação é necessário ter registro e obter as credenciais de acesso nos seguintes sites:
- https://openweathermap.org
- https://developer.spotify.com

Outro requisito importante para rodar a aplicação é que as portas "8080" e "6379" não estejam em uso no momento.

#### 1) Clone o projeto
```
git clone https://github.com/felipearomani/imusic.git
```
#### 2) Acesse a pasta do projeto
```
cd imusic
```

#### 3) Preencha as variáveis de ambiente
No arquivo "docker-compose.yaml" na raiz do projeto, na sessão "environments" do serviço "imusic" alteres as seguintes variáveis para seus valores:
- OPEN_WEATHER_APP_ID
- SPOTIFY_CLIENT_ID
- SPOTIFY_CLIENT_SECRET

#### 4) Rode o comando
```
docker-compose up
```
Agora o projeto ficará disponível na porta "8080" do seu localhost.

## Como rodar os testes

#### 1) Clone o projeto 
```
git clone https://github.com/felipearomani/imusic.git
```

#### 2) Acesse a pasta do projeto
```
cd imusic
```

#### 3) Preencha as variáveis de ambiente do container no comando 
Variáveis que precisam ser preenchidas no comando abaixo:

- OPEN_WEATHER_APP_ID
- SPOTIFY_CLIENT_ID
- SPOTIFY_CLIENT_SECRET

Estas variáveis são usadas para executar o testes de integração.


```
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v "$(pwd)":/usr/src/imusic -w /usr/src/imusic -e OPEN_WEATHER_APP_ID=123 -e SPOTIFY_CLIENT_ID=123 -e SPOTIFY_CLIENT_SECRET=123 maven:3-jdk-11 mvn test
```

## Como usar

Endpoints no Postman [Aqui](https://www.getpostman.com/collections/5a481bf0de7571603df6)

```
[GET] /playlists
```
```
Parâmetros URL: 
city = cidade
lat = latitude
lng = longiotude

Deve-se usar a busca por "city" ou "lat" e "lng", os dois juntos não é permitido. 
```

## Respostas

### Sucesso

Status 200 Ok
```json
{
    "city": "Campinas",
    "temperature": 21.05,
    "category": "pop",
    "musics": [
        {
            "name": "Don't Start Now"
        },
        {
            "name": "Blinding Lights"
        },
        {
            "name": "Intentions"
        },
        {
            "name": "No Time To Die"
        },
        {
            "name": "Falling"
        },
        {
            "name": "The Box"
        },
        {
            "name": "Life Is Good (feat. Drake)"
        },
        {
            "name": "You should be sad"
        },
        {
            "name": "My Oh My (feat. DaBaby)"
        },
        {
            "name": "Say So"
        },
        {
            "name": "ROXANNE"
        },
        {
            "name": "everything i wanted"
        },
        {
            "name": "What A Man Gotta Do"
        },
        {
            "name": "Rare"
        },
        {
            "name": "Dance Monkey"
        },
        {
            "name": "Physical"
        },
        {
            "name": "Know Your Worth"
        },
        {
            "name": "Yummy"
        },
        {
            "name": "Adore You"
        },
        {
            "name": "What If I Told You That I Love You"
        },
        {
            "name": "Before You Go"
        },
        {
            "name": "Only The Young - Featured in Miss Americana"
        },
        {
            "name": "No Shame"
        },
        {
            "name": "me & ur ghost"
        },
        {
            "name": "Godzilla (feat. Juice WRLD)"
        },
        {
            "name": "Circles"
        },
        {
            "name": "Never Seen The Rain"
        },
        {
            "name": "No Judgement"
        },
        {
            "name": "Lose Control"
        },
        {
            "name": "SUGAR"
        },
        {
            "name": "Blueberry Faygo"
        },
        {
            "name": "Sunday Best"
        },
        {
            "name": "Birthday"
        },
        {
            "name": "Forever Yours - Avicii Tribute"
        },
        {
            "name": "High Fashion (feat. Mustard)"
        },
        {
            "name": "stupid"
        },
        {
            "name": "Roses - Imanbek Remix"
        },
        {
            "name": "Run"
        },
        {
            "name": "If The World Was Ending (feat. Julia Michaels)"
        },
        {
            "name": "Maniac"
        },
        {
            "name": "Alone, Pt. II"
        },
        {
            "name": "Tusa"
        },
        {
            "name": "Good News"
        },
        {
            "name": "Lose You To Love Me"
        },
        {
            "name": "Suicidal"
        },
        {
            "name": "Yellow Hearts"
        },
        {
            "name": "July"
        },
        {
            "name": "Own It (feat. Ed Sheeran & Burna Boy)"
        },
        {
            "name": "Woah"
        },
        {
            "name": "RITMO (Bad Boys For Life)"
        }
    ]
}
```

### Cidade desconhecida
Status 404 Not Found
```json
{
    "error": "City Gotham City not found"
}
```

### Erro de consulta
O erro ocorre ao tentar fazer uma consulta por cidade e pontos de geolocalização ao mesmo tempo, ou enviar latitude e longitude inválidos.

Status 400 Bad Request
```json
{
    "error": "You can't specify city and lat/lng together, choose one"
}
```