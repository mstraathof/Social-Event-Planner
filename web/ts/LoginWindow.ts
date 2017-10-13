var Handlebars: any;
var tUsername: String;
class LoginWindow {
    /**
    * The name of the DOM entry associated with LoginWindow
    */
    private static readonly NAME = "LoginWindow";
    
    /**
     * Track if the Singleton has been initialized
    */
    private static isInit = false;
    /**
    * Initialize the LoginWindow singleton.  
    * This needs to be called from any public static method, to ensure that the 
    * Singleton is initialized before use.
    */
        private static init() {
            if (!LoginWindow.isInit) {
                if(Gusername != null){
                    GloggedIn = true;
                }
                if(GloggedIn == false){
                    $("body").append(Handlebars.templates[LoginWindow.NAME + ".hb"]());
                    $("#" + LoginWindow.NAME + "-Submit").click(LoginWindow.submitForm);
                    $("#" + LoginWindow.NAME + "-Close").click(LoginWindow.hide);
                    LoginWindow.isInit = true;
                    GloggedIn = true;
                    LoginWindow.refresh();                
                }else{
                    window.alert("Already Logged In as :"+Gusername);
                }
            }
        }
    /**
     * refresh gives a public method to initialize the LoginWindow
     */
        public static refresh() {
            LoginWindow.init();
        }
    /**
     * hide() method hides the login form
     */
        private static hide() {
            $("#" + LoginWindow.NAME + "-user").val("");
            $("#" + LoginWindow.NAME + "-pass").val("");
            //These lines hide the modal background(the shadow when bringing up a new entry form)
            $("#" + LoginWindow.NAME).modal("hide");
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();
        }
    /**
     * show() method presents the login form
     */
        public static show() {
            $("#" + LoginWindow.NAME + "-user").val("");
            $("#" + LoginWindow.NAME + "-pass").val("");
            $("#" + LoginWindow.NAME).modal("show");
        }
    /**
     * submitForm() method submits the login form and sends AJAX call
     */
        private static submitForm() {
            // get the values of the two fields, force them to be strings, and check 
            // that neither is empty
            let tUsername = "" + $("#" + LoginWindow.NAME + "-user").val();
            let Gpassword = "" + $("#" + LoginWindow.NAME + "-pass").val();
            GloggedIn == true;

            if(tUsername != null || tUsername != undefined){
                Gusername = tUsername;
            }
            LoginWindow.hide();
            LoginWindow.refresh();

            $.ajax({
                type: "POST",
                url: "/login",
                dataType: "json",
                data: JSON.stringify({ mUsername: Gusername, mPassword: Gpassword }),
                success: LoginWindow.onLoginResponse
            });            
        }
    
        /**
         * onSubmitResponse runs when the AJAX call in submitForm() returns a 
         * result.
         * 
         * @param data The object returned by the server
         */
        private static onLoginResponse(data: any) {
            GuserKey = data.mLoginData;
            if (data.mStatus === "ok") {
                $("nav.navbar-default").hide();
                NavbarLoggedIn.refresh();
            }
            // Handle explicit errors with a detailed popup message
            else if (data.mStatus === "error") {
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
        public static loginCheck(){
            $.ajax({
                type: "POST",
                url: "/loginCheck",
                dataType: "json",
                data: JSON.stringify({ mUsername: Gusername, mKey: GuserKey }),
                success: LoginWindow.onLoginCheckResponse
            });

        }
        public static onLoginCheckResponse(data: any){
            var loggedIn = data.mStatus;
            if(loggedIn == true){
                window.alert("logged in");
            }else{
                window.alert("NOT logged in");
            }
        }
        */
    }