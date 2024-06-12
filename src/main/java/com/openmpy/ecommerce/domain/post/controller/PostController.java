package com.openmpy.ecommerce.domain.post.controller;

import com.openmpy.ecommerce.domain.post.dto.request.CreatePostRequestDto;
import com.openmpy.ecommerce.domain.post.dto.request.UpdatePostRequestDto;
import com.openmpy.ecommerce.domain.post.dto.response.CreatePostResponseDto;
import com.openmpy.ecommerce.domain.post.dto.response.GetPostResponseDto;
import com.openmpy.ecommerce.domain.post.dto.response.UpdatePostResponseDto;
import com.openmpy.ecommerce.domain.post.service.PostService;
import com.openmpy.ecommerce.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<CreatePostResponseDto> create(
            @Valid @RequestBody CreatePostRequestDto requestDto,
            Authentication authentication
    ) {
        String email = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        CreatePostResponseDto responseDto = postService.create(email, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<GetPostResponseDto> get(@PathVariable Long postId) {
        GetPostResponseDto responseDto = postService.get(postId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<GetPostResponseDto>> gets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<GetPostResponseDto> responseDtos = postService.gets(page, size);
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<UpdatePostResponseDto> update(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequestDto requestDto,
            Authentication authentication
    ) {
        String email = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        UpdatePostResponseDto responseDto = postService.update(email, postId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        String email = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        postService.delete(email, postId);
        return ResponseEntity.noContent().build();
    }
}
