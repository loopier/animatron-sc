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

}