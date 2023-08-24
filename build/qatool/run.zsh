#!/bin/zsh

docker run --rm -v $(pwd):/app -v mvn:/root/.m2 essar/qatool