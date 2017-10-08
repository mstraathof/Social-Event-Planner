/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
var Handlebars: any;

class CreateAccountForm {
        
        /**
         * The name of the DOM entry associated with NewEntryForm
         */
        private static readonly NAME = "CreateAccountForm";
    
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
            if (!CreateAccountForm.isInit) {
                    $("body").append(Handlebars.templates[CreateAccountForm.NAME + ".hb"]());
                    $("#" + CreateAccountForm.NAME + "-Submit").click(CreateAccountForm.submitForm);
                    $("#" + CreateAccountForm.NAME + "-Close").click(CreateAccountForm.hide);
                    //window.alert(loggedIn);
                    CreateAccountForm.isInit = true;
                    CreateAccountForm.refresh();
            }
        }
    
        /**
         * Refresh() doesn't really have much meaning, but just like in sNavbar, we
         * have a refresh() method so that we don't have front-end code calling
         * init().
         */
        public static refresh() {
            CreateAccountForm.init();
        }
        /**
         * Hide the NewEntryForm.  Be sure to clear its fields first
         */
        private static hide() {
            $("#" + CreateAccountForm.NAME + "-user").val("");
            $("#" + CreateAccountForm.NAME + "-realname").val("");
            $("#" + CreateAccountForm.NAME + "-email").val("");
            $("#" + CreateAccountForm.NAME + "-password").val("");
            //These lines hide the modal background(the shadow when bringing up a new entry form)
            $("#" + CreateAccountForm.NAME).modal("hide");
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();
        }

        public static show() {
            $("#" + CreateAccountForm.NAME + "-user").val("");
            $("#" + CreateAccountForm.NAME + "-realname").val("");
            $("#" + CreateAccountForm.NAME + "-email").val("");
            $("#" + CreateAccountForm.NAME + "-password").val("");
            $("#" + CreateAccountForm.NAME).modal("show");
        }

        private static submitForm() {
            // get the values of the two fields, force them to be strings, and check 
            // that neither is empty
            let userName = "" + $("#" + CreateAccountForm.NAME + "-user").val();
            let realName = "" + $("#" + CreateAccountForm.NAME + "-realname").val();
            let email = "" + $("#" + CreateAccountForm.NAME + "-email").val();
            let password = "" + $("#" + CreateAccountForm.NAME + "-password").val();
            if(userName.length >= 500)
            {
                window.alert("Error: UserName exceeds 500");
                //check if username exists already or nah
                return;
            }
            if(realName.length >= 50)
            {
                window.alert("Error: Name exceeds 50");
                return;
            }
            if (email == "" || !email.includes("@") || !email.includes(".com")) {
                window.alert("Error: Invalid Email");
                return;
            }
            //must have seperate includes forpassword
            if (password.length >= 50 || !password.includes("1234567890")) {
                window.alert("Error: Password must have a number");
                return;
            }

            CreateAccountForm.hide();
            //window.alert(username+" "+password);
            CreateAccountForm.refresh();
            // // set up an AJAX post.  When the server replies, the result will go to

            $.ajax({
                type: "POST",
                url: "/register",
                dataType: "json",
                data: JSON.stringify({ mUsername: userName, mRealName: userName, mEmail: email, mPassword: password }),
                success: CreateAccountForm.onCreateResponse
            });
        }
    
        /**
         * onSubmitResponse runs when the AJAX call in submitForm() returns a 
         * result.
         * 
         * @param data The object returned by the server
         */
        private static onCreateResponse(data: any) {
            window.alert("good");
        }
    }