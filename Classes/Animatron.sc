Animatron : Pdef {
	var <>addr;
	var <>port;
	*new { | newAddr, newPort |
		^super.new.init(newAddr, newPort);
	}

	init { | newAddr, newPort |
		this.addr = newAddr;
		this.port = newPort;
	}
}