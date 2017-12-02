<div id="NewEntryForm" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Create a Buzz</h4>
            </div>
            <form id="NewEntryForm-form" name="form">
            <div class="modal-body">
                <p>
                    <label for="NewEntryForm-title">Title:</label>
                    <input class="form-control" type="text" id="NewEntryForm-title" name="title" />
                </p>
                <p>
                    <label for="NewEntryForm-message">Message:</label>
                    <textarea class="form-control" id="NewEntryForm-message" name="message"></textarea>
                </p>
                <p>
                    <label for="NewEntryForm-url">URL:</label>
                    <input class="form-control" type="url" id="NewEntryForm-url" name="url" />
                </p>
                <p>
                    <label for="NewEntryForm-file">Upload File:</label>
                    <input type="file" id="NewEntryForm-file" name="file" />
                </p>
            </div>
            </form>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" id="NewEntryForm-OK">OK</button>
                <button type="button" class="btn btn-default" id="NewEntryForm-Close">Cancel</button>
            </div>
        </div>
    </div>
</div>