# Animatron

## Usage

``` supercollider
// boot the server to default port
a = Animatron.boot;
// send a command with an Animatron instance
a.cmd("/list/assets");
// simpler version without using an instance
"/list/assets".cmd;

// create an actor named 'lola' using the animation 'kazoo'
~lola a: \kazoo
// change the size
~lola size: 0.5
// change more than one attribute at a time
~lola angle: 360.rand size: 90.rand

// create a red backround
~bg a: \solid r: 0.5 behind: "*"
// set the 3 color values
~bg color: [1,0,0]

(
Tdef(\a, {
    inf.do{|i|
        "/color bl* % % %".cmd(1.1, 1.0.rand, 1.0.rand);
        "/angle bl* %".cmd(360.rand);
        "/position/y bla %".cmd(1.0.rand);
        "/position/x bla %".cmd(1.0.rand);
        0.5.wait;
    };
}).play
)
```

