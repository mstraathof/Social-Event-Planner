/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var describe: any;
var beforeAll: any;
var it: any;
var expect: any;
var spyOn: any;
var $: any;


describe("Ajax Test", function () {
    // Wait for CSS to load before running tests
    beforeAll(function (done: any) {
        $(document).ready(done);
    });
    /**
     * Mocks an Ajax call for a sucessful new entry
     */
    it("NewEntryForm Fake Ajax Call-Success", function () {
        // Checks the settings sent by the ajax call from the add button
        function mockAjax(settings: any) {
            expect(settings.type).toEqual("POST");
            expect(settings.url).toEqual("/messages");
            expect(settings.dataType).toEqual("json");
            expect(typeof settings.success).toEqual("function");

            var data = JSON.parse(settings.data);
            expect(data.mSubject).toEqual("some title");
            expect(data.mMessage).toEqual("some message");
        }
        // Replace $.ajax with my mock function
        spyOn($, "ajax").and.callFake(mockAjax);
        // Navigate to the add form
        $('#Navbar-add').click();
        // Enter some data and click add
        $("#NewEntryForm-title").val("some title");
        $("#NewEntryForm-message").val("some message");
        $('#NewEntryForm-OK').click();
        expect($.ajax).toHaveBeenCalled();
        $('#NewEntryForm-Close').click();
        $('#NewEntryForm').modal("hide");
    });
    /**
     * Mocks an Ajax Call for a failure if the title is more then 50 characters
     */
    it("NewEntryForm Fake Ajax Call-Title Failure", function () {
        // Checks the settings sent by the ajax call from the add button
        function mockAjax(settings: any) {
            expect(settings.type).toEqual("POST");
            expect(settings.url).toEqual("/messages");
            expect(settings.dataType).toEqual("json");
            expect(typeof settings.success).toEqual("function");

            var data = JSON.parse(settings.data);
        }
        // Replace $.ajax with my mock function
        spyOn($, "ajax").and.callFake(mockAjax);
        // Navigate to the add form
        $('#Navbar-add').click();
        // Enter some data and click add
        $("#NewEntryForm-title").val("1oa4I1ECO5JugPCxNriZaqcolGJCrzZr0jFkJ6HZVIdbboa9R5IggeGvbmjo");
        $("#NewEntryForm-message").val("some message");
        $('#NewEntryForm-OK').click();
        expect($.ajax).not.toHaveBeenCalled();
        $('#NewEntryForm-Close').click();
        $('#NewEntryForm').modal("hide");
    });
    /**
     * Mocks an Ajax call for a failure if the message is more then 500 characters
     */
    it("NewEntryForm Fake Ajax Call-Message Failure", function () {
        // Checks the settings sent by the ajax call from the add button
        function mockAjax(settings: any) {
            expect(settings.type).toEqual("POST");
            expect(settings.url).toEqual("/messages");
            expect(settings.dataType).toEqual("json");
            expect(typeof settings.success).toEqual("function");

            var data = JSON.parse(settings.data);
        }
        // Replace $.ajax with my mock function
        spyOn($, "ajax").and.callFake(mockAjax);
        // Navigate to the add form
        $('#Navbar-add').click();
        // Enter some data and click add
        $("#NewEntryForm-title").val("some Title");
        $("#NewEntryForm-message").val("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibu");
        $('#NewEntryForm-OK').click();
        expect($.ajax).not.toHaveBeenCalled();
        $('#NewEntryForm-Close').click();
        $('#NewEntryForm').modal("hide");
    });
    /**
     * Mocks an Ajax call for a failure if nothing is entered into the title and message fields
     */
    it("NewEntryForm Fake Ajax Call- No Title or Message Failure", function () {
        // Checks the settings sent by the ajax call from the add button
        function mockAjax(settings: any) {
            expect(settings.type).toEqual("POST");
            expect(settings.url).toEqual("/messages");
            expect(settings.dataType).toEqual("json");
            expect(typeof settings.success).toEqual("function");

            var data = JSON.parse(settings.data);
        }
        // Replace $.ajax with my mock function
        spyOn($, "ajax").and.callFake(mockAjax);
        // Navigate to the add form
        $('#Navbar-add').click();
        // Enter some data and click add
        $("#NewEntryForm-title").val("");
        $("#NewEntryForm-message").val("");
        $('#NewEntryForm-OK').click();
        expect($.ajax).not.toHaveBeenCalled();
        $('#NewEntryForm-Close').click();
        $('#NewEntryForm').modal("hide");
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
        $('#NewEntryForm').modal("hide");        
    });
    /**
     * Checks that the modals for AddElement and EditElement are hidden before starting up the page
     */
    it("UI Test: Tests that AddElement and EditElement Divs are hidden on start", function(){
        let newEntryVisibility = $('#NewEntryForm').is(':visible');
        expect(newEntryVisibility).toEqual(false);
        let editEntryVisibility = $('#EditEntryForm').is(':visible');
        expect(editEntryVisibility).toEqual(false);
    });
    
});



