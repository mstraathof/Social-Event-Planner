/**
 * The NavbarLoggedIn Singleton is the navigation bar at the top of the page for logged users.  Through 
 * its HTML, it is designed so that clicking the "brand" part will refresh the
 * page.  Apart from that, it has an "add" button, which forwards to 
 * NewEntryForm
 */
var Handlebars: any;
class NavbarLoggedIn {
    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * The name of the DOM entry associated with Navbar
     */
    private static readonly NAME = "NavbarLoggedIn";

    /**
     * Initialize the NavbarLoggedIn Singleton by creating its element in the DOM and
     * configuring its button.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use.
     */
    private static init() {
        if (!NavbarLoggedIn.isInit) {
            $("body").prepend(Handlebars.templates[NavbarLoggedIn.NAME + ".hb"]());
            $("#"+NavbarLoggedIn.NAME+"-add").click(NewEntryForm.show);
            $("#"+NavbarLoggedIn.NAME+"-profile").click(NavbarLoggedIn.loadProfile);
            $("#"+NavbarLoggedIn.NAME+"-viewBuzz").click(NavbarLoggedIn.viewBuzz);
            $("#"+NavbarLoggedIn.NAME+"-logOut").click(NavbarLoggedIn.logOut);
            $("#"+NavbarLoggedIn.NAME+"-changePass").click(NewPassForm.show);
            //$("div.container-fluid well span6").hide();
            //$("div.container-fluid well span6").hide();
            //ProfilePage.show(Gusername);
            $("div.container-fluid well span6").hide(); 
            ElementList.refresh();
            //NavbarLoggedIn.isInit = true;
        }
    }
    /**
     * Method in navbar to allow a user to log out
     * Sends AJAX call to authenticate logout
     */
    public static logOut(){
        $.ajax({
            type: "POST",
            url: "/logout",
            dataType: "json",
            data: JSON.stringify({ mUsername: Gusername }),
            success: NavbarLoggedIn.onLogOutResponse
        });
        
    }
    /**
     * Method for the response from the AJAX call to log out
     */
    private static onLogOutResponse(data:any){
        if (data.mStatus === "ok") {
            window.alert("Logged Out Successfully");
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("Problem Logging Out");
        }
        location.reload();
    }
    /**
     * Method in navbar to allow a user to view all message on The Buzz
     */
    public static viewBuzz(){
        viewingYours=false;
        $("nav.xyz").hide();
        $('body').removeClass('div.container-fluid well span6');
        $("div.container-fluid well span6").hide();
        $("#ElementList").remove();
        $("#ProfilePage").remove();
        $("#ViewComments").remove();
        ElementList.refresh();
    }
    /**
     * Method in navbar to allow a user to view their profile page
     */
    public static loadProfile(){
        ProfilePage.show(Gusername);
    }
    /**
     * Refresh() doesn't really have much meaning for the navbar, but we'd 
     * rather not have anyone call init(), so we'll have this as a stub that
     * can be called during front-end initialization to ensure the navbar
     * is configured.
     */
    public static refresh() {
        NavbarLoggedIn.init();
    }
}