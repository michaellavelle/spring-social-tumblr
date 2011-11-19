package org.springframework.social.tumblr.api;

/**
 * @author sam
 * @version $Id$
 */
public class Follower {

    private String name;
    private String url;
    private long updated;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }
}
