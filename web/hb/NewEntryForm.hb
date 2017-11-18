<div id="NewEntryForm" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Add a New Entry</h4>
            </div>
            <form id="NewEntryForm-form" name="form">
            <div class="modal-body">
                <label for="NewEntryForm-title">Title</label>
                <input class="form-control" type="text" id="NewEntryForm-title" name="title" />
                <label for="NewEntryForm-message">Message</label>
                <textarea class="form-control" id="NewEntryForm-message" name="message"></textarea>
                <label for="NewEntryForm-url">URL</label>
                <input class="form-control" type="url" id="NewEntryForm-url" name="url" />
                <label for="NewEntryForm-file">Upload File</label>
                <input type="file" id="NewEntryForm-file" name="file" />
            </div>
            </form>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" id="NewEntryForm-OK">OK</button>
                <button type="button" class="btn btn-default" id="NewEntryForm-Close">Close</button>
            </div>
        </div>
    </div>
</div>