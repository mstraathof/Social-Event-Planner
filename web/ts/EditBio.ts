/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */

var Handlebars: any;
class EditBio {
    
        /**
         * The name of the DOM entry associated with NewEntryForm
         */
        private static readonly NAME = "EditBio";
    
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
            if (!EditBio.isInit) {
                $("body").append(Handlebars.templates[EditBio.NAME + ".hb"]());
                //window.alert("appended");
                $("#EditBio-OK").click(EditBio.changeBio);
                $("#EditBio-Close").click(EditBio.hideEdit);
                EditBio.isInit = true;
            }
        }

        /**
         * Refresh() doesn't really have much meaning, but just like in sNavbar, we
         * have a refresh() method so that we don't have front-end code calling
         * init().
         */
        public static refresh() {
            EditBio.init();
        }

        public static changeBio() {
                // get the values of the two fields, force them to be strings, and check 
                // that neither is empty
                let newBio = "" + $("#" + EditBio.NAME + "-newBio").val();
                //window.alert(newBio);

                $.ajax({
                    type: "POST",
                    url: "/profile",
                    dataType: "json",
                    data: JSON.stringify({ mUsername: Gusername, mProfile: newBio }),
                    success: EditBio.onUpdateResponse
                });
                $("nav.xyz").hide();
                $("#ViewComments").remove();
                ProfilePage.show(Gusername);
                EditBio.hideEdit();
                // set up an AJAX post.  When the server replies, the result will go to
                // onSubmitResponse
                // $.ajax({
                //     type: "POST",
                //     url: "/messages",
                //     dataType: "json",
                //     data: JSON.stringify({ mSubject: title, mMessage: msg }),
                //     success: NewEntryForm.onSubmitResponse
                // });
        }

        public static onUpdateResponse(data: any){

            //window.alert("Updated Bio Successfully");
        }

        public static hideEdit() {
            $("#" + EditBio.NAME + "-newBio").val("");
            //These lines hide the modal background(the shadow when bringing up a new entry form)
            $("#" + EditBio.NAME).modal("hide");
            //$('body').removeClass('modal-open');
            //$('.modal-backdrop').remove();
        }
        
        public static showEdit() {
            $("#" + EditBio.NAME + "-newBio").val("");
            $("#" + EditBio.NAME).modal("show");
            //EditBio.refresh();
        }
    }
