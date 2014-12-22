package edu.uci.eecs.scriptsafe.merge.graph;

public class ScriptBranchNode extends ScriptNode {

	private ScriptNode target;

	public ScriptBranchNode(int opcode) {
		super(opcode);
	}

	public void setTarget(ScriptNode target) {
		this.target = target;
	}

	public ScriptNode getTarget() {
		return target;
	}
}
