// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;
var Handlebars: any;
var prof: string;

class ViewComments {
    /**
     * The name of the DOM entry associated with ViewComments
     */
    private static readonly NAME = "ViewComments";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
    * Initialize the ViewComments singleton.  
    * This needs to be called from any public static method, to ensure that the 
    * Singleton is initialized before use.
    */
    private static init() {
        if (!ViewComments.isInit) {
            ViewComments.isInit = true;
        }
    }

    /**
    * refresh() is the public method for updating the ElementList
    */
    public static refresh() {
        // Make sure the singleton is initialized
        ViewComments.init();
        
    }

    /**
     * Method to update the comments list for a message
     * @param data
     */
    public static update(data: any) {
        $("#ViewComments").remove();
        $("nav.xyz").remove();
        // Remove the table of data, if it exists
        $("#" + ViewComments.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("body").append(Handlebars.templates[ViewComments.NAME + ".hb"](data));
        //$("."+ElementList.NAME+"-editbtn").click(ElementList.clickEdit);
        //window.alert("MESSAGE ID IS: "+mesID);
        //ViewComments.addComment();
        $("."+ViewComments.NAME+"-viewProfile").click(ViewComments.getProfile);
        $("."+ViewComments.NAME+"-addComment").click(NewCommentForm.show);
        
    }
    /**
     * Method to get the profile of a user
     */
    private static getProfile(){
        $("#ViewComments").remove();
        let user = $(this).data("value");
        ProfilePage.show(user);
    }

    
}