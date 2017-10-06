/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
/// <reference path="ts/LoginWindow.ts"/>
/// <reference path="ts/CreateAccountForm.ts"/>

// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;

// Prevent compiler errors when using Handlebars
var Handlebars: any;

// a global for the EditEntryForm of the program.  See newEntryForm for 
// explanation
var editEntryForm: EditEntryForm;
var loginWindow: LoginWindow;
var createAccountForm: CreateAccountForm;
//var loggedIn = false;

// Run some configuration code when the web page loads
$(document).ready(function () {
    Navbar.refresh();
    NewEntryForm.refresh();
    CreateAccountForm.refresh();
    LoginWindow.refresh();
    ElementList.refresh();
    EditEntryForm.refresh();
    // Create the object that controls the "Edit Entry" form
    editEntryForm = new EditEntryForm();
    loginWindow = new LoginWindow();
    createAccountForm = new CreateAccountForm();
    // set up initial UI state

    $("#editElement").hide();

});