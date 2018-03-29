package com.eyun.advertising.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Post.
 */
@Entity
@Table(name = "post")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "sort_id")
    private Integer sort_id;

    @Column(name = "created_time")
    private Instant created_time;

    @Column(name = "modified_time")
    private Instant modified_time;

    @Column(name = "version")
    private Integer version;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    private Advertising advertising;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public Post location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSort_id() {
        return sort_id;
    }

    public Post sort_id(Integer sort_id) {
        this.sort_id = sort_id;
        return this;
    }

    public void setSort_id(Integer sort_id) {
        this.sort_id = sort_id;
    }

    public Instant getCreated_time() {
        return created_time;
    }

    public Post created_time(Instant created_time) {
        this.created_time = created_time;
        return this;
    }

    public void setCreated_time(Instant created_time) {
        this.created_time = created_time;
    }

    public Instant getModified_time() {
        return modified_time;
    }

    public Post modified_time(Instant modified_time) {
        this.modified_time = modified_time;
        return this;
    }

    public void setModified_time(Instant modified_time) {
        this.modified_time = modified_time;
    }

    public Integer getVersion() {
        return version;
    }

    public Post version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Post deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getStatus() {
        return status;
    }

    public Post status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Advertising getAdvertising() {
        return advertising;
    }

    public Post advertising(Advertising advertising) {
        this.advertising = advertising;
        return this;
    }

    public void setAdvertising(Advertising advertising) {
        this.advertising = advertising;
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
        Post post = (Post) o;
        if (post.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), post.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Post{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", sort_id=" + getSort_id() +
            ", created_time='" + getCreated_time() + "'" +
            ", modified_time='" + getModified_time() + "'" +
            ", version=" + getVersion() +
            ", deleted='" + isDeleted() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
