package com.example.myapplication;


//Custom note builder, having all data handles we would need for notes
//Using Adam Sinicki's tutorial
public class NotesBuilder {

    private String title, content;

    public NotesBuilder(){}

        public NotesBuilder(String title, String content){
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
}

