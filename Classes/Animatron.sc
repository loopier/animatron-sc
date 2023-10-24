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
		this.pdef = Pdef(key, Pbind(\amp, 0, \finish, {|e|
			e.keysValuesDo{ |k,val|
				var cmd = k.asString;
				if( (cmd == "amp") || (cmd == "dur") || (cmd == "server") || (cmd == "finish") ) {
					// cmd.debug("skip");
					nil;
				} {
					cmd.findRegexp("[A-Z]").do{|regex|
						cmd = cmd.replace(regex[1], "/"++regex[1].toLower);
					};
					cmd.replace("_", "/");
					// "/% % %".format(cmd, this.name, val).debug;
					if( val.isKindOf(Array) ) {
						topEnvironment[key].osc.sendMsg("/"++(cmd.asString), this.name, *val);
					}{
						topEnvironment[key].osc.sendMsg("/"++(cmd.asString), this.name, val);
					}
				}
			}
		}));
		this.pdef.play;
	}

	prAddPbindParam { | param, value |
		var pairs = this.pdef.source.patternpairs;
		value.debug(param);
		pairs = pairs.asDict;
		pairs[param] = value;
		pairs = pairs.asPairs;
		this.source = Pbind(*pairs);
	}

	doesNotUnderstand { | selector, args |
		this.pdef.perform(selector, args);
		^this;
	}

	a { | ...args | this.anim(*args) }
	// n { | ...args | this.name = args }
	palette { | color |
		color.debug;
		this.pdef.perform(color, this.name);
	}

	size { | args | this.prAddPbindParam(\size, args) }
	parent { | args | this.prAddPbindParam(\parent, args) }

}

Animatron {
	classvar <>osc;

	*new { | addr = "localhost", port = 56101 |
		this.osc = NetAddr(addr, port);
		this.prReplyListener;
		^super.new;
	}

	*boot { | addr = "localhost", port = 56101 |
		// "animatron".unixCmd;
		this.osc = NetAddr(addr, port);
		this.prReplyListener;
		^super.new;
	}

	*prReplyListener {
		OSCdef(\status, { arg args, time, srcAddr, port; args[1..].first.postln; }, "/status/reply");
		OSCdef(\error, { arg args, time, srcAddr, port; args[1..].first.postln; }, "/error/reply");
	}

	*cmd { | cmd ...args| Animatron.osc.sendMsg(cmd, *args) }
	cmd { | cmd ...args| Animatron.osc.sendMsg(cmd, *args) }

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