# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh

Phase 2 Sequence Diagram:

https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+iMykoKp+h-Ds0KPMB4lUEiMAIEJ4oYoJwkEkSYCkm+hi7jS+4MkyU76XOnl3kuwowGKEpujKcplu8So3gFDoWVZ4Vbm5gpufUFHmIQcgoPZtloE5ahgGsSXyK5zruVSt78vUh7Zc+8Tnpe17DvFlTLgGa4Bk1yXle2Omlg54oZKoAGYP1IHVLphHlsRpHfBRVH1iRqENlpSaVKJOF4QR+k0WRYwLYhS1zXRTaeN4fj+F4KDoDEcSJNdt0Ob4WCialk2lg00gRvxEbtBG3Q9HJqgKcMh1Ieh8KVP16WXothTjQlLrWUJL25fYL0FSS24VTIVX0kYKDcMel4YvVzX4wKQX1BkMwQDQXUvj1nZ9WZ-rPaew2jYjFm6ZpH2YcgqYwLh+GjKdDHncx-gouu-jYOKGr8WiMAAOJKhob2859qt-YD9hKmDcNHYUa3Q2zpbg+gY0W+9naw-BJvDMgOTqzm9lom7ahYy5OOCi1BOMmAXuqBi-m8oFbXBS0OjyOK1kqxAABmlYIDABs5rFEfxUjXYCYnKcmmnGdqH760PLC7OexrXMIIBtvl6BLxjCXqhLA04wlwAktIBYAIzhMEgQgps8S6igbqcnsqzDMsySgGqk+QYs3wlwAckqK8XDAnT8xJ8LJkL2Ei9tozLK3BYdy3So9-3g-D8so-j0vnwryss9jPPICL0R8xv+fSoN5zC3jvCWjELoBA4AAdjcE4FATgYgRmCHALiAA2eAE5DBexgEUI+YlfQfXqFJDo+tDbTGNkhLM68lR70TObSulsKHoCoYAmhNsGF20ssjWq6IvYYjgJgr2Psyosw8tnAmPCUB8OoXMcOC4qZR3qI+dOFZIBGG0HoAwWd5ETXtmrJUr5erlxhhgo8UilS13rhwxuhCYCdxvr3eoA8h60IwuXTaJ8xb2LmLfJx98wFS0upYImNlNh3SQAkMAwS+wQDCQAKQgPHbB-gv5qlwSUfBHYahEKaMyGSPQS5G0dpQ0Y2AEDAGCVAOAEAbJQDWN3aQrioYVy-A7SiTtSnlMqdU2p9SHGmWsQQvRAArRJaA+EJPFEIlAhJCoiK4bjAOo505MmkQ4uR+57zBVVkyfRcwPRLN0Qs+oXtDGiMqnFAmoyplKgxGUiplAenQA2ZHIUSjOpoBQGE05W5Dk2L0T8+QZcPwW3qJM8ZFj-x13YV+I5fNIaCwyVtLxASmKXS8BU8JkSMXykQMGWAwBsBlKynGNAOCtZDOyY0b6v1-qA2MAikFDDagwvMpSqyIBuB4DDmXC54jlmcvxTyv5iiYCCu5fMwcTLWnwC5VASxrK3HaxeE0xFwtRZZjAUAA
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
