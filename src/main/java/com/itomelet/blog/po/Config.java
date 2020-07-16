package com.itomelet.blog.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_config")
public class Config {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String footerAbout;
    private String footerCopyRight;
    private String footerPoweredBy;
    private String footerPoweredByURL;
    private String websiteDescription;
    private String websiteIcon;
    private String websiteLogo;
    private String websiteName;
    private String yourAvatar;
    private String yourEmail;
    private String yourName;
    private String footerICP;
    private String Qq;
    //打赏二维码
    private String appreciation;
    //个人二维码
    private String personalQrCode;
    //个人描述
    private String personalDescription;


    public String getQq() {
        return Qq;
    }

    public void setQq(String qq) {
        Qq = qq;
    }

    public String getAppreciation() {
        return appreciation;
    }

    public void setAppreciation(String appreciation) {
        this.appreciation = appreciation;
    }

    public String getFooterICP() {
        return footerICP;
    }

    public void setFooterICP(String footerICP) {
        this.footerICP = footerICP;
    }

    public String getPersonalQrCode() {
        return personalQrCode;
    }

    public void setPersonalQrCode(String personalQrCode) {
        this.personalQrCode = personalQrCode;
    }

    public String getPersonalDescription() {
        return personalDescription;
    }

    public void setPersonalDescription(String personalDescription) {
        this.personalDescription = personalDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFooterAbout() {
        return footerAbout;
    }

    public void setFooterAbout(String footerAbout) {
        this.footerAbout = footerAbout;
    }

    public String getFooterCopyRight() {
        return footerCopyRight;
    }

    public void setFooterCopyRight(String footerCopyRight) {
        this.footerCopyRight = footerCopyRight;
    }

    public String getFooterPoweredBy() {
        return footerPoweredBy;
    }

    public void setFooterPoweredBy(String footerPoweredBy) {
        this.footerPoweredBy = footerPoweredBy;
    }

    public String getFooterPoweredByURL() {
        return footerPoweredByURL;
    }

    public void setFooterPoweredByURL(String footerPoweredByURL) {
        this.footerPoweredByURL = footerPoweredByURL;
    }

    public String getWebsiteDescription() {
        return websiteDescription;
    }

    public void setWebsiteDescription(String websiteDescription) {
        this.websiteDescription = websiteDescription;
    }

    public String getWebsiteIcon() {
        return websiteIcon;
    }

    public void setWebsiteIcon(String websiteIcon) {
        this.websiteIcon = websiteIcon;
    }

    public String getWebsiteLogo() {
        return websiteLogo;
    }

    public void setWebsiteLogo(String websiteLogo) {
        this.websiteLogo = websiteLogo;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getYourAvatar() {
        return yourAvatar;
    }

    public void setYourAvatar(String yourAvatar) {
        this.yourAvatar = yourAvatar;
    }

    public String getYourEmail() {
        return yourEmail;
    }

    public void setYourEmail(String yourEmail) {
        this.yourEmail = yourEmail;
    }

    public String getYourName() {
        return yourName;
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }

    @Override
    public String toString() {
        return "Config{" +
               "id=" + id +
               ", footerAbout='" + footerAbout + '\'' +
               ", footerCopyRight='" + footerCopyRight + '\'' +
               ", footerPoweredBy='" + footerPoweredBy + '\'' +
               ", footerPoweredByURL='" + footerPoweredByURL + '\'' +
               ", websiteDescription='" + websiteDescription + '\'' +
               ", websiteIcon='" + websiteIcon + '\'' +
               ", websiteLogo='" + websiteLogo + '\'' +
               ", websiteName='" + websiteName + '\'' +
               ", yourAvatar='" + yourAvatar + '\'' +
               ", yourEmail='" + yourEmail + '\'' +
               ", yourName='" + yourName + '\'' +
               '}';
    }
}
