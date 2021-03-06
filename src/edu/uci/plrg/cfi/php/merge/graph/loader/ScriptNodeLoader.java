package edu.uci.plrg.cfi.php.merge.graph.loader;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import edu.uci.plrg.cfi.common.io.LittleEndianInputStream;
import edu.uci.plrg.cfi.common.log.Log;
import edu.uci.plrg.cfi.php.merge.ScriptMergeWatchList;
import edu.uci.plrg.cfi.php.merge.graph.ScriptBranchNode;
import edu.uci.plrg.cfi.php.merge.graph.ScriptNode;
import edu.uci.plrg.cfi.php.merge.graph.ScriptNode.TypeFlag;
import edu.uci.plrg.cfi.php.merge.graph.ScriptRoutineGraph;

public class ScriptNodeLoader {

	public interface LoadContext {
		ScriptRoutineGraph createRoutine(int routineHash);

		ScriptRoutineGraph getRoutine(int routineHash);
	}

	private LoadContext loadContext;

	public ScriptNodeLoader() {
	}

	public ScriptNodeLoader(LoadContext loadContext) {
		this.loadContext = loadContext;
	}

	public void setLoadContext(LoadContext loadContext) {
		this.loadContext = loadContext;
	}

	public void loadNodes(File nodeFile) throws IOException {
		int routineHash, opcodeField, opcode, extendedValue, lineNumber, nodeIndex = 0;
		Set<TypeFlag> typeFlags;
		ScriptNode node, previousNode = null;
		ScriptRoutineGraph routine = null;
		LittleEndianInputStream input = new LittleEndianInputStream(nodeFile);

		while (input.ready(0xc)) {
			routineHash = input.readInt();

			if (routineHash == 1)
				Log.log("entry");

			if (routine == null || routine.hash != routineHash) {
				previousNode = null;
				routine = loadContext.getRoutine(routineHash);
			}

			if (routine == null) {
				Log.message("Create routine %x", routineHash);
				routine = loadContext.createRoutine(routineHash);
			}

			opcodeField = input.readInt();
			opcode = opcodeField & 0xff;
			extendedValue = (opcodeField >> 8) & 0xff;
			lineNumber = opcodeField >> 0x10;
			typeFlags = ScriptNode.identifyTypes(opcode, extendedValue);

			// parse out extended value for include/eval nodes
			nodeIndex = input.readInt();
			node = createNode(routineHash, opcode, typeFlags, lineNumber, nodeIndex);
			if (nodeIndex > routine.getNodeCount()) {
				Log.warn("Skipping node with disjoint index %d in routine 0x%x with %d nodes", nodeIndex, routineHash,
						routine.getNodeCount());
				continue;
			}
			if (previousNode != null)
				previousNode.setNext(node);
			previousNode = node;

			Log.message("%s: @%d#%d Opcode 0x%x (%x) [%s]", getClass().getSimpleName(), nodeIndex, lineNumber, opcode,
					routineHash, node.typeFlags);
			if (ScriptMergeWatchList.watch(routineHash)) {
				Log.log("%s: @%d Opcode 0x%x (%x) [%s]", getClass().getSimpleName(), nodeIndex, opcode, routineHash,
						node.typeFlags);
			}

			routine.addNode(node);
		}

		if (input.ready())
			Log.error("Input file " + nodeFile.getAbsolutePath() + " has trailing data!");
		input.close();
	}

	private ScriptNode createNode(int routineHash, int opcode, Set<TypeFlag> typeFlags, int lineNumber, int index) {
		if (typeFlags.contains(TypeFlag.BRANCH)) {
			// int userLevel = (index >>> 26);
			// index = (index & 0xfff);
			return new ScriptBranchNode(routineHash, typeFlags, opcode, index, lineNumber, 0);
		} else {
			return new ScriptNode(routineHash, typeFlags, opcode, lineNumber, index);
		}
	}
}
