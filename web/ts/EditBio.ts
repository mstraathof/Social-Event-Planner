/**
 * EditBioForm form encapsulates all of the code for the form for adding an entry
 */

var Handlebars: any;
class EditBio {
    
        /**
         * The name of the DOM entry associated with EditBio
         */
        private static readonly NAME = "EditBio";
    
        /**
         * Track if the Singleton has been initialized
         */
        private static isInit = false;
    
        /**
         * Initialize the EditBio form by creating its element in the DOM and 
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

        /**
         * changeBio will take the form and put in AJAX call to upadte your profile
         */
        public static changeBio() {
                let newBio = "" + $("#" + EditBio.NAME + "-newBio").val();
                if(newBio.length > 500){
                    window.alert("Too long of a bio. Max 500 characters");
                    return;
                }
                $.ajax({
                    type: "POST",
                    url: "/profile",
                    dataType: "json",
                    data: JSON.stringify({ mUsername: Gusername, mProfile: newBio, mKey: GuserKey }),
                    success: EditBio.onUpdateResponse
                });
                $("nav.xyz").hide();
                $("#ViewComments").remove();
                ProfilePage.show(Gusername);
                EditBio.hideEdit();
        }
        
        /**
         * onUpdateResponse will get the backends response to the updated bio
         */
        public static onUpdateResponse(data: any){
            if (data.mStatus === "logout") {
                window.alert("Session Timed Out");
                location.reload();
            }
            //window.alert("Updated Bio Successfully");
        }

        /**
         * Hides the EditBio form
         */
        public static hideEdit() {
            $("#" + EditBio.NAME + "-newBio").val("");
            $("#" + EditBio.NAME).modal("hide");
            //$('body').removeClass('modal-open');
            //$('.modal-backdrop').remove();
        }
        
        /**
         * Shows the EditBio form
         */
        public static showEdit() {
            $("#" + EditBio.NAME + "-newBio").val("");
            $("#" + EditBio.NAME).modal("show");
            //EditBio.refresh();
        }
    }
