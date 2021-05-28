package org.acme.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SearchableAddress extends PanacheEntity {

    public SearchableAddress() {}

    @Transient
    public BigDecimal score;

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
