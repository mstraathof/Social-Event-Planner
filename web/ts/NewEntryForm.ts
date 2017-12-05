/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */

var Handlebars: any;
class NewEntryForm {
    
        /**
         * The name of the DOM entry associated with NewEntryForm
         */
        private static readonly NAME = "NewEntryForm";
    
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
            if (!NewEntryForm.isInit) {
                $("body").append(Handlebars.templates[NewEntryForm.NAME + ".hb"]());
                $("#" + NewEntryForm.NAME + "-OK").click(NewEntryForm.submitForm);
                $("#" + NewEntryForm.NAME + "-Close").click(NewEntryForm.hide);
                NewEntryForm.isInit = true;
            }
        }
    
        /**
         * Refresh() doesn't really have much meaning, but just like in sNavbar, we
         * have a refresh() method so that we don't have front-end code calling
         * init().
         */
        public static refresh() {
            NewEntryForm.init();
        }
        /**
         * Hide the NewEntryForm.  Be sure to clear its fields first
         */
        private static hide() {
            $("#" + NewEntryForm.NAME + "-title").val("");
            $("#" + NewEntryForm.NAME + "-message").val("");
            //These lines hide the modal background(the shadow when bringing up a new entry form)
            $("#" + NewEntryForm.NAME).modal("hide");
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();
        }
        /**
         * Show the NewEntryForm.  Be sure to clear its fields, because there are
         * ways of making a Bootstrap modal disapper without clicking Close, and
         * we haven't set up the hooks to clear the fields on the events associated
         * with those ways of making the modal disappear.
         */
        public static show() {
            $("#" + NewEntryForm.NAME + "-title").val("foo");
            $("#" + NewEntryForm.NAME + "-message").val("bar");
            $("#" + NewEntryForm.NAME + "-url").val("https://xkcd.com/");
            $("#" + NewEntryForm.NAME + "-file").val("");
            $("#" + NewEntryForm.NAME).modal("show");
        }
        /**
         * Send data to submit the form only if the fields are both valid.  
         * Immediately hide the form when we send data, so that the user knows that 
         * their click was received.
         */
        private static submitForm() {
            // get form fields
            let title = "" + $("#" + NewEntryForm.NAME + "-title").val();   // prepend "" to force into a string
            let message = "" + $("#" + NewEntryForm.NAME + "-message").val();
            let url = "" + $("#" + NewEntryForm.NAME + "-url").val();
            let file = $("#" + NewEntryForm.NAME + "-file")[0].files[0];
            let filename = "error";
            if (file) {
                //console.log("file found");        // DEBUG
                filename = file.name;
            }
            console.log("filename:" + filename);

            if(message.length >= 500)
            {
                window.alert("Error: Message cannot exceed 500 characters");
                return;
            }
            if(title.length >= 50)
            {
                window.alert("Error: Title cannot exceed 50 characters");
                return;
            }
            if (title === "") {
                window.alert("Error: Title cannot be blank");
                return;
            }
            if (message === "") {
                window.alert("Error: Message cannot be blank");
                return;
            }
            // TODO: validate url
            NewEntryForm.hide();        // close modal dialog.

            // set up an AJAX post.  When the server replies, the onSubmitResponse will be called.
            /*
            // simple, one-part POST before requirement to send a file to the backend server.
            $.ajax({
                type: "POST",
                url: "/messages",
                dataType: "json",
                data: JSON.stringify({ mSubject: title, mMessage: msg, mUsername: Gusername, mKey: GuserKey }),
                success: NewEntryForm.onSubmitResponse
                // failure: alert "upload failed. please retry"
            });
            */
            //var formData = new FormData(<HTMLFormElement>document.getElementById(NewEntryForm.NAME + "-form"));
            var formData = new FormData();
            formData.append('mUsername', Gusername);            
            formData.append('mKey', "" + GuserKey);            
            formData.append('mSubject', title);            
            formData.append('mMessage', message);
            formData.append('mUrl', url);
            formData.append('mFile', file);
            formData.append('mFilename', filename);

            $.ajax({
                type: "POST",
                url: "/messages",
                //url: "https://forums.wholetomato.com/mira/echo.aspx",
                dataType: "json",      // dataType of response to POST
                data: formData,
                contentType: false,
                processData: false,
                success: NewEntryForm.onSubmitResponse
            });
        }
    
        /**
         * onSubmitResponse runs when the AJAX call in submitForm() returns a 
         * result.
         * 
         * @param data The object returned by the server
         */
        private static onSubmitResponse(data: any) {
            
            //console.log("response to POST: " + data);   // DEBUG

            // If we get an "ok" message, clear the form and refresh the main 
            // listing of messages
            if (data.mStatus === "logout") {
                window.alert("Session Timed Out");
                location.reload();
            }
            
            if (data.mStatus === "ok") {
                if(viewingYours!=true){
                    $("nav.xyz").remove();
                }
                ElementList.refresh();
            }
            // Handle explicit errors with a detailed popup message
            else if (data.mStatus === "error") {
                window.alert("The server replied with an error:\n" + data.mMessage);
            }
            // Handle other errors with a less-detailed popup message
            else {
                window.alert("Unspecified error");
            }
        }
    }