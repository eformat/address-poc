package org.acme.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Indexed
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address extends PanacheEntity implements Comparable {

    @FullTextField(analyzer = "address")
    public String address;

    public void setAddress(String address) {
        this.address = address;
    }

    @Transient
    public BigDecimal score;

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String address_detail_pid;
    public String street_locality_pid;
    public String locality_pid;
    public String building_name;
    public String lot_number_prefix;
    public String lot_number;
    public String lot_number_suffix;

    @FullTextField(analyzer = "flatType")
    @KeywordField(name = "flat_type_sort", searchable = Searchable.YES, sortable = Sortable.YES, normalizer = "sort")
    public String flat_type;
    public String flat_number_prefix;

    @FullTextField(analyzer = "flat")
    @KeywordField(name = "flat_number_sort", searchable = Searchable.YES, sortable = Sortable.YES, normalizer = "sort")
    public String flat_number;

    public String flat_number_suffix;
    public String level_type;
    public String level_number_prefix;
    public Integer level_number;
    public String level_number_suffix;
    public String number_first_prefix;

    @FullTextField(analyzer = "number")
    @KeywordField(name = "number_first_sort", searchable = Searchable.YES, sortable = Sortable.YES, normalizer = "sort")
    public String number_first;

    public String number_first_suffix;
    public String number_last_prefix;
    public Integer number_last;
    public String number_last_suffix;

    @FullTextField(analyzer = "location")
    @KeywordField(name = "street_name_sort", searchable = Searchable.YES, sortable = Sortable.YES, normalizer = "sort")
    public String street_name;

    public String street_class_code;
    public String street_class_type;

    @FullTextField(analyzer = "streetType")
    @KeywordField(name = "street_type_sort", searchable = Searchable.YES, sortable = Sortable.NO)
    public String street_type_code;
    public String street_suffix_code;
    public String street_suffix_type;

    @FullTextField(analyzer = "suburb")
    @KeywordField(name = "locality_sort", searchable = Searchable.YES, sortable = Sortable.YES, normalizer = "sort")
    public String locality_name;

    public String state_abbreviation;
    public String postcode;
    public Double latitude;
    public Double longitude;
    public String geocode_type;
    public Integer confidence;
    public String alias_principal;
    public String primary_secondary;
    public String legal_parcel_id;
    public Date date_created;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        Address other = (Address) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Address)) {
            return 1;
        }
        Address other = (Address) o;
        return this.score.compareTo(other.score);
    }
}
