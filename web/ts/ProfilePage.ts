var $: any;
var Handlebars: any;
var usernameToDisplay: string;
var successProfile = false;
class ProfilePage{
    
    private static isInit = false;
    
        /**
         * The name of the DOM entry associated with Navbar
         */
        private static readonly NAME = "ProfilePage";

        /**
         * Initialize the Navbar Singleton by creating its element in the DOM and
         * configuring its button.  This needs to be called from any public static
         * method, to ensure that the Singleton is initialized before use.
         */
        private static init() {
            //NavbarLoggedIn.refresh();
            if (!ProfilePage.isInit) {
                //$("div.panel-default").hide();
                //$("nav.navbar-default").hide();
                //NavbarLoggedIn.refresh();
                $("nav.xyz").hide();
                //window.alert("end of profile init");
                //$("nav.navbar-default").hide();
                
            }
        }

        public static show(username: string) {
            window.alert("hit profile show");
            ProfilePage.init();
            //NavbarLoggedIn.refresh();
            //$("div.panel-default").hide();
            //$("nav.navbar-default").hide();
            usernameToDisplay = username;
            $.ajax({
                type: "GET",
                url: "/profile/"+usernameToDisplay,
                dataType: "json",
                success: ProfilePage.onDisplayProfile
            });
            if(successProfile==false){
                window.alert("Trouble with login. Please log out and back in");
            }
        }

        public static onDisplayProfile(data:any){
            $("#ElementList").remove();
            //ElementList.refreshUser(Gusername);
            $("body").append(Handlebars.templates[ProfilePage.NAME + ".hb"](data));
            $("#"+ProfilePage.NAME+"-editBio").click(EditBio.showEdit);
            successProfile = true;
            if(Gusername == usernameToDisplay){
                ElementList.refreshUser(Gusername);
            }
            //ProfilePage.init(Gusername);
            //window.alert(username+" is logged in");
        }

        public static editBio() {
            window.alert("allow to edit bio now");
            EditBio.showEdit();
        }

        public static changeBio(username: string) {
            let newBio = $("#EditBio-newBio").val();
            window.alert(newBio);

        }
        public static hideEdit() {
            let newBio = "";
            $("#EditBio").modal("hide");
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();
        }
    }
