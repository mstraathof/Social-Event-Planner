var $: any;
var Handlebars: any;

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
            window.alert("entered into profile script");
            if (!ProfilePage.isInit) {
                NavbarLoggedIn.refresh();
                ElementList.refresh;
            }
        }

        public static show(username: string) {
            $("div.panel-default").hide();
            $("nav.navbar-default").hide();
            Gusername = username;
            $("body").prepend(Handlebars.templates[ProfilePage.NAME + ".hb"]());
            window.alert(username+" is logged in");
        }
    }
