var Handlebars: any;
var tUsername: String;
class LoginWindow {
        private static readonly NAME = "LoginWindow";

        private static isInit = false;

        private static init() {
            if (!LoginWindow.isInit) {
                if(Gusername != null){
                    //window.alert("already logged in");
                    GloggedIn = true;
                }
                if(GloggedIn == false){
                    $("body").append(Handlebars.templates[LoginWindow.NAME + ".hb"]());
                    $("#" + LoginWindow.NAME + "-Submit").click(LoginWindow.submitForm);
                    $("#" + LoginWindow.NAME + "-Close").click(LoginWindow.hide);
                    //window.alert(loggedIn);
                    LoginWindow.isInit = true;
                    GloggedIn = true;
                    LoginWindow.refresh();                
                }else{
                    window.alert("Already Logged In as :"+Gusername);
                }
            }
        }
    
        public static refresh() {
            LoginWindow.init();
        }

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
            let tUsername = "" + $("#" + LoginWindow.NAME + "-user").val();
            let Gpassword = "" + $("#" + LoginWindow.NAME + "-pass").val();
            GloggedIn == true;
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
            if(tUsername != null || tUsername != undefined){
                Gusername = tUsername;
            }
            LoginWindow.hide();
            window.alert(Gusername+" "+Gpassword);
            LoginWindow.refresh();

            $("nav.navbar-default").hide();
            window.alert(Gusername+" , "+GuserKey+" , "+Gpassword);
            // // set up an AJAX post.  When the server replies, the result will go to

            $.ajax({
                type: "POST",
                url: "/login",
                dataType: "json",
                data: JSON.stringify({ mUsername: Gusername, mPassword: Gpassword }),
                success: LoginWindow.onLoginResponse
            });
            //GuserKey = 1;
            NavbarLoggedIn.refresh();
        }
    
        /**
         * onSubmitResponse runs when the AJAX call in submitForm() returns a 
         * result.
         * 
         * @param data The object returned by the server
         */
        private static onLoginResponse(data: any) {
            GuserKey = data.mLoginData;
            window.alert("Key i got: "+GuserKey);
            window.alert("good: "+data);
        }
    }