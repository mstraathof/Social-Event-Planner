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
            $("#" + NewEntryForm.NAME + "-title").val("");
            $("#" + NewEntryForm.NAME + "-message").val("");
            $("#" + NewEntryForm.NAME + "-url").val("");
            $("#" + NewEntryForm.NAME + "-file").val("");
            $("#" + NewEntryForm.NAME).modal("show");
        }
        /**
         * Send data to submit the form only if the fields are both valid.  
         * Immediately hide the form when we send data, so that the user knows that 
         * their click was received.
         */
        private static submitForm() {
            // get the values of the two fields, force them to be strings, and check 
            // that neither is empty
            let title = "" + $("#" + NewEntryForm.NAME + "-title").val();
            let msg = "" + $("#" + NewEntryForm.NAME + "-message").val();
// !!!!!!!!!!!
            let url = "" + $("#" + NewEntryForm.NAME + "-url").val();
            let file = "" + $("#" + NewEntryForm.NAME + "-file").val();
            if(msg.length >= 500)
            {
                window.alert("Error: Message cannot exceed 500 characters");
                return;
            }
            if(title.length >= 50)
            {
                window.alert("Error: Title cannot exceed 50 characters");
                return;
            }
            if (title === "" || msg === "") {
                window.alert("Error: Title and message cannot be blank");
                return;
            }
            // TODO: validate url
            NewEntryForm.hide();
            // set up an AJAX post.  When the server replies, the result will go to
            // onSubmitResponse
            /*
            $.ajax({
                type: "POST",
                url: "/messages",
                dataType: "json",
                data: JSON.stringify({ mSubject: title, mMessage: msg, mUsername: Gusername, mKey: GuserKey }),
                success: NewEntryForm.onSubmitResponse
                // failure: alert "upload failed. please retry"
            });
            */
            //var formData = new FormData($("#" + NewEntryForm.NAME + "-form"));
            var formData = new FormData(<HTMLFormElement>document.getElementById(NewEntryForm.NAME + "-form"));
            //var formData = new FormData();
            formData.append('name','mira');
            formData.append('title',document.getElementById(NewEntryForm.NAME + "-title").nodeValue);
            console.log(document.getElementById(NewEntryForm.NAME + "-form").nodeName);
            //console.log(document.getElementById(NewEntryForm.NAME + "-form").toString);
            /*
            $.ajax({
                type: "POST",
                url: "/messages",
                dataType: "json",
                data: formData,
                contentType: false,
                processData: false,
                success: NewEntryForm.onSubmitResponse
                // failure: alert "upload failed. please retry"
            });
            */
            console.log("formData:");
            for (var value of formData.values()) {
                console.log(value); 
            }
            console.log("after loop");
            console.log("name: " + formData.get("name"));
            console.log("title: " + formData.get("title"));
            console.log("message: " + formData.get("message"));
            console.log("url: " + formData.get("url"));
            console.log("after formData");
        }
    
        /**
         * onSubmitResponse runs when the AJAX call in submitForm() returns a 
         * result.
         * 
         * @param data The object returned by the server
         */
        private static onSubmitResponse(data: any) {
            
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