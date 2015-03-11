package edu.uci.eecs.scriptsafe.merge;

import edu.uci.eecs.scriptsafe.merge.ScriptMerge.Side;
import edu.uci.eecs.scriptsafe.merge.graph.ScriptFlowGraph;
import edu.uci.eecs.scriptsafe.merge.graph.ScriptRoutineGraph;

public class IncrementalDynamicRoutineMerge extends DynamicRoutineMerge {

	public IncrementalDynamicRoutineMerge(ScriptFlowGraph left) {
		super(left);
	}

	@Override
	protected void remapRoutine(ScriptRoutineGraph routine, int toHash, Side fromSide) {
		if (fromSide == Side.RIGHT)
			throw new MergeException("Attempt to append a dynamic routine from the right!");

		leftRemapping[ScriptRoutineGraph.getDynamicRoutineIndex(routine.hash)] = ScriptRoutineGraph
				.getDynamicRoutineIndex(toHash);
	}
}
