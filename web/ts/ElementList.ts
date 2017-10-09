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
    */
    public static refresh() {
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

    public static refreshUser(username:string) {
        // Make sure the singleton is initialized
        ElementList.init();
        // Issue a GET, and then pass the result to update()
        $.ajax({
            type: "GET",
            url: "/messages/"+username,
            dataType: "json",
            success: ElementList.update
        });
    }
    

    /**
    * update() is the private method used by refresh() to update the 
    * It initializes the editbtn (window Tied to seeing the title and message)
    * also initializes the upvote and downvote button 
    */
    private static update(data: any) {
        // Remove the table of data, if it exists
        $("#" + ElementList.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("body").append(Handlebars.templates[ElementList.NAME + ".hb"](data));
        //$("."+ElementList.NAME+"-editbtn").click(ElementList.clickEdit);
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
     * Sends mChangeVote with a value of 1 to the database
     */
    private static upvote(){
        $("#editElement").hide();
        let id = $(this).data("value");
        let up = 1;
        $.ajax({
            type: "PUT",
            url: "/messages/"+id,
            dataType: "json",
            data: JSON.stringify({ mChangeVote: up}),
            success: ElementList.onSubmitResponse
        });
    }
    /**
     * Method used for downvoting entries
     * Called when downvote button on Elementlist is pressed
     * Sends mChangeVote with a value of -1 to the database
     */
    private static downvote(){
        $("#editElement").hide();
        let id = $(this).data("value");
        let down = -1;

        $.ajax({
            type: "PUT",
            url: "/messages/"+id,
            dataType: "json",
            data: JSON.stringify({ mChangeVote: down}),
            success: ElementList.onSubmitResponse
        });
    }

    private static getProfile(){
        $("#editElement").hide();
        let user = $(this).data("value");
        ProfilePage.show(user);
        //
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
    
}