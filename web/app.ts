/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
/// <reference path="ts/NavbarLoggedIn.ts"/>
/// <reference path="ts/LoginWindow.ts"/>
/// <reference path="ts/CreateAccountForm.ts"/>
/// <reference path="ts/ProfilePage.ts"/>
/// <reference path="ts/EditBio.ts"/>
/// <reference path="ts/ViewComments.ts"/>
/// <reference path="ts/NewCommentForm.ts"/>
/// <reference path="ts/NewPassForm.ts"/>

// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;

var Handlebars: any;
var GloggedIn=false;
var Gusername:string;
var Gpassword = "";
var GuserKey = -1;
var headers = false;
var viewingYours=false;

// a global for the EditEntryForm of the program.  See newEntryForm for 
// explanation
var editEntryForm: EditEntryForm;
var loginWindow: LoginWindow;
var createAccountForm: CreateAccountForm;
var changePass: NewPassForm;
var editBio: EditBio;
var mesID:number;
// Run some configuration code when the web page loads

$(document).ready(function () {
    if(GloggedIn == false){
        Navbar.refresh();
    }else{
        NavbarLoggedIn.refresh();
    }
    NewEntryForm.refresh();
    NewCommentForm.refresh();
    CreateAccountForm.refresh();
    LoginWindow.refresh();
    //ElementList.refresh();
    EditEntryForm.refresh();
    EditBio.refresh();
    NewPassForm.refresh();
    // Create the object that controls the "Edit Entry" form
    editEntryForm = new EditEntryForm();
    changePass = new NewPassForm();
    loginWindow = new LoginWindow();
    createAccountForm = new CreateAccountForm();
    editBio = new EditBio();
    // set up initial UI state

    $("#editElement").hide();

});

