<div id="EditEntryForm" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <label for="EditEntryForm-title">Title</label>
                <input class="form-control" type="text" id="EditEntryForm-title" readonly/>
                <label for="EditEntryForm-message">Message</label>
                <textarea class="form-control" id="EditEntryForm-message" readonly></textarea>
                <span id="EditEntryForm-date"></span>
                <span id="EditEntryForm-votes"></span>
            </div>
            <div class="modal-footer">
                <!--
                <button id="EditEntryForm-upvote" data-value="{{this.mId}}">Upvote</button>
                <button id="EditEntryForm-downvote" value="{{this.mId}}">Downvote</button>
                -->
                <button type="button" class="btn btn-default" id="EditEntryForm-Close">Close</button>
            </div>
        </div>
    </div>
</div>