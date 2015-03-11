package edu.uci.eecs.scriptsafe.merge.graph;

public class RoutineEdge {

	public enum Type {
		CALL,
		THROW;
	}

	protected int fromRoutineHash;
	protected int fromIndex;
	protected int toRoutineHash;
	protected int userLevel;

	public RoutineEdge(int fromRoutineHash, int fromIndex, int toRoutineHash, int userLevel) {
		this.fromRoutineHash = fromRoutineHash;
		this.fromIndex = fromIndex;
		this.toRoutineHash = toRoutineHash;
		this.userLevel = userLevel;
	}

	public boolean isSameEntryType(RoutineEdge other) {
		return other.getEntryType() == Type.CALL;
	}

	public Type getEntryType() {
		return Type.CALL;
	}

	public int getFromRoutineHash() {
		return fromRoutineHash;
	}

	public int getFromRoutineIndex() {
		return fromIndex;
	}

	public int getToRoutineHash() {
		return toRoutineHash;
	}

	public void setToRoutineHash(int toRoutineHash) {
		this.toRoutineHash = toRoutineHash;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public String printFromNode() {
		return String.format("0x%x|0x%x %d", fromRoutineHash, fromRoutineHash, fromIndex);
	}

	public String printToNode() {
		return String.format("0x%x|0x%x", toRoutineHash, toRoutineHash);
	}
}
