FROM nginx

RUN rm /bin/sh && ln -s /bin/bash /bin/sh

RUN apt-get update \
    && apt-get install -y curl git\
    && apt-get -y autoclean

ENV NVM_DIR /usr/local/nvm
ENV NODE_VERSION 9.2.0

RUN curl --silent -o- https://raw.githubusercontent.com/creationix/nvm/v0.31.2/install.sh | bash

RUN source $NVM_DIR/nvm.sh \
    && nvm install $NODE_VERSION \
    && nvm alias default $NODE_VERSION \
    && nvm use default

ENV NODE_PATH $NVM_DIR/v$NODE_VERSION/lib/node_modules
ENV PATH $NVM_DIR/versions/node/v$NODE_VERSION/bin:$PATH

RUN node -v
RUN npm -v

#copy app
RUN mkdir app
WORKDIR app
COPY . .

RUN npm install && npm run build && mv ./build/* /usr/share/nginx/html

COPY default.conf /etc/nginx/conf.d/default.conf
