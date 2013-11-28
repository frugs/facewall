package data.dao.database;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import java.util.List;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.newArrayList;

public class FacewallDB {
    public enum NodeIndex {

        Persons("Persons", "id"),
        Teams("Teams", "id");

        final String name;

        final String key;
        private NodeIndex(String name, String key) {
            this.name = name;
            this.key = key;
        }

    }
    private final GraphDatabaseService db;

    public FacewallDB(GraphDatabaseService db) {
        this.db = db;
    }

    public Transaction beginTransaction() {
        return db.beginTx();
    }

    public IndexHits<Node> lookupNodesInIndex(IndexQuery indexQuery) {
        Index<Node> index = db.index().forNodes(indexQuery.indexName);
        return index.get(indexQuery.keyName, indexQuery.queriedValue);
    }

    public List<Node> findRelatedNodes(Node node) {
        List<Node> relatedNodes = newArrayList();
        for (Relationship relationship : node.getRelationships()) {
            relatedNodes.add(relationship.getOtherNode(node));
        }

        return copyOf(relatedNodes);
    }
}