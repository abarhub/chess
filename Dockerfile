FROM openjdk:11-slim
WORKDIR /
ADD chess-0.0.1-SNAPSHOT.jar chess.jar
CMD java -jar chess.jar
