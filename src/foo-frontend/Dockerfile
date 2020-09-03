FROM node:12.18

RUN mkdir /usr/src/app

ADD . /usr/src/app

WORKDIR /usr/src/app

COPY package.json /usr/src/app/package.json

RUN npm install --silent

EXPOSE 3000:3000

# start app
CMD ["npm", "start"]