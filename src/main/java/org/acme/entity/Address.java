package org.acme.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.Entity;
import java.util.Date;

@Entity(name = "address")
@Indexed
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address extends SearchableAddress implements Comparable {

    @GenericField(searchable = Searchable.NO)
    public String address;

    public void setAddress(String address) {
        this.address = address;
    }

    @GenericField(searchable = Searchable.NO)
    public String address_detail_pid;
    @GenericField(searchable = Searchable.NO)
    public String street_locality_pid;
    @GenericField(searchable = Searchable.NO)
    public String locality_pid;
    @GenericField(searchable = Searchable.NO)
    public String building_name;
    @GenericField(searchable = Searchable.NO)
    public String lot_number_prefix;
    @GenericField(searchable = Searchable.NO)
    public String lot_number;
    @GenericField(searchable = Searchable.NO)
    public String lot_number_suffix;
    @GenericField(searchable = Searchable.NO)
    public String flat_type;
    @GenericField(searchable = Searchable.NO)
    public String flat_number_prefix;
    @GenericField(searchable = Searchable.NO)
    public String flat_number;
    @GenericField(searchable = Searchable.NO)
    public String flat_number_suffix;
    @GenericField(searchable = Searchable.NO)
    public String level_type;
    @GenericField(searchable = Searchable.NO)
    public String level_number_prefix;
    @GenericField(searchable = Searchable.NO)
    public Integer level_number;
    @GenericField(searchable = Searchable.NO)
    public String level_number_suffix;
    @GenericField(searchable = Searchable.NO)
    public String number_first_prefix;
    @GenericField(searchable = Searchable.NO)
    public String number_first;
    @GenericField(searchable = Searchable.NO)
    public String number_first_suffix;
    @GenericField(searchable = Searchable.NO)
    public String number_last_prefix;
    @GenericField(searchable = Searchable.NO)
    public Integer number_last;
    @GenericField(searchable = Searchable.NO)
    public String number_last_suffix;
    @GenericField(searchable = Searchable.NO)
    public String street_name;
    @GenericField(searchable = Searchable.NO)
    public String street_class_code;
    @GenericField(searchable = Searchable.NO)
    public String street_class_type;
    @GenericField(searchable = Searchable.NO)
    public String street_type_code;
    @GenericField(searchable = Searchable.NO)
    public String street_suffix_code;
    @GenericField(searchable = Searchable.NO)
    public String street_suffix_type;
    @GenericField(searchable = Searchable.NO)
    public String locality_name;
    @GenericField(searchable = Searchable.NO)
    public String state_abbreviation;
    @GenericField(searchable = Searchable.NO)
    public String postcode;
    @GenericField(searchable = Searchable.NO)
    public Double latitude;
    @GenericField(searchable = Searchable.NO)
    public Double longitude;
    @GenericField(searchable = Searchable.NO)
    public String geocode_type;
    @GenericField(searchable = Searchable.NO)
    public Integer confidence;
    @GenericField(searchable = Searchable.NO)
    public String alias_principal;
    @GenericField(searchable = Searchable.NO)
    public String primary_secondary;
    @GenericField(searchable = Searchable.NO)
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
