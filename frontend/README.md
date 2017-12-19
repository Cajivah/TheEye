## TheEye -- aplikacja frontendowa

### Wdrożenie
#### Wymagania
1. `Docker CE` -- instalacja zależna od środowiska, dostępna pod adresem: https://docs.docker.com/engine/installation/. 

####  Uruchomienie lokalnie dla celów testowych
Zakłada się, że użytkownik znajduje się w tym samym katalogu, w którym znajduje się ten plik.
1. `docker build . -t theeye-front-image:latest`
2. `docker run --net host --name theeye-front theeye-front-image:latest`
3. Aplikacja jest dostępna pod adresem `http://localhost:80`

####  Uruchomienie produkcyjne
Zakłada się, że użytkownik znajduje się w tym samym katalogu, w którym znajduje się ten plik.
1. `docker build . -t theeye-front-image:latest`
2. `docker run -p 80:80 --name theeye-front theeye-front-image:latest`
3. Aplikacja jest dostępna pod adresem `http://localhost:80`

### Środowisko deweloperskie
#### Wymagania
1. `npm v5.5.1` i `NodeJS v9.2.0` -- dostępne za pośrednictwem `nvm` https://github.com/creationix/nvm
    1.1 Po zainstalowaniu `nvm` należy wykonać komendę `nvm install 9.2.0` aby zainstalować `NodeJS` i `npm`

#### Uruchomienie
Zakłada sie, że użytkownik jest w tym samym katalogu, w którym znajduje się ten plik.
1. `npm install`
2. `npm start`