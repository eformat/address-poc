package org.acme.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.Entity;

@Entity(name = "serviceaddress")
@Indexed
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceAddress extends SearchableAddress implements Comparable {

    @GenericField(searchable = Searchable.NO)
    public String service_location_id;
    @GenericField(searchable = Searchable.NO)
    public String tui;
    @GenericField(searchable = Searchable.NO)
    public String vui;
    @GenericField(searchable = Searchable.NO)
    public String tlc;
    @GenericField(searchable = Searchable.NO)
    public String plsam;
    @GenericField(searchable = Searchable.NO)
    public Double wgs84_lat;
    @GenericField(searchable = Searchable.NO)
    public Double wgs84_long;
    @GenericField(searchable = Searchable.NO)
    public String in_use_type;
    @GenericField(searchable = Searchable.NO)
    public String addr_type;
    @GenericField(searchable = Searchable.NO)
    public String unit_type;
    @GenericField(searchable = Searchable.NO)
    public String unit_identifier_low;
    @GenericField(searchable = Searchable.NO)
    public String unit_identifier_high;
    @GenericField(searchable = Searchable.NO)
    public String house_low;
    @GenericField(searchable = Searchable.NO)
    public String house_low_suffix;
    @GenericField(searchable = Searchable.NO)
    public String house_high;
    @GenericField(searchable = Searchable.NO)
    public String house_high_suffix;
    @GenericField(searchable = Searchable.NO)
    public String level_type;
    @GenericField(searchable = Searchable.NO)
    public String level_number;
    @GenericField(searchable = Searchable.NO)
    public String street_name;
    @GenericField(searchable = Searchable.NO)
    public String street_type;
    @GenericField(searchable = Searchable.NO)
    public String street_direction;
    @GenericField(searchable = Searchable.NO)
    public String habitation_name;
    @GenericField(searchable = Searchable.NO)
    public String locality_type;
    @GenericField(searchable = Searchable.NO)
    public String suburb;
    @GenericField(searchable = Searchable.NO)
    public String city_name;
    @GenericField(searchable = Searchable.NO)
    public String city_locality;
    @GenericField(searchable = Searchable.NO)
    public String region;
    @GenericField(searchable = Searchable.NO)
    public String postcode_zone;
    @GenericField(searchable = Searchable.NO)
    public String position_type;
    @GenericField(searchable = Searchable.NO)
    public String format;
    @GenericField(searchable = Searchable.NO)
    public String latest_alpha_data_source;
    @GenericField(searchable = Searchable.NO)
    public String nzamsufi;
    @GenericField(searchable = Searchable.NO)
    public String is_residential;
    @GenericField(searchable = Searchable.NO)
    public String rd_seg_side;
    @GenericField(searchable = Searchable.NO)
    public String latest_spatial_data_source;
    @GenericField(searchable = Searchable.NO)
    public String auto_created;
    @GenericField(searchable = Searchable.NO)
    public String auto_created_reason;
    @GenericField(searchable = Searchable.NO)
    public String building_association;
    @GenericField(searchable = Searchable.NO)
    public String town_type;
    @GenericField(searchable = Searchable.NO)
    public String complex_addr_type;
    @GenericField(searchable = Searchable.NO)
    public String complex_name;
    @GenericField(searchable = Searchable.NO)
    public String ta_code;
    @GenericField(searchable = Searchable.NO)
    public String region_name;
    @GenericField(searchable = Searchable.NO)
    public String meshblock_id;
    @GenericField(searchable = Searchable.NO)
    public String name_type;
    @GenericField(searchable = Searchable.NO)
    public String suffix_abbrev;
    @GenericField(searchable = Searchable.NO)
    public String name_status;
    @GenericField(searchable = Searchable.NO)
    public String rd_seg_renamed_indicator;
    @GenericField(searchable = Searchable.NO)
    public String type_abbrev;
    @GenericField(searchable = Searchable.NO)
    public String building_type;
    @GenericField(searchable = Searchable.NO)
    public String site_code;
    @GenericField(searchable = Searchable.NO)
    public String location_code;
    @GenericField(searchable = Searchable.NO)
    public String parcel_id;
    @GenericField(searchable = Searchable.NO)
    public String density_zone;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceAddress)) {
            return false;
        }
        ServiceAddress other = (ServiceAddress) o;
        return address.equals(other.address);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof ServiceAddress)) {
            return 1;
        }
        ServiceAddress other = (ServiceAddress) o;
        return this.score.compareTo(other.score);
    }

    @Override
    public int hashCode() {
        return 33;
    }
}

