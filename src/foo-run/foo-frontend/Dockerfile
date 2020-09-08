FROM node:12.18

RUN mkdir /usr/src/app

WORKDIR /usr/src/app

ADD /foo-frontend /usr/src/app/

RUN npm install --silent

EXPOSE 3000:3000

# start app
CMD ["npm", "start"]