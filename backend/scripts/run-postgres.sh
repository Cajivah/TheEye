#!/bin/zsh
if [ ! "$(docker ps -q -f name=postgres-theeye)" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=postgres-theeye)" ]; then
        # cleanup
        docker rm postgres-theeye
    fi
    # run your container
    docker run -d -p 127.0.0.1:35432:5432 --name postgres-theeye postgres-theeye:latest
fi
