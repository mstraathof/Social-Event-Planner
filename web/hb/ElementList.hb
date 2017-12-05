<div class="container" id="mainContainer">
    {{#each mMessageData}}
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-6">
                    <p>
                        <a href="mailto:{{this.mUsername}}@lehigh.edu" data-toggle="tooltip" title="{{this.mUsername}}@lehigh.edu">{{this.mUsername}}</a>
                        <button id="buttonViewProfile{{this.mId}}" type="button" class="btn btn-default btn-xs" data-value="{{this.mUsername}}">View Profile</button>
                    </p>
                    <p class="subject-feed">{{this.mSubject}}</p>
                    <p>{{this.mMessage}}
                        {{#if this.mUrl}}
                            <a href="{{this.mUrl}}" data-toggle="tooltip" title="{{this.mUrl}}" target="_blank"><span class="glyphicon glyphicon-link"></span></a>
                        {{/if}}
                    </p>
                    <p class="date-feed">{{this.mCreateTime}}</p>
                    <p>
                        <a href="#" data-toggle="tooltip" title="Vote count"><span class="badge">{{this.mVotes}}</span></a>
                        <!--onclick="alert('Vote up.'); return false;">-->
                        <a id="aVoteUp{{this.mId}}" href="#" data-toggle="tooltip" title="Vote Up" data-value="{{this.mId}}" onclick="upvote(); return false;"><span class="glyphicon glyphicon-thumbs-up"></span></a>
                        <a id="aVoteDown{{this.mId}}" href="#" data-toggle="tooltip" title="Vote Down" data-value="{{this.mId}}" onclick="ElementList.downvote(); return false;"><span class="glyphicon glyphicon-thumbs-down"></span></a>
                    </p>
                </div>
                <div class="col-xs-6">
                    <img src="https://quiet-taiga-79213.herokuapp.com/messages/images/download/{{this.mFileId}}" class="img-responsive thumbnail img-feed pull-right" height=300 />
                </div>
            </div>
            <div id="comments{{this.mId}}" style="display: none">
            </div>
            <button id="buttonComment{{this.mId}}" type="button" class="btn btn-default btn-xs">Comment</button></p>
        </div>
    </div>
    {{/each}}
</div>