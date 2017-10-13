var $: any;
var Handlebars: any;
var usernameToDisplay: string;

class ProfilePage{
    
    private static isInit = false;
    
        /**
         * The name of the DOM entry associated with ProfilePage
         */
        private static readonly NAME = "ProfilePage";

        /**
         * Initialize the ProfilePage Singleton by creating its element in the DOM and
         * configuring its button.  This needs to be called from any public static
         * method, to ensure that the Singleton is initialized before use.
         */
        private static init() {
            //NavbarLoggedIn.refresh();
            if (!ProfilePage.isInit) {
                //$("div.panel-default").hide();
                //$("nav.navbar-default").hide();
                //NavbarLoggedIn.refresh();
                $("nav.xyz").remove();
                //window.alert("end of profile init");
                //$("nav.navbar-default").hide();
                
            }
        }

        /** Method to show the profile of the user passed in
         * @param username
         */
        public static show(username: string) {
            
            $("#ViewComments").remove();
            //window.alert("hit profile show");
            ProfilePage.init();
            //NavbarLoggedIn.refresh();
            //$("div.panel-default").hide();
            //$("nav.navbar-default").hide();
            usernameToDisplay = username;
            //window.alert(usernameToDisplay);
            $.ajax({
                type: "GET",
                url: "/profile/"+usernameToDisplay,
                dataType: "json",
                success: ProfilePage.onDisplayProfile
            });
            
        }
        /** Method to show the profile of the user passed in in method show(username)
         * data will contain all info of the user from the AJAX call
         * @param data
         */
        public static onDisplayProfile(data:any){
            ProfilePage.init();
            $("#ElementList").remove();
            //ElementList.refreshUser(Gusername);
            $("body").append(Handlebars.templates[ProfilePage.NAME + ".hb"](data));
            //window.alert("Before cilick");
            if(Gusername == usernameToDisplay){
                $("#"+ProfilePage.NAME+"-editBio").show();
            }else{
                $("#"+ProfilePage.NAME+"-editBio").hide();
            }

            $("#"+ProfilePage.NAME+"-editBio").click(ProfilePage.editBio);
            
            if(Gusername == usernameToDisplay){
                viewingYours=true;
                ElementList.refreshUser(Gusername);
                headers = true;
            }
            //ProfilePage.init(Gusername);
            //window.alert(username+" is logged in");
        }
        /** 
         * Method to edit your bio (user logged in)
         */
        public static editBio() {
            //window.alert("called method");
            if(Gusername == usernameToDisplay){
                //window.alert("allow to edit bio now");
                EditBio.showEdit();
            }else{
                window.alert("You may only edit your own profile bio");
            }
            EditBio.refresh();
            
        }
        /** 
         * Method to change the bio of your profile
         * @param username
         */
        public static changeBio(username: string) {
            let newBio = $("#EditBio-newBio").val();
            //window.alert(newBio);

        }
        /** 
         * Method to hide the edit bio form
         */
        public static hideEdit() {
            let newBio = "";
            $("#EditBio").modal("hide");
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();
        }
    }
