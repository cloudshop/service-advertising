package com.eyun.advertising.service;

import com.eyun.advertising.domain.Post;
import com.eyun.advertising.repository.PostRepository;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Post.
 */
@Service
@Transactional
public class PostService {

    private final Logger log = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Save a post.
     *
     * @param post the entity to save
     * @return the persisted entity
     */
    public Post save(Post post) {
        log.debug("Request to save Post : {}", post);
        return postRepository.save(post);
    }

    /**
     * Get all the posts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) {
        log.debug("Request to get all Posts");
        return postRepository.findAll(pageable);
    }

    /**
     * Get one post by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Post findOne(Long id) {
        log.debug("Request to get Post : {}", id);
        return postRepository.findOne(id);
    }

    /**
     * Delete the post by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
    	//设置修改时间和删除属性
        log.debug("Request to delete Post : {}", id);
        Post post = postRepository.getOne(id);
        post.setDeleted(true);
        post.setModified_time(Instant.now());
        postRepository.save(post);
        
        return ;
    }    
    
}
