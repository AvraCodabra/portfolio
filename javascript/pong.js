var settings = {
    areaWidth : 800,
    areaHeight : 400,
    peddalWidth : 10,
    peddalLength : 70,
    ballSize: 5,
    speed : 26,
    ballspeed : 10,
    turns: 0.1,
    peddelspeed : 10,
    color: "#ffffff",
    padding : 10   
};

var score1 = 0;
var score2 = 0;

function startGame() {

    score1 = 0;
    score2 = 0;
    ped1 = new Peddel(settings.peddalLength, settings.peddalWidth, settings.color, settings.padding, (settings.areaHeight/2));
    ped2 = new Peddel(settings.peddalLength, settings.peddalWidth, settings.color, (settings.areaWidth-(settings.padding+settings.peddalWidth)), (settings.areaHeight/2));
    ball = new Ball(settings.ballSize, settings.color, (settings.areaWidth/2), ((settings.areaHeight/2)+(settings.peddalLength/2)));
    var alert = true;
    myGameArea.start();

}

function Peddel(length, width, color, x, y) {
    this.width = width;
    this.length = length;
    this.x = x;
    this.y = y;
    this.color = color;
    this.update = function () {
        this.center = this.y+(this.length/2)
        ctx = myGameArea.context;
        ctx.fillStyle = this.color;
        ctx.fillRect(this.x, this.y, this.width, this.length);
        }
    };

function Ball (size, color, x, y) {
    this.size = size;
    this.color = color;
    this.x = x;
    this.y = y;
    this.vx = settings.ballspeed;
    this.vy = 0;
    this.update = function () {
        this.x = (this.x+this.vx);
        this.y = (this.y+this.vy);
        ctx = myGameArea.context;
        ctx.lineWidth=10;
        ctx.strokeStyle= this.color;
        ctx.beginPath();
        ctx.arc(this.x, this.y,settings.ballSize,0,2*Math.PI);
        ctx.stroke();
        }
    this.hit = function (){
        if (this.vx>0) {     //right side
            if ((this.y >= ped2.y) && (this.y <=(ped2.y+settings.peddalLength))) {
                this.vx = ((this.vx)*(-1));
                this.vy = (this.y-ped2.center)*settings.turns;

            };
        }
        else {     //left side
            if (this.y >= ped1.y && this.y <= (ped1.y+settings.peddalLength)) {
                this.vx *= -1;
                this.vy = (this.y-ped1.center)*settings.turns;
            }
        }

    }
}

var head = {    
    update : function () {
        ctx = myGameArea.topcontext;
        ctx.font = "50px Aharoni";
        ctx.fillStyle = settings.color;
        ctx.textAlign= "center";
        ctx.fillText("PONG", settings.areaWidth/2, myGameArea.topbar.height/1.5);
        ctx.font = "80px Aharoni";
        ctx.fillText(score1, 50, myGameArea.topbar.height/1.5);
        ctx.fillText(score2, settings.areaWidth-50, myGameArea.topbar.height/1.5);
        //console.log(1);
    }



};

var myGameArea = {
    topbar : document.createElement("canvas"),
    canvas : document.createElement("canvas"),
    start : function () {
        this.topbar.width = settings.areaWidth;
        this.topbar.height = 100 ;
        this.topcontext = this.topbar.getContext("2d");
        this.canvas.width = settings.areaWidth;
        this.canvas.height = settings.areaHeight;
        this.context = this.canvas.getContext("2d");
        document.body.insertBefore(this.topbar, document.getElementById("score"));
        document.body.insertBefore(this.canvas, document.getElementById("gameBox"));

        this.interval = setInterval(updateGameArea, settings.speed);
        window.addEventListener('keydown', function (e) {
            myGameArea.keys = (myGameArea.keys || []);
            myGameArea.keys[e.keyCode] = true;
        })
        window.addEventListener('keyup', function (e) {
            myGameArea.keys[e.keyCode] = false; 
        });

    },
    point : function (s) {
        if (s>=settings.areaWidth) {
            score2++;
            
            console.log(score2);
        }
        if (s<=0) {
            score1++;
            console.log(score1);
        }
        ball.x = (settings.areaWidth/2);
        ball.y = (settings.areaHeight/2);
        ball.vy =0;
    },
    
    clear : function () {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
        this.topcontext.clearRect(0, 0, this.topbar.width, this.topbar.height);
    },
    reset : function() {
        
    },
}

function updateGameArea() {
    myGameArea.clear();
    
    //move
    if ((ped1.y >0) &&  myGameArea.keys && myGameArea.keys[81]) {ped1.y-=settings.peddelspeed;}; 
    if ((ped1.y < settings.areaHeight-settings.peddalLength) && myGameArea.keys && myGameArea.keys[65]) {ped1.y+=settings.peddelspeed;}; 
    if ((ped2.y >0) && myGameArea.keys && myGameArea.keys[38]) {ped2.y-=settings.peddelspeed;};
    if ((ped2.y < settings.areaHeight-settings.peddalLength) && myGameArea.keys && myGameArea.keys[40]) {ped2.y+=settings.peddelspeed;}; 
    //hit
    if (ball.x==(ped1.x+settings.peddalWidth) || (ball.x==ped2.x)) {ball.hit()};
    //wall
    if ((ball.y <= 0) || (ball.y >= settings.areaHeight)) {ball.vy *= (-1);};
    //out
    if ((ball.x <= 0) || (ball.x >= settings.areaWidth)) {myGameArea.point(ball.x)};
    ped1.update();
    ped2.update();
    ball.update();
    head.update();
    if (alert) {
        alert('the controls\nleft player: Q & A\n right player: up & down');
        alert = false;
    }
    
}
