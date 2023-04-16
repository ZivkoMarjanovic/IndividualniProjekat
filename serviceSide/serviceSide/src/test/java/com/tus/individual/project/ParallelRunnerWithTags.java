package com.tus.individual.project;


import com.intuit.karate.Runner.Builder;
import org.junit.jupiter.api.Test;

class ParallelRunnerWithTags {

	@Test
	public void executeKarateTest() {
		Builder aRunner = new Builder();
		aRunner.path("classpath:com/tus/individual/project/tags");
		aRunner.tags("@Confidence");
		aRunner.parallel(5);
	}

}
