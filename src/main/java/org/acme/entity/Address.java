package org.acme.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.Entity;
import java.util.Date;

@Entity(name = "address")
@Indexed
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address extends SearchableAddress implements Comparable {

    @KeywordField(searchable = Searchable.NO)
    public String address;

    public void setAddress(String address) {
        this.address = address;
    }

    @KeywordField(searchable = Searchable.NO)
    public String address_detail_pid;
    @KeywordField(searchable = Searchable.NO)
    public String street_locality_pid;
    @KeywordField(searchable = Searchable.NO)
    public String locality_pid;
    @KeywordField(searchable = Searchable.NO)
    public String building_name;
    @KeywordField(searchable = Searchable.NO)
    public String lot_number_prefix;
    @KeywordField(searchable = Searchable.NO)
    public String lot_number;
    @KeywordField(searchable = Searchable.NO)
    public String lot_number_suffix;

    @FullTextField(analyzer = "flatType")
    @KeywordField(name = "flat_type_sort", searchable = Searchable.YES, sortable = Sortable.YES, normalizer = "sort")
    public String flat_type;

    @KeywordField(searchable = Searchable.NO)
    public String flat_number_prefix;

    @FullTextField(analyzer = "flat")
    @KeywordField(name = "flat_number_sort", searchable = Searchable.YES, sortable = Sortable.YES, normalizer = "sort")
    public String flat_number;

    @KeywordField(searchable = Searchable.NO)
    public String flat_number_suffix;
    @KeywordField(searchable = Searchable.NO)
    public String level_type;
    @KeywordField(searchable = Searchable.NO)
    public String level_number_prefix;
    @GenericField(searchable = Searchable.NO)
    public Integer level_number;
    @KeywordField(searchable = Searchable.NO)
    public String level_number_suffix;
    @KeywordField(searchable = Searchable.NO)
    public String number_first_prefix;

    @FullTextField(analyzer = "number")
    @KeywordField(name = "number_first_sort", searchable = Searchable.YES, sortable = Sortable.YES, normalizer = "sort")
    public String number_first;

    @KeywordField(searchable = Searchable.NO)
    public String number_first_suffix;
    @KeywordField(searchable = Searchable.NO)
    public String number_last_prefix;
    @GenericField(searchable = Searchable.NO)
    public Integer number_last;
    @KeywordField(searchable = Searchable.NO)
    public String number_last_suffix;

    @FullTextField(analyzer = "location")
    @KeywordField(name = "street_name_sort", searchable = Searchable.YES, sortable = Sortable.YES, normalizer = "sort")
    public String street_name;

    @KeywordField(searchable = Searchable.NO)
    public String street_class_code;
    @KeywordField(searchable = Searchable.NO)
    public String street_class_type;

    @FullTextField(analyzer = "streetType")
    @KeywordField(name = "street_type_sort", searchable = Searchable.YES, sortable = Sortable.NO)
    public String street_type_code;

    @KeywordField(searchable = Searchable.NO)
    public String street_suffix_code;
    @KeywordField(searchable = Searchable.NO)
    public String street_suffix_type;

    @FullTextField(analyzer = "suburb")
    @KeywordField(name = "locality_sort", searchable = Searchable.YES, sortable = Sortable.YES, normalizer = "sort")
    public String locality_name;

    @KeywordField(searchable = Searchable.NO)
    public String state_abbreviation;
    @KeywordField(searchable = Searchable.NO)
    public String postcode;
    @GenericField(searchable = Searchable.NO)
    public Double latitude;
    @GenericField(searchable = Searchable.NO)
    public Double longitude;
    @KeywordField(searchable = Searchable.NO)
    public String geocode_type;
    @GenericField(searchable = Searchable.NO)
    public Integer confidence;
    @KeywordField(searchable = Searchable.NO)
    public String alias_principal;
    @KeywordField(searchable = Searchable.NO)
    public String primary_secondary;
    @KeywordField(searchable = Searchable.NO)
    public String legal_parcel_id;
    @GenericField(searchable = Searchable.NO)
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
        return address.equals(other.address);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Address)) {
            return 1;
        }
        Address other = (Address) o;
        return this.score.compareTo(other.score);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
