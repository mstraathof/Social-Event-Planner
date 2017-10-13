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
    * refresh() is the public method for updating the ElementList
    * it updates the list accordingly if you are viewing all messages, 
    * or the messages you posted, liked, disliked, and commented 
    */

    public static refresh() {
        //window.alert("Username: "+ Gusername);
        if(viewingYours==true){
            headers = true;
            ElementList.refreshUser(Gusername);
        }else{
            ElementList.refreshAll();
        }

    }
    /**
    * refreshAll() updates the list of all messages on TheBuzz
    */
    public static refreshAll() {
        // Make sure the singleton is initialized
        ElementList.init();
        // Issue a GET, and then pass the result to update()
        $.ajax({
            type: "GET",
            url: "/messages",
            dataType: "json",
            success: ElementList.update
        });
    }
    /**
    * refreshUser() updates the list of all messages you have made,
    * liked, disliked, and commented
    */
    public static refreshUser(username:string) {
        // Make sure the singleton is initialized
        ElementList.init();
        // Issue a GET, and then pass the result to update()
        $.ajax({
            type: "GET",
            url: "/profile/"+username,
            dataType: "json",
            success: ElementList.update
        });
    }
    

    /**
    * update() is the private method used by refresh() to update the 
    * list and initialize buttons for liking and viewing profiles
    */
    private static update(data: any) {
        
        // Remove the table of data, if it exists
        $("#" + ElementList.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("body").append(Handlebars.templates[ElementList.NAME + ".hb"](data));
        if(headers == false){
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
            data: JSON.stringify({ mUsername: Gusername, mMessageId: id}),
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
            data: JSON.stringify({ mUsername: Gusername, mMessageId: id}),
            success: ElementList.onVoteResponse
        });
    }

    /**
    * A response from the AJAX call
    */
    private static onVoteResponse(){
        ElementList.refresh()
    }

    /**
    * Method to view profile of user. Allows you to see the username, real name, email, 
    * and bio of the person who posted the buzz.
    */
    private static getProfile(){
        $("#editElement").hide();
        let user = $(this).data("value");
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
        mesID = msgToView;
        //window.alert(msgToView);
        $.ajax({
            type: "GET",
            url: "/comments/"+msgToView,
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
            url: "/comments/"+messageid,
            dataType: "json",
            success: ElementList.showComments
        });
        
    }
    /**
     * showComments will get the data of the AJAX call and actually display the comments of the message
     * @param data The object returned by the server
     */
    private static showComments(data: any) {
        $("#ElementList").remove();
        ViewComments.update(data);
    }
}