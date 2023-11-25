+String {
	separateBySpaces {
		// This regex isn't perfect, but works
		// reasonably well for most of my cases...
		var regex = '"(.+?)"|(?:([^"]+?)(?=\\s))|(?:(?<=\\s)([^"]+))'.asString;
		var arr = this.asString.findRegexp(regex).flop.last.clump(4).collect{ |x|
			x.drop(1).collect(_.stripWhiteSpace)
		}.flatten.reject(_.isEmpty);
		if (arr.isEmpty) { arr = [ this ] };
		^arr
	}

	cmd { | ...args |
		var arr;
		var str = this.format(*args);
		arr = str.separateBySpaces;
		if (arr.notEmpty) {
			if( Animatron.osc.isNil ) {
				"Animatron not running.".warn
			} {
				Animatron.osc.sendMsg(*arr);
			};
		};
	}
}