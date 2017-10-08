/**
 * The Navbar Singleton is the navigation bar at the top of the page.  Through 
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
     * Initialize the Navbar Singleton by creating its element in the DOM and
     * configuring its button.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use.
     */
    private static init() {
        if (!NavbarLoggedIn.isInit) {
            $("body").prepend(Handlebars.templates[NavbarLoggedIn.NAME + ".hb"]());
            $("#"+NavbarLoggedIn.NAME+"-add").click(NewEntryForm.show);
            $("#"+NavbarLoggedIn.NAME+"-profile").click(NavbarLoggedIn.loadProfile);
            //$("div.container-fluid well span6").hide();
            //$("div.container-fluid well span6").hide();
            //ProfilePage.show(Gusername);
            $("div.container-fluid well span6").hide(); 
            ElementList.refresh();
            //NavbarLoggedIn.isInit = true;
        }
    }
    public static loadProfile(){
        window.alert("hittt me "+Gusername);
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