package com.elepy.tests.dao;

import com.elepy.dao.FilterType;
import com.elepy.dao.Page;
import com.elepy.exceptions.ElepyException;
import com.elepy.tests.ElepyConfigHelper;
import com.elepy.tests.ElepySystemUnderTest;
import com.elepy.tests.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.elepy.dao.FilterType.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class FiltersTest implements ElepyConfigHelper {
    private ElepySystemUnderTest elepy;

    @BeforeEach
    void before() {
        elepy = ElepySystemUnderTest.create();

        this.configureElepy(elepy);
        elepy.addModel(Product.class);

        elepy.start();

        Unirest.setHttpClient(HttpClients.custom().disableCookieManagement().build());
    }

//    EQUALS("Equals", "equals",FieldType.values()),
//    NOT_NULL("Has Value", "notNull", FieldType.values()),
//    IS_NULL("Doesn't have value", "isNull", FieldType.values()),
//    NOT_EQUALS("Not Equals", "notEquals", FieldType.values()),
//    CONTAINS("Contains", "contains", FieldType.TEXT, FieldType.ARRAY),
//
//    //Numbers & Dates
//    GREATER_THAN("Greater than", "gt", FieldType.NUMBER, FieldType.DATE),
//    LESSER_THAN("Lesser than", "lt", FieldType.NUMBER, FieldType.DATE),
//    GREATER_THAN_OR_EQUALS("Greater than or equal to", "gte", FieldType.NUMBER, FieldType.DATE),
//    LESSER_THAN_OR_EQUALS("Lesser than or equal to", "lte", FieldType.NUMBER, FieldType.DATE),
//
//    //Strings
//    STARTS_WITH("Starts with", "startsWith", FieldType.TEXT, FieldType.ARRAY);


    @Test
    public void canFilter_EQUALS_onString() {
        var product = new Product();
        product.setShortDescription("Ryan");

        seedWithProducts(product);


        assertThat(executeFilter("shortDescription", EQUALS, "Ryan"))
                .hasSize(1);
        assertThat(executeFilter("shortDescription", EQUALS, "NotRyan"))
                .hasSize(0);
    }

    @Test
    public void canFilter_EQUALS_onNumber() {
        var product = new Product();
        product.setPrice(BigDecimal.TEN);

        seedWithProducts(product);


        assertThat(executeFilter("price", EQUALS, 10))
                .hasSize(1);
        assertThat(executeFilter("price", EQUALS, 20))
                .hasSize(0);
    }

    @Test
    public void canFilter_NOT_EQUALS_onString() {
        var product = new Product();
        product.setShortDescription("Ryan");

        seedWithProducts(product);


        assertThat(executeFilter("shortDescription", NOT_EQUALS, "Ryan"))
                .hasSize(0);
        assertThat(executeFilter("shortDescription", NOT_EQUALS, "NotRyan"))
                .hasSize(1);
    }

    @Test
    public void canFilter_NOT_EQUALS_onNumber() {
        var product = new Product();
        product.setPrice(BigDecimal.TEN);

        seedWithProducts(product);

        assertThat(executeFilter("price", NOT_EQUALS, 20))
                .hasSize(1);
        assertThat(executeFilter("price", NOT_EQUALS, 10))
                .hasSize(0);
    }

    @Test
    public void canFilter_IS_NULL_onString() {
        var product = new Product();
        product.setShortDescription(null);

        seedWithProducts(product);


        assertThat(executeFilter("shortDescription", IS_NULL, "true"))
                .hasSize(1);
    }

    @Test
    public void canFilter_NOT_NULL_onString() {
        var product = new Product();
        product.setShortDescription("not null");

        seedWithProducts(product);


        assertThat(executeFilter("shortDescription", NOT_NULL, "true"))
                .hasSize(1);
    }

    @Test
    public void canFilter_STARTS_WITH_onString() {
        var product = new Product();
        product.setShortDescription("Ryan");

        seedWithProducts(product);


        assertThat(executeFilter("shortDescription", STARTS_WITH, "Rya"))
                .hasSize(1);
        assertThat(executeFilter("shortDescription", STARTS_WITH, "NotRya"))
                .hasSize(0);
    }

    @Test
    public void canFilter_CONTAINS_onString() {
        var product = new Product();
        product.setShortDescription("Ryan");

        seedWithProducts(product);


        assertThat(executeFilter("shortDescription", CONTAINS, "ya"))
                .hasSize(1);
        assertThat(executeFilter("shortDescription", CONTAINS, "Notya"))
                .hasSize(0);
    }

    @Test
    public void canFilter_CONTAINS_onArray() {
        var product = new Product();
        product.setTags(List.of("Ryan", "Made", "This"));

        seedWithProducts(product);


        assertThat(executeFilter("tags", CONTAINS, "Ryan"))
                .hasSize(1);
        assertThat(executeFilter("tags", CONTAINS, "Made"))
                .hasSize(1);
        assertThat(executeFilter("tags", CONTAINS, "This"))
                .hasSize(1);
        assertThat(
                executeFilters(
                        filter("tags", CONTAINS, "Ryan"),
                        filter("tags", CONTAINS, "Made"),
                        filter("tags", CONTAINS, "This")
                )
        ).hasSize(1);
        assertThat(executeFilter("tags", CONTAINS, "NotInArray"))
                .hasSize(0);
    }


    @Test
    void canFilter_GREATER_THAN_onNumber() {

        var product = new Product();
        product.setPrice(BigDecimal.TEN);

        seedWithProducts(product);


        assertThat(executeFilter("price", GREATER_THAN, 9))
                .hasSize(1);
        assertThat(executeFilter("price", GREATER_THAN, 10))
                .hasSize(0);
    }

    @Test
    void canFilter_GREATER_THAN_OR_EQUALS_onNumber() {

        var product = new Product();
        product.setPrice(BigDecimal.TEN);

        seedWithProducts(product);


        assertThat(executeFilter("price", GREATER_THAN_OR_EQUALS, 9))
                .hasSize(1);
        assertThat(executeFilter("price", GREATER_THAN_OR_EQUALS, 10))
                .hasSize(1);
        assertThat(executeFilter("price", GREATER_THAN_OR_EQUALS, 11))
                .hasSize(0);
    }


    @Test
    void canFilter_LESSER_THAN_onNumber() {

        var product = new Product();
        product.setPrice(BigDecimal.TEN);

        seedWithProducts(product);


        assertThat(executeFilter("price", LESSER_THAN, 11))
                .hasSize(1);
        assertThat(executeFilter("price", LESSER_THAN, 10))
                .hasSize(0);
    }

    @Test
    void canFilter_LESSER_THAN_OR_EQUALS_onNumber() {

        var product = new Product();
        product.setPrice(BigDecimal.TEN);

        seedWithProducts(product);


        assertThat(executeFilter("price", LESSER_THAN_OR_EQUALS, 11))
                .hasSize(1);
        assertThat(executeFilter("price", LESSER_THAN_OR_EQUALS, 10))
                .hasSize(1);
        assertThat(executeFilter("price", LESSER_THAN_OR_EQUALS, 9))
                .hasSize(0);
    }

    protected FilterOption filter(String fieldName, FilterType filterType, Object value) {
        return new FilterOption(fieldName, filterType, value);
    }

    protected List<Product> executeFilter(String fieldName, FilterType filterType, Object value) {
        return executeFilters(filter(fieldName, filterType, value));
    }

    protected List<Product> executeFilters(FilterOption... options) {
        return executeFilters(List.of(options));
    }

    protected List<Product> executeFilters(Iterable<FilterOption> options) {
        try {
            final var request = Unirest.get(elepy.url() + "/products");

            options.forEach(option -> request.queryString(String.format("%s_%s", option.fieldName, option.filterType.getName()), option.value));

            var response = request.asJson();
            if (response.getStatus() >= 400) {
                throw new ElepyException(response.getBody().getObject().getString("message"), response.getStatus());
            }
            final var mapper = new ObjectMapper();
            JavaType type = mapper.getTypeFactory().constructParametricType(Page.class, Product.class);
            return ((Page<Product>) mapper.readValue(response.getBody().toString(), type)).getValues();
        } catch (JsonProcessingException | UnirestException e) {
            throw new RuntimeException(e);
        }
    }


    protected void seedWithProducts(Product... product) {
        elepy.getCrudFor(Product.class).create(product);
    }

    private static class FilterOption {
        private final String fieldName;
        private final FilterType filterType;
        private final Object value;

        FilterOption(String fieldName, FilterType filterType, Object value) {
            this.fieldName = fieldName;
            this.filterType = filterType;
            this.value = value;
        }
    }
} 