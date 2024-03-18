package com.example.fileloader;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AccountIndexManager {

    private final ElasticsearchOperations elasticsearchOperations;

    public AccountIndexManager(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public void createMonthlyIndexWithAlias(String aliasName) {
        String indexName = "account-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
        IndexOperations indexOperations = elasticsearchOperations.indexOps(indexCoordinates);

        if (!indexOperations.exists()) {
            indexOperations.create();
            indexOperations.putMapping(indexOperations.createMapping(Account.class));
            indexOperations.alias(aliasName);
        }
    }
}

@Document(indexName = "account-#{T(java.time.LocalDate).now().format(T(java.time.format.DateTimeFormatter).ofPattern(\"yyyy-MM\"))}")
class Account {
    @Id
    private String id;

    // Define other fields using @Field annotation
    @Field(type = FieldType.Text)
    private String accountName;

    // Other account fields...

    // Getters and setters...
}
