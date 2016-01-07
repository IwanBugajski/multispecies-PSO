package pl.edu.agh.miss.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Properties;

import org.bson.Document;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import pl.edu.agh.miss.output.SimulationResult;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class SimulationResultDAO {
    private static final String DB_PROPERTIES_FILE = "db.properties";

    private static final String DB_URI = "db_uri";

    private static final String DB_NAME = "db_name";

    private static SimulationResultDAO simulationResultDAO;

    private final MongoClient mongoClient;

    private final MongoDatabase mongoDatabase;

    private SimulationResultDAO(MongoClient mongoClient, MongoDatabase mongoDatabase) {
        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoDatabase;
    }

    public void writeResult(SimulationResult result) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.valueToTree(result);
        ((ObjectNode)jsonNode).put("timestamp", new Timestamp(System.currentTimeMillis()).toString());
        mongoDatabase.getCollection("simulation_results")
                                       .insertOne(Document.parse(jsonNode.toString()));
    }

    public void close() throws IOException {
        if (mongoClient != null) {
            mongoClient.close();
            simulationResultDAO = null;
        }
    }

    public static SimulationResultDAO getInstance() throws IOException {
        if (simulationResultDAO == null) {
            simulationResultDAO = createSimulationResultDAO();
        }
        return simulationResultDAO;
    }

    private static SimulationResultDAO createSimulationResultDAO() throws IOException {
        Properties props = new Properties();
        InputStream input = SimulationResultDAO.class.getResourceAsStream("/" + DB_PROPERTIES_FILE);

        props.load(input);

        String dbUri = props.getProperty(DB_URI);
        String dbName = props.getProperty(DB_NAME);

        MongoClient mongoClient = new MongoClient(new MongoClientURI(dbUri));
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        return new SimulationResultDAO(mongoClient, mongoDatabase);
    }
}
