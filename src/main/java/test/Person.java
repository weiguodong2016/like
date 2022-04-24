package test;

import java.util.List;

public class Person {

    private String id;
    private List likeIds;
    private boolean matched;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List getLikeIds() {
        return likeIds;
    }

    public void setLikeIds(List likeIds) {
        this.likeIds = likeIds;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", likeIds=" + likeIds +
                ", matched=" + matched +
                '}';
    }

}
