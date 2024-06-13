package com.openmpy.ecommerce.domain.post.scheduler;

import com.openmpy.ecommerce.domain.post.entity.PostEntity;
import com.openmpy.ecommerce.domain.post.repository.PostImageRepository;
import com.openmpy.ecommerce.domain.post.repository.PostLikeRepository;
import com.openmpy.ecommerce.domain.post.repository.PostReportRepository;
import com.openmpy.ecommerce.domain.post.repository.PostRepository;
import com.openmpy.ecommerce.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PostScheduler {

    private static final Logger log = LoggerFactory.getLogger(PostScheduler.class);

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostReportRepository postReportRepository;
    private final S3Service s3Service;

    // 정각마다 실행
    @Scheduled(cron = "0 0 * * * *")
    public void deleteReportedPosts() {
        List<PostEntity> postEntities = postRepository.findByIsDeletedTrue();

        for (PostEntity postEntity : postEntities) {
            postImageRepository.findAllByPostEntity(postEntity).forEach(postImageEntity -> {
                s3Service.deleteFile(postImageEntity.getImageUrl());
                postImageRepository.delete(postImageEntity);
            });

            postLikeRepository.deleteAll(postLikeRepository.findAllByPostEntity(postEntity));
            postReportRepository.deleteAll(postReportRepository.findAllByPostEntity(postEntity));
        }
        postRepository.deleteAll(postEntities);

        log.info("Delete reported posts count: {}", postEntities.size());
    }
}
