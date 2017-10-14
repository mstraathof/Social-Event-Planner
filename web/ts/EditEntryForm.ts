/**
 * EditEntryForm encapsulates all of the code for the form for adding an entry
 */
var $: any;
var Handlebars: any;
class EditEntryForm {
    
        /**
         * The name of the DOM entry associated with EditEntryForm
         */
        private static readonly NAME = "EditEntryForm";
        public static idCurrentlyEditing = null;
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
                EditEntryForm.show;
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
            let id = $(this).data("id");
            let createTime = $(this).data("createTime");
            let votes = $(this).data("votes");
            $("#" + EditEntryForm.NAME + "-title").val(subject);
            $("#" + EditEntryForm.NAME + "-message").val(message);
            $("#" + EditEntryForm.NAME + "-id").val(id);
            $("#" + EditEntryForm.NAME + "-date").val(createTime);
            $("#" + EditEntryForm.NAME + "-votes").val(votes);
            $("#" + EditEntryForm.NAME).modal("show");
        }

    }