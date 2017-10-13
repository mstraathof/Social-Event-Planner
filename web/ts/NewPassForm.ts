/**
 * NewPassForm encapsulates all of the code for the form for adding an entry
 */

var Handlebars: any;
class NewPassForm {
    
        /**
         * The name of the DOM entry associated with NewPassForm
         */
        private static readonly NAME = "NewPassForm";
    
        /**
         * Track if the Singleton has been initialized
         */
        private static isInit = false;
    
        /**
         * Initialize the NewPassForm by creating its element in the DOM and 
         * configuring its buttons.  This needs to be called from any public static 
         * method, to ensure that the Singleton is initialized before use
         */
        private static init() {
            if (!NewPassForm.isInit) {
                $("body").append(Handlebars.templates[NewPassForm.NAME + ".hb"]());
                $("#" + NewPassForm.NAME + "-OK").click(NewPassForm.submitForm);
                $("#" + NewPassForm.NAME + "-Close").click(NewPassForm.hide);
                NewPassForm.isInit = true;
            }
        }
    
        /**
         * Refresh() doesn't really have much meaning, but just like in sNavbar, we
         * have a refresh() method so that we don't have front-end code calling
         * init().
         */
        public static refresh() {
            NewPassForm.init();
        }
        /**
         * Hide the NewPassForm.  Be sure to clear its fields first
         */
        private static hide() {
            $("#" + NewPassForm.NAME + "-oldPass").val("");
            $("#" + NewPassForm.NAME + "-newPass").val("");
            //These lines hide the modal background(the shadow when bringing up a new entry form)
            $("#" + NewPassForm.NAME).modal("hide");
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();
        }
        /**
         * Show the NewPassForm.  Be sure to clear its fields, because there are
         * ways of making a Bootstrap modal disapper without clicking Close, and
         * we haven't set up the hooks to clear the fields on the events associated
         * with those ways of making the modal disappear.
         */
        public static show() {
            $("#" + NewPassForm.NAME + "-oldPass").val("");
            $("#" + NewPassForm.NAME + "-newPass").val("");
            $("#" + NewPassForm.NAME).modal("show");
        }
        /**
         * Send data to submit the form only if the fields are both valid.  
         * Immediately hide the form when we send data, so that the user knows that 
         * their click was received.
         */
        private static submitForm() {
            // get the values of the two fields, force them to be strings, and check 
            // that neither is empty
            let oldPass = "" + $("#" + NewPassForm.NAME + "-oldPass").val();
            let newPass = "" + $("#" + NewPassForm.NAME + "-newPass").val();
            if(oldPass.length >= 30 || oldPass == "")
            {
                window.alert("Error: Make a reasonable password");
                return;
            }
            if(newPass.length >= 30 || newPass == "")
            {
                window.alert("Error: Make a reasonable password");
                return;
            }
            NewPassForm.hide();
            // set up an AJAX post.  When the server replies, the result will go to
            // onSubmitResponse
            window.alert("before ajax"+oldPass+","+newPass);
            $.ajax({
                type: "PUT",
                url: "/changePassword/"+Gusername,
                dataType: "json",
                data: JSON.stringify({ mCurrentPassword: oldPass, mNewPassword: newPass}),
                success: NewPassForm.onChangeResponse
            });
        }
        /** Takes the response from a successful AJAX call to create a new password
         * @param data
         */
        public static onChangeResponse(data: any){
            window.alert("success");
            if (data.mStatus === "ok") {
                window.alert("Changed Successfully");            }
            // Handle explicit errors with a detailed popup message
            else if (data.mStatus === "error") {
                window.alert("The server replied with an error:\n" + data.mMessage);
            }
        }
    }