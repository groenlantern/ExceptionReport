/**
 * $LastChangedBy: Andre $
 * $LastChangedDate: 2016-07-07 09:50:33 +0200 (Thu, 07 Jul 2016) $
 * $LastChangedRevision: 994 $
 */
// Got it from https://github.com/dwmkerr/starfield
/*
 Starfield lets you take a div and turn it into a starfield.
 
 */

//	Define the starfield class.
function Starfield() {
    this.fps = 30;
    this.canvas = null;
    this.width = 0;
    this.height = 0;
    this.minVelocity = 15;
    this.maxVelocity = 30;
    this.stars = 100;
    this.intervalId = 0;
    this.starColour = '#ffffff';
}

/**
 * ALE added
 * @param {string} starColour the colour of the stars
 */
function Starfield(starColour) {
    this.fps = 30;
    this.canvas = null;
    this.width = 0;
    this.height = 0;
    this.minVelocity = 15;
    this.maxVelocity = 30;
    this.stars = 100;
    this.intervalId = 0;
    this.starColour = starColour;
}

//	The main function - initialises the starfield.
Starfield.prototype.initialise = function (div) {
    var self = this;

    //	Store the div.
    this.containerDiv = div;
    self.width = window.innerWidth;
    self.height = window.innerHeight;

    window.addEventListener('resize', function resize(event) {
        self.width = window.innerWidth;
        self.height = window.innerHeight;
        self.height = 55;
        self.canvas.height = 55;
        self.canvas.width = self.width;
        self.draw();
    });

    //	Create the canvas.
    var canvas = document.createElement('canvas');
    div.appendChild(canvas);
    this.canvas = canvas;
    this.canvas.width = this.width;
    this.canvas.height = this.height;
};

Starfield.prototype.start = function () {

    //	Create the stars.
    var stars = [];
    for (var i = 0; i < this.stars; i++) {
        stars[i] = new Star(Math.random() * this.width, Math.random() * this.height, Math.random() * 3 + 1,
                (Math.random() * (this.maxVelocity - this.minVelocity)) + this.minVelocity);
    }
    this.stars = stars;

    var self = this;
    //	Start the timer.
    this.intervalId = setInterval(function () {
        self.update();
        self.draw();
    }, 1000 / this.fps);
};

Starfield.prototype.stop = function () {
    clearInterval(this.intervalId);
};

Starfield.prototype.update = function () {
    var dt = 1 / this.fps;

    //find middle
    var mx = this.width / 2;
    var my = this.height / 2;
    for (var i = 0; i < this.stars.length; i++) {
        var star = this.stars[i];
        var ang = Math.atan2(my - star.y, mx - star.x);
        star.x -= dt * star.velocity * Math.cos(ang);
        star.y -= dt * star.velocity * Math.sin(ang);
        //	If the star has moved from the bottom of the screen, spawn it at the top.
        if ((star.y <= 0) || (star.y > this.height) || (star.x <= 0) || (star.x > this.width)) {
            var newX = (mx + 50) - (Math.random() * 100);
            var newY = (my + 10) - (Math.random() * 20);
            this.stars[i] = new Star(newX, newY, Math.random() * 3 + 1,
                    (Math.random() * (this.maxVelocity - this.minVelocity)) + this.minVelocity);
        }
    }
};

Starfield.prototype.draw = function () {

    //	Get the drawing context.
    var ctx = this.canvas.getContext("2d");

    //	Draw the background.
    ctx.fillStyle = '#000000';
    ctx.fillRect(0, 0, this.width, this.height);

    //	Draw stars.
    ctx.fillStyle = this.starColour;//'#ffffff';
    for (var i = 0; i < this.stars.length; i++) {
        var star = this.stars[i];
        ctx.fillRect(star.x, star.y, star.size, star.size);
    }
};

function Star(x, y, size, velocity) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.velocity = velocity;
}

//ALE
var container = document.getElementById('starscontainer');
var starfield = new Starfield('#FFFFCC');
starfield.initialise(container);
starfield.height = 55;
starfield.canvas.height = 55;
starfield.start();

function randomise() {
    starfield.stop();
    starfield.stars = Math.random() * 1000 + 50;
    starfield.minVelocity = Math.random() * 30 + 5;
    starfield.maxVelocity = Math.random() * 50 + starfield.minVelocity;
    starfield.start();
}
