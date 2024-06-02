package com.example.mangamania_app.model;




public class PostComment {


	/*  this is the structure for POST("comment/save")   then it will construct the actual comment object to save to repository
	 {
	 	"userId": "" ,
	 	"commentText": "" ,
	 	"rootComment": "" ,
	 }
	 */


    private String commentText;
    private String rootComment;
    private String chapterId;


    public PostComment(String commentText, String rootComment, String chapterId) {
        super();
        this.commentText = commentText;
        this.rootComment = rootComment;
        this.chapterId = chapterId;

    }




    public String getCommentText() {
        return commentText;
    }


    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }


    public String getRootComment() {
        return rootComment;
    }


    public void setRootComment(String rootComment) {
        this.rootComment = rootComment;
    }


    public String getChapterId() {
        return chapterId;
    }


    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

}
