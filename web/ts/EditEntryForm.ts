/**
 * EditEntryForm encapsulates all of the code for the form for adding an entry
 */
class EditEntryForm {
    
        /**
         * The name of the DOM entry associated with EditEntryForm
         */
        private static readonly NAME = "EditEntryForm";
    
        /**
         * Track if the Singleton has been initialized
         */
        private static isInit = false;
    
        /**
         * Initialize the EditEntryForm by creating its element in the DOM and 
         * configuring its buttons.  This needs to be called from any public static 
         * method, to ensure that the Singleton is initialized before use
         */
        public static init() {
            if (!EditEntryForm.isInit) {
                $("body").append(Handlebars.templates[EditEntryForm.NAME + ".hb"]());
                $(".ElementList-editbtn").click(EditEntryForm.show);
                $("#"+EditEntryForm.NAME+"-upvote").click(EditEntryForm.upvote);
                $("#"+EditEntryForm.NAME+"-downvote").click(EditEntryForm.downvote);
                $("#" + EditEntryForm.NAME + "-Close").click(EditEntryForm.hide);
                EditEntryForm.isInit = true;
            }
        }
    
        /**
         * Refresh() doesn't really have much meaning, but just like in sNavbar, we
         * have a refresh() method so that we don't have front-end code calling
         * init().
         */
        public static refresh() {
            EditEntryForm.init();
        }
    
        /**
         * Hide the EditEntryForm.  Be sure to clear its fields first
         */
        private static hide() {
            $("#" + EditEntryForm.NAME + "-title").val("");
            $("#" + EditEntryForm.NAME + "-message").val("");
            $("#" + EditEntryForm.NAME).modal("hide");
        }
    
        /**
         * Show the EditEntryForm.  Be sure to clear its fields, because there are
         * ways of making a Bootstrap modal disapper without clicking Close, and
         * we haven't set up the hooks to clear the fields on the events associated
         * with those ways of making the modal disappear.
         */
        public static show() {
            let subject = $(this).data("subject");
            let message = $(this).data("message");
            $("#" + EditEntryForm.NAME + "-title").val(subject);
            $("#" + EditEntryForm.NAME + "-message").val(message);
            $("#" + EditEntryForm.NAME).modal("show");
        }
    
    
        /**
         * Send data to submit the form only if the fields are both valid.  
         * Immediately hide the form when we send data, so that the user knows that 
         * their click was received.
         */
        private static submitForm() {
            // get the values of the two fields, force them to be strings, and check 
            // that neither is empty
            let title = "" + $("#" + EditEntryForm.NAME + "-title").val();
            let msg = "" + $("#" + EditEntryForm.NAME + "-message").val();
            let id = "" + $("#" + EditEntryForm.NAME + "-id").val();
            if (title === "" || msg === "") {
                window.alert("Error: title or message is not valid");
                return;
            }
            EditEntryForm.hide();
            // set up an AJAX post.  When the server replies, the result will go to
            // onSubmitResponse
            $.ajax({
                type: "PUT",
                url: "/messages" + id,
                dataType: "json",
                data: JSON.stringify({ mTitle: title, mMessage: msg }),
                success: EditEntryForm.onSubmitResponse
            });
        }

        private static upvote(){
            let id = "" + $("#" + EditEntryForm.NAME + "-id").val();
            alert("Upvote Button Pressed");
            //let votes = 1 + $("#" + EditEntryForm.NAME + "-votes").val();
            /*
            $.ajax({
                type: "PUT",
                url: "/messages" + id,
                dataType: "json",
                data: JSON.stringify({ mVotes: votes}),
                success: EditEntryForm.onSubmitResponse
            });
            */
        }

        private static downvote(){
            let id = "" + $("#" + EditEntryForm.NAME + "-id").val();
            alert("Downvote Button Pressed");
            //let votes = 1 - $("#" + EditEntryForm.NAME + "-votes").val();
            /*
            $.ajax({
                type: "PUT",
                url: "/messages" + id,
                dataType: "json",
                data: JSON.stringify({ mVotes: votes}),
                success: EditEntryForm.onSubmitResponse
            });
            */
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
            if (data.mStatus === "ok") {
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