package com.openmpy.ecommerce.domain.post.dto.response;

import java.util.List;

public record GetsPostResponseDto(
        List<GetPostResponseDto> posts
) {
}
