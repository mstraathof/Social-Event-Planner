/**
 * The Navbar Singleton is the navigation bar at the top of the page.  Through 
 * its HTML, it is designed so that clicking the "brand" part will refresh the
 * page.  Apart from that, it has an "add" button, which forwards to 
 * NewEntryForm
 */
var Handlebars: any;
var $         : any;
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
            $("body").append("<h4 id = 2>Login to your Lehigh email to make a Buzz'</h4>");
            Navbar.isInit = true;
        }
    }
     private static onLoginResponse(data: any) {
         window.alert("hi");
            GuserKey  = data.mLoginData;
            Gusername = data.mUsername;
            if (data.mStatus === "ok") {
                $("nav.navbar-default").hide();
                var x               = document.getElementById("Gsignin");
                    x.style.display = "none";
                //$("Gsignin").hide();
                $('#1').hide();
                $('#2').hide();
                NavbarLoggedIn.refresh();
            }
            else if (data.mStatus === "wrongDomain") {
                NavbarLoggedIn.signOut();
                window.alert("Failed to Log you in. Please Make sure the credentials are correct.");
            }
            // Handle explicit errors with a detailed popup message
            else if (data.mStatus === "notVerified") {
                window.alert("Failed to Log you in. Please Make sure the credentials are correct.");
            }
            // Handle other errors with a less-detailed popup message
            else {
                window.alert("Unspecified error");
            }
            //window.alert("Key i got: "+GuserKey);
            //window.alert("good: "+data);
            //LoginWindow.loginCheck();
        }
    /*
    public static onSignIn (googleUser)
    {
        var profile = googleUser.getBasicProfile();
        console.log("ID: " + profile.getId()); // Don't send this directly to your server!
        console.log('Full Name: ' + profile.getName());
        console.log('Given Name: ' + profile.getGivenName());
        console.log('Family Name: ' + profile.getFamilyName());
        console.log("Image URL: " + profile.getImageUrl());
        console.log("Email: " + profile.getEmail());

        // The ID token you need to pass to your backend:
        var id_token = googleUser.getAuthResponse().id_token;
        console.log("ID Token: " + id_token);
         $.ajax({
                type    : "POST",
                url     : "/tokensignin",
                dataType: "json",
                data    : JSON.stringify({ token_id: id_token }),
                success : LoginWindow.onLoginResponse
            });       

    }
    public static signOut()
    {
        var auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut().then(function () {
        console.log('User signed out.');
        });
    }
        */
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
