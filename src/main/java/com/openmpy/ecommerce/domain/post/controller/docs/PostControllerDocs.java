package com.openmpy.ecommerce.domain.post.controller.docs;

import com.openmpy.ecommerce.domain.post.dto.request.CreatePostRequestDto;
import com.openmpy.ecommerce.domain.post.dto.request.UpdatePostRequestDto;
import com.openmpy.ecommerce.domain.post.dto.response.CreatePostResponseDto;
import com.openmpy.ecommerce.domain.post.dto.response.GetPostResponseDto;
import com.openmpy.ecommerce.domain.post.dto.response.UpdatePostResponseDto;
import com.openmpy.ecommerce.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "게시글", description = "게시글 관련 API")
public interface PostControllerDocs {

    @Operation(summary = "게시글 작성 기능", description = "게시글 작성 API")
    ResponseEntity<CreatePostResponseDto> create(
            @Valid @RequestPart CreatePostRequestDto requestDto,
            @RequestPart(required = false) List<MultipartFile> multipartFiles,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "게시글 조회 기능", description = "게시글 조회 API")
    ResponseEntity<GetPostResponseDto> get(@PathVariable Long postId);

    @Operation(summary = "게시글 목록 조회 기능", description = "게시글 목록 조회 API")
    ResponseEntity<Page<GetPostResponseDto>> gets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "게시글 수정 기능", description = "게시글 수정 API")
    ResponseEntity<UpdatePostResponseDto> update(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "게시글 삭제 기능", description = "게시글 삭제 API")
    ResponseEntity<Void> delete(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "게시글 검색 기능", description = "게시글 검색 API")
    ResponseEntity<Page<GetPostResponseDto>> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "게시글 좋아요 기능", description = "게시글 좋아요 API")
    ResponseEntity<Void> like(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "게시글 신고 기능", description = "게시글 신고 API")
    ResponseEntity<Void> report(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );
}
