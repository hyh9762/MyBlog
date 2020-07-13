package com.itomelet.blog.vo;

public class BlogQuery {
    private String title;
    private String content;
    private boolean recommend;
    private Long typeId;

    public BlogQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "BlogQuery{" +
               "title='" + title + '\'' +
               ", content='" + content + '\'' +
               ", recommend=" + recommend +
               ", typeId=" + typeId +
               '}';
    }
}
