package com.hitorus.pick_a_book;


public class Book {

    public enum Status {PENDING, READING, FINISHED}

    private String name;
    private String author;
    private Status status;

    public Book () {}

    public Book (String name, String author)
    {
        this.name = name;
        this.author = author;
        this.status = Status.PENDING;
    }

    public Book (String name, String author, String status)
    {
        this.name = name;
        this.author = author;
        this.status = Status.valueOf(status);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

