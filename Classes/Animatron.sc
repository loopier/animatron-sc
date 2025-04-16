Actor {
	var <>name;
	var <>osc;
	var <>pdef;

	*new { |animation|
		^super.new.init(animation);
	}

	init { |animation|
		var code = thisProcess.interpreter.cmdLine.split($ )[0];
		var key = code.findRegexp("~[a-zA-Z0-9]+")[0][1].replace("~", "").asSymbol;
		animation.debug(key);

		this.name = key.asString;
		if( Animatron.osc.isNil ) { Animatron.boot };
		this.osc = Animatron.osc;
		this.osc.sendMsg("/create", name, animation);
	}

	doesNotUnderstand { | selector, args |
		if( args.isArray ) { args = args.join(" ") };
		this.cmd("/"++selector + this.name + args);
	}

	a { | anim | this.cmd("/anim" + this.name + anim) }

	palette { | color |
		color.debug;
	}

	cmd { |...args|
		if (args.notEmpty) {
			var arr = args[0].separateBySpaces;
			if (arr.notEmpty) {
				arr = arr ++ args[1..];
				this.osc.sendMsg(*arr);
				arr
			};
		};
	}

	//
	color { | rgb | this.cmd("/color" + this.name + rgb[0] + rgb[1] + rgb[2]) }
	red { | r | this.cmd("/color/r" + this.name + r) }
	green { | g | this.cmd("/color/g" + this.name + g) }
	blue { | b | this.cmd("/color/b" + this.name + b) }
	r { | r | this.red(r) }
	g { | g | this.green(g) }
	b { | b | this.blue(b) }

	// overrides
	size { | args | this.cmd("/size" + this.name + args) }
}

Animatron {
	var <>osc;
	classvar <>classOsc;

	*new { | addr = "localhost", port = 56101 |
		var instance = super.new;
		instance.osc = NetAddr(addr, port);
		this.classOsc = NetAddr(addr, port);
		this.prReplyListener;
		^instance;
	}

	*boot { | addr = "localhost", port = 56101 |
		// "animatron".unixCmd;
		var instance = super.new;
		instance.osc = NetAddr(addr, port);
		this.classOsc = NetAddr(addr, port);
		this.prReplyListener;
		^instance;
	}

	*prReplyListener {
		// OSCdef(\status, { arg args, time, srcAddr, port; args[1..].first.postln; }, "/status/reply");
		// OSCdef(\error, { arg args, time, srcAddr, port; args[1..].first.postln; }, "/error/reply");
		OSCdef(\listActorsReply, { arg msg;
			"Actors: %".format(msg[1..]).postln;
		}, "/list/actors/reply");
		OSCdef(\listAnimsReply, { arg msg;
			"Available animations:\n\t%".format(join(msg[1..], "\n\t")).postln;
		}, "/list/anims/reply");
		OSCdef(\listAssetsReply, { arg msg;
			"Available assets:\n\t%".format(join(msg[1..], "\n\t")).postln;
		}, "/list/assets/reply");
		OSCdef(\errorReply, { arg msg;
			"Error: %".format(msg[1]).postln;
		}, "/error/reply");
		OSCdef(\statusReply, { arg msg;
			"Status: %".format(msg[1]).postln;
		}, "/status/reply");
	}

	*cmd { |...args|
		if (args.notEmpty) {
			var arr = args[0].format(*args[1..]).separateBySpaces;
			this.osc.sendMsg(*arr);
		};
	}
	// *cmd { |...args|
	// 	args.debug("*cmd");
	// 	if (args.notEmpty) {
	// 		var arr = args[0].separateBySpaces;
	// 		if (arr.notEmpty) {
	// 			arr = arr ++ args[1..];
	// 			this.osc.sendMsg(*arr).debug;
	// 			arr
	// 		};
	// 	};
	// }

	cmd { | ...args|
		if (args.notEmpty) {
			var arr = args[0].format(*args[1..]).separateBySpaces;
			this.osc.sendMsg(*arr);
		};
	}

	post { | ...args| this.osc.sendMsg("/post", *args) }

	doesNotUnderstand { | selector, args |
		var str = selector.asString;

		// str.findRegexp("[A-Z]").do{|regex|
		// 	str = str.replace(regex[1], "/"++regex[1].toLower);
		// };

		// selector.debug("not understood");
		// str.debug("sending");
		this.osc.sendMsg("/"++str, *args);
	}
}