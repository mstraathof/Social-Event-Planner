// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;
var describe: any;
var it: any;
var expect: any;
describe("Tests of basic math functions", function() {
    
   
    it("Adding 1 should work", function() {
        var foo = 0;
        foo += 1;
        expect(foo).toEqual(1);
    });

    it("Subtracting 1 should work", function () {
        var foo = 0;
        foo -= 1;
        expect(foo).toEqual(-1);
    });

    
});
describe("Tests of UI",function() {

    it("UI Test: Add Button Hides Listing", function(){
        // click the button for showing the add button
        $('#showFormButton').click();
        // expect that the add form is not hidden
        expect( $('#addElement').attr("style").indexOf("display: none;")).toEqual(-1);
        // expect tha tthe element listing is hidden
        expect( $('#showElements').attr("style").indexOf("display: none;")).toEqual(0);
        // reset the UI, so we don't mess up the next test
        $('#addCancel').click();        
    });
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

    
});