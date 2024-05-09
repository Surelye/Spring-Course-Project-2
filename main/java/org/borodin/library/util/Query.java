package org.borodin.library.util;

public class Query {

    private String text;

    public Query(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Query() {}

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Query{" +
                "text='" + text + '\'' +
                '}';
    }
}
