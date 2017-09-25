/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;
var describe: any;
var it: any;
var expect: any;
describe("Tests of Functionality",function(){
    it("Function Test: Test if Add Element Works", function(){
        $('#Navbar-add').click();
        $("#NewEntryForm-title").val("Test Message");
        $("#NewEntryForm-message").val("Test Message");
        $("NewEntryForm-OK").click();
    });
    it("Function Test: Tests Edit Button", function(){
        $('#ID-1').click();
        let title = $('#ID-1').data("subject");
        alert(title);
    });
});
describe("Tests of UI",function() {
    /**
     * This test checks to see if the title and message fields are empty before going to 
     * add a new entry to the database
     */
    it("UI Test: Title and Message Fields are Empty on Startup", function(){
        // click the button for showing the add button
        $('#Navbar-add').click();
        // Gets the strings currently in the -title and -message tag
        let title = $("#NewEntryForm-title").val();
        let message = $("#NewEntryForm-message").val();
        //Checks to see if both entries are empty
        expect(title).toEqual("");
        expect(message).toEqual("");
        //Closes out of AddEntry Form
        $('#NewEntryForm-Close').click();        
    });
    /*
    //Checks the text for the h3 tag is different for different div tags
    it("UI Test: Header Title changes Text", function(){
        expect( $("#showElements > h3").text()).toEqual("All Messages");
        expect( $("#addElement > h3").text()).toEqual("Add a New Entry");
        expect( $("#editElement > h3").text()).toEqual("Edit an Entry");   
    });
    //Tests that when the add button on the 'Add a New Entry screen' is pressed, an Alert is given
    //signifying no entry was submitted on an empty form, and the page doesnt change in this case
    it("UI Test: Add Button Stays On Add Page and no Entry is Submitted with Null Lines", function(){
        expect( $('#addElement').attr("style").indexOf("display: none;")).toEqual(0);
        // click the button for showing the add button
        $('#showFormButton').click();
        // expect that the add form is not hidden
        expect( $('#addElement').attr("style").indexOf("display: none;")).toEqual(-1);
        //Click the add button
        $('#addButton').click();
        //Checks that the screen hasn't change back to the showElements 
        expect( $('#addElement').attr("style").indexOf("display: none;")).toEqual(-1);
        //Clicks the cancel button to change back to the showElements tag
        $('#addCancel').click();
        //Checks that the addElements tag is no longer being displayed
        expect( $('#addElement').attr("style").indexOf("display: none;")).toEqual(0);
    });
    */
    
});