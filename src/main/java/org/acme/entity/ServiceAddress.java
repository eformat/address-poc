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
    public String SERVICE_LOCATION_ID;
    @GenericField(searchable = Searchable.NO)
    public String TUI;
    @GenericField(searchable = Searchable.NO)
    public String VUI;
    @GenericField(searchable = Searchable.NO)
    public String TLC;
    @GenericField(searchable = Searchable.NO)
    public String PLSAM;
    @GenericField(searchable = Searchable.NO)
    public Double WGS84_LAT;
    @GenericField(searchable = Searchable.NO)
    public Double WGS84_LONG;
    @GenericField(searchable = Searchable.NO)
    public String IN_USE_TYPE;
    @GenericField(searchable = Searchable.NO)
    public String ADDR_TYPE;
    @GenericField(searchable = Searchable.NO)
    public String UNIT_TYPE;
    @GenericField(searchable = Searchable.NO)
    public String UNIT_IDENTIFIER_LOW;
    @GenericField(searchable = Searchable.NO)
    public String UNIT_IDENTIFIER_HIGH;
    @GenericField(searchable = Searchable.NO)
    public String HOUSE_LOW;
    @GenericField(searchable = Searchable.NO)
    public String HOUSE_LOW_SUFFIX;
    @GenericField(searchable = Searchable.NO)
    public String HOUSE_HIGH;
    @GenericField(searchable = Searchable.NO)
    public String HOUSE_HIGH_SUFFIX;
    @GenericField(searchable = Searchable.NO)
    public String LEVEL_TYPE;
    @GenericField(searchable = Searchable.NO)
    public String LEVEL_NUMBER;
    @GenericField(searchable = Searchable.NO)
    public String STREET_NAME;
    @GenericField(searchable = Searchable.NO)
    public String STREET_TYPE;
    @GenericField(searchable = Searchable.NO)
    public String STREET_DIRECTION;
    @GenericField(searchable = Searchable.NO)
    public String HABITATION_NAME;
    @GenericField(searchable = Searchable.NO)
    public String LOCALITY_TYPE;
    @GenericField(searchable = Searchable.NO)
    public String SUBURB;
    @GenericField(searchable = Searchable.NO)
    public String CITY_NAME;
    @GenericField(searchable = Searchable.NO)
    public String CITY_LOCALITY;
    @GenericField(searchable = Searchable.NO)
    public String REGION;
    @GenericField(searchable = Searchable.NO)
    public String POSTCODE_ZONE;
    @GenericField(searchable = Searchable.NO)
    public String POSITION_TYPE;
    @GenericField(searchable = Searchable.NO)
    public String FORMAT;
    @GenericField(searchable = Searchable.NO)
    public String LATEST_ALPHA_DATA_SOURCE;
    @GenericField(searchable = Searchable.NO)
    public String NZAMSUFI;
    @GenericField(searchable = Searchable.NO)
    public String IS_RESIDENTIAL;
    @GenericField(searchable = Searchable.NO)
    public String RD_SEG_SIDE;
    @GenericField(searchable = Searchable.NO)
    public String LATEST_SPATIAL_DATA_SOURCE;
    @GenericField(searchable = Searchable.NO)
    public String AUTO_CREATED;
    @GenericField(searchable = Searchable.NO)
    public String AUTO_CREATED_REASON;
    @GenericField(searchable = Searchable.NO)
    public String BUILDING_ASSOCIATION;
    @GenericField(searchable = Searchable.NO)
    public String TOWN_TYPE;
    @GenericField(searchable = Searchable.NO)
    public String COMPLEX_ADDR_TYPE;
    @GenericField(searchable = Searchable.NO)
    public String COMPLEX_NAME;
    @GenericField(searchable = Searchable.NO)
    public String TA_CODE;
    @GenericField(searchable = Searchable.NO)
    public String REGION_NAME;
    @GenericField(searchable = Searchable.NO)
    public String MESHBLOCK_ID;
    @GenericField(searchable = Searchable.NO)
    public String NAME_TYPE;
    @GenericField(searchable = Searchable.NO)
    public String SUFFIX_ABBREV;
    @GenericField(searchable = Searchable.NO)
    public String NAME_STATUS;
    @GenericField(searchable = Searchable.NO)
    public String RD_SEG_RENAMED_INDICATOR;
    @GenericField(searchable = Searchable.NO)
    public String TYPE_ABBREV;
    @GenericField(searchable = Searchable.NO)
    public String BUILDING_TYPE;
    @GenericField(searchable = Searchable.NO)
    public String SITE_CODE;
    @GenericField(searchable = Searchable.NO)
    public String LOCATION_CODE;
    @GenericField(searchable = Searchable.NO)
    public String PARCEL_ID;
    @GenericField(searchable = Searchable.NO)
    public String DENSITY_ZONE;

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

