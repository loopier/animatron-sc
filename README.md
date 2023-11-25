# Animatron

## Usage

``` supercollider
// boot the server to default port
a = Animatron.boot;
// send a command with an Animatron instance
a.cmd("/list/assets");
// simpler version without using an instance
"/list/assets".cmd;
"/load solid".cmd;
"/load kazoo".cmd;

// create a red backround
~bg a: \solid r: 0.5 behind: "*"
// create an actor named 'lola' using the animation 'kazoo'
~lola a: \kazoo
// change the size
~lola size: 0.5 color: ([1.0,0.5,0]*1.0)
// change more than one attribute at a time
~lola angle: 360.rand size: 90.rand

// change the bg color
~bg color: {0.5.rand}.dup(3)

(
Tdef(\a, {
    inf.do{|i|
        "/color lola % % %".cmd(1.1, 1.0.rand, 1.0.rand);
        "/angle lola %".cmd(360.rand);
        "/position/y lola %".cmd(1.0.rand);
        "/position/x lola %".cmd(1.0.rand);
        0.5.wait;
    };
}).play
)
```

