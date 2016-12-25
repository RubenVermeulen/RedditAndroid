package be.rubenvermeulen.android.redditapp.models;

import java.util.List;

public class ResultData {
    private List<Topic> children;

    public List<Topic> getChildren() {
        return children;
    }

    public void setChildren(List<Topic> children) {
        this.children = children;
    }
}
