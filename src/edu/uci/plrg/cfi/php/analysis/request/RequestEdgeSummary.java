package edu.uci.plrg.cfi.php.analysis.request;

import edu.uci.plrg.cfi.php.feature.FeatureRoleCountElement;
import edu.uci.plrg.cfi.php.merge.graph.RoutineId;
import edu.uci.plrg.cfi.php.merge.graph.ScriptRoutineGraph;

public class RequestEdgeSummary implements FeatureRoleCountElement {
	public final RequestCallSiteSummary callSite;
	public final RoutineId calleeId;
	public final ScriptRoutineGraph callee;

	int adminCount = 0;
	int anonymousCount = 0;

	RequestEdgeSummary(RequestCallSiteSummary callSite, RoutineId calleeId, ScriptRoutineGraph callee) {
		this.callSite = callSite;
		this.calleeId = calleeId;
		this.callee = callee;
	}

	@Override
	public int getAdminCount() {
		/* count 1 for admin if no anonymous edges occur */
		return anonymousCount == 0 ? 1 : 0;
	}

	@Override
	public int getAnonymousCount() {
		/* count 1 for anonymous if any anonymous edges occur */
		return anonymousCount > 0 ? 1 : 0;
	}

	boolean matches(ScriptRoutineGraph callee) {
		return this.callee.hash == callee.hash;
	}
}
