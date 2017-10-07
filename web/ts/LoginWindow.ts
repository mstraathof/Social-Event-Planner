/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
var loggedIn = false;
var Handlebars: any;
var username = "";
class LoginWindow {
        
        /**
         * The name of the DOM entry associated with NewEntryForm
         */
        private static readonly NAME = "LoginWindow";
    
        /**
         * Track if the Singleton has been initialized
         */
        private static isInit = false;
    
        /**
         * Initialize the NewEntryForm by creating its element in the DOM and 
         * configuring its buttons.  This needs to be called from any public static 
         * method, to ensure that the Singleton is initialized before use
         */
        private static init() {
            if (!LoginWindow.isInit) {
                if(username != ""){
                    //window.alert("already logged in");
                    loggedIn = true;
                }
                if(loggedIn == false){
                    $("body").append(Handlebars.templates[LoginWindow.NAME + ".hb"]());
                    $("#" + LoginWindow.NAME + "-Submit").click(LoginWindow.submitForm);
                    $("#" + LoginWindow.NAME + "-Close").click(LoginWindow.hide);
                    //window.alert(loggedIn);
                    LoginWindow.isInit = true;
                    loggedIn = true;
                    LoginWindow.refresh();                
                }else{
                    window.alert("Already Logged In as :"+username);
                }
            }
        }
    
        /**
         * Refresh() doesn't really have much meaning, but just like in sNavbar, we
         * have a refresh() method so that we don't have front-end code calling
         * init().
         */
        public static refresh() {
            LoginWindow.init();
        }
        /**
         * Hide the NewEntryForm.  Be sure to clear its fields first
         */
        private static hide() {
            $("#" + LoginWindow.NAME + "-user").val("");
            $("#" + LoginWindow.NAME + "-pass").val("");
            //These lines hide the modal background(the shadow when bringing up a new entry form)
            $("#" + LoginWindow.NAME).modal("hide");
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();
        }

        public static show() {
            $("#" + LoginWindow.NAME + "-user").val("");
            $("#" + LoginWindow.NAME + "-pass").val("");
            $("#" + LoginWindow.NAME).modal("show");
        }

        private static submitForm() {
            // get the values of the two fields, force them to be strings, and check 
            // that neither is empty
            let username = "" + $("#" + LoginWindow.NAME + "-user").val();
            let password = "" + $("#" + LoginWindow.NAME + "-pass").val();
            loggedIn == true;
            // if(msg.length >= 500)
            // {
            //     window.alert("Error: Message exceeds 500");
            //     return;
            // }
            // if(title.length >= 50)
            // {
            //     window.alert("Error: Title exceeds 50");
            //     return;
            // }
            // if (title === "" || msg === "") {
            //     window.alert("Error: title or message is not valid");
            //     return;
            // }
            
            LoginWindow.hide();
            window.alert(username+" "+password);
            LoginWindow.refresh();
            Navbar.refresh();
            // // set up an AJAX post.  When the server replies, the result will go to

            $.ajax({
                type: "GET",
                url: "/users",
                dataType: "json",
                data: JSON.stringify({ mUsername: username, mPassword: password }),
                success: LoginWindow.onLoginResponse
            });
            userKey = "abc";
        }
    
        /**
         * onSubmitResponse runs when the AJAX call in submitForm() returns a 
         * result.
         * 
         * @param data The object returned by the server
         */
        private static onLoginResponse(data: any) {
            window.alert("good: "+data);
        }
    }