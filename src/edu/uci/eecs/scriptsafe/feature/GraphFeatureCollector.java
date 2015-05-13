package edu.uci.eecs.scriptsafe.feature;

import java.nio.ByteBuffer;
import java.util.Map;

import edu.uci.eecs.scriptsafe.analysis.request.RequestCallSiteSummary;
import edu.uci.eecs.scriptsafe.analysis.request.RequestEdgeSummary;
import edu.uci.eecs.scriptsafe.merge.graph.RoutineEdge;
import edu.uci.eecs.scriptsafe.merge.graph.ScriptRoutineGraph;

public class GraphFeatureCollector {

	private FeatureDataSource dataSource;

	private final FeatureRoleCounts totalRoutines = new FeatureRoleCounts();
	private final FeatureRoleCounts totalEdges = new FeatureRoleCounts();

	final FeatureResponseGenerator responseGenerator = new FeatureResponseGenerator();

	void setDataSource(FeatureDataSource dataSource) {
		this.dataSource = dataSource;

		responseGenerator.addField(totalRoutines);
		responseGenerator.addField(dataSource.wordList.createNonEmptyRoutineResponseField()); // updated on k cycle
		responseGenerator.addField(totalEdges);
	}

	ByteBuffer getFeatures() {
		responseGenerator.resetAllFields();

		for (Map.Entry<Integer, Integer> entry : dataSource.trainingRequestGraph.calledRoutineUserLevel.entrySet())
			totalRoutines.increment(entry.getValue() >= 2);

		for (RequestCallSiteSummary callSite : dataSource.trainingRequestGraph.callSites.values()) {
			for (RequestEdgeSummary edge : callSite.getEdges())
				totalEdges.addCounts(edge);
		}

		return responseGenerator.generateResponse();
	}
}
