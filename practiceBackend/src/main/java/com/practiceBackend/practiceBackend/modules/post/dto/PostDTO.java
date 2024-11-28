package com.practiceBackend.practiceBackend.modules.post.dto;

import com.practiceBackend.practiceBackend.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private Long postId;
    private String writer;
    private String title;
    private String content;
    private boolean privatePost;
    private List<String> tags;
    private List<String> attachment;
    private boolean blockComment;
    private int views;
    private int likes;
    private LocalDateTime postDate;

    public static PostDTO convertToDTO(Post post) {
        return
        PostDTO.builder().postId(post.getPostkey())
                .writer(post.getUser().getUserid())
                .title(post.getTitle())
                .content(post.getText())
                .privatePost(post.isIsprivate())
                .tags(
                        post.getTags()
                        .stream()
                                .map(e -> e.getTagname())
                                .collect(Collectors.toList())
                )
                .attachment(
                        post.getAttachments()
                                .stream()
                                .map(e -> e.getFilename())
                                .collect(Collectors.toList())
                )
                .blockComment(post.isIsblockComment())
                .views(post.getViews())
                .likes(post.getLikes())
                .postDate(post.getCreatedAt())
                .build();


    }
}