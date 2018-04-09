package com.eyun.advertising.service;

import com.eyun.advertising.domain.Advertising;
import com.eyun.advertising.domain.Post;
import com.eyun.advertising.repository.AdvertisingRepository;
import com.eyun.advertising.repository.PostRepository;
import java.time.Instant;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.hibernate.engine.internal.JoinSequence.Join;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.JpaSort.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Advertising.
 */
@Service
@Transactional
public class AdvertisingService {

    private final Logger log = LoggerFactory.getLogger(AdvertisingService.class);

    private final AdvertisingRepository advertisingRepository;

    private PostRepository postRepository;
    
    public AdvertisingService(AdvertisingRepository advertisingRepository) {
        this.advertisingRepository = advertisingRepository;
    }

    /**
     * Save a advertising.
     *
     * @param advertising the entity to save
     * @return the persisted entity
     */
    public Advertising save(Advertising advertising) {
        log.debug("Request to save Advertising : {}", advertising);
        return advertisingRepository.save(advertising);
    }

    /**
     * Get all the advertisings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Advertising> findAll(Pageable pageable) {
        log.debug("Request to get all Advertisings");
        return advertisingRepository.findAll(pageable);
    }

    /**
     * Get one advertising by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Advertising findOne(Long id) {
        log.debug("Request to get Advertising : {}", id);
        return advertisingRepository.findOne(id);
    }

    /**
     * Delete the advertising by id.
     * 物理删除 设置修改时间和删除属性
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Advertising : {}", id);
        Advertising advertising = advertisingRepository.getOne(id);
        advertising.setModified_time(Instant.now());
        advertising.setDeleted(true);
        advertisingRepository.save(advertising);
        return ;
    }
    
    /**
     * 条件分页查询
     */
    public Page<Advertising> findDeleteAd(Pageable pageable ){
    	
    	Page<Advertising> page =  advertisingRepository.findAll(new Specification<Advertising>(){
    		/**
    		 * root是查询结果的一个实体对象，也就是查询结果返回的主要对象
    		 * criteriaQuery是构建查询条件，里面的方法都是各种查询方式，distinct，select，where，groupby，having，orderby这些方法
    		 * criteriaBuilder 这个接口 主要是用来进行一些函数操作
    		 */
			@Override
			public Predicate toPredicate(Root<Advertising> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				query.where(cb.equal(root.get("deleted"), true));				
				return null;
			}
    		
    	},pageable);
		return page;
    }

	public Page<Advertising> findNotDeletedAd(Pageable pageable) {
		Page<Advertising> page =  advertisingRepository.findAll(new Specification<Advertising>(){
    		/**
    		 * root是查询结果的一个实体对象，也就是查询结果返回的主要对象
    		 * criteriaQuery是构建查询条件，里面的方法都是各种查询方式，distinct，select，where，groupby，having，orderby这些方法
    		 * criteriaBuilder 这个接口 主要是用来进行一些函数操作
    		 */
			@Override
			public Predicate toPredicate(Root<Advertising> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				query.where(cb.equal(root.get("deleted"), false));				
				return null;
			}
    		
    	},pageable);
		return page;
	}

	/**
	 * 查找location对应的广告
	 * @param location
	 */
	public List<Advertising> findByLocation(String location,Boolean flag) {
		//设置条件参数
		List<Advertising> advertising = advertisingRepository.findAll(new Specification<Advertising>(){
			@Override
			public Predicate toPredicate(Root<Advertising> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//先设置advertising的条件
				Predicate p1 = cb.equal(root.get("deleted"), flag);
				//关联post表，root.getModel().getSet("posts",Post.class)得到post模型，posts为Advertsing的属性。使用内连接
				SetJoin<Advertising, Post> join = root.join(root.getModel().getSet("posts", Post.class),JoinType.INNER);
				Predicate p2 = cb.equal(join.get("location"), location);
				//查询
				query.where(cb.and(p1,p2));
				return null;
			}
		}
	);	
		return advertising;
	}
	
    
    /**
     * 单表查询未删除的广告
     * @param pageable
     * @return
     */
	public List<Advertising> notDeletedAd() {
		List<Advertising> list =  advertisingRepository.findAll(new Specification<Advertising>(){
    		/**
    		 * root是查询结果的一个实体对象，也就是查询结果返回的主要对象
    		 * criteriaQuery是构建查询条件，里面的方法都是各种查询方式，distinct，select，where，groupby，having，orderby这些方法
    		 * criteriaBuilder 这个接口 主要是用来进行一些函数操作
    		 */
			@Override
			public Predicate toPredicate(Root<Advertising> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				query.where(cb.equal(root.get("deleted"), false));				
				return null;
			}
    		
    	});
		
		return list;
	}
	
	

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
