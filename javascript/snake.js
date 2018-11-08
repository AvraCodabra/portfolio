var snake = {
    px: 100,
    py: 100,
    size: 20,
    area: 400,
    speed: 100,
    color: "rgb(0,250,0)"
};

var block, apple, score;
var max = 0;
var trail = [];

var myGameArea = {
    canvas : document.createElement("canvas"),
    start : function () {
        this.canvas.width = snake.area;
        this.canvas.height = snake.area;
        this.context = this.canvas.getContext("2d");
        document.body.insertBefore(this.canvas, document.getElementById("snakeGame"));
        this.interval = setInterval(updateGameArea, snake.speed);
        window.addEventListener('keydown', function (e) {
            myGameArea.key = e.keyCode;
        });
    },
    
    size : function (s) {
        if (s == 'Big') {snake.size = 10};
        if (s == 'Normal') {snake.size = 20};
        reset();
        console.log(s);
    },

    stop : function () {
        clearInterval(this.interval);
        
        if (score>max) {
            max=score;
            window.alert("New High Record!  *"+score+"*");
            reset();
        }; //max
    },
    clear : function () {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
};

function startGame() {
    score = 0;
    myGameArea.start();
    block = new component(snake.size, snake.size, snake.color, snake.px, snake.py);
    apple = new point(snake.size, snake.size, "red");
    apple1 = new point(snake.size, snake.size, "red");
    
}
function reset() {
    score = 0;
    trail.length = 0;
    myGameArea.key = null;
    myGameArea.stop();
    startGame();
}

function component(width, height, color, x, y) {
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
    this.vx = 0;
    this.vy = 0;
    this.update = function () {
        ctx = myGameArea.context;
        ctx.fillStyle = color;
        ctx.fillRect(this.x - 1, this.y - 1, this.width - 1, this.height - 1);   
        for (i=0; i<trail.length; i++){
            ctx.fillRect(trail[i].x - 1, trail[i].y - 1, this.width - 1, this.height - 1);   
        }
    };
    
}
function point(width, height, color) {
    this.width = width;
    this.height = height;
    this.x = Math.floor(Math.random() * (snake.size)) * (snake.area/snake.size);
    this.y = Math.floor(Math.random() * (snake.size)) * (snake.area/snake.size);
    this.update = function () {
        ctx = myGameArea.context;
        ctx.fillStyle = color;
        ctx.fillRect(this.x - 1, this.y - 1, this.width - 1, this.height - 1);
        
    };
    this.eat = function () {
        do {
            this.x = Math.floor(Math.random() * (snake.size)) * (snake.area/snake.size);
            this.y = Math.floor(Math.random() * (snake.size)) * (snake.area/snake.size);
            var p = true;
            for (i = 0; i < trail.length; i++){
                if (this.x == trail[i].x && this.y == trail[i].y) {
                    p = false;
                    i = trail.length;
                }
            }
        } while (p== false); //apple on sanke check
        score++;
    };
}
      
function updateGameArea() {
    myGameArea.clear();
    //trail
    trail.push({x : block.x, y : block.y});
    if (trail.length>score) {trail.shift();}
    //move   
    switch (myGameArea.key) {
        case 37: // left
            if (block.vx == 0)     
                {block.vx=-1;
                 block.vy=0;
            }
            break;
        case 39: // right
            if (block.vx == 0)     
                {block.vx=1;
                 block.vy=0;
            }
            break;
        case 38: // down
            if (block.vy == 0)     
                {block.vy=-1;
                 block.vx=0;
            }
            break;
        case 40: // up
            if (block.vy == 0)     
                {block.vy= 1;
                 block.vx=0;
            }
            break;
        } 
        block.x +=snake.size*block.vx;
        block.y +=snake.size*block.vy;
    //edge
        if (block.x<0) {block.x=myGameArea.canvas.width-snake.size};
        if (block.x>myGameArea.canvas.width-snake.size) {block.x=0};
        if (block.y<0) {block.y=myGameArea.canvas.width-snake.size};
        if (block.y>myGameArea.canvas.width-snake.size) {block.y=0};
    //eat
        if ((block.x==apple.x) && (block.y==apple.y)) {apple.eat()};
        if (snake.size==10) {
            if ((block.x==apple1.x) && (block.y==apple1.y)) {apple1.eat()}
        };
    //game over 
        var p = true;
        for (i=0; i<trail.length; i++){
            if (block.x == trail[i].x && block.y == trail[i].y){
                p = false;
                break;
            }
        }
    block.update();
    apple.update();
    if ( snake.size==10) {apple1.update()};
    document.getElementById("score").innerHTML = "Score: "+score; //Score
    document.getElementById("max").innerHTML = "Best: "+max;
    if (p == false) {myGameArea.stop()};
    
}
