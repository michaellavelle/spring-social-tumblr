package org.springframework.social.tumblr.api;

import org.springframework.util.MultiValueMap;

/**
 * @author sam
 * @version $Id$
 */
public class ModifyReblogPost extends ModifyPost {

    private String reblogKey;
    private String comment;

    public ModifyReblogPost() {
        super(PostType.REBLOG);
    }

    public String getReblogKey() {
        return reblogKey;
    }

    public void setReblogKey(String reblogKey) {
        this.reblogKey = reblogKey;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public MultiValueMap<String, String> toParameterMap() {
        MultiValueMap<String, String> map = super.toParameterMap();

        map.add("reblog_key", reblogKey);

        if (comment != null) {
            map.add("comment", comment);
        }

        return map;
    }
}
