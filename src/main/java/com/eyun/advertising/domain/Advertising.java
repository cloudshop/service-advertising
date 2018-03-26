package com.eyun.advertising.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Advertising.
 */
@Entity
@Table(name = "advertising")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Advertising implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image")
    private String image;

    @Column(name = "jhi_link")
    private String link;

    @Column(name = "alt")
    private String alt;

    @Column(name = "extend")
    private String extend;

    @Column(name = "created_time")
    private Instant created_time;

    @Column(name = "modified_time")
    private Instant modified_time;

    @Column(name = "version")
    private Integer version;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public Advertising image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public Advertising link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAlt() {
        return alt;
    }

    public Advertising alt(String alt) {
        this.alt = alt;
        return this;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getExtend() {
        return extend;
    }

    public Advertising extend(String extend) {
        this.extend = extend;
        return this;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public Instant getCreated_time() {
        return created_time;
    }

    public Advertising created_time(Instant created_time) {
        this.created_time = created_time;
        return this;
    }

    public void setCreated_time(Instant created_time) {
        this.created_time = created_time;
    }

    public Instant getModified_time() {
        return modified_time;
    }

    public Advertising modified_time(Instant modified_time) {
        this.modified_time = modified_time;
        return this;
    }

    public void setModified_time(Instant modified_time) {
        this.modified_time = modified_time;
    }

    public Integer getVersion() {
        return version;
    }

    public Advertising version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Advertising advertising = (Advertising) o;
        if (advertising.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), advertising.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Advertising{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", link='" + getLink() + "'" +
            ", alt='" + getAlt() + "'" +
            ", extend='" + getExtend() + "'" +
            ", created_time='" + getCreated_time() + "'" +
            ", modified_time='" + getModified_time() + "'" +
            ", version=" + getVersion() +
            "}";
    }
}
