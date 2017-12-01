// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;
var Handlebars: any;

// a global for the main ElementList of the program.  See newEntryForm for 
// explanation
var mainList: ElementList;

/**
 * The ElementList Singleton provides a way of displaying all of the data 
 * stored on the server as an HTML table.
 */
class ElementList {
    /**
     * The name of the DOM entry associated with ElementList
     */
    private static readonly NAME = "ElementList";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
    * Initialize the ElementList singleton.  
    * This needs to be called from any public static method, to ensure that the 
    * Singleton is initialized before use.
    */
    private static init() {
        if (!ElementList.isInit) {
            ElementList.isInit = true;
        }
    }

    /**
    * refresh) updates the feed of all messages on The Buzz
    */
    public static refresh() {
        // Make sure the singleton is initialized
        ElementList.init();
        // Issue a GET, and then pass the result to update()
        $.ajax({
            type: "GET",
            url: "/messages",
            //url: "https://forums.wholetomato.com/mira/messages.aspx",
            dataType: "json",           // JSON response will contain all message buzzes, not comments.
            success: ElementList.update
        });
    }

    /**
    * update() is the private method used by refresh() to update the 
    * list and initialize buttons for viewing profiles, voting, and commenting.
    */
    private static update(data: any) {
        // replace main container, whatever it contains, with one for the feed.
        $("#mainContainer").remove();
        // Use a template to re-generate the feed.
        $("body").append(Handlebars.templates[ElementList.NAME + ".hb"](data));
        //console.log(JSON.stringify(data));        // uncomment to debug data object.
        //{"mStatus":"ok","mMessageData":[{"mId":"10","mSubject":"Favorite movie", ...
        
        // get and display images, and comments, associated with messages
        var i: number;
        for (i=0; i<data.mMessageData.length; i++)
        {
            let mId = data.mMessageData[i].mId;     // mId is referenced often
            console.log("mId = " + mId);

            // hook up the view-profile button.
            $('#buttonViewProfile' + mId).click(ElementList.getProfile);

            // get and show a message image
            // set src attribute of img in Javascript because Mira cannot figure out how in jQuery.
            //document.getElementById("img" + data.mMessageData[i].mId).setAttribute("src", "https://forum.wholetomato.com/mira/TomPetty.jpg");
            //document.getElementById("img" + data.mMessageData[i].mId).setAttribute("src", "https://forum.wholetomato.com/mira/WhatABurgerWoman.jpg");
            //$('#img' + mId).show();

            // get image associated with message and display
            $.ajax({
                type: "GET",
                url: "/messages/images/download/" + data.mMessageData[i].mWebUrl,
                dataType: "binary",     // TODO: dataType could be wrong, might be image/png
                success: function(result) {
                    document.getElementById("img" + mId).setAttribute("src", "data:image/png;base64," + result);
                    $('#img' + mId).show(); 
                },
                error: function(xmlRequest) {
                    console.log("GET image for mId " + mId + " failed: " + xmlRequest.status + " " + xmlRequest.statusText);
                    console.log(xmlRequest.responseText);
                }
            });


            // submit async request for comments for the message.
            $.ajax({
                type: "GET",
                //url: "/comments/"+messageid+"/"+Gusername+"/"+GuserKey,
                url: "/comments/"+mId+"/"+data.mMessageData[i].Gusername+"/"+data.mMessageData[i].GuserKey,
                //url: "https://forum.wholetomato.com/mira/comments/" + data.mMessageData[i].mId + ".aspx",
                dataType: "json",
                success: ElementList.addComments,
                error: function (xmlRequest) {      // remove if no/invalid response implies message has no comments.
                    console.log("GET comments for mId " + mId + " failed: " + xmlRequest.status + " " + xmlRequest.statusText);
                    console.log(xmlRequest.responseText);
                }
            });

            // hook up the button that lets one add a comment.
            $('#buttonComment' + mId).click(NewCommentForm.show);
        }

        if (headers == false) {
            $('#yours').hide();
            $('#liked').hide();
            $('#disliked').hide();
            $('#commented').hide();  
        }
        headers = false;

        $("."+ElementList.NAME+"-comments").click(ElementList.viewComments);
        $("."+ElementList.NAME+"-profile").click(ElementList.getProfile);
        $("."+ElementList.NAME+"-upvote").click(ElementList.upvote);
        $("."+ElementList.NAME+"-downvote").click(ElementList.downvote);
        
    }

    // add comments to browser document.
    // all comments must be for a single message!
    private static addComments(data: any) {
        //console.log(JSON.stringify(data));      // uncomment to debug
        if (data.mStatus === "logout") {
            window.alert("Session Timed Out");
            location.reload();
        }

        var comments: any;   // a string of html that will include all comments for a message
        comments = "";
        var i: number;
        for (i=0; i<data.mMessageData.length; i++)
        {
            // todo: make mUsername a link to send mail or view profile.
            comments += 
                '<div class="row">'
                    + '<div class="col-xs-6">'
                        + '<blockquote class="blockquote-feed">'
                            + '<p>' + data.mMessageData[i].mComment + '</p>'
                            + '<footer>' + data.mMessageData[i].mUsername + '</footer>'
                            //+ '<footer>'
                            //+ '<p>' + data.mMessageData[i].mUsername
                            //+ '<a href="mailto:' + data.mMessageData[i].mUsername + '@lehigh.edu"' + data.mMessage[i].mUsername + '/>'
                            //+ '</p> </footer>'
                        + '</blockquote>'
                    + '</div>'
                    + '<div class="col-xs-6">'
                    + '</div>'
                + '</div>';
        }
        //console.log("addComments() i=" + i);
        //console.log("addComments() comments=" + comments);
        if (i>0)        // there was at least one comment
        {
            // add comments to message associated with first comment, i.e. data.mMessageData[0].mId
            document.getElementById("comments" + data.mMessageData[0].mId).innerHTML = comments;
            document.getElementById("comments" + data.mMessageData[0].mId).style.display = "inline";
        }
    }

    /**
    * clickDelete is the code we run in response to a click of a delete button
    * Not currently being used in the app
    */
    private static clickDelete() {
        // for now, just print the ID that goes along with the data in the row
        // whose "delete" button was clicked
        let id = $(this).data("value");
        $.ajax({
            type: "DELETE",
            url: "/messages/" + id,
            dataType: "json",
            // TODO: we should really have a function that looks at the return
            //       value and possibly prints an error message.
            success: ElementList.refresh
        });
    }
    /**
     * Method used for upvoting entries
     * Called when upvote button on Elementlist is pressed
     * Sends mUsername and mMessageId
     */
    private static upvote(){
        $("#editElement").hide();
        let id = $(this).data("value");

        $.ajax({
            type: "POST",
            url: "/upVote",
            dataType: "json",
            data: JSON.stringify({ mUsername: id.Gusername, mMessageId: id, mKey: id.GuserKey}),    
            // might need to remove id.Guser
            success: ElementList.onVoteResponse
        });
    }
    /**
     * Method used for downvoting entries
     * Called when downvote button on Elementlist is pressed
     * Sends Username and mMessageId
     */
    private static downvote(){
        $("#editElement").hide();
        let id = $(this).data("value");

        $.ajax({
            type: "POST",
            url: "/downVote",
            dataType: "json",
            data: JSON.stringify({ mUsername: id.Gusername, mMessageId: id, mKey: id.GuserKey}),
            success: ElementList.onVoteResponse
        });
    }

    /**
    * A response from the AJAX call
    */
    private static onVoteResponse(data: any){
        if (data.mStatus === "logout") {
            window.alert("Session Timed Out");
            location.reload();
        }
        ElementList.refresh()
    }

    /**
    * Method to view profile of user. Allows you to see the username, real name, email, 
    * and bio of the person who posted the buzz.
    */
    private static getProfile(){
        $("#editElement").hide();
        let user = $(this).data("value");
        console.log("getProfile() of " + user);
        ProfilePage.show(user);
    }

    /**
     * onSubmitResponse determines if the upvote and downvote was successful
     * mStatus will be 1 upon successfull 
     * @param data Has info on if upvote and downvote was successful 
     */
    private static onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form and refresh the main 
        // listing of messages
        //alert("mStatus"+data.mStatus);
        if (data.mStatus === "1") {
            ElementList.refresh();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }

    /**
     * clickEdit is the code we run in response to the click of a data row
     * Will bring up a window that shows the current title and message
     */
    private static clickEdit() {
        // as in clickDelete, we need the ID of the row
        //EditEntryForm.idCurrentlyEditing = $(this).data("value");
        //alert("Here");
        //$("."+ElementList.NAME+"-editbtn").click(EditEntryForm.init);
        $("."+ElementList.NAME+"-editbtn").click(EditEntryForm.show);
    }

    /**
     * viewComments allows you to see all the comments tied to a specific message, as well as add a new one. 
     */
    public static viewComments() {
        var msgToView = $(this).data("value");
        //mesID = msgToView;
        //window.alert(msgToView);
        $.ajax({
            type: "GET",
            url: "/comments/"+msgToView+"/"+msgToView.Gusername+"/"+msgToView.GuserKey,
            dataType: "json",
            success: ElementList.showComments
        });        

    }

    /**
     * viewCommentsGivenID lets you view the comments of a message given an id
     *  @param messageId id of message to see comments ofS
     */
    public static viewCommentsGivenID(messageid: number) {
        $.ajax({
            type: "GET",
            url: "/comments/"+messageid+"/"+Gusername+"/"+GuserKey,
            dataType: "json",
            success: ElementList.showComments
        });
        
    }
    /**
     * showComments will get the data of the AJAX call and actually display the comments of the message
     * @param data The object returned by the server
     */
    private static showComments(data: any) {
        if (data.mStatus === "logout") {
            window.alert("Session Timed Out");
            location.reload();
        }

        $("#ElementList").remove();
        ViewComments.update(data);
    }

}