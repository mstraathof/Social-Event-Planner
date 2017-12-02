// globals
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
        ProfilePage.init();
        usernameToDisplay = username;

        $.ajax({
            type: "GET",
            url: "/profile/" + usernameToDisplay + "/" + Gusername + "/" + GuserKey,
            dataType: "json",
            success: ProfilePage.displayProfile
        });
    }

    /** Method to show the profile of the user passed in in method show(username)
     * data will contain all info of the user from the AJAX call
     * @param data
     */
    public static displayProfile(data:any){
        if (data.mStatus === "logout") {
            window.alert(Gkey);
            window.alert("Session Timed Out");
            location.reload();
        }
        ProfilePage.init();

        //console.log("displayProfile() for " + usernameToDisplay + ":");
        //console.log(JSON.stringify(ProfilePage.profile));

        // replace main container, whatever it contains, with one for a profile.
        $("#mainContainer").remove();
        $("body").append(Handlebars.templates[ProfilePage.NAME + ".hb"](data));
        if (Gusername == usernameToDisplay) {
            //$("#"+ProfilePage.NAME+"-editBio").show();
            //$("#"+ProfilePage.NAME+"-editBio").click(ProfilePage.editBio);
            $("#buttonEditBio").show();
            $("#buttonEditBio").click(ProfilePage.editBio);
        }
    }

    /** 
     * Method to edit your bio (user logged in)
     */
    public static editBio() {
        //window.alert("called method");
        if (Gusername == usernameToDisplay) {
            EditBio.showEdit();
        } else {
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
