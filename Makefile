startServer: WebApp.class
		sudo java WebApp 80
WebApp.class: WebApp.java Backend.java BackendInterface.java BaseGraph.java DijkstraGraph.java Frontend.java FrontendInterface.java GraphADT.java HashtableMap.java MapADT.java
		javac -cp .:../junit5.jar WebApp.java
		javac -cp .:../junit5.jar Backend.java
		javac -cp .:../junit5.jar BackendInterface.java
		javac -cp .:../junit5.jar BaseGraph.java
		javac -cp .:../junit5.jar DijkstraGraph.java
		javac -cp .:../junit5.jar Frontend.java
		javac -cp .:../junit5.jar FrontendInterface.java
		javac -cp .:../junit5.jar GraphADT.java
		javac -cp .:../junit5.jar HashtableMap.java
		javac -cp .:../junit5.jar MapADT.java

runAllTests: BackendTests.class FrontendTests.class HashtableMap.class DijkstraGraph.class
		java -jar ../junit5.jar -cp . -c BackendTests
		java -jar ../junit5.jar -cp . -c FrontendTests
		java -jar ../junit5.jar -cp . -c HashtableMap
		java -jar ../junit5.jar -cp . -c DijkstraGraph

BackendTests.class: BackendTests.java
		javac -cp .:../junit5.jar BackendTests.java

FrontendTests.class: FrontendTests.java
		javac -cp .:../junit5.jar FrontendTests.java

HashtableMap.class: HashtableMap.java
		javac -cp .:../junit5.jar HashtableMap.java

DijkstraGraph.class: DijkstraGraph.java
		javac -cp .:../junit5.jar DijkstraGraph.java

clean:
		rm -f *.class
