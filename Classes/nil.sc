+ Nil {
	anim { |animation, actor=nil|
		var code = actor ? thisProcess.interpreter.cmdLine.split($ )[0];
		var name = code.findRegexp("~[a-zA-Z0-9]+")[0][1].replace("~", "").asSymbol;
		// Pdef(connection, Pbind(\type, \ziva, \s, snd, \scale, Pdefn(\scale), \root, Pdefn(\root)));
		// Pdef(connection).play;
		// History.eval("% = Pdef('%')".format(code, connection));
		// History.eval("%.play".format(code));
		// ^Pdef(connection);
		History.eval("% = Actor('%')".format(code, animation));
		^topEnvironment[name];
	}

	a { |animation| ^this.anim(animation) }

}