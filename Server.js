/*******/
/*
/* DRONE
/*
/******/

var dirLog = "./var/logs/drone";
var SERVER_PORT = 1337;

var net = require('net');
var sockets = [];

function closeSocket(socket) {
	var i = sockets.indexOf(socket);
	if (i != -1) {
		sockets.splice(i, 1);
	}
}

function CallbackSocket(socket) {
	socket.on('data', function(data) {
		receiveData(data);
	});
	socket.on('end', function() {
		console.log('socket closed by user');
		closeSocket(socket);
	});
}

function receiveData(paquet) {
	//console.log(paquet);
	paquet = paquet.toString("hex");
	//console.log(paquet);
	var paquetDecode = {
		token : paquet.slice(0,8),
		opcode : paquet.slice(8,10),
		data : paquet.slice(10,16)
	}
	console.log("token : " + paquetDecode.token);
	console.log("opcode : " + paquetDecode.opcode);
	console.log("data : " + paquetDecode.data);
}

var server = net.createServer(CallbackSocket);

// server correctly started ?
server.on('listening', function () {
	console.log("Server started on port " + SERVER_PORT);
});
server.on('error', function (err) {
	console.log("Error while starting the server on port " + SERVER_PORT + " : " + err);
});
server.on('connection', function (socket) {
    sockets.push(socket);
    console.log("New client");
    socket.write('welcome!\n');
});

server.listen(SERVER_PORT);
