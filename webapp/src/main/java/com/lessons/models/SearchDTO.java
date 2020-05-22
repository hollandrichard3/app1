package com.lessons.models;

public class SearchDTO  {
    private String rawQuery;
    private String indexName;

    public String getIndexName() {
        return this.indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getRawQuery () {
        return this.rawQuery;
    }

    public void setRawQuery ( String rawQuery ) {
        this.rawQuery = rawQuery;
    }


    public String toString() {
        return("rawQuery=" + this.rawQuery + "   indexName=" + this.indexName);
    }
}