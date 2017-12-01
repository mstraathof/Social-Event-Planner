/**
 * NewCommentForm encapsulates all of the code for the form for making a new comment on a message/buzz
 */

var Handlebars: any;
class NewCommentForm {
    
        /**
         * The name of the DOM entry associated with NewCommentForm
         */
        private static readonly NAME = "NewCommentForm";
    
        /**
         * Track if the Singleton has been initialized
         */
        private static isInit = false;
    
        /**
         * Initialize the NewCommentForm by creating its element in the DOM and 
         * configuring its buttons.  This needs to be called from any public static 
         * method, to ensure that the Singleton is initialized before use
         */
        private static init() {
            if (!NewCommentForm.isInit) {
                $("body").append(Handlebars.templates[NewCommentForm.NAME + ".hb"]());
                $("#" + NewCommentForm.NAME + "-OK").click(NewCommentForm.submitForm);
                $("#" + NewCommentForm.NAME + "-Close").click(NewCommentForm.hide);
                NewCommentForm.isInit = true;
            }
        }
    
        /**
         * Refresh() doesn't really have much meaning, but just like in sNavbar, we
         * have a refresh() method so that we don't have front-end code calling
         * init().
         */
        public static refresh() {
            NewCommentForm.init();
        }
        /**
         * Hide the NewCommentForm.  Be sure to clear its fields first
         */
        private static hide() {
            $("#" + NewCommentForm.NAME + "-comment").val("");
            //These lines hide the modal background(the shadow when bringing up a new entry form)
            $("#" + NewCommentForm.NAME).modal("hide");
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();
        }
        /**
         * Show the NewCommentForm.  Be sure to clear its fields, because there are
         * ways of making a Bootstrap modal disapper without clicking Close, and
         * we haven't set up the hooks to clear the fields on the events associated
         * with those ways of making the modal disappear.
         */
        public static show() {
            //window.alert();
            $("#" + NewCommentForm.NAME + "-comment").val("");
            $("#" + NewCommentForm.NAME).modal("show");
        }
        /**
         * Send data to submit the form only if the fields are both valid.  
         * Immediately hide the form when we send data, so that the user knows that 
         * their click was received.
         */
        private static submitForm() {
            // get the values of the two fields, force them to be strings, and check 
            // that neither is empty
// !!!!!!!!!!!

            let comment = "" + $("#" + NewCommentForm.NAME + "-comment").val();
            let url = "" + $("#" + NewCommentForm.NAME + "-url").val();            
            let file = $("#" + NewCommentForm.NAME + "-file")[0].files[0];

            if(comment === ""){
                window.alert("Comment is required.");
                return;
            }
            if(comment.length > 255){
                window.alert("Comment cannot exceed 255 characters.");
                return;
            }
            NewCommentForm.hide();
            var formData = new FormData();
            formData.append('mUsername', Gusername);
            formData.append('mKey', "" + GuserKey);     // convert number to string.
            formData.append('mMessageId', "" + mesID);
            formData.append('mComment', comment);
            formData.append('mUrl', url);
            formData.append('mFile', file);
            // set up an AJAX post.  When the server replies, onSubmitResponse() will be called.
            //window.alert(mesID+" , "+comment+" , "+Gusername);
            /*
            $.ajax({
                type: "POST",
                url: "/comments",
                dataType: "json",
                data: JSON.stringify({ mUsername: Gusername, mMessageId: mesID, mComment: comment, mKey: GuserKey }),
                success: NewCommentForm.onSubmitResponse
            });
            */
            $.ajax({
                type: "POST",
                url: "/comments",
                //url: "https://forums.wholetomato.com/mira/echoComment.aspx",
                dataType: "json",      // dataType of response to POST
                data: formData,
                contentType: false,
                processData: false,
                success: NewCommentForm.onSubmitResponse
            });
        }
    
        /**
         * onSubmitResponse runs when the AJAX call in submitForm() returns a 
         * result.
         * 
         * @param data The object returned by the server
         */
        private static onSubmitResponse(data: any) {
            //console.log(data);                       // DEBUG
            if (data.mStatus === "logout") {
                window.alert("Session Timed Out");
                location.reload();
            }
            // If we get an "ok" message, clear the form and refresh the main 
            // listing of messages
            if (data.mStatus === "ok") {
                //NewCommentForm.refresh();
                $("#ViewComments").remove();
                //window.alert(mesID);
                ElementList.viewCommentsGivenID(data.mesID);
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