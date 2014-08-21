package com.xgr.wonderful.sns;

/**
 * @author kingofglory email: kingofglory@yeah.net blog: http:www.google.com
 * @date 2014-3-3 TODO
 */

public class TencentShareEntity {

    private String title;

    private String imgUrl;

    private String targetUrl;

    private String summary;

    private String comment;

    /**
     * 设置默认分享参数
     */
    public TencentShareEntity() {
        this(TencentShareConstants.TITLE, TencentShareConstants.IMG_URL, TencentShareConstants.TARGET_URL,
            TencentShareConstants.SUMMARY, TencentShareConstants.COMMENT);
    }

    /**
     * 设置动态分享参数
     * @param title
     * @param imgUrl
     * @param targetUrl
     * @param summary
     */
    public TencentShareEntity(String title, String imgUrl, String targetUrl, String summary, String comment) {
        this.title=title;
        this.imgUrl=imgUrl;
        this.targetUrl=targetUrl;
        this.summary=summary;
        this.comment=comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl=imgUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl=targetUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary=summary;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment=comment;
    }

    @Override
    public String toString() {
        return "TencentShareEntity [title=" + title + ", imgUrl=" + imgUrl + ", targetUrl=" + targetUrl + ", summary=" + summary
            + ", comment=" + comment + "]";
    }

}
