package com.itomelet.blog.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_blog")
public class Blog {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;
    private String firstPicture;
    private String flag;
    private Integer views;
    private boolean appreciation;
    private boolean shareStatement; //转载声明
    private boolean commentabled;  //评论
    private boolean published;
    private boolean recommend; //是否推荐
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    private String description;
    private String subUrl;

    @Transient
    private String[] tagIds;

    @ManyToOne
    private User user;

    @ManyToOne
    private Type type;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "blog")
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    public Blog() {
    }

    public Blog(Long id, String title, String firstPicture, Integer views, Boolean published, Type type, Date createTime) {
        this.id = id;
        this.title = title;
        this.firstPicture = firstPicture;
        this.views = views;
        this.published = published;
        this.type = type;
        this.createdTime = createTime;
    }

    /*public void init() {
        this.tagIds = tagsToIds(this.tags);
    }

      private String[] tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {

            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }*/

    public String getSubUrl() {
        return subUrl;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTagIds() {
        return tagIds;
    }

    public void setTagIds(String[] tagIds) {
        this.tagIds = tagIds;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public boolean isAppreciation() {
        return appreciation;
    }

    public void setAppreciation(boolean appreciation) {
        this.appreciation = appreciation;
    }

    public boolean isShareStatement() {
        return shareStatement;
    }

    public void setShareStatement(boolean shareStatement) {
        this.shareStatement = shareStatement;
    }

    public boolean isCommentabled() {
        return commentabled;
    }

    public void setCommentabled(boolean comment) {
        this.commentabled = comment;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createTime) {
        this.createdTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Blog{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", content='" + content + '\'' +
               ", firstPicture='" + firstPicture + '\'' +
               ", flag='" + flag + '\'' +
               ", views=" + views +
               ", appreciation=" + appreciation +
               ", shareStatement=" + shareStatement +
               ", commentabled=" + commentabled +
               ", published=" + published +
               ", recommend=" + recommend +
               ", createTime=" + createdTime +
               ", updateTime=" + updateTime +
               ", description='" + description + '\'' +
               ", tagIds='" + tagIds + '\'' +
               ", user=" + user +
               ", type=" + type +
               ", tags=" + tags +
               ", comments=" + comments +
               '}';
    }
}
