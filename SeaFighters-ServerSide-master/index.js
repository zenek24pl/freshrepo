var app = require('express')();
var server = require('http').createServer(app).listen(8080);
var io = require('socket.io').listen(server);

var port = process.env.PORT || 8080;
var address = process.env.address;

var morgan = require('morgan');

var playerCount = 0;
var jsonStringTEST = "5,3,1,,3,1,0,,1,2,1,,3,3,1,,7,5,1";

var player1init = "";
var player2init = "";

app.use(morgan('dev'));
app.use('/', function(req, res){
	
})

console.log(port)

server.listen(port, function(){
	console.log("Server is now running...");
});
//io.use(morgan('dev'));
io.on('connection', function(socket){
	playerCount++;
	if (playerCount == 1) {
		player1init = "";
		player2init = "";
	}
	console.log("Player connected");
	console.log(playerCount);

	socket.broadcast.emit('newPlayer', { players: playerCount });
	//socket.emit('2playerConnected', {ships: jsonStringTEST });

	socket.on('test', function(data){
		console.log("test");
		//socket.broadcast.emit('2playerConnected', data);
	});

	//socket.on('							getPlayerID', function(){
		socket.emit('getID', { id: playerCount });
	//});

	socket.on('init', function(data){
		if (player1init == "") {
			player1init = data;
			console.log(player1init);
			console.log(player2init);
		} else {
			player2init = data;
			console.log(player1init);
			console.log(player2init);
			socket.emit('playersReady');
			socket.broadcast.emit('playersReady');
		}
	});

	socket.on('getShips', function(data){
		if (data == 1) {
			socket.emit('2playerConnected', player1init);
		} else {
			socket.emit('2playerConnected', player2init);
		}
	});

	socket.on('enemyMove', function(data){
		console.log(data);
		socket.broadcast.emit('enemyMove', data);
	});

	socket.on('turnChange', function(){
		socket.broadcast.emit('turnChange');
	});

	socket.on('disconnect', function(){
		playerCount--;
		if (playerCount==1) {
			player2init = "";
		} else if (playerCount == 0) {
			player2init = "";
			player1init = "";
		}
		console.log("Player disconnect");
		console.log(playerCount);
	});
});
