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
		this.pdef = Pdef(key, Pbind(\amp, 0, \finish, {|e| e.keysValuesDo{ |k,v|
			v.debug(k);
			// topEnvironment[key].perform(k, v);
			// topEnvironment[key].osc.sendMsg().debug(key);
		} }));
	}

	doesNotUnderstand { | selector ...args |
		// var str = selector.asString;
		// parent
		// var responds = this.class.findRespondingMethodFor(selector);
		// responds.debug(this.class);

		// str.findRegexp("[A-Z]").do{|regex|
		// 	str = str.replace(regex[1], "/"++regex[1].toLower);
		// };
		// selector.debug("not understood");
		// str.debug("sending");
		// this.osc.sendMsg("/"++str, name, *args);
		var pairs = this.pdef.soruce.patternpairs.asDict;
		pairs[selector] = args;
		// this.pdef.source = Pbind(*pairs.asPairs);
	}

	a { | ...args | this.anim(*args) }

	bla { | args |
		Pdef(name.asSymbol, Pbind(\amp, 0, \angle, args, \finish, {
			currentEnvironment.keysValuesDo{|k,v|
				this.osc.sendMsg("/"++k.asString, name, v);
			}}
		)).play
	}

}

Animatron {
	classvar <>osc;

	*boot { | addr = "localhost", port = 56101 |
		this.osc = NetAddr(addr, port);
		^super.new;
	}

	post { | ...args| this.osc.sendMsg("/post", *args) }

	doesNotUnderstand { | selector ...args |
		var str = selector.asString;

		str.findRegexp("[A-Z]").do{|regex|
			str = str.replace(regex[1], "/"++regex[1].toLower);
		};

		selector.debug("not understood");
		str.debug("sending");
		this.osc.sendMsg("/"++str, *args);
	}
}