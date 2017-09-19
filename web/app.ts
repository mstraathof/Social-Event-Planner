/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>

// Prevent compiler errors when using Handlebars
var Handlebars: any;

// Run some configuration code when the web page loads
$(document).ready(function () {
    // Create the object that controls the "New Entry" form
    newEntryForm = new NewEntryForm();
    // Create the object for the main data list, and populate it with data from
    // the server
    mainList = new ElementList();

    // Create the object that controls the "Edit Entry" form
    editEntryForm = new EditEntryForm();

    // Populate the Element List Singleton with data from the server
    ElementList.refresh();
    
    // set up initial UI state
    $("#editElement").hide();
    $("#addElement").hide();
    $("#showElements").show();
    // set up the "Add Message" button
    $("#showFormButton").click(function () {
        $("#addElement").show();
        $("#showElements").hide();
    });
});