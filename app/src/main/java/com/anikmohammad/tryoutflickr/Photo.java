package com.anikmohammad.tryoutflickr;

class Photo {
    private String title;
    private String author;
    private String author_id;
    private String tags;
    private String link;
    private String imageURL;

    public Photo(String title, String author, String author_id, String tags, String link, String imageURL) {
        this.title = title;
        this.author = author;
        this.author_id = author_id;
        this.tags = tags;
        this.link = link;
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public String getTags() {
        return tags;
    }

    public String getLink() {
        return link;
    }

    public String getImageURL() {
        return imageURL;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", author_id='" + author_id + '\'' +
                ", tags='" + tags + '\'' +
                ", link='" + link + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
