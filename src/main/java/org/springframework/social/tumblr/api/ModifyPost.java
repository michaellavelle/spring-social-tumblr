package org.springframework.social.tumblr.api;

import org.springframework.core.io.Resource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.*;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author sam
 * @version $Id$
 */
public abstract class ModifyPost {

    private Long id;

    private PostType type;

    private PostState state;

    private List<String> tags;

    private String tweet;

    private Date date;

    private boolean markdown;

    private String slug;

    protected ModifyPost(PostType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PostType getType() {
        return type;
    }

    public void setType(PostType type) {
        this.type = type;
    }

    public PostState getState() {
        return state;
    }

    public void setState(PostState state) {
        this.state = state;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isMarkdown() {
        return markdown;
    }

    public void setMarkdown(boolean markdown) {
        this.markdown = markdown;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public MultiValueMap<String, String> toParameterMap() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        if (id != null) {
            map.add("id", id.toString());
        }
        
        map.add("type", getType().getType());

        if (getState() != null) {
            map.add("state", getState().getState());
        }
        if (getTags() != null && !getTags().isEmpty()) {
            map.add("tags", StringUtils.collectionToCommaDelimitedString(getTags()));
        }
        if (getTweet() != null) {
            map.add("tweet", getTweet());
        }
        if (getDate() != null) {
            DateFormatter formatter = new DateFormatter();
            formatter.setPattern("yyyy-MM-dd HH:mm:ss z");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            String dateString = formatter.print(getDate(), Locale.getDefault());
            map.add("date", dateString);
        }

        if (isMarkdown()) {
            map.add("markdown", Boolean.toString(isMarkdown()));
        }
        if (getSlug() != null) {
            map.add("slug", getSlug());
        }

        return map;
    }

    // TODO figure out how to do this the way Tumblr expects
    String convertResourceToString(Resource resource) {
        try {
//            Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF8"));
//            String bytesString = FileCopyUtils.copyToString(reader);
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
//            bytes = Base64.encode(bytes);
            String bytesString = new String(bytes);
//            bytesString = bytesString.replaceAll("/", "%2F");
//            StringBuilder sb = new StringBuilder();
//            for (byte b : bytes) {
//                sb.append(Integer.toHexString((int) (b & 0xff)));
//            }
//            return sb.toString();
//            return new String(bytes);
            System.err.println("bytesString");
            System.err.println(bytesString);
            System.err.println("url encoded");
            System.err.println(URLEncoder.encode(bytesString, "UTF-8"));
            System.err.println("oauth encoded");
            System.err.println(oauthEncode(bytesString));
            return bytesString;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final BitSet UNRESERVED;

    static {
        BitSet alpha = new BitSet(256);
        for (int i = 'a'; i <= 'z'; i++) {
            alpha.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            alpha.set(i);
        }
        BitSet digit = new BitSet(256);
        for (int i = '0'; i <= '9'; i++) {
            digit.set(i);
        }
        BitSet unreserved = new BitSet(256);
        unreserved.or(alpha);
        unreserved.or(digit);
        unreserved.set('-');
        unreserved.set('.');
        unreserved.set('_');
        unreserved.set('~');
        UNRESERVED = unreserved;
    }

    private static String oauthEncode(String param) {
        try {
            // See http://tools.ietf.org/html/rfc5849#section-3.6
            byte[] bytes = encode(param.getBytes("UTF-8"), UNRESERVED);
            return new String(bytes, "US-ASCII");
        } catch (Exception shouldntHappen) {
            throw new IllegalStateException(shouldntHappen);
        }
    }

    private static byte[] encode(byte[] source, BitSet notEncoded) {
        Assert.notNull(source, "'source' must not be null");
        ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length * 2);
        for (int i = 0; i < source.length; i++) {
            int b = source[i];
            if (b < 0) {
                b += 256;
            }
            if (notEncoded.get(b)) {
                bos.write(b);
            }
            else {
                bos.write('%');
                char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
                char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
                bos.write(hex1);
                bos.write(hex2);
            }
        }
        return bos.toByteArray();
    }

}
