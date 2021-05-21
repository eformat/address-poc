package org.acme.service;

import io.quarkus.runtime.StartupEvent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.acme.entity.Address;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@GraphQLApi
public class AddressResource {

    private final Logger log = LoggerFactory.getLogger(AddressResource.class);

    @Inject
    SearchSession searchSession;

    @Inject
    RestClient restClient;

    @ConfigProperty(name = "quarkus.hibernate-search-orm.schema-management.strategy")
    String indexLoadStrategy;

    @ConfigProperty(name = "index.onstart")
    String indexOnStart;

    @Transactional
    void onStart(@Observes StartupEvent ev) throws InterruptedException {
        if (indexOnStart.equalsIgnoreCase("true")) {
            searchSession.massIndexer()
                    .batchSizeToLoadObjects(20000)
                    .startAndWait();
        }
    }

    /*
      Optimized approach, uses elastic low level client
      Load query from json file, same query we can use to the elastic rest end point
     */
    private List<Address> _fetchLowLevelClient(String search, Integer size) {
        SearchRequest searchRequest = new SearchRequest("oneaddress-read");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String queryJson;
        if (search == null || search.isEmpty()) {
            JsonObject matchAll = new JsonObject().put("match_all", new JsonObject());
            queryJson = matchAll.encode();
        } else {
            queryJson = _readFile("/query-suggest-match.json");
            JsonObject qJson = new JsonObject(queryJson);
            _addJson(qJson, "size", size.toString());
            _addJson(qJson, "query.match.address.query", search.toLowerCase());
            _addJson(qJson, "suggest.address.prefix", search.toLowerCase());
            queryJson = qJson.encode();
        }
        log.info(">>> JSON " + queryJson);
        // make low level query request
        Request request = new Request(
                "POST",
                "/address-read/_search");
        request.setJsonEntity(queryJson);
        JsonObject json = null;
        try {
            org.elasticsearch.client.Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            json = new JsonObject(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        log.info(">>> search returned");
        return _processSearch(json);
    }

    private List<Address> _processSearch(JsonObject result) {
        JsonArray matches = result.getJsonObject("hits").getJsonArray("hits");
        JsonArray suggestions = result
                .getJsonObject("suggest")
                .getJsonArray("address").getJsonObject(0)
                .getJsonArray("options");

        HashSet<Address> uniqueList = new HashSet<>();
        for (int i = 0; i < matches.size(); i++) {
            JsonObject hit = matches.getJsonObject(i);
            Address address = hit.getJsonObject("_source").mapTo(Address.class);
            float score = hit.getFloat("_score");
            address.setScore(BigDecimal.valueOf(score));
            uniqueList.add(address);
        }
        for (int i = 0; i < suggestions.size(); i++) {
            JsonObject hit = suggestions.getJsonObject(i);
            Address address = hit.getJsonObject("_source").mapTo(Address.class);
            float score = hit.getFloat("_score");
            address.setScore(BigDecimal.valueOf(score));
            uniqueList.add(address);
        }
        List<Address> list = new ArrayList<>(uniqueList);
        list.sort(Collections.reverseOrder());
        return list;
    }

    /*
      Search using elastic completion suggester.
      We want ONE call to ES here to keep things fast.
      This requires the data is engineered into a single address.address text field first.
     */
    @Query
    @Description("Search address by search term")
    public List<Address> address(@Name("search") String search, @Name("size") Optional<Integer> size) {
        String finalSearch = (search == null) ? "" : search.trim().toLowerCase();
        log.info(">>> Final Search Words: finalSearch(" + finalSearch + ")");
        return _fetchLowLevelClient(search, size.orElse(15));
    }

    public String _readFile(String fileName) {
        String contents = null;
        try (InputStream inputStream = getClass().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            contents = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }

    // _addJson(jsonObject, "ab.g", "foo2");
    private static void _addJson(JsonObject jsonObject, String key, String value) {
        if (key.contains(".")) {
            String innerKey = key.substring(0, key.indexOf("."));
            String remaining = key.substring(key.indexOf(".") + 1);

            if (jsonObject.containsKey(innerKey)) {
                _addJson(jsonObject.getJsonObject(innerKey), remaining, value);
            } else {
                JsonObject innerJson = new JsonObject();
                jsonObject.put(innerKey, innerJson);
                _addJson(innerJson, remaining, value);
            }
        } else {
            jsonObject.put(key, value);
        }
    }
}
