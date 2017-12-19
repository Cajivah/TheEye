## TheEye -- aplikacja backendowa

### Wdrożenie
#### Wymagania
1. `Docker CE` -- instalacja zależna od środowiska, dostępna pod adresem: https://docs.docker.com/engine/installation/. 

####  Uruchomienie lokalnie dla celów testowych
Zakłada się, że użytkownik znajduje się w tym samym katalogu, w którym znajduje się ten plik.
1. `docker build . -t theeye-back-image:latest` 
1.1. Może zająć około 30 min na średniej klasy laptopach 
2. `docker run --net host --name theeye-back theeye-back-image:latest`
3. Aplikacja jest dostępna pod adresem `http://localhost:8080`

####  Uruchomienie produkcyjne
Zakłada się, że użytkownik znajduje się w tym samym katalogu, w którym znajduje się ten plik.
1. `docker build . -t theeye-front-image:latest`
2. `docker run -p 8080:8080 --name theeye-back theeye-back-image:latest`
3. Aplikacja jest dostępna pod adresem `http://localhost:8080`

### Środowisko deweloperskie
#### Wymagania
1. `JDK 1.8` -- pobranie możliwe ze strony Oracle (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). Instalacja jest zależna od środowiska, instrukcja dostępna na stronie https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html.
2. `Gradle 4.4` -- pliki do pobrania oraz instrukcja dostępne pod adresem https://gradle.org/install/
3. `OpenCV 3.3.1` wraz z zależnościami -- przy kompilacji mogą wystąpić trudności. W projekcie bazowano na poradniku: https://elbauldelprogramador.com/en/compile-opencv-3.2-with-java-intellij-idea/ 
#### Uruchomienie
Zakłada sie, że użytkownik jest w tym samym katalogu, w którym znajduje się ten plik.
1. `gradle bootRun`
#### Testy
1. Po wykonaniu powyższych kroków, uruchomienie testów jest możliwe za pomocą 
    `gradle junitPlatformTest`