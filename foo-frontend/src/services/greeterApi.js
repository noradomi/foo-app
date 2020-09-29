import { GreetServiceClient } from '../grpc/greet_grpc_web_pb.js';
import { Greeting, GreetRequest } from '../grpc/greet_pb.js';
import { getJwtFromStorage } from '../utils/utils.js';

const URL = 'http://' + window.location.hostname + ':8080';

const client = new GreetServiceClient(URL, null, null);

const metadata = { jwt: getJwtFromStorage() };

export default {
	sayHello: (name, callback) => {
		const greeting = new Greeting();
		greeting.setFirstName(name);
		greeting.setLastName(name);

		const greetRequest = new GreetRequest();
		greetRequest.setGreeting(greeting);
		client.greet(greetRequest, metadata, callback);
	}
};
