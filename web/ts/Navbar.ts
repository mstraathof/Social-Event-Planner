/**
 * The Navbar Singleton is the navigation bar at the top of the page.  Through 
 * its HTML, it is designed so that clicking the "brand" part will refresh the
 * page.  Apart from that, it has an "add" button, which forwards to 
 * NewEntryForm
 */
var Handlebars: any;
var $: any;
// var Gusername:string;
// var Gpassword:string;
// var GuserKey:number;
// var GloggedIn:boolean;

// var editEntryForm: EditEntryForm;
// var loginWindow: LoginWindow;
//var createAccountForm: CreateAccountForm;

class Navbar {
    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * The name of the DOM entry associated with Navbar
     */
    private static readonly NAME = "Navbar";

    /**
     * Initialize the Navbar Singleton by creating its element in the DOM and
     * configuring its button.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use.
     */
    private static init() {
        if (!Navbar.isInit) {
            $("body").prepend(Handlebars.templates[Navbar.NAME + ".hb"]());
            $("#"+Navbar.NAME+"-createAccount").click(CreateAccountForm.show);
            $("#"+Navbar.NAME+"-logIn").click(LoginWindow.show);
            $("body").append("<h1 id = 1>Welcome to The Buzz</h1>");
            $("body").append("<h4 id = 2>Create a free Account or Log-In to start Buzzin'</h4>");

            Navbar.isInit = true;
        }
    }

    /**
     * Refresh() doesn't really have much meaning for the navbar, but we'd 
     * rather not have anyone call init(), so we'll have this as a stub that
     * can be called during front-end initialization to ensure the navbar
     * is configured.
     */
    public static refresh() {
        Navbar.init();
    }
}
