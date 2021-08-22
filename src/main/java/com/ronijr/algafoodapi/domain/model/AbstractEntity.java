package com.ronijr.algafoodapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Transient;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractEntity<T> {

    @Transient
    @JsonIgnore
    private UUID uuid = null;

    public abstract T getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof AbstractEntity)) return false;
        AbstractEntity<?> that = (AbstractEntity<?>) o;
        if (this.getId() != null && that.getId() != null) return this.getId().equals(that.getId());
        return this.getUUID().equals(that.getUUID());
    }

    @Override
    public int hashCode() {
        if (this.getId() != null) {
            return Objects.hash(this.getId());
        }
        return this.getUUID().hashCode();
    }

    public final UUID getUUID() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        return this.uuid;
    }
}
