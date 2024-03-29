package com.eyun.advertising.repository;

import com.eyun.advertising.domain.Advertising;
import com.eyun.advertising.domain.Post;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Post entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostRepository extends JpaRepository<Post, Long>,JpaSpecificationExecutor<Post> {

}
