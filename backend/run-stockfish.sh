#!/bin/zsh
if [ ! "$(docker ps -q -f name=stockfish-theeye)" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=stockfish-theeye)" ]; then
        # cleanup
        docker rm stockfish-theeye
    fi
    # run your container
    docker run -d -p 127.0.0.1:32345:8080 --name stockfish-theeye stockfish-theeye:latest
fi
